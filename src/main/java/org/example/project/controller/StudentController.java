package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.criteria.StudentSearchCriteria;
import org.example.project.dto.request.CreateStudentDto;
import org.example.project.dto.request.UpdateStudentDto;
import org.example.project.dto.response.StudentResponseDto;
import org.example.project.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<?> createStudents(@Valid @RequestBody Set<CreateStudentDto> dto) {
        try {
            List<StudentResponseDto> students = studentService.addStudents(dto);
            return ResponseEntity.ok(students);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudents(StudentSearchCriteria criteria) {
        List<StudentResponseDto> students = studentService.findAll(criteria, criteria.buildPageRequest("user.username"));
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            StudentResponseDto student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody UpdateStudentDto dto) {
        try {
            StudentResponseDto updated = studentService.updateStudent(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/enroll/{sectionId}")
    public ResponseEntity<?> enrollToSection(@PathVariable Long sectionId) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            studentService.enrollStudentByUsernameToSection(username, sectionId);
            return ResponseEntity.ok("Student enrolled successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
