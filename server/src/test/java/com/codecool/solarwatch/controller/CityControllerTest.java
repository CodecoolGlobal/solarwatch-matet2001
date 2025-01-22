package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.service.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class CityControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    @BeforeEach
    void setUp() {
        // Clear any existing data and insert test data
        cityService.deleteAllCities();

        City city1 = new City();
        city1.setName("City1");
        cityService.createCity(city1);

        City city2 = new City();
        city2.setName("City2");
        cityService.createCity(city2);

        assertEquals(2, cityRepository.count());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnAllCities() throws Exception {
        mockMvc.perform(get("/api/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // Expect two cities
                .andExpect(jsonPath("$[0].name", is("City1")))
                .andExpect(jsonPath("$[1].name", is("City2")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnCityById() throws Exception {
        mockMvc.perform(get("/api/city/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("City1")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturn404ForCityByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/city/999"))  // Non-existing ID
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnCityByName() throws Exception {
        mockMvc.perform(get("/api/city/name/City1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("City1")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturn404ForCityByNameNotFound() throws Exception {
        mockMvc.perform(get("/api/city/name/NonExistingCity"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateCity() throws Exception {
        City newCity = new City();
        newCity.setName("NewCity");

        mockMvc.perform(post("/api/city/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCity)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("NewCity")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateCity() throws Exception {
        City updatedCity = new City();
        updatedCity.setName("UpdatedCity");

        mockMvc.perform(put("/api/city/admin/1")  // Assuming ID 1 exists
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdatedCity")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturn404ForCityToUpdateNotFound() throws Exception {
        City updatedCity = new City();
        updatedCity.setName("UpdatedCity");

        mockMvc.perform(put("/api/city/admin/999")  // Non-existing ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCity)))
                .andExpect(status().isNotFound());
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCity(@PathVariable long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();  // Explicitly return 204 No Content
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturn404ForCityToDeleteNotFound() throws Exception {
        mockMvc.perform(delete("/api/city/admin/999"))  // Non-existing ID
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnForbiddenForAdminEndpoints() throws Exception {
        // Trying to access an admin-only endpoint with a "USER" role
        mockMvc.perform(post("/api/city/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new City())))
                .andExpect(status().isForbidden());
    }
}