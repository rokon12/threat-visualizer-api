package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;

import java.util.List;

public interface AbuseConfidenceScoreService {
    List<AbuseConfidenceScore> findAll();
}
