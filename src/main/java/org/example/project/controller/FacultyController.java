package org.example.project.controller;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateFacultyDto;
import org.example.project.dto.request.UpdateFacultyDto;
import org.example.project.dto.response.FacultyResponseDto;
import org.example.project.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FacultyResponseDto>> getFaculties() {
        List<FacultyResponseDto> response = facultyService.getAll();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> updateFaculty(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacultyDto dto) {
        FacultyResponseDto updated = facultyService.updateFaculty(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}
