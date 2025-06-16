package com.example.vet_backend.controller;

import com.example.vet_backend.dto.CommunityCommentDTO;
import com.example.vet_backend.service.CommunityCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/community/comments")
@RequiredArgsConstructor
public class CommunityCommentController {

    private final CommunityCommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommunityCommentDTO dto) {
        commentService.createComment(dto);
        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Page<CommunityCommentDTO>> getCommentsPaged(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByPostIdPaged(postId, page, size));
    }
    @PostMapping("/{commentId}/vote")
    public ResponseEntity<?> voteComment(@PathVariable Long commentId, @RequestBody Map<String, Object> payload) {
        boolean isUpvote = (Boolean) payload.get("isUpvote");
        boolean cancel = (Boolean) payload.get("cancel");
        commentService.voteComment(commentId, isUpvote, cancel);
        return ResponseEntity.ok("처리 완료");
    }

}