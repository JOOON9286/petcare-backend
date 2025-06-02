package com.example.vet_backend.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private Long appointmentId;
    private String title;
    private String status;
//    private String statusCall;
    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;

    private Long userId;
    private Long vetId;
    private Long petId;

    private String symptoms;
    private String medicalHistory;
    private String additionalInfo;

    private String userName;
    private String userPhone;

    private String petName;
    private String petSpecies;
    private String petBreed;
    private String petGender;
    private Float petWeight;
    private LocalDateTime petBirthDate;

    private String vetName;
    private String hospitalName;
}
