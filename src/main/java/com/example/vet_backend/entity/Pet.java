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
    private Long petId; //반려동물 ID

    private String name; //반려동물 이름

    private String species; //반려동물 이름

    private String breed; // 종(개,고양이)

    private String gender; //성별

    private Float weight; //몸무게

    private LocalDateTime birthDate; //생년월일

    @Column(length = 255)
    private String medicalHistory; //병력 정보

    private LocalDateTime createdAt; //등록일

    @ManyToOne
    @JoinColumn(name = "user_id")   //사용자 ID
    private User owner;

    @Column(nullable = false)
    private boolean neutered = false;

}
