package org.example.project.repository;

import org.example.project.model.Section;
import org.example.project.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query("""
        SELECT t FROM TimeSlot t JOIN t.section s WHERE s.id = :sectionId
    """)
    Set<TimeSlot> getAllBySection(Long sectionId);


}
