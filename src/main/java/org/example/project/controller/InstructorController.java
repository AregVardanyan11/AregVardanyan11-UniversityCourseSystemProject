package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateInstructorDto;
import org.example.project.dto.response.InstructorResponseDto;
import org.example.project.model.Instructor;
import org.example.project.service.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/instructor")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    public ResponseEntity<?> createInstructor(@Valid @RequestBody Set<CreateInstructorDto> dto) {
        List<InstructorResponseDto> instructors;
        try {
            instructors = instructorService.addInstructors(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok(instructors);
    }
}
