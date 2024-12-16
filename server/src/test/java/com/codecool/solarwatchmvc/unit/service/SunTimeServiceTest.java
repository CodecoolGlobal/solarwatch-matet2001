package com.codecool.solarwatchmvc.unit.service;

import com.codecool.solarwatchmvc.model.City;
import com.codecool.solarwatchmvc.model.SunTimeResponse;
import com.codecool.solarwatchmvc.model.SunTimes;
import com.codecool.solarwatchmvc.repository.CityRepository;
import com.codecool.solarwatchmvc.service.SunTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SunTimeServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private SunTimeService sunTimeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchCitySunTimesByDate_returnsSunTimes() {
        //Arrange
        SunTimes citySunTimes = new SunTimes("5:13:19 AM", "3:42:52 PM", LocalDate.now());
        SunTimeResponse sunTimeResponse = new SunTimeResponse(citySunTimes,"OK");
        City mockCity = new City("Gödöllő" ,47.4979f, 19.0402f, "HU");
        

        LocalDate date = LocalDate.now();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", mockCity.getLat(), mockCity.getLon(), formattedDate);

        //Mock
        when(cityRepository.save(mockCity)).thenReturn(mockCity);
        when(restTemplate.getForObject(url, SunTimeResponse.class)).thenReturn(sunTimeResponse);

        //Act
        SunTimes result = sunTimeService.fetchCitySunTimesByDate(mockCity, date);

        //Assert
        assertNotNull(result);
        assertEquals(result.getSunrise(), "5:13:19 AM");
        assertEquals(result.getSunset(), "3:42:52 PM");

        verify(restTemplate, times(1)).getForObject(url, SunTimeResponse.class);
    }

    @Test
    void fetchCitySunTimesByDate_throwsExceptionWhenInvalidDateIsGiven() {
        String invalidDate = "asd";  // A string that's not parsable as a date

        // Use LocalDate.parse() or other logic to simulate incorrect date handling
        DateTimeException exception = assertThrows(DateTimeException.class, () -> {
            // Trying to parse an invalid date string (hypothetical if you changed logic to accept string dates)
            LocalDate.parse(invalidDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        });

        assertEquals("Text 'asd' could not be parsed at index 0", exception.getMessage());
    }

    @Test
    void fetchCitySunTimesByDate_throwsExceptionWhenCityNotFound() {
        City mockCity = new City("Gödöllő" ,47.4979f, 19.0402f, "HU");
        LocalDate date = LocalDate.now();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", mockCity.getLat(), mockCity.getLon(), formattedDate);

        when(restTemplate.getForObject(url, SunTimeResponse.class)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sunTimeService.fetchCitySunTimesByDate(mockCity, LocalDate.now()));

        assertEquals("No sun times found for: " + mockCity + ", " + formattedDate, exception.getMessage());

        verify(restTemplate, times(1)).getForObject(url, SunTimeResponse.class);
    }
}