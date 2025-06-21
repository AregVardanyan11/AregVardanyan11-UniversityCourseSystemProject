package org.example.project.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseHierarchy {
    private Long id;
    private String fullName;
    private Set<CourseHierarchy> prerequisites;

}
