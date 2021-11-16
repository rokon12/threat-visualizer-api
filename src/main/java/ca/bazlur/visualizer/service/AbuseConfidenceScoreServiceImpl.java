package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.dto.Feature;
import ca.bazlur.visualizer.domain.dto.GeoJsonView;
import ca.bazlur.visualizer.domain.dto.Geometry;
import ca.bazlur.visualizer.domain.dto.Properties;
import ca.bazlur.visualizer.domain.dto.SearchAbuseConfidenceScoreQuery;
import ca.bazlur.visualizer.repo.AbuseConfidenceScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static ca.bazlur.visualizer.util.SortHelper.prepareSortBy;

@Service
@AllArgsConstructor
public class AbuseConfidenceScoreServiceImpl implements AbuseConfidenceScoreService {
    private final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;

    @Override
    public GeoJsonView getAbuseConfidenceScore(final int page, final int limit, final String sortBy) {
        var sortOrder = prepareSortBy(sortBy);
        return GeoJsonView.builder()
                          .features(abuseConfidenceScoreRepository.findAll(PageRequest.of(page, limit, sortOrder))
                                                                  .get()
                                                                  .map(this::toFeature)
                                                                  .toList())
                          .build();
    }

    @Override
    public GeoJsonView search(final int page, final int size, final String sortBy, final SearchAbuseConfidenceScoreQuery query) {
        var features = abuseConfidenceScoreRepository.search(page, size, sortBy, query)
                                                     .stream()
                                                     .map(this::toFeature)
                                                     .toList();
        return GeoJsonView.builder()
                          .features(features)
                          .build();

    }

    private Feature toFeature(final AbuseConfidenceScore item) {
        return Feature.builder()
                      .geometry(Geometry.builder()
                                        .type(Geometry.GeometryType.Point)
                                        .coordinates(List.of(item.getLatitude(), item.getLongitude()))
                                        .build())
                      .properties(Properties.builder()
                                            .score(item.getScore())
                                            .country(item.getCountry())
                                            .ip(item.getIpAddress())
                                            .city(item.getCity())
                                            .lastReportedAt(item.getLastReportedAt())
                                            .build())
                      .build();
    }
}
