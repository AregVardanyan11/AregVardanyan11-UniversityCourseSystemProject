package org.example.project.dto.criteria;

import lombok.*;
import org.example.project.dto.enums.InstructorSortBy;
import org.example.project.dto.enums.SortDirection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorSearchCriteria extends SearchCriteria<InstructorSortBy, SortDirection> {

    private String name;
    private String surname;
    private String email;
    private String instructorId;

    @Override
    protected String resolveSortField(InstructorSortBy instructorSortBy) {
        return switch (getSortBy()) {
            case NAME -> "name";
            case SURNAME -> "surname";
            case EMAIL -> "user.email";
            case INSTRUCTOR_ID -> "instructorId";
        };
    }
}
