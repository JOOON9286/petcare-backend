package com.example.vet_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    // --- 기존 필드 ---
    private Long paymentId;
    private Long amount;         // Integer -> Long 변경 권장 (금액이 커질 수 있음)
    private String method;
    private String status;
    private Long prescriptionId;

    // --- 토스 결제 승인을 위해 추가해야 할 필드 (필수) ---
    private String paymentKey;   // 토스에서 발급해준 결제 키
    private String orderId;      // 주문 ID
}