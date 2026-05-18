package com.ghostreply.ghostreply.controller;

import com.ghostreply.ghostreply.dto.ReplyRequest;
import com.ghostreply.ghostreply.dto.ReplyResponse;
import com.ghostreply.ghostreply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reply")

@CrossOrigin("*")
public class ReplyController {
    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }
    @PostMapping("/generate")
    public ReplyResponse generateReplies(@RequestBody ReplyRequest request){
        return replyService.generateReplies(request);
    }
}
