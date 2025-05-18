package com.example.vet_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
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

