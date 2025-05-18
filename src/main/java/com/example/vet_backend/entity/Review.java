package com.example.vet_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private Integer rating;
    private String content;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
