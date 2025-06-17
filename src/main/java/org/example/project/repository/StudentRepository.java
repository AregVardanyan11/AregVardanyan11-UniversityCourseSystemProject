package org.example.project.repository;

import org.example.project.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Long, Student> {

}
