package org.example.project.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.request.CreateTimeSlotDto;
import org.example.project.dto.response.TimeSlotResponseDto;
import org.example.project.model.Section;
import org.example.project.model.TimeSlot;
import org.example.project.repository.SectionRepository;
import org.example.project.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public List<TimeSlotResponseDto> addTimeSlots(Set<CreateTimeSlotDto> dtos) {
        return dtos.stream().map(this::addTimeSlot)
                .collect(Collectors.toList());
    }

    public TimeSlotResponseDto addTimeSlot(CreateTimeSlotDto dto) {
        Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        boolean hasConflict = timeSlotRepository.existsByClassroomAndDayAndTimeOverlap(
                dto.getClassroom(), dto.getDay(), dto.getStartTime(), dto.getEndTime());

        if (hasConflict) {
            throw new IllegalArgumentException("Time conflict detected in classroom " + dto.getClassroom() + " on " + dto.getDay());
        }

        TimeSlot timeSlot = TimeSlot.builder()
                .classroom(dto.getClassroom())
                .day(dto.getDay())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .section(section)
                .build();

        TimeSlot saved = timeSlotRepository.save(timeSlot);

        return TimeSlotResponseDto.builder()
                .id(saved.getId())
                .classroom(saved.getClassroom())
                .day(saved.getDay())
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .sectionId(section.getId())
                .build();
    }

}
