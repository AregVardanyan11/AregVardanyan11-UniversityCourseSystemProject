package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateFacultyDto;
import org.example.project.dto.request.UpdateFacultyDto;
import org.example.project.dto.response.FacultyResponseDto;
import org.example.project.model.Faculty;
import org.example.project.repository.FacultyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Transactional
    public List<FacultyResponseDto> addFaculties(List<CreateFacultyDto> dtos){
        return dtos.stream()
                .map(this::addFaculty)
                .toList();
    }

    public FacultyResponseDto addFaculty(CreateFacultyDto dto) {
        if (facultyRepository.findByCode(dto.getCode()).isPresent()) {
            throw new IllegalArgumentException("Faculty with " + dto.getCode() + " code already exists");
        }

        Faculty faculty = Faculty.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .build();

        faculty = facultyRepository.save(faculty);

        return map(faculty);
    }

    public List<FacultyResponseDto> getAll() {
        return facultyRepository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Transactional
    public FacultyResponseDto updateFaculty(Long id, UpdateFacultyDto dto) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found with id " + id));

        faculty.setName(dto.getName());
        faculty.setCode(dto.getCode());

        Faculty saved = facultyRepository.save(faculty);

        return map(faculty);
    }

    @Transactional
    public void deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found with id " + id));
        facultyRepository.delete(faculty);
    }

    private FacultyResponseDto map(Faculty faculty) {
        return FacultyResponseDto.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .code(faculty.getCode())
                .build();
    }
}