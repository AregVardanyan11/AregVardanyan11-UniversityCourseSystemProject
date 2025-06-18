package org.example.project.controller;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateFacultyDto;
import org.example.project.dto.response.FacultyResponseDto;
import org.example.project.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<?> addFaculty(@RequestBody List<CreateFacultyDto> dtos) {
        List<FacultyResponseDto> response;
        try {
            response = facultyService.addFaculties(dtos);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
