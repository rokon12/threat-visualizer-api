package ca.bazlur.visualizer.domain.mapper;

import ca.bazlur.visualizer.domain.User;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
