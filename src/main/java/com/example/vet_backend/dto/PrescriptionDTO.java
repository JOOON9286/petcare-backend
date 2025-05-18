package com.example.vet_backend.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDTO {
    private Long prescriptionId;
    private String medicineName;
    private String dosage;
    private String sideEffects;
    private String notes;
    private LocalDateTime createdAt;
    private Long appointmentId;
}
