package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.criteria.InstructorSearchCriteria;
import org.example.project.dto.request.CreateInstructorDto;
import org.example.project.dto.request.UpdateInstructorDto;
import org.example.project.dto.response.InstructorResponseDto;
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

    @GetMapping
    public ResponseEntity<?> getAll(InstructorSearchCriteria criteria){
        List<InstructorResponseDto> instructors = instructorService.findAll(criteria, criteria.buildPageRequest("user.email"));
        return ResponseEntity.ok(instructors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponseDto> updateInstructor(@PathVariable Long id,
                                                                  @Valid @RequestBody UpdateInstructorDto dto) {
        InstructorResponseDto updated = instructorService.updateInstructor(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
        return ResponseEntity.noContent().build();
    }
}