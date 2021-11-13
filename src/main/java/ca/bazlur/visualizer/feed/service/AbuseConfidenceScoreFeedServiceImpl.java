package ca.bazlur.visualizer.feed.service;

import ca.bazlur.visualizer.feed.dto.AbuseConfidenceScoreData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AbuseConfidenceScoreFeedServiceImpl implements AbuseConfidenceScoreFeedService {

    private final RestTemplate restTemplate;

    @Value("${abuse.db.api.key}")
    private String apiKey;

    @Value("${abuse.db.api.url}")
    private String abuseIPDBUrl;

    public AbuseConfidenceScoreFeedServiceImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
}
