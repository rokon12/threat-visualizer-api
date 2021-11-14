package ca.bazlur.visualizer;

import ca.bazlur.visualizer.domain.Role;
import ca.bazlur.visualizer.repo.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class ThreatVisualizedApplication implements CommandLineRunner {

    public ThreatVisualizedApplication(final RoleRepository repository, final Environment environment) {
        this.repository = repository;
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(ThreatVisualizedApplication.class, args);
    }

    private final RoleRepository repository;
    private final Environment environment;

    @Override
    public void run(final String... args) throws Exception {
        log.info("Application started with profile:{}", Arrays.toString(environment.getActiveProfiles()));

        if (repository.count() <= 0) {
            var role = Role.builder()
                           .authority(Role.ROLE_USER)
                           .build();
            repository.saveAndFlush(role);
        }
    }
}
