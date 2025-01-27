package com.codecool.solarwatch.security;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class JwtUtilsTests {

    private JWTUtils jwtUtils;

    @Autowired
    JwtUtilsTests(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Test
    void shouldGetUsernameFromJwtToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .signWith(jwtUtils.key())
                .compact();

        String username = jwtUtils.getUsernameFromJwtToken(token);
        assertEquals("testUser", username);
    }

    @Test
    void shouldValidateJwtToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .signWith(jwtUtils.key())
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);
        assertTrue(isValid);
    }

    @Test
    void shouldReturnFalseForInvalidJwtToken() {
        String invalidToken = "invalid.token.value";

        boolean isValid = jwtUtils.validateJwtToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseForExpiredJwtToken() {
        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setExpiration(Date.from(Instant.now().minusSeconds(60))) // Token expired 60 seconds ago
                .signWith(jwtUtils.key())
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(expiredToken);
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseForEmptyJwtToken() {
        String emptyToken = "";

        boolean isValid = jwtUtils.validateJwtToken(emptyToken);
        assertFalse(isValid);
    }
}
