package org.example.project.repository;


import org.example.project.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
        SELECT c FROM Course c JOIN c.faculty f WHERE f.id=:facultyId
        """)
    Set<Course> coursesByFaculty(Long facultyId);
}
