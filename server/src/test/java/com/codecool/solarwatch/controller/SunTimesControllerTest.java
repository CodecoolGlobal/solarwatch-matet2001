package com.codecool.solarwatch.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codecool.solarwatch.model.SunTimes;
import com.codecool.solarwatch.repository.SunTimesRepository;
import com.codecool.solarwatch.service.SunTimesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.hamcrest.Matchers.is;
import jakarta.transaction.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class SunTimesControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SunTimesService sunTimesService;

    @Autowired
    private SunTimesRepository sunTimesRepository;

    @Transactional
    @BeforeEach
    void setUp() {
        // Clear any existing data and insert test data
        sunTimesService.deleteAllSunTimes();

        SunTimes sunTimes1 = new SunTimes("06:00", "18:00", LocalDate.of(2025, 1, 1));
        SunTimes createdSunTimes1 = sunTimesService.createSunTimes(sunTimes1);
        System.out.println("Created SunTimes 1: " + createdSunTimes1);

        SunTimes sunTimes2 = new SunTimes("06:10", "18:10", LocalDate.of(2025, 1, 2));
        SunTimes createdSunTimes2 = sunTimesService.createSunTimes(sunTimes2);
        System.out.println("Created SunTimes 2: " + createdSunTimes2);

        assertEquals(2, sunTimesRepository.count());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnAllSunTimes() throws Exception {
        mockMvc.perform(get("/api/sun-times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].sunrise", is("06:00")))
                .andExpect(jsonPath("$[1].sunrise", is("06:10")));
    }

//    @Test
//    @WithMockUser(username = "user", roles = {"USER"})
//    void shouldReturnSunTimesById() throws Exception {
//        mockMvc.perform(get("/api/sun-times/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.sunrise", is("06:00")))
//                .andExpect(jsonPath("$.id", is(1)));
//    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturn404ForSunTimesByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/sun-times/999")) // Non-existing ID
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnSunTimesByDate() throws Exception {
        mockMvc.perform(get("/api/sun-times/date/2025-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sunrise", is("06:00")))
                .andExpect(jsonPath("$[0].date", is("2025-01-01")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnEmptyListForSunTimesByDateNotFound() throws Exception {
        mockMvc.perform(get("/api/sun-times/date/2025-12-31")) // Non-existing date
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateSunTimes() throws Exception {
        SunTimes newSunTimes = new SunTimes("06:20", "18:20", LocalDate.of(2025, 1, 3));

        mockMvc.perform(post("/api/sun-times/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSunTimes)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sunrise", is("06:20")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateSunTimes() throws Exception {
        SunTimes updatedSunTimes = new SunTimes("06:30", "18:30", LocalDate.of(2025, 1, 1));

        mockMvc.perform(put("/api/sun-times/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSunTimes)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sunrise", is("06:30")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturn404ForSunTimesToUpdateNotFound() throws Exception {
        SunTimes updatedSunTimes = new SunTimes("06:30", "18:30", LocalDate.of(2025, 1, 1));

        mockMvc.perform(put("/api/sun-times/admin/999") // Non-existing ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSunTimes)))
                .andExpect(status().isNotFound());
    }

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    void shouldDeleteSunTimes() throws Exception {
//        mockMvc.perform(delete("/api/sun-times/admin/1"))
//                .andExpect(status().isOk());
//    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturn404ForSunTimesToDeleteNotFound() throws Exception {
        mockMvc.perform(delete("/api/sun-times/admin/999")) // Non-existing ID
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnForbiddenForAdminEndpoints() throws Exception {
        // Trying to access an admin-only endpoint with a "USER" role
        mockMvc.perform(post("/api/sun-times/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SunTimes("06:40", "18:40", LocalDate.of(2025, 1, 4)))))
                .andExpect(status().isForbidden());
    }
}