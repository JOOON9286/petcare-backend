package com.example.vet_backend.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long reviewId;
    private Integer rating;
    private String content;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
    private Long appointmentId;
}
