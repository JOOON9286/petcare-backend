package com.example.vet_backend.service;

import com.example.vet_backend.dto.CommunityPostDTO;
import com.example.vet_backend.entity.CommunityPost;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.repository.CommunityPostRepository;
import com.example.vet_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityPostService {

    private final CommunityPostRepository postRepository;
    private final UserRepository userRepository;

    public List<CommunityPostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> CommunityPostDTO.builder()
                        .postId(post.getPostId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .likes(post.getLikes())
                        .userId(post.getUser().getUserId())
                        .userName(post.getUser().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public void createPost(CommunityPostDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        CommunityPost post = CommunityPost.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .likes(0)
                .user(user)
                .build();
        postRepository.save(post);
    }

    public void processLike(Long postId, boolean cancel) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        int currentLikes = post.getLikes();

        if (cancel && currentLikes > 0) {
            post.setLikes(currentLikes - 1);
        } else if (!cancel) {
            post.setLikes(currentLikes + 1);
        }

        postRepository.save(post);
    }
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
    public void updatePost(Long postId, String newTitle, String newContent) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        post.setTitle(newTitle);
        post.setContent(newContent);
        postRepository.save(post);
    }
}
