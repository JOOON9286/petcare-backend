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

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VetProfileRepository vetProfileRepository;
    private final PetRepository petRepository;

    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        VetProfile vet = vetProfileRepository.findById(dto.getVetId())
                .orElseThrow(() -> new IllegalArgumentException("수의사 없음"));

        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new IllegalArgumentException("반려동물 없음"));

        Appointment appointment = new Appointment();
        appointment.setTitle(dto.getTitle());
        appointment.setStatus("예약");
        appointment.setStatusCall("대기");
        appointment.setScheduledTime(dto.getScheduledTime());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUser(user);
        appointment.setVet(vet);
        appointment.setPet(pet);
        appointment.setSymptoms(dto.getSymptoms());
        appointment.setMedicalHistory(dto.getMedicalHistory());
        appointment.setAdditionalInfo(dto.getAdditionalInfo());

        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentDTO.builder()
                .appointmentId(saved.getAppointmentId())
                .title(saved.getTitle())
                .status(saved.getStatus())
                .statusCall(saved.getStatusCall())
                .scheduledTime(saved.getScheduledTime())
                .createdAt(saved.getCreatedAt())
                .userId(saved.getUser().getUserId())
                .vetId(saved.getVet().getVetId())
                .petId(saved.getPet().getPetId())
                .symptoms(saved.getSymptoms())
                .medicalHistory(saved.getMedicalHistory())
                .additionalInfo(saved.getAdditionalInfo())
                .build();
    }
}
