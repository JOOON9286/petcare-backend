package com.example.vet_backend.controller;

import com.example.vet_backend.dto.PaymentDTO;
import com.example.vet_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 프론트엔드에서 결제창(위젯) 승인 성공 후 호출하는 API
    // URL: http://localhost:8080/api/payment/confirm
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            // 서비스 로직 호출 (토스 서버로 최종 승인 요청)
            String response = paymentService.paymentConfirm(paymentDTO);

            // 성공 시 토스에서 받은 결과를 그대로 프론트엔드에 반환
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            // 실패 시 400 Bad Request와 에러 메시지 반환
            return ResponseEntity.badRequest().body("결제 실패: " + e.getMessage());
        }
    }
}