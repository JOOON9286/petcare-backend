package com.example.vet_backend.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostDTO {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
}
