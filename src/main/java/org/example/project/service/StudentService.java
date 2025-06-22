package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.criteria.StudentSearchCriteria;
import org.example.project.dto.request.CreateStudentDto;
import org.example.project.dto.request.UpdateStudentDto;
import org.example.project.dto.response.StudentResponseDto;
import org.example.project.model.Faculty;
import org.example.project.model.Section;
import org.example.project.model.Student;
import org.example.project.model.User;
import org.example.project.repository.FacultyRepository;
import org.example.project.repository.SectionRepository;
import org.example.project.repository.StudentRepository;
import org.example.project.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
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
    private final SectionRepository sectionRepository;

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
                .surname(dto.getSurname())
                .faculty(faculty)
                .build();

        Student saved = studentRepository.save(student);

        return mapper(saved);
    }

    @Transactional
    public List<StudentResponseDto> addStudents(Set<CreateStudentDto> dtos) {
        return dtos.stream()
                .map(this::addStudent)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDto> findAll(StudentSearchCriteria criteria, PageRequest pageRequest) {
        return studentRepository.search(criteria, pageRequest).stream().map(this::mapper).collect(Collectors.toList());
    }

    @Transactional
    public StudentResponseDto updateStudent(Long id, UpdateStudentDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id " + id));

        if (userRepository.existsByEmailOrUsername(dto.getEmail(), dto.getUsername()) &&
                (!student.getUser().getEmail().equals(dto.getEmail()) || !student.getUser().getUsername().equals(dto.getUsername()))) {
            throw new IllegalArgumentException("Username or email already in use");
        }

        Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found"));

        student.setName(dto.getName());
        student.setSurname(dto.getSurname());
        student.setFaculty(faculty);
        student.getUser().setEmail(dto.getEmail());
        student.getUser().setUsername(dto.getUsername());

        return mapper(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id " + id));
        studentRepository.delete(student);
    }

    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(this::mapper)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id " + id));
    }


    private StudentResponseDto mapper(Student saved) {
        return StudentResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .surname(saved.getSurname())
                .studentId(saved.getStudentId())
                .userId(saved.getUser().getId())
                .facultyId(saved.getFaculty().getId())
                .email(saved.getUser().getEmail())
                .username(saved.getUser().getUsername())
                .build();
    }

    public void enrollStudentByUsernameToSection(String username, Long sectionId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Student student = studentRepository.getByUserId(user.getId());

        Section section = sectionRepository.getById(sectionId);

        section.enrollTheStudent(student);
    }
}
