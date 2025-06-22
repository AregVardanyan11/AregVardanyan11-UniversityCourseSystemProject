package org.example.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignGradeRequest {
    @NotNull
    private Long studentId;

    @NotNull
    private Long sectionId;

    @NotNull
    private String grade;
}
