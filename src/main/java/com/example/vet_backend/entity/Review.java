package com.example.vet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private Integer rating;
    private String content;

    @Column(nullable = false)
    private Boolean isAnonymous = false;

    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;

    @ManyToOne
    @JoinColumn(name = "user_id") // 작성자
    private User user;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
