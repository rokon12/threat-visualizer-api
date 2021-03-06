package ca.bazlur.visualizer.config.security;

import ca.bazlur.visualizer.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private User user;
    private String TOKEN;
    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtSecret", "std123");
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtIssuer", "bazlur.ca");
        ReflectionTestUtils.setField(jwtTokenUtil, "expiryDuration", 300000);

        user = User.builder()
                   .id(101)
                   .fullName("Bazlur Rahman")
                   .username("bazlur@jugbd.org")
                   .enabled(true)
                   .password("$2a$10$fkEvLyKmmcV/8WbuJqthWOZPwseCoQ6vPS.4R63uqgBzn0f1SdlV")
                   .createdAt(LocalDateTime.now())
                   .build();

        TOKEN = jwtTokenUtil.generateAccessToken(user);
    }

    @Test
    void testGenerateAccessToken() {
        var token = jwtTokenUtil.generateAccessToken(user);
        assertNotNull(token);
    }

    @Test
    void testGetUsername() {
        var username = jwtTokenUtil.getUsername(TOKEN);
        assertNotNull(username);
        assertEquals("bazlur@jugbd.org", username);
    }

    @Test
    void testValidate() {
        assertTrue(jwtTokenUtil.validate(TOKEN));
    }

    @Test
    void testValidate_invalidToken_shouldReturnFalse() {
        assertFalse(jwtTokenUtil.validate(TOKEN + "XYZ"));
    }

    @Test
    void testValidate_ExpiredToken_shouldReturnFalse() throws InterruptedException {
        ReflectionTestUtils.setField(jwtTokenUtil, "expiryDuration", 1);
        var token = jwtTokenUtil.generateAccessToken(user);
        TimeUnit.SECONDS.sleep(2);
        assertFalse(jwtTokenUtil.validate(token));
    }
}
