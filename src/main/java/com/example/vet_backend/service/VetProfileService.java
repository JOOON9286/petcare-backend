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

    public VetProfileDTO getVetProfileByEmail(String email) {
        VetProfile vetProfile = vetProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 수의사를 찾을 수 없습니다."));

        return VetProfileDTO.builder()
                .vetId(vetProfile.getVetId())
                .specialty(vetProfile.getSpecialty())
                .licenseNumber(vetProfile.getLicenseNumber())
                .profilePhoto(vetProfile.getProfilePhoto())
                .ratingAvg(vetProfile.getRatingAvg())
                .reviewCount(vetProfile.getReviewCount())
                .isOnline(vetProfile.getIsOnline())
                .userId(vetProfile.getUser().getUserId())
                .userName(vetProfile.getUser().getName())
                .hospitalId(vetProfile.getHospital() != null ? vetProfile.getHospital().getHospitalId() : null)
                .hospitalName(vetProfile.getHospital() != null ? vetProfile.getHospital().getName() : null)
                .build();
    }

}