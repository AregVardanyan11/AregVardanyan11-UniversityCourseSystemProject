package org.example.project.service;

import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.criteria.CourseSearchCriteria;
import org.example.project.dto.request.CreateCourseDto;
import org.example.project.dto.request.UpdateCourseDto;
import org.example.project.dto.response.CourseHierarchy;
import org.example.project.dto.response.CourseResponseDto;
import org.example.project.model.Course;
import org.example.project.model.Faculty;
import org.example.project.repository.CourseRepository;
import org.example.project.repository.FacultyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import guru.nidi.graphviz.engine.*;

import static guru.nidi.graphviz.model.Factory.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

        return map(saved);
    }

    public List<CourseResponseDto> findAll(CourseSearchCriteria criteria, PageRequest name) {
        return courseRepository.search(criteria,name).stream().map(this::map).collect(Collectors.toList());
    }

    @Transactional
    public CourseResponseDto updateCourse(Long id, UpdateCourseDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id " + id));

        Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found"));

        if (!dto.getNumCode().equals(course.getNumCode()) &&
                courseRepository.findByFacultyIdAndNumCode(faculty.getId(), dto.getNumCode()).isPresent()) {
            throw new IllegalArgumentException("Course with the code " + dto.getNumCode() + " of the faculty "
                    + faculty.getName()+ " exists");
        }

        course.setFaculty(faculty);
        course.setNumCode(dto.getNumCode());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setCredit(dto.getCredit());

        if (dto.getAddPrerequisiteIds() != null) {
            Set<Course> toAdd = new HashSet<>(courseRepository.findAllById(dto.getAddPrerequisiteIds()));
            course.getPrerequisites().addAll(toAdd);
        }

        if (dto.getRemovePrerequisiteIds() != null) {
            Set<Course> toRemove = new HashSet<>(courseRepository.findAllById(dto.getRemovePrerequisiteIds()));
            course.getPrerequisites().removeAll(toRemove);
        }

        Course updated = courseRepository.save(course);

        return map(updated);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id " + id));
        courseRepository.delete(course);
    }

    public byte[] generateHierarchyImage(Long courseId) {
        CourseHierarchy root = getTree(courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found")));
        return renderHierarchyGraph(root);
    }

    private byte[] renderHierarchyGraph(CourseHierarchy root) {
        Map<Long, Node> nodes = new HashMap<>();
        Graph g = graph("courseHierarchy").directed();
        Graph completeGraph = buildGraph(root, g, nodes);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Graphviz.fromGraph(completeGraph).render(Format.PNG).toOutputStream(os);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to render hierarchy image", e);
        }
    }

    private Graph buildGraph(CourseHierarchy current, Graph g, Map<Long, Node> nodes) {
        Node currentNode = nodes.computeIfAbsent(current.getId(), id -> node(current.getFullName()));
        Graph result = g.with(currentNode);

        for (CourseHierarchy prereq : current.getPrerequisites()) {
            Node prereqNode = nodes.computeIfAbsent(prereq.getId(), id -> node(prereq.getFullName()));
            result = result.with(currentNode.link(prereqNode));
            result = buildGraph(prereq, result, nodes);
        }

        return result;
    }

    private CourseResponseDto map(Course saved) {
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

    private CourseHierarchy getTree(Course course) {
        return getTree(course, new HashSet<>());
    }

    private CourseHierarchy getTree(Course course, Set<Long> visited) {
        if (visited.contains(course.getId())) {
            return CourseHierarchy.builder()
                    .id(course.getId())
                    .fullName(course.getFaculty().getCode() + course.getNumCode() + " (Cycle)")
                    .prerequisites(Set.of())
                    .build();
        }

        visited.add(course.getId());

        return CourseHierarchy.builder()
                .id(course.getId())
                .fullName(course.getFaculty().getCode() + course.getNumCode())
                .prerequisites(
                        course.getPrerequisites().stream()
                                .map(prereq -> getTree(prereq, visited))
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
