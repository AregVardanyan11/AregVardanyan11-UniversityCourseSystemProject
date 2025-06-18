package org.example.project.dto.response;

import lombok.*;
import org.example.project.model.enums.WeekDay;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotResponseDto {
    private Long id;
    private String classroom;
    private WeekDay day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long sectionId;
}
