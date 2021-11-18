package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.dto.AuthRequest;
import ca.bazlur.visualizer.domain.dto.AuthTokenView;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.dto.UserView;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserView create(CreateUserRequest request);

    AuthTokenView login(AuthRequest request);
}
