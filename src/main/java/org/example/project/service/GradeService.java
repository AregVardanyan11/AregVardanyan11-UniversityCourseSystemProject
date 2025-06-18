package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateGradeDto;
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
        if (gradeRepository.existsById(dto.getLetter())) {
            throw new IllegalArgumentException("Grade with " + dto.getLetter() + " letter already exists");
        }

        Grade grade = Grade.builder()
                .letter(dto.getLetter().toUpperCase())
                .value(dto.getValue())
                .build();

        grade = gradeRepository.save(grade);

        return GradeResponseDto.builder()
                .letter(grade.getLetter())
                .value(grade.getValue())
                .build();
    }

    @Transactional
    public List<GradeResponseDto> createGrades(Set<CreateGradeDto> dtos) {
        return dtos.stream()
                .map(this::createGrade)
                .collect(Collectors.toList());
    }
}
