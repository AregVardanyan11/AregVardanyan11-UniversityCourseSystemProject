package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateStudentDto;
import org.example.project.dto.response.StudentResponseDto;
import org.example.project.model.Faculty;
import org.example.project.model.Student;
import org.example.project.model.User;
import org.example.project.repository.FacultyRepository;
import org.example.project.repository.StudentRepository;
import org.example.project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;

    @Transactional
    public StudentResponseDto addStudent(CreateStudentDto dto) {
        if(studentRepository.existsByStudentId(dto.getStudentId())) {
            throw new IllegalArgumentException("Student ID " + dto.getStudentId() + " already exists");
        }
        if(userRepository.existsByEmailOrUsername(dto.getEmail(), dto.getUsername())){
            throw new IllegalArgumentException("Username " + dto.getUsername() + " already exists");
        }

        Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found"));

        User user = User.builder().email(dto.getEmail()).username(dto.getUsername()).build();

        Student student = Student.builder()
                .name(dto.getName())
                .studentId(dto.getStudentId())
                .user(user)
                .faculty(faculty)
                .build();

        Student saved = studentRepository.save(student);

        return StudentResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .studentId(saved.getStudentId())
                .userId(user.getId())
                .facultyId(faculty.getId())
                .email(saved.getUser().getEmail())
                .username(saved.getUser().getUsername())
                .build();
    }

    @Transactional
    public List<StudentResponseDto> addStudents(Set<CreateStudentDto> dtos) {
        return dtos.stream()
                .map(this::addStudent)
                .collect(Collectors.toList());
    }
}
