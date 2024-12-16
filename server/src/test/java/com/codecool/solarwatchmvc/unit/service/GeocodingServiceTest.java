package com.codecool.solarwatchmvc.unit.service;

import com.codecool.solarwatchmvc.model.City;
import com.codecool.solarwatchmvc.repository.CityRepository;
import com.codecool.solarwatchmvc.service.GeocodingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeocodingServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private GeocodingService geocodingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchCityByName_returnsCity() {
        // Arrange
        City mockCity = new City("Gödöllő", 47.4979f, 19.0402f, "HU");
        City[] mockResponse = {mockCity};
        String cityName = "Budapest";

        String url = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
                cityName, "c828fd578e8452122384274a198de8c3");

        // Simulate city not found in the database
        when(cityRepository.findByName(cityName)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(url, City[].class)).thenReturn(mockResponse);

        // Act
        City result = geocodingService.getCityByName(cityName);

        // Assert
        assertNotNull(result);
        assertEquals("Gödöllő", result.getName());
        assertEquals("HU", result.getCountry());
        assertEquals(47.4979f, result.getLat());
        assertEquals(19.0402f, result.getLon());

        // Verify interactions
        verify(cityRepository, times(1)).findByName(cityName);
        verify(restTemplate, times(1)).getForObject(url, City[].class);
    }

    @Test
    void fetchCityByName_throwsExceptionWhenNoCityFound() {
        // Arrange
        String cityName = "InvalidCity";
        String url = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
                cityName, "c828fd578e8452122384274a198de8c3");

        when(cityRepository.findByName(cityName)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(url, City[].class)).thenReturn(new City[]{});

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> geocodingService.getCityByName(cityName));

        assertEquals("No city location found for: InvalidCity", exception.getMessage());

        // Verify interactions
        verify(cityRepository, times(1)).findByName(cityName);
        verify(restTemplate, times(1)).getForObject(url, City[].class);
    }
}
