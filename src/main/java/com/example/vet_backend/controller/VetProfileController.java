package com.example.vet_backend.controller;

import com.example.vet_backend.dto.VetProfileDTO;
import com.example.vet_backend.service.VetProfileService;
import com.example.vet_backend.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //수의사 정보 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllVetProfiles() {
        try {
            List<VetProfileDTO> vetProfiles = vetProfileService.getAllVetProfiles();
            return ResponseEntity.ok(vetProfiles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "서버 오류: " + e.getMessage()));
        }
    }

    //수의사 정보 조회
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

    //수의사 정보 수정
    @PutMapping("/me")
    public ResponseEntity<?> updateVetProfile(HttpServletRequest request, @RequestBody VetProfileDTO dto) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            String email = jwtTokenProvider.getEmail(token);

            VetProfileDTO updated = vetProfileService.updateVetProfile(email, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "서버 오류: " + e.getMessage()));
        }
    }


}