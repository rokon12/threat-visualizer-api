package ca.bazlur.visualizer.feed.service;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreData;
import ca.bazlur.visualizer.domain.mapper.DataMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

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
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("Accept", "application/json");
        headers.add("Key", apiKey);

        var httpEntity = new HttpEntity<AbuseConfidenceScoreData>(headers);
        var forEntity
            = restTemplate.exchange(abuseIPDBUrl, HttpMethod.GET, httpEntity, AbuseConfidenceScoreData.class);
        return forEntity.getBody();
    }

    public AbuseConfidenceScore mapIPAddressToGeoLocation(final AbuseConfidenceScoreDTO dto) {
        var abuseConfidenceScore = mapStructMapper.toAbuseConfidenceScore(dto);
        var geoIpLocation = rawDBDemoGeoIPLocationService.getLocation(dto.getIpAddress());

        if (geoIpLocation.isPresent()) {
            var geoIPLocation = geoIpLocation.get();
            abuseConfidenceScore.setCity(geoIPLocation.getCity());
            abuseConfidenceScore.setCountry(geoIPLocation.getCountry());
            abuseConfidenceScore.setLongitude(geoIPLocation.getLongitude());
            abuseConfidenceScore.setLatitude(geoIPLocation.getLatitude());
        }

        return abuseConfidenceScore;
    }
}
