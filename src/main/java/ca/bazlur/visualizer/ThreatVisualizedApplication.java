package ca.bazlur.visualizer;

import ca.bazlur.visualizer.domain.Role;
import ca.bazlur.visualizer.repo.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ThreatVisualizedApplication implements CommandLineRunner {

    public ThreatVisualizedApplication(final RoleRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ThreatVisualizedApplication.class, args);
    }

    private final RoleRepository repository;

    @Override
    public void run(final String... args) throws Exception {
        if (repository.count() <= 0) {
            var role = Role.builder()
                           .authority(Role.ROLE_USER)
                           .build();
            repository.saveAndFlush(role);
        }
    }
}
