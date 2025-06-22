package org.example.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.criteria.SectionSearchCriteria;
import org.example.project.dto.request.CreateSectionDto;
import org.example.project.dto.request.UpdateSectionDto;
import org.example.project.dto.response.SectionResponseDto;
import org.example.project.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<?> createSection(@Valid @RequestBody Set<CreateSectionDto> dtos) {
        try {
            List<SectionResponseDto> response = sectionService.createSections(dtos);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getSections(SectionSearchCriteria criteria) {
        List<SectionResponseDto> response = sectionService.getSections(criteria, criteria.buildPageRequest("year"));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSection(@PathVariable Long id, @Valid @RequestBody UpdateSectionDto dto) {
        try {
            SectionResponseDto updated = sectionService.updateSection(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}