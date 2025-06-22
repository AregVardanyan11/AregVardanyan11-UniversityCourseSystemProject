package org.example.project.controller;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.LoginRequest;
import org.example.project.dto.request.RegisterRequest;
import org.example.project.dto.response.AuthResponseDto;
import org.example.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody Map<String, String> body) {
        String token = body.get("refreshToken");
        return ResponseEntity.ok(userService.refresh(token));
    }
}
