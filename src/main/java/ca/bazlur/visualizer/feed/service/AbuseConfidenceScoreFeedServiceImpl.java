package ca.bazlur.visualizer.feed.service;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreData;
import ca.bazlur.visualizer.domain.mapper.DataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
public class AbuseConfidenceScoreFeedServiceImpl implements AbuseConfidenceScoreFeedService {

    private final RestTemplate restTemplate;
    private final DataMapper mapStructMapper;
    private final RawDBDemoGeoIPLocationService rawDBDemoGeoIPLocationService;

    @Value("${abuse.db.api.key}")
    private String apiKey;

    @Value("${abuse.db.api.url}")
    private String abuseIPDBUrl;

    public AbuseConfidenceScoreFeedServiceImpl(final RestTemplate restTemplate,
                                               final DataMapper mapStructMapper,
                                               final RawDBDemoGeoIPLocationService rawDBDemoGeoIPLocationService) {
        this.restTemplate = restTemplate;
        this.mapStructMapper = mapStructMapper;
        this.rawDBDemoGeoIPLocationService = rawDBDemoGeoIPLocationService;
    }

    @Override
    public AbuseConfidenceScoreData getBlackListedIps() {
        var requestEntity = RequestEntity
            .get(URI.create(abuseIPDBUrl))
            .headers(httpHeaders -> {
                httpHeaders.add("Accept", "application/json");
                httpHeaders.add("Key", apiKey);
            }).build();

        var response = restTemplate.exchange(requestEntity, AbuseConfidenceScoreData.class);
        return response.getBody();
    }

    public AbuseConfidenceScore mapIPAddressToGeoLocation(final AbuseConfidenceScoreDTO dto) {
        var abuseConfidenceScore = mapStructMapper.toAbuseConfidenceScore(dto);
        var geoIpLocation = rawDBDemoGeoIPLocationService.getLocation(dto.getIpAddress());

        if (geoIpLocation.isPresent()) {
            try {
                var geoIPLocation = geoIpLocation.get();
                abuseConfidenceScore.setCity(geoIPLocation.getCity());
                abuseConfidenceScore.setCountry(geoIPLocation.getCountry());
                abuseConfidenceScore.setLongitude(geoIPLocation.getLongitude());
                abuseConfidenceScore.setLatitude(geoIPLocation.getLatitude());
            } catch (Exception e) {
                log.info("Failed to get geo location for {}", dto.getIpAddress());
            }
        }

        return abuseConfidenceScore;
    }
}
