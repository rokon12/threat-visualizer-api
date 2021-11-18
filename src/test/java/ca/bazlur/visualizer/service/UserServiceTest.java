package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.config.security.JwtTokenUtil;
import ca.bazlur.visualizer.domain.Role;
import ca.bazlur.visualizer.domain.User;
import ca.bazlur.visualizer.domain.dto.AuthRequest;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.mapper.DataMapperImpl;
import ca.bazlur.visualizer.repo.RoleRepository;
import ca.bazlur.visualizer.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ValidationException;
import java.util.Optional;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private static final String USERNAME = "bazlur@bazlur.com";

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        var passwordEncoder = new BCryptPasswordEncoder();
        var dataMapper = new DataMapperImpl();
        var jwtTokenUtil = new JwtTokenUtil();

        ReflectionTestUtils.setField(jwtTokenUtil, "jwtSecret", "std123");
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtIssuer", "bazlur.ca");
        ReflectionTestUtils.setField(jwtTokenUtil, "expiryDuration", 60 * 60 * 1000);

        userService = new UserService(userRepository, passwordEncoder,
            dataMapper, roleRepository, authenticationManager, jwtTokenUtil);
    }

    @Test
    void create_givenUsernameExist_shouldThrowException() {
        when(userRepository.findByUsername(anyString()))
            .thenReturn(Optional.of(User.builder().build()));
        assertThrows(ValidationException.class, this::createUserWithExistingUserName);
    }

    private void createUserWithExistingUserName() {
        userService.create(CreateUserRequest.builder()
                                            .username(USERNAME)
                                            .build());
    }

    @Test
    void create_givenPasswordsDoNotMatch_shouldThrowException() {
        when(userRepository.findByUsername(anyString()))
            .thenReturn(Optional.empty());

        assertThrows(ValidationException.class, this::createUserWithMismatchPassword);
    }

    private void createUserWithMismatchPassword() {
        userService.create(CreateUserRequest.builder()
                                            .username(USERNAME)
                                            .password("Test1234")
                                            .rePassword("Test123")
                                            .build());
    }

    @Test
    void createSuccess() {
        when(userRepository.findByUsername(anyString()))
            .thenReturn(Optional.empty());
        when(roleRepository.findByAuthority(anyString()))
            .thenReturn(Optional.of(Role.builder()
                                        .id(1)
                                        .authority(Role.ROLE_USER)
                                        .build()));

        userService.create(CreateUserRequest.builder()
                                            .username(USERNAME)
                                            .password("Test1234")
                                            .rePassword("Test1234")
                                            .build());
        verify(userRepository).save(userArgumentCaptor.capture());
        var user = userArgumentCaptor.getValue();
        assertEquals(USERNAME, user.getUsername());
        assertNotEquals("Test1234", user.getPassword());
    }

    @Test
    void testLoadUserByUsernameFail() {
        when(userRepository.findByUsername(anyString()))
            .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
            () -> userService.loadUserByUsername(USERNAME));
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        when(userRepository.findByUsername(anyString()))
            .thenReturn(Optional.of(User.builder()
                                        .username(USERNAME)
                                        .build()));

        var userDetails = userService.loadUserByUsername(USERNAME);
        assertEquals(USERNAME, userDetails.getUsername());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testLoginSuccess() {
        var authRequest = AuthRequest.builder()
                                     .username(USERNAME)
                                     .password("Test1234")
                                     .build();
        var authentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal())
            .thenReturn(User.builder()
                            .username(authRequest.getUsername())
                            .authorities(Set.of(Role.builder().authority(Role.ROLE_USER).build()))
                            .build());

        var authTokenView = userService.login(authRequest);

        assertNotNull(authTokenView, "AuthTokenView must not null");
        assertNotNull(authTokenView.getToken(), "Token must not null");
        assertEquals(authRequest.getUsername(), authTokenView.getUsername(), "Username must match!");
    }

    @Test
    void testLoginFail() {
        var authRequest = AuthRequest.builder()
                                     .username(USERNAME)
                                     .password("Test1234")
                                     .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid Credentials"));

        assertThrows(BadCredentialsException.class,
            () -> userService.login(authRequest));
    }
}
