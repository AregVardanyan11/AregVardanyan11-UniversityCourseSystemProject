package org.example.project.repository;

import org.example.project.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Long, Instructor> {
}
