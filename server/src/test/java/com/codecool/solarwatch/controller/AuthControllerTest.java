package com.codecool.solarwatch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.codecool.solarwatch.model.Role;
import com.codecool.solarwatch.model.SolarUser;
import com.codecool.solarwatch.repository.RoleRepository;
import com.codecool.solarwatch.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    String credentials = """
            {
                "email": "matet2001@gmail.com",
                "username": "testuser",
                "password": "password123"
            }
            """;

    @BeforeAll
    void setUpOnce() {
        roleRepository.save(new Role("USER"));
    }

    @Test
    void register_returns200() throws Exception {

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registration success"));

        assertTrue(userRepository.findByUsername("testuser").isPresent());
    }

    @Test
    void login_returns_jwtToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").exists())
                .andExpect(jsonPath("$.message").value("User login successfully"));
    }

    @Test
    void register_fails_whenUsernameIsTaken() throws Exception {
        // Arrange: Save a user with the same username
        userRepository.save(new SolarUser("testuser", "password123"));

        // Act & Assert: Try to register with the same username and expect a BAD_REQUEST response
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is taken!"));
    }
}
