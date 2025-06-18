package org.example.project.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.project.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    boolean existsByInstructorId(@NotBlank String instructorId);
}
