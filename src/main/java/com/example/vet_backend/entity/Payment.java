package com.example.vet_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Integer amount;
    private String method;
    private String status; // e.g., PENDING, PAID, FAILED

    @OneToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;
}
