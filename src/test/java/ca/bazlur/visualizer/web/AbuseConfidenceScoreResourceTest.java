package ca.bazlur.visualizer.web;

import ca.bazlur.visualizer.domain.dto.SearchAbuseConfidenceScoreQuery;
import ca.bazlur.visualizer.domain.dto.SearchRequest;
import ca.bazlur.visualizer.repo.AbuseConfidenceScoreRepository;
import ca.bazlur.visualizer.util.JsonHelper;
import ca.bazlur.visualizer.util.TestDataSetup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@WithMockUser
class AbuseConfidenceScoreResourceTest {

    private final MockMvc mockMvc;
    private final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;

    @Autowired
    AbuseConfidenceScoreResourceTest(final MockMvc mockMvc,
                                     final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository) {
        this.mockMvc = mockMvc;
        this.abuseConfidenceScoreRepository = abuseConfidenceScoreRepository;
    }

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        var abuseConfidenceScores = TestDataSetup.prepareTestData();
        abuseConfidenceScoreRepository.saveAllAndFlush(abuseConfidenceScores);
    }

    @AfterEach
    void tearDown() {
        abuseConfidenceScoreRepository.deleteAll();
    }

    @Test
    void testGetScore() throws Exception {
        this.mockMvc
            .perform(get("/api/v1/abuse-confidence-score")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.type").value("FeatureCollection"))
            .andExpect(jsonPath("$.features", hasSize(50)))
            .andExpect(jsonPath("$.features[0].type").value("Feature"))
            .andExpect(jsonPath("$.features[0].geometry.coordinates", hasSize(2)))
            .andExpect(jsonPath("$.features[0].geometry.type").value("Point"))
            .andExpect(jsonPath("$.features[0].properties.score").value(100))
            .andExpect(jsonPath("$.features[1].type").value("Feature"))
            .andExpect(jsonPath("$.features[1].geometry.coordinates", hasSize(2)))
            .andExpect(jsonPath("$.features[1].geometry.type").value("Point"))
            .andExpect(jsonPath("$.features[1].properties.score").value(100))
            .andReturn();
    }

    @Test
    void testGetScoreByPageSizeAndOrder() throws Exception {
        this.mockMvc
            .perform(get("/api/v1/abuse-confidence-score?page=0&size=10&sortBy=country")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.type").value("FeatureCollection"))
            .andExpect(jsonPath("$.features", hasSize(10)))
            .andExpect(jsonPath("$.features[0].properties.country").value("Brazil"))
            .andReturn();
    }

    @Test
    void testSearchSuccess() throws Exception {
        this.mockMvc
            .perform(post("/api/v1/abuse-confidence-score/search")
                .content(JsonHelper.toJson(new ObjectMapper(),
                    new SearchRequest<>(SearchAbuseConfidenceScoreQuery.builder()
                                                                       .country("Brazil")
                                                                       .build())))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.type").value("FeatureCollection"))
            .andExpect(jsonPath("$.features", hasSize(2)))
            .andExpect(jsonPath("$.features[0].properties.country").value("Brazil"))
            .andReturn();
    }

    @Test
    void testSearchFail() throws Exception {
        this.mockMvc
            .perform(post("/api/v1/abuse-confidence-score/search")
                .content(JsonHelper.toJson(new ObjectMapper(),
                    (SearchAbuseConfidenceScoreQuery.builder()
                                                    .country("Brazil")
                                                    .build())))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Method argument validation failed"))
            .andExpect(jsonPath("$.details[0].field").value("query"))
            .andExpect(jsonPath("$.details[0].errorMessage").value("must not be null"))
            .andReturn();
    }
}
