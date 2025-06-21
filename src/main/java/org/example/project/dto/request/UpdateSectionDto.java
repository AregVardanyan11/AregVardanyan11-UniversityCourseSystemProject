package org.example.project.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateSectionDto {
    @NotNull
    private Long courseId;

    @NotNull
    private Character letter;

    @NotNull
    private Integer year;

    @NotNull
    private String semester;

    @NotNull
    private Long instructorId;

    @NotNull
    @Pattern(regexp = "^.+\\.pdf$", message = "Syllabus path must point to a PDF file")
    private String syllabusPath;

    @Min(1)
    @NotNull
    private Integer maximumSeats;

    private boolean finished;
}
