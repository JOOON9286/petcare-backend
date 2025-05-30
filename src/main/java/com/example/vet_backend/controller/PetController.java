package com.example.vet_backend.controller;

import com.example.vet_backend.dto.PetDTO;
import com.example.vet_backend.entity.Pet;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.repository.PetRepository;
import com.example.vet_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class PetController {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    //  반려동물 등록
    @PostMapping
    public ResponseEntity<?> registerPet(@RequestBody PetDTO petDTO, Authentication auth) {
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Pet pet = Pet.builder()
                .name(petDTO.getName())
                .species(petDTO.getSpecies())
                .breed(petDTO.getBreed())
                .gender(petDTO.getGender())
                .weight(petDTO.getWeight())
                .birthDate(petDTO.getBirthDate())
                .medicalHistory(petDTO.getMedicalHistory())
                .createdAt(LocalDateTime.now())
                .neutered(petDTO.isNeutered())
                .owner(user)
                .build();

        Pet savedPet = petRepository.save(pet);
        return ResponseEntity.ok(savedPet);
    }

    //  반려동물 조회
    @GetMapping
    public ResponseEntity<?> getPets(Authentication auth) {
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        List<Pet> pets = petRepository.findByOwner(user);
        return ResponseEntity.ok(pets);
    }

    //  반려동물 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePet(@PathVariable Long id, @RequestBody PetDTO petDTO, Authentication auth) {
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("반려동물을 찾을 수 없습니다."));

        // 주인 확인
        if (!pet.getOwner().getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).body("수정 권한이 없습니다.");
        }

        pet.setName(petDTO.getName());
        pet.setSpecies(petDTO.getSpecies());
        pet.setBreed(petDTO.getBreed());
        pet.setWeight(petDTO.getWeight());
        pet.setGender(petDTO.getGender());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setMedicalHistory(petDTO.getMedicalHistory());
        pet.setNeutered(petDTO.isNeutered());

        petRepository.save(pet);
        return ResponseEntity.ok(pet);
    }

    //  반려동물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id, Authentication auth) {
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("반려동물을 찾을 수 없습니다."));

        // 주인 확인
        if (!pet.getOwner().getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).body("삭제 권한이 없습니다.");
        }

        petRepository.delete(pet);
        return ResponseEntity.ok("삭제 완료");
    }
}
