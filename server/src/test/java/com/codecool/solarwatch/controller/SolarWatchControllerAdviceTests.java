package com.codecool.solarwatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class SolarWatchControllerAdviceTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldHandleMissingServletRequestParameterException() throws Exception {
        mockMvc.perform(get("/api/solar-watch")) // Assuming this endpoint requires a parameter like `city`
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Please provide a city name"))
                .andExpect(jsonPath("$.details").value("cityName")); // Adjust parameter name
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldHandleRuntimeException() throws Exception {
        mockMvc.perform(get("/api/throw-runtime")) // Assuming this endpoint throws a RuntimeException
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Some error occurred on the server"));
    }
}
