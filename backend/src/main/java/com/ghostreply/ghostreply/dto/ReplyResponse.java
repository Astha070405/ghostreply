package com.ghostreply.ghostreply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReplyResponse {
    private List<String> replies;
}
