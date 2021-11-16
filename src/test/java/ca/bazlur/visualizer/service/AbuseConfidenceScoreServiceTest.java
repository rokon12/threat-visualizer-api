package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.dto.Feature;
import ca.bazlur.visualizer.domain.dto.SearchAbuseConfidenceScoreQuery;
import ca.bazlur.visualizer.repo.AbuseConfidenceScoreRepository;
import ca.bazlur.visualizer.util.TestDataSetup;
import com.google.common.collect.Ordering;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;


class AbuseConfidenceScoreServiceTest {

    private AbuseConfidenceScoreService abuseConfidenceScoreService;

    @Mock
    private AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;

    private List<AbuseConfidenceScore> testData;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        MockitoAnnotations.openMocks(this);
        abuseConfidenceScoreService = new AbuseConfidenceScoreServiceImpl(abuseConfidenceScoreRepository);
        testData = TestDataSetup.prepareTestData();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetAbuseConfidenceScore() throws IOException, URISyntaxException {
        var tenItems = testData.stream().sorted(Comparator.comparing(AbuseConfidenceScore::getCountry)).limit(10).toList();
        when(abuseConfidenceScoreRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(tenItems));
        var geoJsonView = abuseConfidenceScoreService.getAbuseConfidenceScore(0, 10, "country");

        assertEquals(10, geoJsonView.getFeatures().size());
        var featureComparator = Comparator.comparing((Function<Feature, String>) feature -> feature.getProperties().getCountry());
        assertTrue(Ordering.from(featureComparator).isOrdered(geoJsonView.getFeatures()));
    }

    @Test
    void testSearch() {
        var filteredItems = testData.stream()
                                    .filter(abuseConfidenceScore -> abuseConfidenceScore.getCountry().equals("Brazil"))
                                    .sorted(Comparator.comparing(AbuseConfidenceScore::getCity)).limit(10).toList();
        when(abuseConfidenceScoreRepository.search(eq(0), eq(10), anyString(), any(SearchAbuseConfidenceScoreQuery.class)))
            .thenReturn(filteredItems);

        var geoJsonView = abuseConfidenceScoreService.search(0, 10, "city",
            SearchAbuseConfidenceScoreQuery.builder()
                                           .country("Brazil")
                                           .build());
        assertEquals(2, geoJsonView.getFeatures().size()); // We have two data with country, Brazil in test data
        var featureComparator = Comparator.comparing((Function<Feature, String>) feature -> feature.getProperties().getCity());
        assertTrue(Ordering.from(featureComparator).isOrdered(geoJsonView.getFeatures()));
    }
}
