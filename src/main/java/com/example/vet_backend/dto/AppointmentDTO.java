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
    private String statusCall;
    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;
    private Long userId;
    private Long vetId;
    private Long petId;
}
