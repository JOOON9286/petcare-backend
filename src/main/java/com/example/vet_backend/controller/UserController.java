package com.example.vet_backend.controller;

import com.example.vet_backend.dto.UserDTO;
import com.example.vet_backend.dto.auth.Signup;
import com.example.vet_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입
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

    //정보조회
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo(HttpServletRequest request) {
        UserDTO userDTO = userService.getUserDTOFromToken(request);
        return ResponseEntity.ok(userDTO);
    }

    //정보수정
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateMyInfo(@RequestBody UserDTO requestDto, HttpServletRequest request) {
        UserDTO updatedUserDTO = userService.updateUserInfoAndReturnDTO(request, requestDto);
        return ResponseEntity.ok(updatedUserDTO);
    }
}


