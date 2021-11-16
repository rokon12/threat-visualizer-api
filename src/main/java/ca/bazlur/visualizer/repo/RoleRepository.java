package ca.bazlur.visualizer.repo;

import ca.bazlur.visualizer.domain.Role;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "roles")
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Cacheable
    Optional<Role> findByAuthority(String authority);
}
