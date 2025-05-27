package com.example.vet_backend.service;

import com.example.vet_backend.dto.auth.Login;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.repository.UserRepository;
import com.example.vet_backend.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public String login(Login request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // userId는 user.getId() (long) 이고, String으로 변환해서 넣음
        return jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getRole(),user.getEmail());
    }
}
