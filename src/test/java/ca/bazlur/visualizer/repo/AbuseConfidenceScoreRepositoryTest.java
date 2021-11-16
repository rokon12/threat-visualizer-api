package ca.bazlur.visualizer.repo;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.dto.SearchAbuseConfidenceScoreQuery;
import ca.bazlur.visualizer.util.TestDataSetup;
import com.google.common.collect.Ordering;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AbuseConfidenceScoreRepositoryTest {

    private final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;

    @Autowired
    AbuseConfidenceScoreRepositoryTest(final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository) {
        this.abuseConfidenceScoreRepository = abuseConfidenceScoreRepository;
    }

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        var abuseConfidenceScores = TestDataSetup.prepareTestData();
        abuseConfidenceScoreRepository.saveAllAndFlush(abuseConfidenceScores);
    }

    @Test
    void testSearchByCountryAndSortedByCity() {
        var confidenceScores = abuseConfidenceScoreRepository.search(0, 10, "city",
            SearchAbuseConfidenceScoreQuery
                .builder()
                .country("Brazil")
                .build());
        assertTrue(confidenceScores.size() < 11);
        assertThat(confidenceScores, hasItem(Matchers.hasProperty("country", equalTo("Brazil"))));
        assertTrue(isSorted(confidenceScores, Comparator.comparing(AbuseConfidenceScore::getCity)));
    }

    @Test
    void testSearchByCountryAndSortedByCityDesc() {
        var confidenceScores = abuseConfidenceScoreRepository.search(0, 10, "city,desc",
            SearchAbuseConfidenceScoreQuery
                .builder()
                .country("Brazil")
                .build());
        assertTrue(confidenceScores.size() < 11);
        assertThat(confidenceScores, hasItem(Matchers.hasProperty("country", equalTo("Brazil"))));
        assertTrue(isSorted(confidenceScores, Comparator.comparing(AbuseConfidenceScore::getCity).reversed()));
    }

    @Test
    void testSearchByCountryCodeAndSortedByCityAsc() {
        var confidenceScores = abuseConfidenceScoreRepository.search(0, 10, "city,asc",
            SearchAbuseConfidenceScoreQuery
                .builder()
                .countryCode("GB")
                .build());
        assertTrue(confidenceScores.size() < 11);
        assertThat(confidenceScores, hasItem(Matchers.hasProperty("countryCode", equalTo("GB"))));
        assertTrue(isSorted(confidenceScores, Comparator.comparing(AbuseConfidenceScore::getCity)));
    }


    @Test
    void testSearchByIPAddress() {
        var confidenceScores = abuseConfidenceScoreRepository.search(0, 10, "city",
            SearchAbuseConfidenceScoreQuery
                .builder()
                .ipAddress("200.148.225.183")
                .build());

        System.out.println("confidenceScores = " + confidenceScores);
        assertEquals(1, confidenceScores.size());
        assertThat(confidenceScores, hasItem(Matchers.hasProperty("ipAddress", equalTo("200.148.225.183"))));
    }


    @Test
    void testSearchByCountryPaged() {
        var confidenceScores = abuseConfidenceScoreRepository.search(1, 10, "",
            SearchAbuseConfidenceScoreQuery
                .builder()
                .build());
        assertEquals(10, confidenceScores.size());
    }

    public static boolean isSorted(List<AbuseConfidenceScore> employees,
                                   Comparator<AbuseConfidenceScore> employeeComparator) {
        return Ordering.from(employeeComparator).isOrdered(employees);
    }

    @AfterEach
    void tearDown() {
        abuseConfidenceScoreRepository.deleteAll();
    }
}
