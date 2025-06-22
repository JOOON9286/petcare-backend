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

    // 예약 생성
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto) {
        AppointmentDTO created = appointmentService.createAppointment(dto);
        return ResponseEntity.ok(created);
    }

    // 사용자 예약 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByUser(@PathVariable Long userId) {
        List<AppointmentDTO> list = appointmentService.getAppointmentsByUser(userId);
        return ResponseEntity.ok(list);
    }

    // 수의사 기준 진료 목록
    @GetMapping("/vet/user/{userId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByVetUserId(@PathVariable Long userId) {
        List<AppointmentDTO> list = appointmentService.getAppointmentsByVetUserId(userId);
        return ResponseEntity.ok(list);
    }

    // 예약 상세 조회 (userId 함께 확인)
    @GetMapping("/{appointmentId}/user/{userId}")
    public ResponseEntity<AppointmentDTO> getAppointmentDetailByUser(
            @PathVariable Long appointmentId,
            @PathVariable Long userId) {
        AppointmentDTO dto = appointmentService.getAppointmentDetailByUser(appointmentId, userId);
        return ResponseEntity.ok(dto);
    }

    // 예약 수정 (userId 검증 포함)
    @PutMapping("/{appointmentId}/user/{userId}")
    public ResponseEntity<AppointmentDTO> updateAppointmentByUser(
            @PathVariable Long appointmentId,
            @PathVariable Long userId,
            @RequestBody AppointmentDTO dto) {
        AppointmentDTO updated = appointmentService.updateAppointmentByUser(appointmentId, userId, dto);
        return ResponseEntity.ok(updated);
    }

    // 예약 삭제 (userId 검증 포함)
    @DeleteMapping("/{appointmentId}/user/{userId}")
    public ResponseEntity<Void> deleteAppointmentByUser(
            @PathVariable Long appointmentId,
            @PathVariable Long userId) {
        appointmentService.deleteAppointmentByUser(appointmentId, userId);
        return ResponseEntity.noContent().build();
    }

    // 예약 상태만 변경 (수의사용)
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Void> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestBody String newStatus) {
        appointmentService.updateStatus(appointmentId, newStatus);
        return ResponseEntity.ok().build();
    }
}

