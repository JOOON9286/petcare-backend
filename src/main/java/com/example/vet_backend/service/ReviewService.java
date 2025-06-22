    package com.example.vet_backend.service;

    import com.example.vet_backend.dto.ReviewDTO;
    import com.example.vet_backend.entity.Review;
    import com.example.vet_backend.entity.User;
    import com.example.vet_backend.repository.AppointmentRepository;
    import com.example.vet_backend.repository.ReviewRepository;
    import com.example.vet_backend.repository.UserRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class ReviewService {

        private final ReviewRepository reviewRepository;
        private final UserRepository userRepository;
        private final AppointmentRepository appointmentRepository;

        public List<ReviewDTO> getAllReviews() {
            return reviewRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        public ReviewDTO createReview(ReviewDTO dto) {
            User user = userRepository.findById(dto.getUserId()).orElseThrow();
            Review review = new Review();
            review.setContent(dto.getContent());
            review.setRating(dto.getRating());
            review.setIsAnonymous(dto.getIsAnonymous());
            review.setCreatedAt(LocalDateTime.now());
            review.setLikeCount(0);
            review.setCommentCount(0);
            review.setUser(user);
            review.setAppointment(appointmentRepository.findById(dto.getAppointmentId())
                    .orElseThrow());

            Review saved = reviewRepository.save(review);
            return convertToDTO(saved);
        }

        public ReviewDTO convertToDTO(Review review) {
            return ReviewDTO.builder()
                    .reviewId(review.getReviewId())
                    .rating(review.getRating())
                    .content(review.getContent())
                    .isAnonymous(review.getIsAnonymous())
                    .createdAt(review.getCreatedAt())
                    .likeCount(review.getLikeCount())
                    .commentCount(review.getCommentCount())
                    .userId(review.getUser().getUserId())
                    .userName(review.getUser().getName())
                    .appointmentId(review.getAppointment().getAppointmentId())
                    .vetId(review.getAppointment().getVet().getVetId())
                    .vetName(review.getAppointment().getVet().getUser().getName())
                    .build();
        }

        public void toggleLike(Long reviewId, boolean cancel) {
            Review review = reviewRepository.findById(reviewId).orElseThrow();
            int currentLikes = review.getLikeCount();
            review.setLikeCount(cancel ? Math.max(0, currentLikes - 1) : currentLikes + 1);
            reviewRepository.save(review);
        }
    }
