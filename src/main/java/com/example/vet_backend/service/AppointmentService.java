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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VetProfileRepository vetProfileRepository;
    private final PetRepository petRepository;

    // 1. 예약 생성
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

    // 2. 사용자 예약 목록 조회
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUser_UserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 3. 수의사의 유저ID로 예약 목록 조회
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByVetUserId(Long userId) {
        VetProfile vet = vetProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 userId로 수의사를 찾을 수 없습니다."));
        return getAppointmentsByVet(vet.getVetId());
    }

    // 4. 수의사 ID로 예약 목록 조회
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByVet(Long vetId) {
        List<Appointment> list = appointmentRepository.findByVet_VetId(vetId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 5. 예약 상세 조회 (단건 조회 - 화상 진료용) [이게 필요했습니다!]
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentDetail(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다. ID: " + appointmentId));
        return convertToDTO(appointment);
    }

    // 6. 사용자 본인의 예약 상세 조회
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentDetailByUser(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        if (!appointment.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 예약만 조회할 수 있습니다.");
        }
        return convertToDTO(appointment);
    }

    // 7. 예약 상태 변경 (따옴표 제거 로직 포함)
    public void updateStatus(Long appointmentId, String newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));

        String normalizedStatus = newStatus;
        if (newStatus != null) {
            if (newStatus.startsWith("\"") && newStatus.endsWith("\"")) {
                normalizedStatus = newStatus.substring(1, newStatus.length() - 1);
            } else if (newStatus.startsWith("'") && newStatus.endsWith("'")) {
                normalizedStatus = newStatus.substring(1, newStatus.length() - 1);
            }
        }

        appointment.setStatus(normalizedStatus);
        appointmentRepository.save(appointment);
    }

    // 8. 사용자 본인의 예약 수정
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

    // 9. 사용자 본인의 예약 삭제
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

    // 10. 화상 진료용 예약 ID 찾기 (참고용 유지)
    public Long findAppointmentIdByUserAndVet(Long userId, Long vetUserId) {
        VetProfile vetProfile = vetProfileRepository.findByUserUserId(vetUserId)
                .orElseThrow(() -> new IllegalStateException("수의사 프로필을 찾을 수 없습니다."));

        Long actualVetId = vetProfile.getVetId();

        List<Appointment> appointments = appointmentRepository.findNearestAppointments(
                userId,
                actualVetId,
                "접수됨",
                LocalDateTime.now()
        );

        if (appointments.isEmpty()) return null;
        return appointments.get(0).getAppointmentId();
    }

    // Entity -> DTO 변환
    private AppointmentDTO convertToDTO(Appointment a) {
        if (a == null) return null;

        return AppointmentDTO.builder()
                .appointmentId(a.getAppointmentId())
                .title(a.getTitle())
                .status(a.getStatus())
                .scheduledTime(a.getScheduledTime())
                .createdAt(a.getCreatedAt())

                // User 정보
                .userId(a.getUser() != null ? a.getUser().getUserId() : null)
                .userName(a.getUser() != null ? a.getUser().getName() : null)
                .userPhone(a.getUser() != null ? a.getUser().getPhone() : null)

                // Vet 정보
                .vetId(a.getVet() != null ? a.getVet().getVetId() : null)
                .vetName(a.getVet() != null && a.getVet().getUser() != null ? a.getVet().getUser().getName() : null)
                .hospitalName(a.getVet() != null && a.getVet().getHospital() != null ? a.getVet().getHospital().getName() : null)

                // Pet 정보
                .petId(a.getPet() != null ? a.getPet().getPetId() : null)
                .petName(a.getPet() != null ? a.getPet().getName() : null)
                .petSpecies(a.getPet() != null ? a.getPet().getSpecies() : null)
                .petBreed(a.getPet() != null ? a.getPet().getBreed() : null)
                .petGender(a.getPet() != null ? a.getPet().getGender() : null)
                .petWeight(a.getPet() != null ? a.getPet().getWeight() : null)
                .petBirthDate(a.getPet() != null ? a.getPet().getBirthDate() : null)

                // 상세 정보
                .symptoms(a.getSymptoms())
                .medicalHistory(a.getMedicalHistory())
                .additionalInfo(a.getAdditionalInfo())
                .build();
    }
}