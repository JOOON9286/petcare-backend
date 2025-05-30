package com.example.vet_backend.repository;

import com.example.vet_backend.entity.Pet;
import com.example.vet_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(User user);
}

