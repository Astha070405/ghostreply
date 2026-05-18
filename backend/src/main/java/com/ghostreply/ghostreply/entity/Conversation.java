package com.ghostreply.ghostreply.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String tone;

    @Column(columnDefinition = "TEXT")
    private String generatedReplies;

    private LocalDateTime createdAt;
}
