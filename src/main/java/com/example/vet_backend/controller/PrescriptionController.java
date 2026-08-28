package com.example.vet_backend.controller;

import com.example.vet_backend.dto.PrescriptionDTO;
import com.example.vet_backend.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User; // [핵심] 스프링 기본 User import
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    /**
     * 1. [수의사] 처방전 발급 API
     */
    @PostMapping("/create")
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionDTO dto) {
        try {
            Long createdId = prescriptionService.createPrescription(dto);
            return ResponseEntity.ok("처방전이 성공적으로 발급되었습니다. (ID: " + createdId + ")");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("처방전 발급 중 오류가 발생했습니다.");
        }
    }

    /**
     * 2. [사용자] 내 처방전 목록 조회 API
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyPrescriptions(@AuthenticationPrincipal User userDetails) { // CustomUserDetails -> User 변경
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401).body("로그인이 필요합니다.");
            }

            // JwtTokenProvider에서 userId를 Subject(username)로 넣었으므로
            // userDetails.getUsername()이 곧 DB의 PK(userId)입니다.
            // String으로 저장된 ID를 Long으로 변환합니다.
            Long userId = Long.parseLong(userDetails.getUsername());

            // 서비스 호출
            List<PrescriptionDTO> list = prescriptionService.getMyPrescriptions(userId);

            return ResponseEntity.ok(list);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("유효하지 않은 사용자 ID입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("처방전 목록 조회 실패");
        }
    }
}