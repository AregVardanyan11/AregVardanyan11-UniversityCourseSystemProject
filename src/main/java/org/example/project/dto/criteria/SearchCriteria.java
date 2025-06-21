package org.example.project.dto.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class SearchCriteria<TSortBy extends Enum<TSortBy>, TSortDirection extends Enum<TSortDirection>> {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private int page;
    private int size;

    private TSortBy sortBy;
    private TSortDirection sortDirection;

    public PageRequest buildPageRequest(String defaultSortField) {
        int pageNumber = Math.max(page, 0);
        int pageSize = size <= 0 ? DEFAULT_PAGE_SIZE : size;

        Sort sort = buildSort(defaultSortField);
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    protected abstract String resolveSortField(TSortBy sortBy);

    private Sort buildSort(String defaultField) {
        if (sortBy == null) {
            return Sort.by(defaultField).ascending();
        }

        String field = resolveSortField(sortBy);
        return (sortDirection != null && sortDirection.name().equalsIgnoreCase("DESC"))
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();
    }
}
