package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.dto.GeoJsonView;

public interface AbuseConfidenceScoreService {
    GeoJsonView getAbuseConfidenceScore(final int page, final int size, final String sortBy);
}
