package org.example.project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInstructorDto {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String bio;

    @NotBlank
    private String username;

    @Email
    private String email;
} 