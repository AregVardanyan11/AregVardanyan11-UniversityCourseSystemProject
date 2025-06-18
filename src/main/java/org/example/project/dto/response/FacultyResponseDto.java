package org.example.project.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacultyResponseDto {
    private Long id;
    private String name;
    private String code;
}
