package com.example.vet_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    // 처방전 기본 정보
    private String diagnosis; // 진단명

    @Column(columnDefinition = "TEXT")
    private String usageInstructions; // 용법 및 지시사항

    private String nextAppointment; // 다음 내원일

    private String paymentStatus; // "PENDING"(미결제), "PAID"(결제완료)

    private LocalDateTime createdAt; // 발행일

    //  [스냅샷] 예약 당시의 환자/의사 정보를 박제 (나중에 닉네임이 바뀌어도 기록 유지)
    private String petName;
    private String petSpecies;
    private String petWeight;
    private String doctorName;
    private String clinicName;

    //  약품 리스트 (1:N)
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // 빌더 사용 시 리스트 초기화 방지
    private List<Medicine> medicines = new ArrayList<>();

    //  예약 정보 연결 (1:1) - 여기가 주인!
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    // 연관관계 편의 메서드
    public void addMedicine(Medicine medicine) {
        this.medicines.add(medicine);
        medicine.setPrescription(this);
    }
}