package com.example.vet_backend.service;
import com.example.vet_backend.dto.UserDTO;
import com.example.vet_backend.dto.auth.Signup;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.repository.UserRepository;
import com.example.vet_backend.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    //생성자 주입
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void register(Signup request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }

    public UserDTO getUserDTOFromToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return entityToDTO(user);
    }

    public UserDTO updateUserInfoAndReturnDTO(HttpServletRequest request, UserDTO dto) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
        String currentEmail = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!dto.getEmail().equals(currentEmail)) {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            }
            user.setEmail(dto.getEmail());
        }

        user.setPhone(dto.getPhone());

        User updatedUser = userRepository.save(user);
        return entityToDTO(updatedUser);
    }

    private UserDTO entityToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())  // UserDTO에 name x 이름수정 못함
                .phone(user.getPhone())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

