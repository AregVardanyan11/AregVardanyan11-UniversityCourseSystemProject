package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateGradeDto;
import org.example.project.dto.request.UpdateGradeDto;
import org.example.project.dto.response.GradeResponseDto;
import org.example.project.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("api/v1/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> createGrade(@RequestBody Set<CreateGradeDto> dto) {
        List<GradeResponseDto> saved;
        try {
            saved = gradeService.createGrades(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(saved);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<List<GradeResponseDto>> getAllGrades() {
        List<GradeResponseDto> response = gradeService.getAll();
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{letter}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<GradeResponseDto> updateGrade(@PathVariable String letter,
                                                        @Valid @RequestBody UpdateGradeDto dto) {
        GradeResponseDto updated = gradeService.updateGrade(letter.toUpperCase(), dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{letter}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGrade(@PathVariable String letter) {
        gradeService.deleteGrade(letter.toUpperCase());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{letter}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<GradeResponseDto> getGrade(@PathVariable String letter) {
        GradeResponseDto grade = gradeService.getGrade(letter.toUpperCase());
        return ResponseEntity.ok(grade);
    }
}
