package com.example.vet_backend.service;

import com.example.vet_backend.dto.PetDTO;
import com.example.vet_backend.entity.Pet;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.repository.PetRepository;
import com.example.vet_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetDTO registerPet(PetDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Pet pet = Pet.builder()
                .name(dto.getName())
                .species(dto.getSpecies())
                .breed(dto.getBreed())
                .gender(dto.getGender())
                .weight(dto.getWeight())
                .birthDate(dto.getBirthDate())
                .medicalHistory(dto.getMedicalHistory())
                .neutered(dto.isNeutered())
                .createdAt(LocalDateTime.now())
                .owner(user)
                .isDeleted(false)  // Soft delete 필드 초기값 false
                .build();

        Pet saved = petRepository.save(pet);
        return convertToDTO(saved);
    }

    public List<PetDTO> getPetsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Pet> pets = petRepository.findByOwnerAndIsDeletedFalse(user);  // 삭제 안된 것만 조회
        return pets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public PetDTO updatePet(Long petId, PetDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Pet pet = petRepository.findByPetIdAndIsDeletedFalse(petId)  // 삭제된 펫은 수정 불가
                .orElseThrow(() -> new IllegalArgumentException("반려동물을 찾을 수 없습니다."));

        if (!pet.getOwner().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setBreed(dto.getBreed());
        pet.setGender(dto.getGender());
        pet.setWeight(dto.getWeight());
        pet.setBirthDate(dto.getBirthDate());
        pet.setMedicalHistory(dto.getMedicalHistory());
        pet.setNeutered(dto.isNeutered());

        Pet updated = petRepository.save(pet);
        return convertToDTO(updated);
    }

    //soft delete
    public void deletePet(Long petId, Long userId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("반려동물을 찾을 수 없습니다."));

        if (!pet.getOwner().getUserId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        pet.setDeleted(true); // Soft Delete 처리
        petRepository.save(pet);
    }


    private PetDTO convertToDTO(Pet pet) {
        return PetDTO.builder()
                .petId(pet.getPetId())
                .name(pet.getName())
                .species(pet.getSpecies())
                .breed(pet.getBreed())
                .gender(pet.getGender())
                .weight(pet.getWeight())
                .birthDate(pet.getBirthDate())
                .medicalHistory(pet.getMedicalHistory())
                .neutered(pet.isNeutered())
                .userId(pet.getOwner().getUserId())
                .build();
    }
}
