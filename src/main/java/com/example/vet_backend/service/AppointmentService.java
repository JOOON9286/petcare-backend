package com.example.vet_backend.service;

import com.example.vet_backend.dto.AppointmentDTO;
import com.example.vet_backend.entity.Appointment;
import com.example.vet_backend.entity.Pet;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.entity.VetProfile;
import com.example.vet_backend.repository.AppointmentRepository;
import com.example.vet_backend.repository.PetRepository;
import com.example.vet_backend.repository.UserRepository;
import com.example.vet_backend.repository.VetProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VetProfileRepository vetProfileRepository;
    private final PetRepository petRepository;

    // 예약 생성 (기존 로직 유지)
    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        VetProfile vet = vetProfileRepository.findById(dto.getVetId())
                .orElseThrow(() -> new IllegalArgumentException("수의사 없음"));
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new IllegalArgumentException("반려동물 없음"));

        Appointment appointment = new Appointment();
        appointment.setTitle(dto.getTitle());
        appointment.setStatus("요청됨");
        appointment.setScheduledTime(dto.getScheduledTime());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUser(user);
        appointment.setVet(vet);
        appointment.setPet(pet);
        appointment.setSymptoms(dto.getSymptoms());
        appointment.setMedicalHistory(dto.getMedicalHistory());
        appointment.setAdditionalInfo(dto.getAdditionalInfo());

        Appointment saved = appointmentRepository.save(appointment);
        return convertToDTO(saved);
    }

    // 사용자 예약 목록 조회 (기존 로직 유지)
    public List<AppointmentDTO> getAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUser_UserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 수의사의 유저ID로 예약 목록 조회 (기존 로직 유지)
    public List<AppointmentDTO> getAppointmentsByVetUserId(Long userId) {
        VetProfile vet = vetProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 userId로 수의사를 찾을 수 없습니다."));
        return getAppointmentsByVet(vet.getVetId());
    }

    // 수의사 ID로 예약 목록 조회 (기존 로직 유지)
    public List<AppointmentDTO> getAppointmentsByVet(Long vetId) {
        List<Appointment> list = appointmentRepository.findByVet_VetId(vetId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 예약 상세 조회 (기존 로직 유지)
    public AppointmentDTO getAppointmentDetail(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        return convertToDTO(appointment);
    }

    // 사용자 본인의 예약 상세 조회 (기존 로직 유지)
    public AppointmentDTO getAppointmentDetailByUser(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        if (!appointment.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 예약만 조회할 수 있습니다.");
        }
        return convertToDTO(appointment);
    }

    // 예약 상태 업데이트 (기존 로직 유지)
    public void updateStatus(Long appointmentId, String newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
    }

    // 사용자 본인의 예약 수정 (기존 로직 유지)
    public AppointmentDTO updateAppointmentByUser(Long appointmentId, Long userId, AppointmentDTO dto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));

        if (!appointment.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 예약만 수정할 수 있습니다.");
        }
        if ("완료".equals(appointment.getStatus()) || "취소".equals(appointment.getStatus())) {
            throw new IllegalStateException("완료되거나 취소된 예약은 수정할 수 없습니다.");
        }

        if (dto.getTitle() != null) appointment.setTitle(dto.getTitle());
        if (dto.getScheduledTime() != null) appointment.setScheduledTime(dto.getScheduledTime());
        if (dto.getSymptoms() != null) appointment.setSymptoms(dto.getSymptoms());
        if (dto.getMedicalHistory() != null) appointment.setMedicalHistory(dto.getMedicalHistory());
        if (dto.getAdditionalInfo() != null) appointment.setAdditionalInfo(dto.getAdditionalInfo());

        Appointment saved = appointmentRepository.save(appointment);
        return convertToDTO(saved);
    }

    // 사용자 본인의 예약 삭제 (기존 로직 유지)
    public void deleteAppointmentByUser(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));

        if (!appointment.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 예약만 삭제할 수 있습니다.");
        }
        if ("완료".equals(appointment.getStatus())) {
            throw new IllegalStateException("완료된 예약은 삭제할 수 없습니다.");
        }

        appointmentRepository.delete(appointment);
    }

    // 📌 [핵심] 사용자 ID와 수의사 User ID를 매개변수로 받아 실제 Vet ID로 변환 후 조회
    public Long findAppointmentIdByUserAndVet(Long userId, Long vetUserId) {
        System.out.println("findAppointmentIdByUserAndVet 호출됨 - userId: " + userId + ", vetUserId: " + vetUserId);

        // 1. 수의사 User ID를 사용하여 해당 수의사의 VetProfile을 찾습니다.
        VetProfile vetProfile = vetProfileRepository.findByUserUserId(vetUserId)
                .orElseThrow(() -> {
                    System.err.println("❌ 수의사 User ID(" + vetUserId + ")에 연결된 VetProfile(실제 vetId)을 찾을 수 없습니다.");
                    return new IllegalStateException("수의사 프로필(VetProfile)을 찾을 수 없습니다.");
                });

        // 2. 실제 Appointment 테이블이 사용하는 Vet ID를 추출합니다.
        Long actualVetId = vetProfile.getVetId();
        System.out.println("변환된 실제 vetId (DB 예약 테이블 조회용): " + actualVetId);

        // 3. 추출된 actualVetId를 사용하여 예약 조회
        List<Appointment> appointments = appointmentRepository.findNearestAppointments(
                userId,
                actualVetId, // <-- 변환된 실제 Vet ID 사용
                "\"접수됨\"", // DB 상태 문자열과 일치 확인
                LocalDateTime.now()
        );

        if (appointments.isEmpty()) {
            System.out.println("조건에 맞는 예약 없음.");
            return null; // 찾지 못하면 null 반환 (컨트롤러가 404 처리)
        } else {
            Long appointmentId = appointments.get(0).getAppointmentId();
            System.out.println("찾아낸 예약 ID: " + appointmentId);
            return appointmentId;
        }
    }

    // 엔티티 -> DTO 변환 메서드 (기존 로직 유지)
    private AppointmentDTO convertToDTO(Appointment a) {
        if (a == null) return null;
        return AppointmentDTO.builder()
                .appointmentId(a.getAppointmentId())
                .title(a.getTitle())
                .status(a.getStatus())
                .scheduledTime(a.getScheduledTime())
                .createdAt(a.getCreatedAt())
                // User 정보 (Null 체크 포함)
                .userId(a.getUser() != null ? a.getUser().getUserId() : null)
                .userName(a.getUser() != null ? a.getUser().getName() : null)
                .userPhone(a.getUser() != null ? a.getUser().getPhone() : null)
                // Vet 정보 (Null 체크 포함)
                .vetId(a.getVet() != null ? a.getVet().getVetId() : null)
                .vetName(a.getVet() != null && a.getVet().getUser() != null ? a.getVet().getUser().getName() : null)
                .hospitalName(a.getVet() != null && a.getVet().getHospital() != null ? a.getVet().getHospital().getName() : null)
                // Pet 정보 (Null 체크 포함)
                .petId(a.getPet() != null ? a.getPet().getPetId() : null)
                .petName(a.getPet() != null ? a.getPet().getName() : null)
                .petSpecies(a.getPet() != null ? a.getPet().getSpecies() : null)
                .petBreed(a.getPet() != null ? a.getPet().getBreed() : null)
                .petGender(a.getPet() != null ? a.getPet().getGender() : null)
                .petWeight(a.getPet() != null ? a.getPet().getWeight() : null)
                .petBirthDate(a.getPet() != null ? a.getPet().getBirthDate() : null)
                // 기타 정보
                .symptoms(a.getSymptoms())
                .medicalHistory(a.getMedicalHistory())
                .additionalInfo(a.getAdditionalInfo())
                .build();
    }
}