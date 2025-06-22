package org.example.project.repository;

import org.example.project.model.Takes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TakesRepository extends JpaRepository<Takes, Long> {
    Optional<Takes> findByStudentIdAndSectionId(Long studentId, Long sectionId);
}
