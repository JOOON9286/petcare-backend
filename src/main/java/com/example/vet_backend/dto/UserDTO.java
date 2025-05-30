package com.example.vet_backend.dto;


import lombok.*;

import javax.management.relation.Role;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String email;
    private String password;
    private String phone;
    private Role role;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
