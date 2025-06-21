package org.example.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.project.model.enums.WeekDay;

import java.time.LocalTime;

@Getter
@Setter
public class UpdateTimeSlotDto {

    @NotNull
    private String classroom;

    @NotNull
    private WeekDay day;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}
