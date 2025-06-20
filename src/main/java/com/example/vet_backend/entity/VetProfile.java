package com.example.vet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vet_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VetProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vetId;

    private String specialty;
    private String licenseNumber;

    @Column(columnDefinition = "TEXT")
    private String profilePhoto;

    private Integer ratingAvg;
    private Integer reviewCount;
    private Boolean isOnline;

    @Column(columnDefinition = "TEXT")
    private String introduction;       // 수의사 소개

    private String availableDays;      // 진료 가능 요일 (예: 월,수,금)
    private String availableTime;      // 진료 가능 시간 (예: 10:00~18:00)

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
}

