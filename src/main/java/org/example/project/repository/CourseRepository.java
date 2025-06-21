package org.example.project.repository;


import org.example.project.dto.criteria.CourseSearchCriteria;
import org.example.project.model.Course;
import org.example.project.model.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Set<Course> findAllByFacultyId(Long facultyId);

    Optional<Course> findByFacultyIdAndNumCode(Long facultyId, String numCode);


    @Query("""
    SELECT c FROM Course c
    WHERE (:#{#criteria.name} IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :#{#criteria.name}, '%')))
      AND (:#{#criteria.numCode} IS NULL OR c.numCode = :#{#criteria.numCode})
      AND (:#{#criteria.facultyCode} IS NULL OR c.faculty.code = :#{#criteria.facultyCode})
      AND (:#{#criteria.minCredit} IS NULL OR c.credit >= :#{#criteria.minCredit})
      AND (:#{#criteria.maxCredit} IS NULL OR c.credit <= :#{#criteria.maxCredit})
        """)
    Page<Course> search(CourseSearchCriteria criteria, Pageable pageable);

}
