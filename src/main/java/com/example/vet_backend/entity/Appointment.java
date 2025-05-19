package com.example.vet_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    private String title;
    private String status;
    private String statusCall;

    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;

    // 추가 정보 필드
    @Column(columnDefinition = "TEXT")
    private String symptoms; // 증상

    @Column(columnDefinition = "TEXT")
    private String medicalHistory; // 과거 병력

    @Column(columnDefinition = "TEXT")
    private String additionalInfo; // 알레르기, 복용 중인 약물 등

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vet_id")
    private VetProfile vet;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}

