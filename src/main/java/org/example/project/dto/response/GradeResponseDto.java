package org.example.project.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeResponseDto {
    private String letter;
    private Float value;
}
