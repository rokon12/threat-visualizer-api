package ca.bazlur.visualizer.config;

import ca.bazlur.visualizer.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class DatabaseConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            return Optional.ofNullable(authentication)
                           .filter(auth -> (authentication instanceof User))
                           .map(auth -> (User) auth.getPrincipal())
                           .map(User::getUsername)
                           .or(() -> Optional.of("System"));
        };
    }
}
