package org.example.project.repository;

import org.example.project.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<Long, TimeSlot> {
}
