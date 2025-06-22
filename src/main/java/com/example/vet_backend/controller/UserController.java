package com.example.vet_backend.controller;

import com.example.vet_backend.dto.UserDTO;
import com.example.vet_backend.dto.UserResponseDTO;
import com.example.vet_backend.dto.auth.Signup;
import com.example.vet_backend.entity.User;
import com.example.vet_backend.repository.UserRepository;
import com.example.vet_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // ✅ 생성자에 두 개 다 주입
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Signup request) {
        try {
            userService.register(request);
            return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "회원가입 중 오류 발생: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo(HttpServletRequest request) {
        UserDTO userDTO = userService.getUserDTOFromToken(request);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateMyInfo(@RequestBody UserDTO requestDto, HttpServletRequest request) {
        UserDTO updatedUserDTO = userService.updateUserInfoAndReturnDTO(request, requestDto);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @GetMapping("/doctors")
    public List<UserResponseDTO> getAllDoctors() {
        List<User> doctors = userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("ROLE_DOCTOR"))
                .toList();

        return doctors.stream()
                .map(user -> new UserResponseDTO(user.getUserId(), user.getName()))
                .toList();
    }
}
