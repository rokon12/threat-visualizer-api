package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.repo.AbuseConfidenceScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AbuseConfidenceScoreServiceImpl implements AbuseConfidenceScoreService {
    private final AbuseConfidenceScoreRepository abuseConfidenceScoreRepository;

    @Override
    public List<AbuseConfidenceScore> findAll() {
        return abuseConfidenceScoreRepository.findAll();
    }
}
