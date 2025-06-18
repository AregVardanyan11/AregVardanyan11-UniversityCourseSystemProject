package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateGradeDto;
import org.example.project.dto.response.GradeResponseDto;
import org.example.project.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<?> createGrade(@RequestBody Set<CreateGradeDto> dto) {

        List<GradeResponseDto> saved;
        try{
            saved = gradeService.createGrades(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(saved);
    }
}
