package org.example.project.dto.response;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDto {
    private Long id;
    private String name;
    private String numCode;
    private String description;
    private Integer credit;
    private Long facultyId;
    private Set<Long> prerequisiteIds;
}
