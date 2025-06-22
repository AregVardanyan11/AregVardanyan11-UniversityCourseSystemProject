package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.criteria.InstructorSearchCriteria;
import org.example.project.dto.request.AssignGradeRequest;
import org.example.project.dto.request.CreateInstructorDto;
import org.example.project.dto.request.UpdateInstructorDto;
import org.example.project.dto.response.InstructorResponseDto;
import org.example.project.model.Grade;
import org.example.project.model.Instructor;
import org.example.project.model.Takes;
import org.example.project.model.User;
import org.example.project.repository.GradeRepository;
import org.example.project.repository.InstructorRepository;
import org.example.project.repository.TakesRepository;
import org.example.project.repository.UserRepository;
import org.example.project.security.jwt.UserPrincipal;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;
    private final TakesRepository takesRepository;

    @Transactional
    public InstructorResponseDto addInstructor(CreateInstructorDto dto) {
        if (instructorRepository.existsByInstructorId(dto.getInstructorId())) {
            throw new IllegalArgumentException("Instructor ID "+ dto.getInstructorId() + " already exists");
        }
        if(userRepository.existsByEmailOrUsername(dto.getEmail(), dto.getUsername())){
            throw new IllegalArgumentException("Username " + dto.getUsername() + " already exists");
        }

        User user = User.builder().email(dto.getEmail()).username(dto.getUsername()).build();

        Instructor instructor = Instructor.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .bio(dto.getBio())
                .instructorId(dto.getInstructorId())
                .user(user)
                .build();

        Instructor saved = instructorRepository.save(instructor);

        return map(saved);
    }

    @Transactional
    public List<InstructorResponseDto> addInstructors(Set<CreateInstructorDto> dtos) {
        return dtos.stream()
                .map(this::addInstructor)
                .collect(Collectors.toList());
    }

    public List<InstructorResponseDto> findAll(InstructorSearchCriteria criteria, PageRequest pageRequest) {
        return instructorRepository.search(criteria, pageRequest).stream().map(this::map).collect(Collectors.toList());
    }

    @Transactional
    public InstructorResponseDto updateInstructor(Long id, UpdateInstructorDto dto) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found with id " + id));

        instructor.setName(dto.getName());
        instructor.setSurname(dto.getSurname());
        instructor.setBio(dto.getBio());
        instructor.getUser().setUsername(dto.getUsername());
        instructor.getUser().setEmail(dto.getEmail());

        Instructor saved = instructorRepository.save(instructor);
        return map(saved);
    }

    @Transactional
    public void deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found with id " + id));
        instructorRepository.delete(instructor);
    }

    public InstructorResponseDto map(Instructor saved) {
        return (InstructorResponseDto) InstructorResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .surname(saved.getSurname())
                .bio(saved.getBio())
                .instructorId(saved.getInstructorId())
                .email(saved.getUser().getEmail())
                .username(saved.getUser().getUsername())
                .build();
    }

    @Transactional
    public void assignGradeToStudent(AssignGradeRequest request) {
        // Get instructor username from JWT principal
        String username = ((UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Instructor instructor = instructorRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        // Check if instructor teaches the section
        boolean teaches = instructor.getSections().stream()
                .anyMatch(section -> section.getId().equals(request.getSectionId()));

        if (!teaches) {
            throw new IllegalArgumentException("You don't teach this section");
        }

        Takes takes = takesRepository.findByStudentIdAndSectionId(request.getStudentId(), request.getSectionId())
                .orElseThrow(() -> new IllegalArgumentException("Student not enrolled in this section"));

        Grade grade = gradeRepository.findById(request.getGrade())
                .orElseThrow(() -> new IllegalArgumentException("Grade not found"));

        takes.setGrade(grade);
        takesRepository.save(takes);
    }

}