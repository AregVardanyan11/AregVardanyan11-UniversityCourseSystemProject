package org.example.project.dto.response;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionResponseDto {
    private Long id;
    private Long courseId;
    private Character letter;
    private Integer year;
    private String semester;
    private boolean finished;
    private Long instructorId;
    private Integer maximumSeats;
    private Integer availableSeats;
    private Set<TimeSlotResponseDto> timeSlotIds;
}
