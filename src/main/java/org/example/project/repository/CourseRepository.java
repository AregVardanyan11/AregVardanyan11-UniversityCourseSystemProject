package org.example.project.repository;


import org.example.project.model.Course;
import org.example.project.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Set<Course> findAllByFacultyId(Long facultyId);

    Optional<Course> findByFacultyIdAndNumCode(Long facultyId, String numCode);
}
