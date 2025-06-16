package com.example.vet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "community_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private int likes;  // 좋아요 수

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
