package com.example.vet_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long paymentId;
    private Integer amount;
    private String method;
    private String status;
    private Long prescriptionId;
}
