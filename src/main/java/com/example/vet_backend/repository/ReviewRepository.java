package com.example.vet_backend.repository;

import com.example.vet_backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // (선택) 명시해도 되고 안 해도 자동 등록됨
public interface ReviewRepository extends JpaRepository<Review, Long> {
}