package com.example.vet_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login {

    private String email;
    private String password;
}
