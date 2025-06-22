package com.example.vet_backend.controller;

import com.example.vet_backend.dto.ReviewDTO;
import com.example.vet_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewDTO> getReviews() {
        return reviewService.getAllReviews();
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO dto) {
        return ResponseEntity.ok(reviewService.createReview(dto));
    }

    @PostMapping("/{reviewId}/like")
    public void likeReview(@PathVariable Long reviewId,
                           @RequestBody(required = false) Map<String, Object> body) {
        boolean cancel = body != null && Boolean.TRUE.equals(body.get("cancel"));
        reviewService.toggleLike(reviewId, cancel);
    }
}

