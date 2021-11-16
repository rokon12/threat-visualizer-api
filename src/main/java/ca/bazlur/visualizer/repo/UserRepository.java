package ca.bazlur.visualizer.repo;

import ca.bazlur.visualizer.domain.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable
    Optional<User> findByUsername(String username);

    @Caching(evict = {
        @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
        @CacheEvict(key = "#p0.username", condition = "#p0.username != null")
    })
    <S extends User> S save(S entity);
}
