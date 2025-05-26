package com.example.vet_backend.controller;

import com.example.vet_backend.dto.VetProfileDTO;
import com.example.vet_backend.service.VetProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vets")
public class VetProfileController {

    private final VetProfileService vetProfileService;

    public VetProfileController(VetProfileService vetProfileService) {
        this.vetProfileService = vetProfileService;
    }

    @GetMapping
    public ResponseEntity<?> getVetProfileByQuery(@RequestParam Long vetId) {
        try {
            VetProfileDTO vetProfile = vetProfileService.getVetProfileById(vetId);
            return ResponseEntity.ok(vetProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "서버 오류: " + e.getMessage()));
        }
    }
}