package ca.bazlur.visualizer.web;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.repo.AbuseConfidenceScoreRepository;
import ca.bazlur.visualizer.util.JsonHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@WithMockUser
class AbuseConfidenceScoreResourceTest {

    private final MockMvc mockMvc;
    private final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;

    @Value("classpath:data/abuse_confidence_score.json")
    Resource resourceFile;

    private final ObjectMapper objectMapper;

    @Autowired
    AbuseConfidenceScoreResourceTest(final MockMvc mockMvc,
                                     final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository, final ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.abuseConfidenceScoreRepository = abuseConfidenceScoreRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        abuseConfidenceScoreRepository.saveAllAndFlush(
            List.of(AbuseConfidenceScore.builder()
                                        .ipAddress("177.170.213.41")
                                        .city("Itaquaquecetuba")
                                        .country("Brazil")
                                        .countryCode("BR")
                                        .lastReportedAt(OffsetDateTime.now())
                                        .score(100)
                                        .latitude(-23.4553)
                                        .longitude(-46.3348)
                                        .build(),
                AbuseConfidenceScore.builder()
                                    .ipAddress("161.35.82.195")
                                    .city("Amsterdam")
                                    .country("Netherlands")
                                    .countryCode("NL")
                                    .lastReportedAt(OffsetDateTime.now())
                                    .score(100)
                                    .latitude(52.352)
                                    .longitude(4.9392)
                                    .build()));

    }

    private void addTestData() throws IOException {
        var json = Files.readString(Path.of(resourceFile.getURI()));
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        var abuseConfidenceScores
            = JsonHelper.fromJsonWithOffsetDateTimeAndDateTimeFormatter(objectMapper, dateTimeFormatter, json, new TypeReference<List<AbuseConfidenceScore>>() {
        });
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
            .andExpect(jsonPath("$.features", hasSize(2)))
            .andExpect(jsonPath("$.features[0].type").value("Feature"))
            .andExpect(jsonPath("$.features[0].geometry.coordinates", hasSize(2)))
            .andExpect(jsonPath("$.features[0].geometry.type").value("Point"))
            .andExpect(jsonPath("$.features[0].properties.country").value("Brazil"))
            .andExpect(jsonPath("$.features[0].properties.score").value(100))
            .andExpect(jsonPath("$.features[0].properties.city").value("Itaquaquecetuba"))
            .andExpect(jsonPath("$.features[0].properties.ip").value("177.170.213.41"))
            .andExpect(jsonPath("$.features[1].type").value("Feature"))
            .andExpect(jsonPath("$.features[1].geometry.coordinates", hasSize(2)))
            .andExpect(jsonPath("$.features[1].geometry.type").value("Point"))
            .andExpect(jsonPath("$.features[1].properties.country").value("Netherlands"))
            .andExpect(jsonPath("$.features[1].properties.score").value(100))
            .andExpect(jsonPath("$.features[1].properties.city").value("Amsterdam"))
            .andExpect(jsonPath("$.features[1].properties.ip").value("161.35.82.195"))
            .andReturn();
    }

    @Test
    void testGetScoreByPageSize() throws Exception {
        addTestData();

        this.mockMvc
            .perform(get("/api/v1/abuse-confidence-score?page=0&size=10&sortBy=country")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.type").value("FeatureCollection"))
            .andExpect(jsonPath("$.features", hasSize(10)))
            .andExpect(jsonPath("$.features[0].properties.country").value("Brazil"))
            .andReturn();
    }
}
