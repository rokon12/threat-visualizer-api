package ca.bazlur.visualizer.feed.service;

import ca.bazlur.visualizer.feed.dto.AbuseConfidenceScoreData;

public interface AbuseConfidenceScoreFeedService {

    AbuseConfidenceScoreData getBlackListedIps();
}
