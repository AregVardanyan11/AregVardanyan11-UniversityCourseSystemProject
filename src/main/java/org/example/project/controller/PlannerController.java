package org.example.project.controller;

import lombok.RequiredArgsConstructor;

import org.example.project.dto.request.PromptDto;
import org.example.project.model.Student;
import org.example.project.model.User;
import org.example.project.security.jwt.JwtUtil;
import org.example.project.security.jwt.UserPrincipal;
import org.example.project.service.AuthService;
import org.example.project.service.StudentService;
import org.example.project.service.gemini.PlannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ask-gemini")
@RequiredArgsConstructor
public class PlannerController {

    private final PlannerService plannerService;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final StudentService studentService;
    private final AuthService authService;

    @PostMapping("/plan")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> generatePlan(@RequestBody PromptDto promptDto,
                                          @RequestHeader("Authorization") String authHeader) {
        User user = authService.getUserFromHearer(authHeader);
        String response = plannerService.generatePlan(user, promptDto.getPrompt());
        return ResponseEntity.ok(response);
    }
}
