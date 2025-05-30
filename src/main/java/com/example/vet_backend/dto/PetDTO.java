package com.example.vet_backend.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDTO {

    private Long petId;
    private String name;
    private String species;
    private String breed;
    private String gender;
    private Float weight;
    private LocalDateTime birthDate;
    private String medicalHistory;
    private LocalDateTime createdAt;
    private Long userId;
    private boolean neutered;

}
