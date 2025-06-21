package org.example.project.dto.criteria;

import lombok.*;
import org.example.project.dto.enums.StudentSortBy;
import org.hibernate.query.SortDirection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSearchCriteria extends SearchCriteria<StudentSortBy, SortDirection> {
    private String name;
    private String surname;
    private String studentId;
    private Long facultyId;
    private String username;
    private Float gpa;
    private Boolean belowGpa;
    private String email;

    @Override
    protected String resolveSortField(StudentSortBy sortBy) {
        return switch (sortBy) {
            case NAME -> "name";
            case SURNAME -> "surname";
            case STUDENT_ID -> "studentId";
            case USERNAME -> "user.username";
        };
    }
}
