package com.example.vet_backend.repository;

import com.example.vet_backend.entity.VetProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VetProfileRepository extends JpaRepository<VetProfile, Long> {

    Optional<VetProfile> findByUser_UserId(Long userId);


}
