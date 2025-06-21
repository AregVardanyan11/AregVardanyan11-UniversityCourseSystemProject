package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateGradeDto;
import org.example.project.dto.request.UpdateGradeDto;
import org.example.project.dto.response.GradeResponseDto;
import org.example.project.model.Grade;
import org.example.project.repository.GradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    public GradeResponseDto createGrade(CreateGradeDto dto) {
        if (gradeRepository.existsById(dto.getLetter().toUpperCase())) {
            throw new IllegalArgumentException("Grade with " + dto.getLetter() + " letter already exists");
        }

        Grade grade = Grade.builder()
                .letter(dto.getLetter().toUpperCase())
                .value(dto.getValue())
                .build();

        grade = gradeRepository.save(grade);

        return map(grade);
    }

    @Transactional
    public List<GradeResponseDto> createGrades(Set<CreateGradeDto> dtos) {
        return dtos.stream()
                .map(this::createGrade)
                .collect(Collectors.toList());
    }

    public List<GradeResponseDto> getAll() {
        return gradeRepository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    public GradeResponseDto getGrade(String letter) {
        Grade grade = gradeRepository.findById(letter)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with letter " + letter));
        return map(grade);
    }

    @Transactional
    public GradeResponseDto updateGrade(String letter, UpdateGradeDto dto) {
        Grade grade = gradeRepository.findById(letter)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with letter " + letter));

        grade.setValue(dto.getValue());
        grade = gradeRepository.save(grade);
        return map(grade);
    }

    @Transactional
    public void deleteGrade(String letter) {
        Grade grade = gradeRepository.findById(letter)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with letter " + letter));
        gradeRepository.delete(grade);
    }

    private GradeResponseDto map(Grade grade) {
        return GradeResponseDto.builder()
                .letter(grade.getLetter())
                .value(grade.getValue())
                .build();
    }
}

