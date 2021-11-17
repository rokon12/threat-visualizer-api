package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.Role;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.dto.UserView;
import ca.bazlur.visualizer.domain.mapper.DataMapper;
import ca.bazlur.visualizer.repo.RoleRepository;
import ca.bazlur.visualizer.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataMapper mapper;
    private final RoleRepository roleRepository;

    @Transactional
    public UserView create(CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ValidationException("Username exists!");
        }

        if (!request.getPassword().equals(request.getRePassword())) {
            throw new ValidationException("Passwords don't match!");
        }

        var user = mapper.toUser(request);
        user.setAuthorities(getDefaultRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return mapper.toUserView(userRepository.save(user));
    }

    private Set<Role> getDefaultRole() {
        return roleRepository.findByAuthority(Role.ROLE_USER)
                             .stream()
                             .collect(Collectors.toSet());
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(format("User with username - %s, not found", username)));
    }
}
