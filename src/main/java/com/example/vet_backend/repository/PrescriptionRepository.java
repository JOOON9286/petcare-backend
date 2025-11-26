package com.example.vet_backend.repository;

import com.example.vet_backend.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    // 특정 예약 ID로 처방전 찾기
    Optional<Prescription> findByAppointment_AppointmentId(Long appointmentId);

    // 내 처방전 목록 조회 (최신순)
    List<Prescription> findByAppointment_User_UserIdOrderByCreatedAtDesc(Long userId);
}