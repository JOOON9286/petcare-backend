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

    private Long userId;      // 작성자 ID
    private String userName;  // 작성자 이름

    private Long vetId;       // 수의사 VetProfile ID
    private String vetName;   // 수의사 이름

    private int likeCount;
    private int commentCount;
}
