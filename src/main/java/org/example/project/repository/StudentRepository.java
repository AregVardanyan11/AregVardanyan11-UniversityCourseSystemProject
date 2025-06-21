package org.example.project.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.project.dto.criteria.StudentSearchCriteria;
import org.example.project.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
        SELECT s
        FROM Student s
        WHERE (:#{#criteria.name} IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT(:#{#criteria.name}, '%')))
          AND (:#{#criteria.surname} IS NULL OR LOWER(s.surname) LIKE LOWER(CONCAT(:#{#criteria.surname}, '%')))
          AND (:#{#criteria.studentId} IS NULL OR LOWER(s.studentId) LIKE LOWER(CONCAT(:#{#criteria.studentId}, '%')))
          AND (:#{#criteria.facultyId} IS NULL OR s.faculty.id = :#{#criteria.facultyId})
          AND (:#{#criteria.username} IS NULL OR LOWER(s.user.username) LIKE LOWER(CONCAT('%', :#{#criteria.username}, '%')))
          AND (:#{#criteria.email} IS NULL OR LOWER(s.user.email) LIKE LOWER(CONCAT('%', :#{#criteria.email}, '%')))
          AND (
              :#{#criteria.gpa} IS NULL OR
              (
                  :#{#criteria.belowGpa} = TRUE AND (
                      (SELECT SUM(tg.value) / COUNT(tg.value)
                       FROM Takes t
                       JOIN t.grade tg
                       WHERE t.student.id = s.id AND tg IS NOT NULL) < :#{#criteria.gpa}
                  )
                  OR
                  :#{#criteria.belowGpa} = FALSE AND (
                      (SELECT SUM(tg.value) / COUNT(tg.value)
                       FROM Takes t
                       JOIN t.grade tg
                       WHERE t.student.id = s.id AND tg IS NOT NULL) > :#{#criteria.gpa}
                  )
              )
          )
    """)
    Page<Student> search(StudentSearchCriteria criteria, Pageable pageable);

}
