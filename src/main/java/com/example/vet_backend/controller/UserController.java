package com.example.vet_backend.controller;

import com.example.vet_backend.dto.auth.Signup;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/users")
@RestController
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Signup request) {
        try {
            userService.register(request);
            return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
        } catch (IllegalArgumentException e) {
            // 클라이언트 잘못 (예: 중복 이메일)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // 서버 문제
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "회원가입 중 오류 발생: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        try {
            User currentUser = userService.getCurrentUser();

            // 필요한 정보만 보내기 (DTO로 변환 추천)
            Map<String, Object> response = Map.of(
                    "userId", currentUser.getUserId(),
                    "email", currentUser.getEmail(),
                    "name", currentUser.getName(),
                    "phone", currentUser.getPhone(),
                    "role", currentUser.getRole()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "사용자 인증 실패: " + e.getMessage()));
        }
    }

}
