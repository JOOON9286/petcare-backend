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
    private String userName;       // 수의사 이름
    private String hospitalName;   // 병원 이름
}

