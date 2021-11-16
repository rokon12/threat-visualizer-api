package ca.bazlur.visualizer.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortHelperTest {

    @Test
    void testPrepareSortBy_givenEmptyString_shouldReturnUnsorted() {
        var orders = SortHelper.prepareSortBy(null);
        assertEquals(Sort.unsorted(), orders);
    }

    @Test
    void testPrepareSortBy_givenFieldNameWithoutOrder_shouldReturnAscendingOrder() {
        var orders = SortHelper.prepareSortBy("country");
        assertEquals(orders.ascending(), orders);
    }

    @Test
    void testPrepareSortBy_givenFieldNameWithOrder_shouldReturnWithOrder() {
        var orders = SortHelper.prepareSortBy("country,desc");
        assertEquals(orders.descending(), orders);
    }

    @Test
    void testPrepareSortBy_givenFieldNameWithOrderMisspelled_shouldReturnWithAscendingOrder() {
        var orders = SortHelper.prepareSortBy("country,des");
        assertEquals(orders.ascending(), orders);
    }
}
