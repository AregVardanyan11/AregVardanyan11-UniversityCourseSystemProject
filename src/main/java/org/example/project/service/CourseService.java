package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateCourseDto;
import org.example.project.dto.response.CourseResponseDto;
import org.example.project.model.Course;
import org.example.project.model.Faculty;
import org.example.project.repository.CourseRepository;
import org.example.project.repository.FacultyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;

    @Transactional
    public List<CourseResponseDto> addCourses(List<CreateCourseDto> dtos) {
        return dtos.stream()
                .map(this::addCourse)
                .toList();
    }

    public CourseResponseDto addCourse(CreateCourseDto dto){
        Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new IllegalArgumentException("Faculty " + dto.getFacultyId() + " not found"));

        if (courseRepository.findByFacultyIdAndNumCode(faculty.getId(), dto.getNumCode()).isPresent()) {
            throw new IllegalArgumentException("Course with the code " + dto.getNumCode() + " of the faculty "
                    + faculty.getName()+ " exists");
        }

        Set<Course> prerequisites = new HashSet<>();
        if (dto.getPrerequisiteIds() != null) {
            prerequisites = new HashSet<>(courseRepository.findAllById(dto.getPrerequisiteIds()));
        }

        Course course = Course.builder()
                .name(dto.getName())
                .numCode(dto.getNumCode())
                .description(dto.getDescription())
                .credit(dto.getCredit())
                .faculty(faculty)
                .prerequisites(prerequisites)
                .build();

        Course saved = courseRepository.save(course);

        return CourseResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .numCode(saved.getNumCode())
                .description(saved.getDescription())
                .credit(saved.getCredit())
                .facultyId(saved.getFaculty().getId())
                .prerequisiteIds(
                        saved.getPrerequisites() != null
                                ? saved.getPrerequisites().stream().map(Course::getId).collect(java.util.stream.Collectors.toSet())
                                : Set.of())
                .build();
    }
}
