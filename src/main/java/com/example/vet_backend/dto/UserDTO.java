package com.example.vet_backend.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
