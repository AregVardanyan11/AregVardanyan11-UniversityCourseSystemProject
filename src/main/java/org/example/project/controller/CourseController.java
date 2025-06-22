package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.criteria.CourseSearchCriteria;
import org.example.project.dto.request.CreateCourseDto;
import org.example.project.dto.request.UpdateCourseDto;
import org.example.project.dto.response.CourseHierarchy;
import org.example.project.dto.response.CourseResponseDto;
import org.example.project.service.CourseService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/course")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> addCourse(@Valid @RequestBody List<CreateCourseDto> dto) {
        List<CourseResponseDto> response;
        try {
            response = courseService.addCourses(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<List<CourseResponseDto>> getAllCourses(CourseSearchCriteria criteria) {
        List<CourseResponseDto> response = courseService.findAll(criteria, criteria.buildPageRequest("name"));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<CourseResponseDto> updateCourse(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateCourseDto dto) {
        CourseResponseDto response = courseService.updateCourse(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/hierarchy-image")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'INSTRUCTOR', 'STUDENT')") 
    public ResponseEntity<?> getHierarchyImage(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(courseService.getHierarchy(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
