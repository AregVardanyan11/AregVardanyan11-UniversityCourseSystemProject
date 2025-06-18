package org.example.project.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InstructorResponseDto extends UserResponseDto {
    private Long id;
    private String name;
    private String surname;
    private String bio;
    private String instructorId;
}
