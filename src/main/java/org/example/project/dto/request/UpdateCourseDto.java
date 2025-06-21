package org.example.project.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCourseDto {

    @NotNull
    private Long facultyId;

    @NotBlank
    @Pattern(regexp = "^[0-9]{3}$", message = "numCode must be exactly 3 digits")
    private String numCode;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(1)
    @Max(5)
    private Integer credit;

    private Set<Long> addPrerequisiteIds;

    private Set<Long> removePrerequisiteIds;
}
