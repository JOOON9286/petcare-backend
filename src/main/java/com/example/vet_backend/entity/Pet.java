package com.example.vet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

    private String name;
    private String species;
    private String breed;
    private String gender;
    private Float weight;
    private LocalDateTime birthDate;

    @Column(length = 255)
    private String medicalHistory;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean neutered = false;

    @Column(nullable = false)
    private boolean isDeleted = false; // 펫이 삭제되어도 진료일정 삭제 여부를 결정하기 위한 Soft Delete

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
