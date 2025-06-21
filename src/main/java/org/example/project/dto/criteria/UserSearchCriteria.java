package org.example.project.dto.criteria;

import lombok.Data;
import org.example.project.dto.enums.SortDirection;
import org.example.project.dto.enums.UserSortBy;

@Data
public class UserSearchCriteria extends SearchCriteria<UserSortBy, SortDirection>{
    private String username;
    private String email;

    @Override
    protected String resolveSortField(UserSortBy sortBy) {
        return switch (sortBy) {
            case USERNAME -> "username";
            case EMAIL -> "email";
        };
    }
}
