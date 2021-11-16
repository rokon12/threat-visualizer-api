package ca.bazlur.visualizer.repo;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import ca.bazlur.visualizer.domain.dto.SearchAbuseConfidenceScoreQuery;
import ca.bazlur.visualizer.util.SortHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Repository
@CacheConfig(cacheNames = "abuseConfidenceScores")
public interface AbuseConfidenceScoreRepository extends JpaRepository<AbuseConfidenceScore, String>, AbuseConfidenceScoreRepositoryCustom {

    @CacheEvict(allEntries = true)
    <S extends AbuseConfidenceScore> List<S> saveAllAndFlush(Iterable<S> entities);

    @Cacheable
    Page<AbuseConfidenceScore> findAll(Pageable pageable);
}

interface AbuseConfidenceScoreRepositoryCustom {
    List<AbuseConfidenceScore> search(final int page, final int size, final String sortBy, SearchAbuseConfidenceScoreQuery query);
}

@Slf4j
@RequiredArgsConstructor
class AbuseConfidenceScoreRepositoryCustomImpl implements AbuseConfidenceScoreRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public List<AbuseConfidenceScore> search(final int page, final int size, final String sortBy, final SearchAbuseConfidenceScoreQuery query) {
        var cb = entityManager.getCriteriaBuilder();
        var cq = cb.createQuery(AbuseConfidenceScore.class);

        var root = cq.from(AbuseConfidenceScore.class);
        var predicates = new ArrayList<Predicate>();

        if (StringUtils.isNotEmpty(query.getIpAddress())) {
            predicates.add(cb.equal(root.get("ipAddress"), query.getIpAddress()));
        }
        if (StringUtils.isNotEmpty(query.getCountryCode())) {
            predicates.add(cb.equal(root.get("countryCode"), query.getCountryCode()));
        }
        if (StringUtils.isNotEmpty(query.getCountry())) {
            predicates.add(cb.equal(root.get("country"), query.getCountry()));
        }
        if (StringUtils.isNotEmpty(query.getCity())) {
            predicates.add(cb.equal(root.get("city"), query.getCity()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        if (StringUtils.isNotEmpty(sortBy)) {
            var orders = SortHelper.prepareSortBy(sortBy);
            orders.stream()
                  .forEach(order ->
                               cq.orderBy(order.isDescending()
                                              ? cb.desc(root.get(order.getProperty()))
                                              : cb.asc(root.get(order.getProperty()))));
        }

        return entityManager.createQuery(cq)
                            .setFirstResult(page * size)
                            .setMaxResults(size).getResultList();
    }
}
