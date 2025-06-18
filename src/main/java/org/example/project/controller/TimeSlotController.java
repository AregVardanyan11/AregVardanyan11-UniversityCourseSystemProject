package org.example.project.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateStudentDto;
import org.example.project.dto.request.CreateTimeSlotDto;
import org.example.project.dto.response.StudentResponseDto;
import org.example.project.dto.response.TimeSlotResponseDto;
import org.example.project.service.StudentService;
import org.example.project.service.TimeSlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/time-slot")
@RequiredArgsConstructor
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    @PostMapping
    public ResponseEntity<?> createTimeSlots(@Valid @RequestBody Set<CreateTimeSlotDto> dto) {
        List<TimeSlotResponseDto> slots;
        try {
            slots = timeSlotService.addTimeSlots(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(slots);
    }
}
