package org.example.project.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.project.model.Course;
import org.example.project.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("""
        SELECT s FROM Student s JOIN s.faculty f WHERE f.id=:facultyId
        """)
    Set<Student> studentsByFaculty(Long facultyId);

    @Query("""
        SELECT st FROM Takes t JOIN t.section se JOIN  t.student st WHERE se.id = :sectionId
        """)
    Set<Student> studentsBySection(Long sectionId);

    @Query("""
        SELECT SUM(g.value)/COUNT(g.value) FROM Takes t
                JOIN t.section se
                JOIN  t.student st
                JOIN t.grade g
                WHERE g!=null AND st.id = :studentId
        """)
    Float calculateGpa(Long studentId);

    boolean existsByStudentId(@NotBlank String studentId);
}
