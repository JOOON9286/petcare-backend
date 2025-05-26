package com.example.vet_backend.service;

import com.example.vet_backend.dto.VetProfileDTO;
import com.example.vet_backend.entity.VetProfile;
import com.example.vet_backend.repository.VetProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class VetProfileService {

    private final VetProfileRepository vetProfileRepository;

    public VetProfileService(VetProfileRepository vetProfileRepository) {
        this.vetProfileRepository = vetProfileRepository;
    }

    public VetProfileDTO getVetProfileById(Long vetId) {
        VetProfile vetProfile = vetProfileRepository.findById(vetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 수의사를 찾을 수 없습니다."));

        return VetProfileDTO.builder()
                .vetId(vetProfile.getVetId())
                .specialty(vetProfile.getSpecialty())
                .licenseNumber(vetProfile.getLicenseNumber())
                .profilePhoto(vetProfile.getProfilePhoto())
                .ratingAvg(vetProfile.getRatingAvg())
                .reviewCount(vetProfile.getReviewCount())
                .isOnline(vetProfile.getIsOnline())
                .userId(vetProfile.getUser().getUserId())
                .hospitalId(vetProfile.getHospital() != null ? vetProfile.getHospital().getHospitalId() : null)
                .userName(vetProfile.getUser().getName())
                .hospitalName(vetProfile.getHospital() != null ? vetProfile.getHospital().getName() : null)
                .build();
    }
}