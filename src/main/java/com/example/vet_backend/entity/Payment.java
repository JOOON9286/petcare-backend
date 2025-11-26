package com.example.vet_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 서비스에서 .builder()를 쓰기 위해 추가
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    //  토스 결제 정보 저장을 위해 추가된 필드들
    @Column(unique = true)
    private String orderId;    // 주문 고유 번호

    private String paymentKey; // 토스 결제 키 (환불 등에 필요)

    private Integer amount;    // 결제 금액

    private String method;     // 결제 수단 (카드, 가상계좌 등)

    private String status;     // 결제 상태 (DONE, CANCELED, ABORTED 등)

    private String orderName;  // 주문명 (예: 동물병원 화상 진료비)

    private LocalDateTime requestedAt; // 결제 요청 일시
    private LocalDateTime approvedAt;  // 결제 승인 일시

    // 처방전과 연결 (1:1 관계)
    @OneToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;
}