package org.example.project.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.project.dto.criteria.InstructorSearchCriteria;
import org.example.project.model.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    boolean existsByInstructorId(@NotBlank String instructorId);

    @Query("""
        SELECT i FROM Instructor i
        WHERE (:#{#criteria.name} IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT(:#{#criteria.name}, '%')))
          AND (:#{#criteria.surname} IS NULL OR LOWER(i.surname) LIKE LOWER(CONCAT(:#{#criteria.surname}, '%')))
          AND (:#{#criteria.email} IS NULL OR LOWER(i.user.email) LIKE LOWER(CONCAT(:#{#criteria.email}, '%')))
          AND (:#{#criteria.instructorId} IS NULL OR LOWER(i.instructorId) LIKE LOWER(CONCAT(:#{#criteria.instructorId}, '%')))
    """)
    Page<Instructor> search(InstructorSearchCriteria criteria, Pageable pageable);
}
