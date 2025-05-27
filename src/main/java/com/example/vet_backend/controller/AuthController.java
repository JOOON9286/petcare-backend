package com.example.vet_backend.controller;
import com.example.vet_backend.dto.auth.Login;
import com.example.vet_backend.dto.auth.Signup;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.repository.UserRepository;
import com.example.vet_backend.service.AuthService;
import com.example.vet_backend.util.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//토큰관련
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Signup request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        if (userRepository.findByName(request.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 사용자입니다.");
        }

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("ROLE_USER");
        userRepository.save(newUser);

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getRole(),user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getUserId()));

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body(Map.of("message", "유효하지 않은 리프레시 토큰입니다."));
        }

        // 리프레시 토큰에서는 이메일이 없을 수도 있으니, 보통은 userId(sub)만 꺼냅니다.
        String userId = jwtTokenProvider.getUserId(refreshToken);

        // 리프레시 토큰에는 이메일이 없을 가능성이 크므로, DB에서 이메일과 역할을 조회해야 합니다.
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtTokenProvider.createToken(
                userId,
                user.getRole(),
                user.getEmail()
        );

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }


}
