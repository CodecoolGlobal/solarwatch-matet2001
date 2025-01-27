package com.codecool.solarwatch.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JWTAuthenticationFilterTest {

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_validJWT_setsAuthentication() throws Exception {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";

        // Mock JWTUtils behavior
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUsernameFromJwtToken(token)).thenReturn(username);

        // Mock UserDetailsServiceImpl behavior
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username, "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Add a valid JWT token to the request header
        request.addHeader("Authorization", "Bearer " + token);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(username, authentication.getName());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidJWT_doesNotSetAuthentication() throws Exception {
        // Arrange
        String token = "invalid.jwt.token";

        // Mock JWTUtils behavior
        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        // Add an invalid JWT token to the request header
        request.addHeader("Authorization", "Bearer " + token);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication); // Ensure no authentication is set
        verify(filterChain).doFilter(request, response);
    }


    @Test
    void doFilterInternal_noJWT_doesNotSetAuthentication() throws Exception {
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotFilter_skipsLoginAndRegisterEndpoints() throws ServletException {
        // Arrange
        MockHttpServletRequest loginRequest = new MockHttpServletRequest();
        loginRequest.setRequestURI("/api/login");

        MockHttpServletRequest registerRequest = new MockHttpServletRequest();
        registerRequest.setRequestURI("/api/register");

        MockHttpServletRequest otherRequest = new MockHttpServletRequest();
        otherRequest.setRequestURI("/api/other");

        // Act & Assert
        assertTrue(jwtAuthenticationFilter.shouldNotFilter(loginRequest));
        assertTrue(jwtAuthenticationFilter.shouldNotFilter(registerRequest));
        assertFalse(jwtAuthenticationFilter.shouldNotFilter(otherRequest));
    }
}
