package com.example.vet_backend.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Signup {

    private String email;
    private String password;
    private String name;
    private String phone;
}
