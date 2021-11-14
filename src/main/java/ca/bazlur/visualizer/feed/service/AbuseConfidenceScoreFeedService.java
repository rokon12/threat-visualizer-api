package ca.bazlur.visualizer.feed.service;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreDTO;
import ca.bazlur.visualizer.domain.dto.AbuseConfidenceScoreData;

public interface AbuseConfidenceScoreFeedService {

    AbuseConfidenceScoreData getBlackListedIps();

    AbuseConfidenceScore mapIPAddressToGeoLocation(final AbuseConfidenceScoreDTO dto);
}
