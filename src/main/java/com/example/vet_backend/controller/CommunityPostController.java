package com.example.vet_backend.controller;

import com.example.vet_backend.dto.CommunityPostDTO;
import com.example.vet_backend.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityPostController {

    private final CommunityPostService postService;

    @GetMapping("/posts")
    public List<CommunityPostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/posts")
    public void createPost(@RequestBody CommunityPostDTO dto) {
        postService.createPost(dto);
    }

    @PostMapping("/posts/{postId}/like")
    public void likePost(@PathVariable Long postId, @RequestBody(required = false) Map<String, Object> body) {
        boolean cancel = body != null && Boolean.TRUE.equals(body.get("cancel"));
        postService.processLike(postId, cancel);
    }
}
