package org.example.project.repository;


import org.example.project.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Long, Course> {
}
