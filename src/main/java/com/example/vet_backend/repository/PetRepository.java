package com.example.vet_backend.repository;

import com.example.vet_backend.entity.Pet;
import com.example.vet_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
//    List<Pet> findByOwner(User user);
    List<Pet> findByOwnerAndIsDeletedFalse(User user); // 삭제 안 된 것만 조회
    Optional<Pet> findByPetIdAndIsDeletedFalse(Long petId); // 상세 조회 시도 시에도 조건
}

