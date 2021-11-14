package ca.bazlur.visualizer.feed.service;

import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreData;
import ca.bazlur.visualizer.domain.dto.GeoIPLocationDTO;
import ca.bazlur.visualizer.domain.dto.MetaDTO;
import ca.bazlur.visualizer.domain.mapper.DataMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AbuseConfidenceScoreFeedServiceTest {

    private AbuseConfidenceScoreFeedService abuseConfidenceScoreFeedService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RawDBDemoGeoIPLocationService rawDBDemoGeoIPLocationService;

    private final OffsetDateTime lastReportedAt = OffsetDateTime.now();
    private final AbuseConfidenceScoreDTO br
        = AbuseConfidenceScoreDTO.builder()
                                 .ipAddress("177.170.213.41")
                                 .countryCode("BR")
                                 .score(100)
                                 .lastReportedAt(lastReportedAt)
                                 .build();
    private final AbuseConfidenceScoreDTO nl
        = AbuseConfidenceScoreDTO.builder()
                                 .ipAddress("161.35.82.195")
                                 .countryCode("NL")
                                 .score(100)
                                 .lastReportedAt(lastReportedAt)
                                 .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        abuseConfidenceScoreFeedService
            = new AbuseConfidenceScoreFeedServiceImpl(restTemplate, new DataMapperImpl(), rawDBDemoGeoIPLocationService);

        ReflectionTestUtils.setField(abuseConfidenceScoreFeedService,
            "abuseIPDBUrl", "https://api.abuseipdb.com/api/v2/blacklist?confidenceMinimum=25");
        ReflectionTestUtils.setField(abuseConfidenceScoreFeedService,
            "apiKey", "9ed87e5fe697c122f883fb290b20094629829af7cba79c8af84256c7626b9044a26bb7930a5a8a37");
    }

    @Test
    void testGetBlackListedIps() {
        var abuseConfidenceScoreData = new AbuseConfidenceScoreData();
        var meta = new MetaDTO();

        meta.setGeneratedAt(lastReportedAt);
        abuseConfidenceScoreData.setMeta(meta);
        abuseConfidenceScoreData.setData(List.of(br, nl));

        var dataResponseEntity = new ResponseEntity<>(abuseConfidenceScoreData, HttpStatus.OK);

        when(restTemplate.exchange(Mockito.any(RequestEntity.class),
            Mockito.<Class<AbuseConfidenceScoreData>>any()))
            .thenReturn(dataResponseEntity);

        var blackListedIps = abuseConfidenceScoreFeedService.getBlackListedIps();
        assertEquals(2, blackListedIps.getData().size());

        var data = blackListedIps.getData();
        assertEquals("177.170.213.41", data.get(0).getIpAddress());
        assertEquals("BR", data.get(0).getCountryCode());
        assertEquals(100, data.get(0).getScore());
        assertEquals(lastReportedAt, data.get(0).getLastReportedAt());

        assertEquals("161.35.82.195", data.get(1).getIpAddress());
        assertEquals("NL", data.get(1).getCountryCode());
        assertEquals(100, data.get(1).getScore());
        assertEquals(lastReportedAt, data.get(1).getLastReportedAt());
    }

    @Test
    void testMapIPAddressToGeoLocation() {
        when(rawDBDemoGeoIPLocationService.getLocation("161.35.82.195"))
            .thenReturn(Optional.of(
                    GeoIPLocationDTO
                        .builder()
                        .city("Amsterdam")
                        .country("Netherlands")
                        .latitude(52.352)
                        .longitude(4.9392)
                        .build())
                       );
        var abuseConfidenceScore = abuseConfidenceScoreFeedService.mapIPAddressToGeoLocation(nl);

        assertEquals("161.35.82.195", abuseConfidenceScore.getIpAddress());
        assertEquals("NL", abuseConfidenceScore.getCountryCode());
        assertEquals("Amsterdam", abuseConfidenceScore.getCity());
        assertEquals("Netherlands", abuseConfidenceScore.getCountry());
        assertEquals(52.352, abuseConfidenceScore.getLatitude());
        assertEquals(4.9392, abuseConfidenceScore.getLongitude());
    }
}
