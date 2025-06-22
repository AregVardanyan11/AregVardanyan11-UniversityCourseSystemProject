package org.example.project.repository;

import org.example.project.model.Section;
import org.example.project.model.TimeSlot;
import org.example.project.model.enums.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    Set<TimeSlot> findAllBySectionId(Long sectionId);

    @Query("""
    SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
    FROM TimeSlot t
    WHERE t.classroom = :classroom
      AND t.day = :day
      AND (
            (:startTime < t.endTime AND :endTime > t.startTime)
          )
        """)
    boolean existsByClassroomAndDayAndTimeOverlap(
            @Param("classroom") String classroom,
            @Param("day") WeekDay day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<TimeSlot> findAllByClassroomAndDayOrderByStartTime(String classroom, WeekDay day);


}
