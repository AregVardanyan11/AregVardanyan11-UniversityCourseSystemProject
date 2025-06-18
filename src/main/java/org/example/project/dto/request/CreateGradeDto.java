package org.example.project.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGradeDto {

    @NotBlank
    @Size(max = 2)
    private String letter;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "4.0", inclusive = true)
    private Float value;
}
