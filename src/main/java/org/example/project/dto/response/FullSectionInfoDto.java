package org.example.project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FullSectionInfoDto {
    CourseResponseDto course;
    SectionResponseDto section;
    List<TimeSlotResponseDto> timeSlots;
}
