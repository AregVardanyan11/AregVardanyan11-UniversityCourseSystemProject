package org.example.project.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSectionDto {
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

    @NotBlank
    @Pattern(regexp = "^.+\\.pdf$", message = "Syllabus path must point to a PDF file")
    private String syllabusPath;

    @NotNull
    @Min(1)
    private Integer maximumSeats;

    private boolean finished;

    private Set<Long> timeSlotIds;
}
