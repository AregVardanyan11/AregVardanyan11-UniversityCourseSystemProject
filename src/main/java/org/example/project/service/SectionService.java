package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateSectionDto;
import org.example.project.dto.response.SectionResponseDto;
import org.example.project.model.*;
import org.example.project.model.enums.Semester;
import org.example.project.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Transactional
    public List<SectionResponseDto> createSections(Set<CreateSectionDto> dtos) {
        return dtos.stream().map(this::addSection).collect(Collectors.toList());
    }

    public SectionResponseDto addSection(CreateSectionDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        Set<TimeSlot> timeSlots = dto.getTimeSlotIds() == null ? Set.of() :
                timeSlotRepository.findAllById(dto.getTimeSlotIds()).stream().collect(Collectors.toSet());

        Section section = Section.builder()
                .course(course)
                .letter(dto.getLetter())
                .year(dto.getYear())
                .semester(Semester.valueOf(dto.getSemester()))
                .instructor(instructor)
                .syllabusPath(dto.getSyllabusPath())
                .maximumSeats(dto.getMaximumSeats())
                .finished(dto.isFinished())
                .timeSlots(timeSlots)
                .build();

        Section saved = sectionRepository.save(section);

        return SectionResponseDto.builder()
                .id(saved.getId())
                .courseId(course.getId())
                .letter(saved.getLetter())
                .year(saved.getYear())
                .semester(saved.getSemester().toString())
                .instructorId(instructor.getId())
                .syllabusPath(saved.getSyllabusPath())
                .maximumSeats(saved.getMaximumSeats())
                .finished(saved.isFinished())
                .timeSlotIds(saved.getTimeSlots().stream().map(TimeSlot::getId).collect(Collectors.toSet()))
                .build();
    }
}
