package org.example.project.repository;


import org.example.project.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Long, Section> {
}
