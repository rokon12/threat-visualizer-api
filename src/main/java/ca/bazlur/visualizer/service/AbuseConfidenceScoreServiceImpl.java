package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.dto.GeoJsonView;
import ca.bazlur.visualizer.domain.dto.SearchAbuseConfidenceScoreQuery;
import ca.bazlur.visualizer.domain.mapper.DataMapper;
import ca.bazlur.visualizer.repo.AbuseConfidenceScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static ca.bazlur.visualizer.util.SortHelper.prepareSortBy;

@Service
@AllArgsConstructor
public class AbuseConfidenceScoreServiceImpl implements AbuseConfidenceScoreService {
    private final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;
    private final DataMapper dataMapper;

    @Override
    public GeoJsonView getAbuseConfidenceScore(final int page, final int limit,
                                               final String sortBy) {
        var sortOrder = prepareSortBy(sortBy);
        return dataMapper.toGeJsonView(
            abuseConfidenceScoreRepository.findAll(PageRequest.of(page, limit, sortOrder)).stream());
    }

    @Override
    public GeoJsonView search(final int page, final int size, final String sortBy,
                              final SearchAbuseConfidenceScoreQuery query) {

        return dataMapper.toGeJsonView(
            abuseConfidenceScoreRepository.search(page, size, sortBy, query).stream());
    }
}
