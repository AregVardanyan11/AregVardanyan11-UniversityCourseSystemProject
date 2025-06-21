package org.example.project.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGradeDto {

    @NotNull
    @Min(0)
    @Max(4)
    private Float value;
}
