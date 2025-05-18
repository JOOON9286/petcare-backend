package com.example.vet_backend.entity;

//처방전

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    private String medicineName;
    private String dosage;
    private String sideEffects;
    private String notes;
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
