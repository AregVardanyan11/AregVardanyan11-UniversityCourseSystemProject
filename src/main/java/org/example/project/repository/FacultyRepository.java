package org.example.project.repository;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.example.project.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Optional<Faculty> findByCode(String code);
}
