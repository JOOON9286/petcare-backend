package com.example.vet_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String name;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String profileUserPhoto;

    @Column(length = 500)
    private String refreshToken;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    @CreatedDate
    private LocalDateTime createdAt;    //생성일
    @LastModifiedDate
    private LocalDateTime updatedAt;    //업뎃일

}
