package org.example.project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserDto {
    @NotBlank
    private String username;

    private String password;

    @Email
    @NotBlank
    private String email;

}
