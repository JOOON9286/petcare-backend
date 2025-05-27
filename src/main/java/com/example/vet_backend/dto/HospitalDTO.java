package com.example.vet_backend.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDTO {

    private Long hospitalId;
    private String name;
    private String address;
    private Float latitude;
    private Float longitude;
    private String phone;
    private String openingHours;
    private Boolean hasWaitingRoom;
    private Boolean petFriendly;
    private LocalDateTime createdAt;


}
