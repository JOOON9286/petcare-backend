package com.example.vet_backend.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDTO {

    private Long prescriptionId;

    //  [핵심] 어떤 예약에 대한 처방전인지 식별하기 위함
    private Long appointmentId;

    private String diagnosis;         // 진단명
    private String usageInstructions; // 용법/지시사항
    private String nextAppointment;   // 다음 내원일
    private String paymentStatus;     // 결제 상태 (PENDING, PAID)

    private LocalDateTime createdAt;  // 생성일(발행일)

    //  조회용 스냅샷 정보 (DB에서 꺼내올 때 채워짐)
    private String petName;
    private String doctorName;
    private String clinicName;

    //  [핵심] 약품 리스트 (처방전 1개에 약 여러 개)
    private List<MedicineDTO> medicines;

    // 내부 클래스로 약품 DTO 정의 (깔끔하게 관리하기 위함)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicineDTO {
        private String name;     // 약품명
        private String dosage;   // 용량/용법 (1일 3회 등)
        private String duration; // 투약 기간 (3일분 등)
        private String note;     // 메모
    }
}