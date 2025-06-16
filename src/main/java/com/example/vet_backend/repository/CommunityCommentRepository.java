package com.example.vet_backend.repository;

import com.example.vet_backend.entity.CommunityComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    Page<CommunityComment> findByPostPostId(Long postId, Pageable pageable);
    int countByPostPostId(Long postId);
}
