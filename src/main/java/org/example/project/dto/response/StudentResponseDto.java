package org.example.project.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StudentResponseDto extends UserResponseDto{
    private Long id;
    private String name;
    private String studentId;
    private Long userId;
    private Long facultyId;
}
