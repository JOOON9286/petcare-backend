package com.example.vet_backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentDTO {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private Long postId;
    private String userName;
    private Long userId;
    private int upvotes;
    private int downvotes;
}
