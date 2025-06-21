package org.example.project.dto.criteria;

import lombok.*;
import org.example.project.dto.enums.SectionSortBy;
import org.example.project.dto.enums.StudentSortBy;
import org.example.project.model.enums.Semester;
import org.example.project.dto.enums.SortDirection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionSearchCriteria extends SearchCriteria<SectionSortBy, SortDirection> {

    private Integer year;
    private Semester semester;
    private Character letter;

    private String courseNumCode;
    private Integer credit;
    private String facultyName;

    private String instructorName;
    private String instructorSurname;


    @Override
    protected String resolveSortField(SectionSortBy sectionSortBy) {
        return switch (getSortBy()) {
            case YEAR -> "year";
            case SEMESTER -> "semester";
            case LETTER -> "letter";
            case COURSE_NUM_CODE -> "course.numCode";
            case INSTRUCTOR_NAME -> "instructor.name";
            default -> "id";
        };
    }
}
