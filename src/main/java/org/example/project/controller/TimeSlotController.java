package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateTimeSlotDto;
import org.example.project.dto.request.UpdateTimeSlotDto;
import org.example.project.dto.response.TimeSlotResponseDto;
import org.example.project.model.enums.WeekDay;
import org.example.project.service.TimeSlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<?> getAllTimeSlots(@RequestParam String classroom,
                                             @RequestParam WeekDay day) {
        if (classroom == null || day == null) {
            return ResponseEntity.ok(timeSlotService.getAll());

        }
        return ResponseEntity.ok(timeSlotService.getSchedule(classroom, day));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTimeSlotById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(timeSlotService.getById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTimeSlot(@PathVariable Long id, @Valid @RequestBody UpdateTimeSlotDto dto) {
        try {
            return ResponseEntity.ok(timeSlotService.updateTimeSlot(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTimeSlot(@PathVariable Long id) {
        try {
            timeSlotService.deleteTimeSlot(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
