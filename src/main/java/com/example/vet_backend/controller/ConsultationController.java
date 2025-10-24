package com.example.vet_backend.controller;

import com.example.vet_backend.service.AppointmentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consult/request")
@RequiredArgsConstructor
public class ConsultationController {

    private final AppointmentService appointmentService;

    @Data
    public static class ConsultRequestDto {
        private Long vetId;
    }

    @Data
    public static class ConsultResponseDto {
        private String reservationId;

        public ConsultResponseDto(String reservationId) {
            this.reservationId = reservationId;
        }
    }

    @PostMapping
    public ResponseEntity<ConsultResponseDto> createConsultRequest(@RequestBody ConsultRequestDto requestDto) {

        // 1. 현재 로그인한 사용자(보호자)의 ID 가져오기 (토큰 기반)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).build();
        }

        Long userId;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            try {
                // 로그인한 사용자 ID (예: 1번) 추출
                userId = Long.parseLong(((UserDetails) principal).getUsername());
            } catch (NumberFormatException e) {
                System.err.println("❌ 사용자 ID 파싱 오류: " + ((UserDetails) principal).getUsername());
                return ResponseEntity.status(500).build();
            }
        } else {
            return ResponseEntity.status(401).build();
        }

        // 📌 [하드코딩 확정] 수의사 User ID는 2L로 고정
        Long fixedVetUserId = 2L;

        System.out.println("Current consult attempt: userId (보호자)=" + userId + ", fixedVetUserId (수의사)=" + fixedVetUserId);

        // 2. 예약 ID 찾기 로직: Service에서 User ID 2를 실제 Vet ID 1로 변환하여 조회
        Long appointmentId = null;
        try {
            // Service 호출: (1)과 (2)를 사용해 DB에서 예약 ID를 찾음
            appointmentId = appointmentService.findAppointmentIdByUserAndVet(userId, fixedVetUserId);
        } catch (IllegalStateException e) {
            System.err.println("❌ 컨트롤러 레벨: 수의사 프로필을 찾을 수 없음. " + e.getMessage());
            return ResponseEntity.status(404).build();
        }

        // 3. 찾지 못한 경우 404 반환 (DB에 조건에 맞는 예약이 없음)
        if (appointmentId == null) {
            System.err.println("❌ 해당 사용자(ID: " + userId + ")와 수의사(User ID: 2)의 '접수됨' 상태 예약을 찾을 수 없습니다. (DB 데이터 확인 필수)");
            return ResponseEntity.status(404).build();
        }

        // 4. 응답 반환
        String reservationId = String.valueOf(appointmentId);
        System.out.println("사용할 Reservation ID (예약 ID): " + reservationId);

        ConsultResponseDto responseDto = new ConsultResponseDto(reservationId);
        return ResponseEntity.ok(responseDto);
    }
}