package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.Role;
import ca.bazlur.visualizer.domain.User;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.dto.UserView;
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

    @Transactional
    public UserView create(CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ValidationException("Username exists!");
        }

        if (!request.getPassword().equals(request.getRePassword())) {
            throw new ValidationException("Passwords don't match!");
        }

        if (request.getAuthorities() == null) {
            request.setAuthorities(Set.of(Role.ROLE_USER));
        }

        var user = User.builder()
                       .username(request.getUsername())
                       .fullName(request.getFullName())
                       .authorities(request.getAuthorities().stream().map(name -> Role.builder().authority(name).build()).collect(Collectors.toSet()))
                       .build();

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var savedUser = userRepository.save(user);
        return UserView.builder()
                       .id(savedUser.getId())
                       .username(savedUser.getUsername())
                       .fullName(savedUser.getFullName())
                       .build();
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(format("User with username - %s, not found", username)));
    }
}
