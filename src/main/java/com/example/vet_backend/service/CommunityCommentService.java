package com.example.vet_backend.service;

import com.example.vet_backend.dto.CommunityCommentDTO;
import com.example.vet_backend.entity.CommunityComment;
import com.example.vet_backend.entity.CommunityPost;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.repository.CommunityCommentRepository;
import com.example.vet_backend.repository.CommunityPostRepository;
import com.example.vet_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityCommentService {

    private final CommunityCommentRepository commentRepository;
    private final CommunityPostRepository postRepository;
    private final UserRepository userRepository;

    public void createComment(CommunityCommentDTO dto) {
        CommunityPost post = postRepository.findById(dto.getPostId()).orElseThrow();
        User user = userRepository.findById(dto.getUserId()).orElseThrow();

        CommunityComment comment = CommunityComment.builder()
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .post(post)
                .user(user)
                .build();
        commentRepository.save(comment);
    }

    public Page<CommunityCommentDTO> getCommentsByPostIdPaged(Long postId, int page, int size) {
        Page<CommunityComment> commentPage = commentRepository.findByPostPostId(postId, PageRequest.of(page, size));
        List<CommunityCommentDTO> dtos = commentPage.getContent().stream()
                .map(comment -> CommunityCommentDTO.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .postId(postId)
                        .userName(comment.getUser().getName())
                        .userId(comment.getUser().getUserId())
                        .upvotes(comment.getUpvotes())
                        .downvotes(comment.getDownvotes())
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, commentPage.getPageable(), commentPage.getTotalElements());
    }
    public void voteComment(Long commentId, boolean isUpvote, boolean cancel) {
        CommunityComment comment = commentRepository.findById(commentId).orElseThrow();

        if (isUpvote) {
            int newUpvotes = cancel ? comment.getUpvotes() - 1 : comment.getUpvotes() + 1;
            comment.setUpvotes(Math.max(0, newUpvotes)); // 최소 0으로 제한
        } else {
            int newDownvotes = cancel ? comment.getDownvotes() - 1 : comment.getDownvotes() + 1;
            comment.setDownvotes(Math.max(0, newDownvotes)); // 최소 0으로 제한
        }

        commentRepository.save(comment);
    }


}
