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
    Set<Section> sectionsByFaculty(Long facultyId);

    @Query("""
        SELECT s FROM Section s WHERE s.year = :year AND s.semester = :semseter
        """)
    Set<Section> sectionsByYearAndSemester(Integer year, Semester semester);

    @Query("""
        SELECT s FROM Section s WHERE s.course.id = :courseId
        """)
    Set<Section> sectionsByCourse(Integer courseId);

    @Query("""
        SELECT s FROM Section s WHERE s.course.id = :courseId AND s.finished = :finished
        """)
    Set<Section> SectionsByCourse(Integer courseId, boolean finished);

    @Query("""
        SELECT se FROM Takes t JOIN t.section se JOIN  t.student st WHERE st.id = :studentId
        """)
    Set<Section> sectionsByStudent(Long studentId);

    @Query("""
        SELECT s FROM Section s JOIN s.instructor i WHERE  i.id= :instructorId
        """)
    Set<Section> sectionsByInstructor(Long instructorId);
}
