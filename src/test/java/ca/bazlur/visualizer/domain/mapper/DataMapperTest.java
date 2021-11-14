package ca.bazlur.visualizer.domain.mapper;

import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DataMapperTest {

    @Autowired
    private DataMapper mapper;

    @Test
    void testToAbuseConfidenceScore() {
        var now = OffsetDateTime.now();
        var scoreDTO = AbuseConfidenceScoreDTO.builder()
                                              .countryCode("BR")
                                              .ipAddress("177.170.213.41")
                                              .score(100)
                                              .lastReportedAt(now)
                                              .build();
        var map = mapper.toAbuseConfidenceScore(scoreDTO);

        assertEquals(map.getCountryCode(), scoreDTO.getCountryCode());
        assertEquals(map.getIpAddress(), scoreDTO.getIpAddress());
        assertEquals(map.getScore(), scoreDTO.getScore());
        assertEquals(map.getLastReportedAt(), scoreDTO.getLastReportedAt());
    }

    @Test
    void testToUser() {
        var userRequest = CreateUserRequest.builder()
                                           .username("username@example.ca")
                                           .fullName("Full name")
                                           .build();
        var user = mapper.toUser(userRequest);
        assertEquals(user.getUsername(), userRequest.getUsername());
        assertEquals(user.getFullName(), userRequest.getFullName());
    }
}
