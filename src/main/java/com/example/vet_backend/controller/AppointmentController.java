package com.example.vet_backend.controller;

import com.example.vet_backend.dto.AppointmentDTO;
import com.example.vet_backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // 1. 예약 생성
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto) {
        AppointmentDTO created = appointmentService.createAppointment(dto);
        return ResponseEntity.ok(created);
    }

    // 2. 사용자 예약 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByUser(@PathVariable Long userId) {
        List<AppointmentDTO> list = appointmentService.getAppointmentsByUser(userId);
        return ResponseEntity.ok(list);
    }

    // 3. 수의사 기준 진료 목록
    @GetMapping("/vet/user/{userId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByVetUserId(@PathVariable Long userId) {
        List<AppointmentDTO> list = appointmentService.getAppointmentsByVetUserId(userId);
        return ResponseEntity.ok(list);
    }

    // 4. 예약 상세 조회 (userId 검증 포함)
    @GetMapping("/{appointmentId}/user/{userId}")
    public ResponseEntity<AppointmentDTO> getAppointmentDetailByUser(
            @PathVariable Long appointmentId,
            @PathVariable Long userId) {
        AppointmentDTO dto = appointmentService.getAppointmentDetailByUser(appointmentId, userId);
        return ResponseEntity.ok(dto);
    }

    // 5. 예약 수정
    @PutMapping("/{appointmentId}/user/{userId}")
    public ResponseEntity<AppointmentDTO> updateAppointmentByUser(
            @PathVariable Long appointmentId,
            @PathVariable Long userId,
            @RequestBody AppointmentDTO dto) {
        AppointmentDTO updated = appointmentService.updateAppointmentByUser(appointmentId, userId, dto);
        return ResponseEntity.ok(updated);
    }

    // 6. 예약 삭제
    @DeleteMapping("/{appointmentId}/user/{userId}")
    public ResponseEntity<Void> deleteAppointmentByUser(
            @PathVariable Long appointmentId,
            @PathVariable Long userId) {
        appointmentService.deleteAppointmentByUser(appointmentId, userId);
        return ResponseEntity.noContent().build();
    }

    // 7. 예약 상태만 변경
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Void> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestBody String newStatus) {
        appointmentService.updateStatus(appointmentId, newStatus);
        return ResponseEntity.ok().build();
    }

    // 8. 화상 진료용 예약 상세 조회 (ID만으로 조회)
    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getAppointmentDetail(@PathVariable Long appointmentId) {
        try {
            // 서비스에 메서드가 없다면 여기서 바로 Repository를 불러도 되지만,
            // 서비스에 메서드를 만들어두셨다면 아래처럼 호출합니다.
            // 만약 서비스에 getAppointmentDetail 메서드가 없다면 에러가 날 수 있으니
            // AppointmentService 파일도 확인해야 합니다.
            return ResponseEntity.ok(appointmentService.getAppointmentDetail(appointmentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("예약 조회 실패: " + e.getMessage());
        }
    }
}