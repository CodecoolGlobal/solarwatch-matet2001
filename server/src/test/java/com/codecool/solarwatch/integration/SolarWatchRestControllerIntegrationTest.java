package com.codecool.solarwatch.integration;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codecool.solarwatch.controller.SolarWatchController;
import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.model.SunTimes;
import com.codecool.solarwatch.service.GeocodingService;
import com.codecool.solarwatch.service.SunTimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

@WebMvcTest(SolarWatchController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@WithMockUser(username = "user", roles = {"USER"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SolarWatchRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SunTimeService sunTimeService;

    @MockBean
    private GeocodingService geocodingService;

    @Test
    void solarWatchPath_returnsValidResponse() throws Exception {
        LocalDate mockDate = LocalDate.of(2024, 11, 21);

        SunTimes mockSunTimes = new SunTimes("5:10:21 AM", "3:01:47 PM", mockDate);
        City mockCity = new City("Gödöllő", 47.601529, 19.3476981, "HU");

        when(geocodingService.getCityByName(anyString())).thenReturn(mockCity);

        when(sunTimeService.getCitySunTimesByDateConvertedToHungarianTimeZone(any(City.class), eq(mockDate)))
                .thenReturn(mockSunTimes);

        mockMvc.perform(get("/api/solar-watch")
                        .param("cityName", "Gödöllő")
                        .param("date", "2024-11-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city.name").value("Gödöllő"))
                .andExpect(jsonPath("$.city.lat").value(47.601529))
                .andExpect(jsonPath("$.city.lon").value(19.3476981))
                .andExpect(jsonPath("$.city.country").value("HU"))
                .andExpect(jsonPath("$.sunTimes.sunrise").value("5:10:21 AM"))
                .andExpect(jsonPath("$.sunTimes.sunset").value("3:01:47 PM"))
                .andExpect(jsonPath("$.sunTimes.date").value("2024-11-21"));
    }
}

