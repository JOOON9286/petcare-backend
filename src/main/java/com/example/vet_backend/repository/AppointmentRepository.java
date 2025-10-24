package com.example.vet_backend.repository;

import com.example.vet_backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // 👈 @Query import
import org.springframework.data.repository.query.Param; // 👈 @Param import
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime; // 👈 LocalDateTime import
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 수의사 ID로 예약 목록 조회
    List<Appointment> findByVet_VetId(Long vetId);

    // 사용자 ID로 예약 목록 조회
    List<Appointment> findByUser_UserId(Long userId);

    // 📌 [수정] 긴 이름 대신 @Query 사용
    @Query("SELECT a FROM Appointment a " +
            "WHERE a.user.userId = :userId " +
            "AND a.vet.vetId = :vetId " +
            "AND a.status = :status " +
            "AND a.scheduledTime > :now " +
            "ORDER BY a.scheduledTime ASC")
    List<Appointment> findNearestAppointments( // 👈 메서드 이름 짧게 변경
                                               @Param("userId") Long userId,       // 👈 @Param으로 파라미터 이름 명시
                                               @Param("vetId") Long vetId,
                                               @Param("status") String status,
                                               @Param("now") LocalDateTime now
    );

    // 사용자 ID와 수의사 ID로 첫 번째 예약 찾기 (이전 코드 유지)
    Optional<Appointment> findFirstByUser_UserIdAndVet_VetId(Long userId, Long vetId);

}