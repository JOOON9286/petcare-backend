package com.example.vet_backend.controller;

import com.example.vet_backend.dto.VetProfileDTO;
import com.example.vet_backend.service.VetProfileService;
import com.example.vet_backend.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vets")
public class VetProfileController {

    private final VetProfileService vetProfileService;
    private final JwtTokenProvider jwtTokenProvider;

    public VetProfileController(VetProfileService vetProfileService, JwtTokenProvider jwtTokenProvider) {
        this.vetProfileService = vetProfileService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<?> getAllVetProfiles() {
        try {
            List<VetProfileDTO> vetProfiles = vetProfileService.getAllVetProfiles();
            return ResponseEntity.ok(vetProfiles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "서버 오류: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInVetProfile(HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            String email = jwtTokenProvider.getEmail(token); //이메일 추출

            VetProfileDTO vetProfile = vetProfileService.getVetProfileByEmail(email);
            return ResponseEntity.ok(vetProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "서버 오류: " + e.getMessage()));
        }
    }


}