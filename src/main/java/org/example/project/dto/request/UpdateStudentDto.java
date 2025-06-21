package org.example.project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentDto {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotNull
    private Long facultyId;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;
}
