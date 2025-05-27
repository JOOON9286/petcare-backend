package com.example.vet_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VetProfileUpdateDTO {
    private String specialty;
    private String licenseNumber;
    private String profilePhoto;
    private Boolean isOnline;
}
