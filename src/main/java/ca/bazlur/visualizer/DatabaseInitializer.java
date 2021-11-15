package ca.bazlur.visualizer;

import ca.bazlur.visualizer.domain.Role;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.repo.RoleRepository;
import ca.bazlur.visualizer.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.info("onApplicationEvent() - adding initial data to the the databases");
        if (roleRepository.count() <= 0) {
            var role = Role.builder()
                           .authority(Role.ROLE_USER)
                           .build();
            roleRepository.saveAndFlush(role);
        }
        var username = "bazlur@bazlur.ca";
        try {
            userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            var roles = Set.of(Role.ROLE_USER);
            userService.create(CreateUserRequest.builder()
                                                .username(username)
                                                .fullName(("Bazlur Rahman"))
                                                .password("Test123")
                                                .rePassword("Test123")
                                                .authorities(roles)
                                                .build());
        }
    }
}
