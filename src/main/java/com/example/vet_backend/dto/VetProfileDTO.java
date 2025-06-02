package com.example.vet_backend.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VetProfileDTO {

    private Long vetId;
    private String specialty;
    private String licenseNumber;
    private String profilePhoto;
    private Integer ratingAvg;
    private Integer reviewCount;
    private Boolean isOnline;
    private Long userId;
    private Long hospitalId;
    private String userName;
    private String hospitalName;

    private String introduction;    // 수의사 소개
    private String availableDays;   // 진료 가능 요일
    private String availableTime;   // 진료 가능 시간
}


