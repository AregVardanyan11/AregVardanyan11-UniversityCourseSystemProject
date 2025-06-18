package org.example.project.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.project.dto.response.UserResponseDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateInstructorDto extends UserResponseDto {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String bio;

    @NotBlank
    private String instructorId;

}
