package com.example.vet_backend.controller;

import com.example.vet_backend.dto.PetDTO;
import com.example.vet_backend.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    // 1. 반려동물 등록
    @PostMapping
    public ResponseEntity<PetDTO> registerPet(@RequestBody PetDTO dto) {
        PetDTO created = petService.registerPet(dto);
        return ResponseEntity.ok(created);
    }

    // 2. 특정 유저의 반려동물 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PetDTO>> getPetsByUserId(@PathVariable Long userId) {
        List<PetDTO> pets = petService.getPetsByUserId(userId);
        return ResponseEntity.ok(pets);
    }

    // 3. 반려동물 수정
    @PutMapping("/{petId}")
    public ResponseEntity<PetDTO> updatePet(
            @PathVariable Long petId,
            @RequestBody PetDTO dto
    ) {
        PetDTO updated = petService.updatePet(petId, dto);
        return ResponseEntity.ok(updated);
    }

    // 4. 반려동물 삭제
    @DeleteMapping("/{petId}/user/{userId}")
    public ResponseEntity<Void> deletePet(
            @PathVariable Long petId,
            @PathVariable Long userId
    ) {
        petService.deletePet(petId, userId);
        return ResponseEntity.ok().build();
    }

}
