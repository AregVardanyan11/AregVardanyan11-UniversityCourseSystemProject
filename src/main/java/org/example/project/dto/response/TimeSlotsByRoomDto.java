package org.example.project.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.project.model.enums.WeekDay;

import java.time.LocalTime;

@Data
@Builder
public class TimeSlotsByRoomDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long sectionId;
    private String fullSectionCourseName;
}
