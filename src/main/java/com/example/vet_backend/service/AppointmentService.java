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

    // 예약 생성
    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        VetProfile vet = vetProfileRepository.findById(dto.getVetId())
                .orElseThrow(() -> new IllegalArgumentException("수의사 없음"));

        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new IllegalArgumentException("반려동물 없음"));

        Appointment appointment = new Appointment();
        appointment.setTitle(dto.getTitle());
        appointment.setStatus("요청됨");  // 기본 예약 상태
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

    // 사용자 예약 목록 조회
    public List<AppointmentDTO> getAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUser_UserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 수의사의 유저ID로 예약 목록 조회
    public List<AppointmentDTO> getAppointmentsByVetUserId(Long userId) {
        VetProfile vet = vetProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 userId로 수의사를 찾을 수 없습니다."));
        return getAppointmentsByVet(vet.getVetId());
    }

    // 수의사 ID로 예약 목록 조회
    public List<AppointmentDTO> getAppointmentsByVet(Long vetId) {
        List<Appointment> list = appointmentRepository.findByVet_VetId(vetId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 예약 상세 조회 (수의사/관리자용)
    public AppointmentDTO getAppointmentDetail(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        return convertToDTO(appointment);
    }

    // 사용자 본인의 예약 상세 조회
    public AppointmentDTO getAppointmentDetailByUser(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        if (!appointment.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 예약만 조회할 수 있습니다.");
        }
        return convertToDTO(appointment);
    }

    // 예약 상태 업데이트 (수의사용)
    public void updateStatus(Long appointmentId, String newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
    }

    // 사용자 본인의 예약 수정
    public AppointmentDTO updateAppointmentByUser(Long appointmentId, Long userId, AppointmentDTO dto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));

        if (!appointment.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 예약만 수정할 수 있습니다.");
        }
        if ("완료".equals(appointment.getStatus()) || "취소".equals(appointment.getStatus())) {
            throw new IllegalStateException("완료되거나 취소된 예약은 수정할 수 없습니다.");
        }

        appointment.setTitle(dto.getTitle());
        appointment.setScheduledTime(dto.getScheduledTime());
        appointment.setSymptoms(dto.getSymptoms());
        appointment.setMedicalHistory(dto.getMedicalHistory());
        appointment.setAdditionalInfo(dto.getAdditionalInfo());

        Appointment saved = appointmentRepository.save(appointment);
        return convertToDTO(saved);
    }

    // 사용자 본인의 예약 삭제
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

    // 엔티티 -> DTO 변환
    private AppointmentDTO convertToDTO(Appointment a) {
        return AppointmentDTO.builder()
                .appointmentId(a.getAppointmentId())
                .title(a.getTitle())
                .status(a.getStatus())
                .scheduledTime(a.getScheduledTime())
                .createdAt(a.getCreatedAt())
                .userId(a.getUser().getUserId())
                .userName(a.getUser().getName())
                .userPhone(a.getUser().getPhone())
                .vetId(a.getVet().getVetId())
                .vetName(a.getVet().getUser() != null ? a.getVet().getUser().getName() : null)
                .hospitalName(a.getVet().getHospital() != null ? a.getVet().getHospital().getName() : null)
                .petId(a.getPet().getPetId())
                .petName(a.getPet().getName())
                .petSpecies(a.getPet().getSpecies())
                .petBreed(a.getPet().getBreed())
                .petGender(a.getPet().getGender())
                .petWeight(a.getPet().getWeight())
                .petBirthDate(a.getPet().getBirthDate())
                .symptoms(a.getSymptoms())
                .medicalHistory(a.getMedicalHistory())
                .additionalInfo(a.getAdditionalInfo())
                .build();
    }
}
