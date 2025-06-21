package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.controller.TimeSlotController;
import org.example.project.dto.criteria.SectionSearchCriteria;
import org.example.project.dto.request.CreateSectionDto;
import org.example.project.dto.request.UpdateSectionDto;
import org.example.project.dto.response.CourseResponseDto;
import org.example.project.dto.response.SectionResponseDto;
import org.example.project.dto.response.TimeSlotResponseDto;
import org.example.project.model.*;
import org.example.project.model.enums.Semester;
import org.example.project.repository.*;
import org.springframework.data.domain.PageRequest;
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

        Section section = Section.builder()
                .course(course)
                .letter(dto.getLetter())
                .year(dto.getYear())
                .semester(Semester.valueOf(dto.getSemester()))
                .instructor(instructor)
                .maximumSeats(dto.getMaximumSeats())
                .finished(dto.isFinished())
                .build();

        Section saved = sectionRepository.save(section);

        return map(saved);
    }

    public List<SectionResponseDto> getSections(SectionSearchCriteria criteria, PageRequest year) {
        return sectionRepository.search(criteria, year).stream().map(this::map).collect(Collectors.toList());
    }

    private SectionResponseDto map(Section saved) {
        return SectionResponseDto.builder()
                .id(saved.getId())
                .courseId(saved.getCourse().getId())
                .letter(saved.getLetter())
                .year(saved.getYear())
                .semester(saved.getSemester().toString())
                .instructorId(saved.getInstructor().getId())
                .maximumSeats(saved.getMaximumSeats())
                .availableSeats(saved.getMaximumSeats()-saved.getReservedSeats())
                .finished(saved.isFinished())
                .timeSlotIds(saved.getTimeSlots().stream().map(this::map).collect(Collectors.toSet()))
                .build();
    }

    private TimeSlotResponseDto map(TimeSlot saved) {
        return TimeSlotResponseDto.builder()
                .classroom(saved.getClassroom())
                .day(saved.getDay())
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime()).build();
    }

    @Transactional
    public SectionResponseDto updateSection(Long id, UpdateSectionDto dto) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Section not found with id " + id));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        section.setCourse(course);
        section.setLetter(dto.getLetter());
        section.setYear(dto.getYear());
        section.setSemester(Semester.valueOf(dto.getSemester()));
        section.setInstructor(instructor);
        section.setSyllabusPath(dto.getSyllabusPath());
        section.setMaximumSeats(dto.getMaximumSeats());
        section.setFinished(dto.isFinished());

        return map(sectionRepository.save(section));
    }

    @Transactional
    public void deleteSection(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Section not found with id " + id));
        sectionRepository.delete(section);
    }

}
