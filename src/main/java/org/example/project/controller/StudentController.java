package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.criteria.StudentSearchCriteria;
import org.example.project.dto.request.CreateStudentDto;
import org.example.project.dto.request.UpdateStudentDto;
import org.example.project.dto.response.StudentResponseDto;
import org.example.project.model.User;
import org.example.project.service.AuthService;
import org.example.project.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final AuthService authService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> createStudents(@Valid @RequestBody Set<CreateStudentDto> dto) {
        try {
            List<StudentResponseDto> students = studentService.addStudents(dto);
            return ResponseEntity.ok(students);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<StudentResponseDto>> getAllStudents(StudentSearchCriteria criteria) {
        List<StudentResponseDto> students = studentService.findAll(criteria, criteria.buildPageRequest("user.username"));
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'REGISTRAR')")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            StudentResponseDto student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody UpdateStudentDto dto) {
        try {
            StudentResponseDto updated = studentService.updateStudent(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/enroll/{sectionId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> enrollToSection(@PathVariable Long sectionId,
                                             @RequestHeader("Authorization") String authHeader) {
        try {
            User user = authService.getUserFromHearer(authHeader);
            studentService.enrollStudentByUsernameToSection(user.getUsername(), sectionId);
            return ResponseEntity.ok("Student enrolled successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
