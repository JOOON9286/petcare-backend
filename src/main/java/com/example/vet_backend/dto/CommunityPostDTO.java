package com.example.vet_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String userName;
    private int likes;

}
