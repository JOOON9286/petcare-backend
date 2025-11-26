package com.example.vet_backend.service;

import com.example.vet_backend.dto.PrescriptionDTO;
import com.example.vet_backend.entity.Appointment;
import com.example.vet_backend.entity.Medicine;
import com.example.vet_backend.entity.Prescription;
import com.example.vet_backend.repository.AppointmentRepository;
import com.example.vet_backend.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    /**
     * [수의사] 처방전 생성 및 저장
     */
    @Transactional
    public Long createPrescription(PrescriptionDTO dto) {
        // 1. 예약 정보 조회
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 정보를 찾을 수 없습니다. ID=" + dto.getAppointmentId()));

        // 2. 중복 발급 체크
        if (prescriptionRepository.findByAppointment_AppointmentId(dto.getAppointmentId()).isPresent()) {
            throw new IllegalStateException("이미 해당 예약에 대해 발급된 처방전이 존재합니다.");
        }

        // 3. 처방전 엔티티 생성
        Prescription prescription = Prescription.builder()
                .diagnosis(dto.getDiagnosis())
                .usageInstructions(dto.getUsageInstructions())
                .nextAppointment(dto.getNextAppointment())
                .paymentStatus("PENDING") // 초기 상태: 결제 대기
                .createdAt(LocalDateTime.now())

                // 📌 [수정됨] Pet 이름 가져오기 (.getPetName() -> .getName())
                .petName(appointment.getPet().getName())

                .petSpecies(appointment.getPet().getSpecies())
                .petWeight(String.valueOf(appointment.getPet().getWeight()))
                .doctorName(appointment.getVet().getUser().getName())

                // 📌 [수정됨] 병원 이름 가져오기
                .clinicName(appointment.getVet().getHospital().getName())

                .appointment(appointment) // 연관관계 설정
                .build();

        // 4. 약품 리스트 추가
        if (dto.getMedicines() != null) {
            for (PrescriptionDTO.MedicineDTO medDto : dto.getMedicines()) {
                Medicine medicine = Medicine.builder()
                        .name(medDto.getName())
                        .dosage(medDto.getDosage())
                        .duration(medDto.getDuration())
                        .note(medDto.getNote())
                        .build();

                prescription.addMedicine(medicine);
            }
        }

        // 5. DB 저장
        Prescription saved = prescriptionRepository.save(prescription);

        return saved.getPrescriptionId();
    }

    /**
     * [사용자] 내 처방전 목록 조회
     */
    public List<PrescriptionDTO> getMyPrescriptions(Long userId) {
        List<Prescription> prescriptions = prescriptionRepository.findByAppointment_User_UserIdOrderByCreatedAtDesc(userId);

        return prescriptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Entity -> DTO 변환
    private PrescriptionDTO convertToDTO(Prescription p) {
        return PrescriptionDTO.builder()
                .prescriptionId(p.getPrescriptionId())
                .appointmentId(p.getAppointment().getAppointmentId())
                .diagnosis(p.getDiagnosis())
                .usageInstructions(p.getUsageInstructions())
                .nextAppointment(p.getNextAppointment())
                .paymentStatus(p.getPaymentStatus())
                .createdAt(p.getCreatedAt())
                .petName(p.getPetName())
                .doctorName(p.getDoctorName())
                .clinicName(p.getClinicName())
                .medicines(p.getMedicines().stream().map(m -> PrescriptionDTO.MedicineDTO.builder()
                        .name(m.getName())
                        .dosage(m.getDosage())
                        .duration(m.getDuration())
                        .note(m.getNote())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}