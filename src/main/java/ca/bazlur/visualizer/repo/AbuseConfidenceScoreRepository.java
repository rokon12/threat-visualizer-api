package ca.bazlur.visualizer.repo;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AbuseConfidenceScoreRepository extends JpaRepository<AbuseConfidenceScore, String> {
}
