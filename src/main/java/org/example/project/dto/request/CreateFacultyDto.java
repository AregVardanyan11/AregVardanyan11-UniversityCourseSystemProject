package org.example.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFacultyDto {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2,4}$", message = "code must be 2 to 4 uppercase letters")
    private String code;
}
