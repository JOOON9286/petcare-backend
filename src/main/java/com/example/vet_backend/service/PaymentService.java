package com.example.vet_backend.service;

import com.example.vet_backend.config.TossPaymentConfig;
import com.example.vet_backend.dto.PaymentDTO;
import com.example.vet_backend.entity.Payment;
import com.example.vet_backend.entity.Prescription;
import com.example.vet_backend.repository.PaymentRepository;
import com.example.vet_backend.repository.PrescriptionRepository; // 📌 추가됨
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TossPaymentConfig tossPaymentConfig;
    private final PaymentRepository paymentRepository;
    private final PrescriptionRepository prescriptionRepository; // 📌 처방전 조회용 리포지토리 주입

    @Transactional
    public String paymentConfirm(PaymentDTO paymentDTO) throws Exception {
        // 1. 토스 결제 승인 API URL
        String url = tossPaymentConfig.getSuccessUrl();

        // 2. 시크릿 키 인코딩
        String widgetSecretKey = tossPaymentConfig.getTestSecretKey();
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 3. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizations);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. 요청 본문 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(paymentDTO);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // 5. 토스 서버로 승인 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(url, request, JsonNode.class);

        // 6. 응답 처리 및 DB 저장
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode body = responseEntity.getBody();

            // 📌 [핵심 로직] OrderId 파싱 -> 처방전 찾기 -> 상태 'PAID'로 변경
            // 프론트엔드에서 보낸 orderId 형식: "ORDER-{예약ID}-{타임스탬프}"
            Prescription prescription = null;
            try {
                String orderId = paymentDTO.getOrderId();
                String[] parts = orderId.split("-");

                // parts[0]="ORDER", parts[1]="예약ID", parts[2]="타임스탬프"
                if (parts.length >= 2) {
                    Long appointmentId = Long.parseLong(parts[1]);

                    // 예약 ID로 처방전 조회
                    prescription = prescriptionRepository.findByAppointment_AppointmentId(appointmentId)
                            .orElse(null);

                    if (prescription != null) {
                        // 1️⃣ 처방전 상태 변경 (결제 완료)
                        prescription.setPaymentStatus("PAID");

                        // 2️⃣ 변경 사항 DB 저장 (필수!)
                        prescriptionRepository.save(prescription);

                        System.out.println("✅ 처방전(ID:" + prescription.getPrescriptionId() + ") 상태가 PAID로 변경되었습니다.");
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ 처방전 상태 업데이트 중 오류 발생 (결제는 진행됨): " + e.getMessage());
            }

            // 7. 결제 정보(Payment) 저장
            Payment payment = Payment.builder()
                    .orderId(paymentDTO.getOrderId())
                    .paymentKey(paymentDTO.getPaymentKey())
                    .amount(paymentDTO.getAmount().intValue())
                    .method(body.get("method").asText())       // 결제 수단
                    .status(body.get("status").asText())       // 결제 상태 (DONE)
                    .orderName(body.get("orderName").asText()) // 주문명
                    .approvedAt(LocalDateTime.now())           // 승인 시간
                    .prescription(prescription)                // 3️⃣ 결제 정보와 처방전 연결 (Foreign Key)
                    .build();

            paymentRepository.save(payment); // 결제 정보 저장

            return body.toPrettyString();
        } else {
            throw new Exception("토스 결제 승인 실패: " + responseEntity.getStatusCode());
        }
    }
}