package ca.bazlur.visualizer.domain.mapper;

import ca.bazlur.visualizer.domain.User;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.dto.Feature;
import ca.bazlur.visualizer.domain.dto.Geometry;
import ca.bazlur.visualizer.util.TestDataSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class DataMapperTest {

    @Autowired
    private DataMapper mapper;

    @Test
    void testToAbuseConfidenceScoreSuccess() {
        var now = OffsetDateTime.now();
        var abuseConfidenceScoreDTO = AbuseConfidenceScoreDTO.builder()
                                                             .countryCode("BR")
                                                             .ipAddress("177.170.213.41")
                                                             .score(100)
                                                             .lastReportedAt(now)
                                                             .build();
        var abuseConfidenceScore = mapper.toAbuseConfidenceScore(abuseConfidenceScoreDTO);

        assertEquals(abuseConfidenceScoreDTO.getCountryCode(), abuseConfidenceScore.getCountryCode());
        assertEquals(abuseConfidenceScoreDTO.getIpAddress(), abuseConfidenceScore.getIpAddress());
        assertEquals(abuseConfidenceScoreDTO.getScore(), abuseConfidenceScore.getScore());
        assertEquals(abuseConfidenceScoreDTO.getLastReportedAt(), abuseConfidenceScore.getLastReportedAt());
    }

    @Test
    void testToAbuseConfidenceScore_givenNull_shouldReturnNull() {
        var abuseConfidenceScore = mapper.toAbuseConfidenceScore(null);
        assertNull(abuseConfidenceScore);
    }

    @Test
    void testToUser() {
        var userRequest = CreateUserRequest.builder()
                                           .username("username@example.ca")
                                           .fullName("Full name")
                                           .build();
        var user = mapper.toUser(userRequest);
        assertEquals(userRequest.getUsername(), user.getUsername());
        assertEquals(userRequest.getFullName(), user.getFullName());
    }

    @Test
    void testToUser_givenNull_shouldReturnNull() {
        var user = mapper.toUser(null);
        assertNull(user);
    }

    @Test
    void testToUserView() {
        var user = User.builder()
                       .username("username@example.ca")
                       .fullName("Full name")
                       .build();
        var userView = mapper.toUserView(user);
        assertEquals(user.getId(), userView.getId());
        assertEquals(user.getUsername(), userView.getUsername());
        assertEquals(user.getFullName(), userView.getFullName());
    }

    @Test
    void testToUserView_givenNull_shouldReturnNull() {
        var userView = mapper.toUserView(null);
        assertNull(userView);
    }

    @Test
    void testToGeoJsonView() throws IOException, URISyntaxException {
        var abuseConfidenceScores = TestDataSetup.prepareTestData();
        var abuseConfidenceScore = abuseConfidenceScores.get(0);
        var geoJsonView = mapper.toGeJsonView(List.of(abuseConfidenceScore).stream());
        assertNotNull(geoJsonView);
        assertEquals("FeatureCollection", geoJsonView.getType());
        assertEquals(1, geoJsonView.getFeatures().size());
        assertEquals(Feature.Type.FEATURE, geoJsonView.getFeatures().get(0).getType());
        assertEquals(2, geoJsonView.getFeatures().get(0).getGeometry().getCoordinates().size());
        assertEquals(Geometry.GeometryType.POINT, geoJsonView.getFeatures().get(0).getGeometry().getType());
        assertNotNull(geoJsonView.getFeatures().get(0).getProperties().getIp());
        assertEquals(geoJsonView.getFeatures().get(0).getGeometry().getCoordinates().get(0), abuseConfidenceScore.getLongitude());
        assertEquals(geoJsonView.getFeatures().get(0).getGeometry().getCoordinates().get(1), abuseConfidenceScore.getLatitude());
    }
}
