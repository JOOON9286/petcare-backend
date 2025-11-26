package com.example.vet_backend.repository;

import com.example.vet_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 나중에 주문번호로 결제 내역을 찾을 일이 생길 수 있으므로 추가해두기
    Optional<Payment> findByOrderId(String orderId);
}