package org.example.project.repository;


import org.example.project.dto.criteria.SectionSearchCriteria;
import org.example.project.model.Course;
import org.example.project.model.Section;
import org.example.project.model.Student;
import org.example.project.model.enums.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Year;
import java.util.Set;

public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query("""
        SELECT s FROM Section s JOIN s.course c JOIN c.faculty f WHERE f.id=:facultyId
        """)
    Set<Section> findAllBy(Long facultyId);

    Set<Section> findAllByYearAndSemester(Integer year, Semester semester);

    Set<Section> findAllByCourseId(Long courseId);

    Set<Section> findAllByCourseIdAndFinished(Long courseId, boolean finished);

    @Query("""
        SELECT se FROM Takes t JOIN t.section se JOIN  t.student st WHERE st.id = :studentId
        """)
    Set<Section> sectionsByStudent(Long studentId);

    Set<Section> findAllByInstructorId(Long instructorId);

    @Query("""
    SELECT s FROM Section s
    JOIN s.course c
    JOIN c.faculty f
    JOIN s.instructor i
    WHERE (:#{#criteria.year} IS NULL OR s.year = :#{#criteria.year})
      AND (:#{#criteria.semester} IS NULL OR s.semester = :#{#criteria.semester})
      AND (:#{#criteria.letter} IS NULL OR s.letter = :#{#criteria.letter})
      AND (:#{#criteria.courseNumCode} IS NULL OR c.numCode = :#{#criteria.courseNumCode})
      AND (:#{#criteria.credit} IS NULL OR c.credit = :#{#criteria.credit})
      AND (:#{#criteria.facultyName} IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :#{#criteria.facultyName}, '%')))
      AND (:#{#criteria.instructorName} IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :#{#criteria.instructorName}, '%')))
      AND (:#{#criteria.instructorSurname} IS NULL OR LOWER(i.surname) LIKE LOWER(CONCAT('%', :#{#criteria.instructorSurname}, '%')))
""")
    Page<Section> search(SectionSearchCriteria criteria, Pageable pageable);

}
