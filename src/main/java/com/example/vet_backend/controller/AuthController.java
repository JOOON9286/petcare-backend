package com.example.vet_backend.controller;
import com.example.vet_backend.dto.Login;
import com.example.vet_backend.dto.Signup;
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

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), "ROLE_USER");
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("email", user.getEmail());
        response.put("name", user.getName());   //사용자 이름도 추가 클라이언트에서 보여주기 위해

        return ResponseEntity.ok(response);
    }
    // 토큰 갱신 (리프레시 토큰)
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body(Map.of("message", "유효하지 않은 리프레시 토큰입니다."));
        }

        String email = jwtTokenProvider.getUserId(refreshToken); // 리프레시 토큰에서 이메일 정보 추출
        String newAccessToken = jwtTokenProvider.createToken(email, "ROLE_USER"); // 새로운 액세스 토큰 생성

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

}
