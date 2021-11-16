package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.dto.GeoJsonView;
import ca.bazlur.visualizer.domain.dto.SearchAbuseConfidenceScoreQuery;

public interface AbuseConfidenceScoreService {
    GeoJsonView getAbuseConfidenceScore(final int page, final int size, final String sortBy);

    GeoJsonView search(final int page, final int size, final String sortBy, final SearchAbuseConfidenceScoreQuery query);
}
