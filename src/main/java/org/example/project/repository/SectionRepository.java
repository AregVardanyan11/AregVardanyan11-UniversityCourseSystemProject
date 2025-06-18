package org.example.project.repository;


import org.example.project.model.Course;
import org.example.project.model.Section;
import org.example.project.model.Student;
import org.example.project.model.enums.Semester;
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
}
