package org.example.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateStudentDto extends CreateUserDto{

    @NotBlank
    private String name;

    @NotBlank
    private String studentId;

    @NotNull
    private Long userId;

    @NotNull
    private Long facultyId;


}
