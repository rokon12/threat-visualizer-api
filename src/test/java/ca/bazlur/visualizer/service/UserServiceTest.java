package ca.bazlur.visualizer.service;

import ca.bazlur.visualizer.domain.Role;
import ca.bazlur.visualizer.domain.User;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.mapper.DataMapper;
import ca.bazlur.visualizer.domain.mapper.DataMapperImpl;
import ca.bazlur.visualizer.repo.RoleRepository;
import ca.bazlur.visualizer.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ValidationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        DataMapper dataMapper = new DataMapperImpl();
        userService = new UserService(userRepository, passwordEncoder, dataMapper, roleRepository);
    }

    @Test
    void create_givenUsernameExist_shouldThrowException() {
        when(userRepository.findByUsername(anyString()))
            .thenReturn(Optional.of(User.builder().build()));
        assertThrows(ValidationException.class,
            () -> userService.create(CreateUserRequest.builder()
                                                      .username("bazlur@bazlur.com")
                                                      .build()));
    }

    @Test
    void create_givenPasswordsDoNotMatch_shouldThrowException() {
        when(userRepository.findByUsername(anyString()))
            .thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
            () -> userService.create(CreateUserRequest.builder()
                                                      .username("bazlur@bazlur.com")
                                                      .password("Test1234")
                                                      .rePassword("Test123")
                                                      .build()));
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
                                            .username("bazlur@bazlur.com")
                                            .password("Test1234")
                                            .rePassword("Test1234")
                                            .build());
        verify(userRepository).save(userArgumentCaptor.capture());
        var user = userArgumentCaptor.getValue();
        assertEquals("bazlur@bazlur.com", user.getUsername());
        assertNotEquals("Test1234", user.getPassword());
    }

    @Test
    void testLoadUserByUsernameFail() {
        when(userRepository.findByUsername(anyString()))
            .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
            () -> userService.loadUserByUsername("bazlur@bazlur.com"));
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        when(userRepository.findByUsername(anyString()))
            .thenReturn(Optional.of(User.builder()
                                        .username("bazlur@bazlur.com")
                                        .build()));

        var userDetails = userService.loadUserByUsername("bazlur@bazlur.com");
        assertEquals("bazlur@bazlur.com", userDetails.getUsername());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
    }
}
