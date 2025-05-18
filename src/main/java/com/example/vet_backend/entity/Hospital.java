package com.example.vet_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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