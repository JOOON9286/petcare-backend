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
    private String name;
    private String specialty;
    private String licenseNumber;
    private String profilePhoto;
    private Integer ratingAvg;
    private Integer reviewCount;
    private Boolean isOnline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
}
