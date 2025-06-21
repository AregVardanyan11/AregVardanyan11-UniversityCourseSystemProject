package org.example.project.dto.criteria;

import lombok.*;
import org.example.project.dto.enums.CourseSortBy;
import org.example.project.dto.enums.SortDirection;
import org.example.project.dto.enums.StudentSortBy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSearchCriteria extends SearchCriteria<CourseSortBy, SortDirection> {

    private String facultyCode;
    private String numCode;
    private String name;
    private Integer minCredit;
    private Integer maxCredit;

    @Override
    protected String resolveSortField(CourseSortBy courseSortBy) {
        return switch (getSortBy()) {
            case NAME -> "name";
            case CREDIT -> "credit";
            case NUM_CODE -> "numCode";
        };
    }
}
