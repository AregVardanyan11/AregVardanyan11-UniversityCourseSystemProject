package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateCourseDto;
import org.example.project.dto.response.CourseResponseDto;
import org.example.project.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/course")
public class CourseController {
    private final CourseService courseService;


    @PostMapping
    public ResponseEntity<?> addCourse(@Valid @RequestBody List<CreateCourseDto> dto) {
        List<CourseResponseDto> response;
        try {
            response = courseService.addCourses(dto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
