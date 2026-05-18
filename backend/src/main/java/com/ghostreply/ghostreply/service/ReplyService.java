package com.ghostreply.ghostreply.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghostreply.ghostreply.dto.ReplyRequest;
import com.ghostreply.ghostreply.dto.ReplyResponse;
import com.ghostreply.ghostreply.entity.Conversation;
import com.ghostreply.ghostreply.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final WebClient webClient;
    private final ConversationRepository conversationRepository;

    @Value("${openrouter.api.key}")
    private String apiKey;

    public ReplyResponse generateReplies(ReplyRequest request) {

        String prompt = """
        You are generating realistic chat replies.

        Generate 5 different replies.

        Tone: %s

        Message:
        "%s"

        RULES:
        - Sound exactly like a real person texting
        - Keep replies short and natural
        - Avoid formal email language
        - No numbering
        - No quotation marks
        - Make replies conversational
        - Vary the style slightly
        - Return each reply on a new line
        """
                .formatted(request.getTone(), request.getMessage());
        String requestBody = """
                {
                  "model": "openai/gpt-3.5-turbo",
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ]
                }
                """.formatted(prompt.replace("\"", "\\\""));

        String response = webClient.post()
                .uri("https://openrouter.ai/api/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode jsonNode = mapper.readTree(response);

            String text = jsonNode
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

            List<String> replies = Arrays.stream(text.split("\n"))
                    .filter(line -> !line.trim().isEmpty())
                    .toList();

            Conversation conversation = new Conversation();

            conversation.setMessage(request.getMessage());
            conversation.setTone(request.getTone());
            conversation.setGeneratedReplies(text);
            conversation.setCreatedAt(LocalDateTime.now());

            conversationRepository.save(conversation);

            return new ReplyResponse(replies);

        } catch (Exception e) {
            throw new RuntimeException("Error parsing AI response");
        }
    }
}