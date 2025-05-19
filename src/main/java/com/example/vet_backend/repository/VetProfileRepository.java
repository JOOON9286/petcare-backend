package com.example.vet_backend.repository;

import com.example.vet_backend.entity.VetProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetProfileRepository extends JpaRepository<VetProfile, Long> {
}
