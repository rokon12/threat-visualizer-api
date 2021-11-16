package ca.bazlur.visualizer.repo;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "abuseConfidenceScores")
public interface AbuseConfidenceScoreRepository extends JpaRepository<AbuseConfidenceScore, String> {

    @CacheEvict(allEntries = true)
    <S extends AbuseConfidenceScore> List<S> saveAllAndFlush(Iterable<S> entities);

    @Cacheable
    Page<AbuseConfidenceScore> findAll(Pageable pageable);
}
