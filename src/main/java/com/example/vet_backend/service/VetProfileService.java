package com.example.vet_backend.service;

import com.example.vet_backend.dto.VetProfileDTO;
import com.example.vet_backend.entity.VetProfile;
import com.example.vet_backend.repository.VetProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .introduction(vetProfile.getIntroduction())
                .availableDays(vetProfile.getAvailableDays())
                .availableTime(vetProfile.getAvailableTime())
                .build();

    }

    // 수의사 전체 조회
    public List<VetProfileDTO> getAllVetProfiles() {
        return vetProfileRepository.findAll().stream()
                .map(vet -> VetProfileDTO.builder()
                        .vetId(vet.getVetId())
                        .specialty(vet.getSpecialty())
                        .licenseNumber(vet.getLicenseNumber())
                        .profilePhoto(vet.getProfilePhoto())
                        .ratingAvg(vet.getRatingAvg())
                        .reviewCount(vet.getReviewCount())
                        .isOnline(vet.getIsOnline())
                        .userId(vet.getUser().getUserId())
                        .userName(vet.getUser().getName())
                        .hospitalId(vet.getHospital() != null ? vet.getHospital().getHospitalId() : null)
                        .hospitalName(vet.getHospital() != null ? vet.getHospital().getName() : null)
                        .introduction(vet.getIntroduction())          // 수의사 소개
                        .availableDays(vet.getAvailableDays())        // 진료 가능 요일
                        .availableTime(vet.getAvailableTime())        // 진료 가능 시간
                        .build()
                )
                .toList();
    }

    public VetProfileDTO updateVetProfile(String email, VetProfileDTO dto) {
        VetProfile vet = vetProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("수의사 프로필을 찾을 수 없습니다."));

        // 프로필 정보 업데이트
        if (dto.getSpecialty() != null) {
            vet.setSpecialty(dto.getSpecialty());
        }
        if (dto.getLicenseNumber() != null) {
            vet.setLicenseNumber(dto.getLicenseNumber());
        }
        if (dto.getProfilePhoto() != null) {
            vet.setProfilePhoto(dto.getProfilePhoto());
        }
        if (dto.getIntroduction() != null) {
            vet.setIntroduction(dto.getIntroduction());
        }
        if (dto.getAvailableDays() != null) {
            vet.setAvailableDays(dto.getAvailableDays());
        }
        if (dto.getAvailableTime() != null) {
            vet.setAvailableTime(dto.getAvailableTime());
        }
        if (dto.getAvailableTime() != null) {
            vet.setAvailableTime(dto.getAvailableTime());
        }
        if (dto.getIsOnline() != null){
            vet.setIsOnline(dto.getIsOnline());
        }


        VetProfile updated = vetProfileRepository.save(vet);

        return VetProfileDTO.builder()
                .vetId(updated.getVetId())
                .specialty(updated.getSpecialty())
                .licenseNumber(updated.getLicenseNumber())
                .profilePhoto(updated.getProfilePhoto())
                .ratingAvg(updated.getRatingAvg())
                .reviewCount(updated.getReviewCount())
                .isOnline(updated.getIsOnline())
                .userId(updated.getUser().getUserId())
                .userName(updated.getUser().getName())
                .hospitalId(updated.getHospital() != null ? updated.getHospital().getHospitalId() : null)
                .hospitalName(updated.getHospital() != null ? updated.getHospital().getName() : null)
                .introduction(updated.getIntroduction())
                .availableDays(updated.getAvailableDays())
                .availableTime(updated.getAvailableTime())
                .build();
    }



}