package org.example.project.repository;


import org.example.project.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Long, Faculty> {
}
