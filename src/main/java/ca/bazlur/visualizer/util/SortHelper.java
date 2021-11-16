package ca.bazlur.visualizer.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

public class SortHelper {
    private SortHelper() {
    }

    public static Sort prepareSortBy(final String sortBy) {
        if (StringUtils.isNotEmpty(sortBy)) {
            if (sortBy.contains(",")) {
                var sortOrders = sortBy.split(",");
                var field = sortOrders[0];
                var sortDirection = getSortDirection(sortOrders[1]);
                return Sort.by(new Sort.Order(sortDirection, field));
            } else {
                return Sort.by(new Sort.Order(null, sortBy));
            }
        } else {
            return Sort.unsorted();
        }
    }

    private static Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }
}
