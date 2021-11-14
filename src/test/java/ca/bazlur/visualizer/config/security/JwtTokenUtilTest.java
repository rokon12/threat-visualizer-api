package ca.bazlur.visualizer.config.security;

import ca.bazlur.visualizer.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private User user;
    private final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJudWxsLGJhemx1ckBqdWdiZC5vcmciLCJpc3MiOiJiYXpsdXIuY2EiLCJpYXQiOjE2MzY4ODMxODIsImV4cCI6MTYzNzQ4Nzk4Mn0.l_yQeDS9rYYg5z1Fg5pMSn7I8lFCg6r2HAfeeH4GhyOoOMtOyTfV9pV48zs9K2Jnh7_s9agqrA2mLdTi3wCJAw";

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtSecret", "std123");
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtIssuer", "bazlur.ca");

        user = User.builder()
                   .id(101)
                   .fullName("Bazlur Rahman")
                   .username("bazlur@jugbd.org")
                   .enabled(true)
                   .password("$2a$10$fkEvLyKmmcV/8WbuJqthWOZPwseCoQ6vPS.4R63uqgBzn0f1SdlV")
                   .createdAt(LocalDateTime.now())
                   .build();
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
    void testValidate_invalidTokenThrowsException() {
        assertFalse(jwtTokenUtil.validate(TOKEN+ "XYZ"));
    }
}
