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
    private String status; // 예약확정, 진료완료 등

    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;

    // 진료 전 문진표 내용
    @Column(columnDefinition = "TEXT")
    private String symptoms;
    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vet_id")
    private VetProfile vet;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    // 📌 [수정] 예약 정보를 조회할 때 처방전 발행 여부를 알기 위해 연결 (DB 컬럼 생성 안됨)
    @OneToOne(mappedBy = "appointment", fetch = FetchType.LAZY)
    private Prescription prescription;
}