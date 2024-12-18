package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCities_returnsAllCities() {
        List<City> mockCities = Arrays.asList(
                new City("City1", 1.0f, 1.0f, "Country1"),
                new City("City2", 2.0f, 2.0f, "Country2")
        );
        when(cityRepository.findAll()).thenReturn(mockCities);

        List<City> result = cityService.getAllCities();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void getCityById_returnsCity() {
        City mockCity = new City("City1", 1.0f, 1.0f, "Country1");
        when(cityRepository.findById(1L)).thenReturn(Optional.of(mockCity));

        Optional<City> result = cityService.getCityById(1L);

        assertTrue(result.isPresent());
        assertEquals("City1", result.get().getName());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void getCityByName_returnsCity() {
        City mockCity = new City("City1", 1.0f, 1.0f, "Country1");
        when(cityRepository.findByName("City1")).thenReturn(Optional.of(mockCity));

        Optional<City> result = cityService.getCityByName("City1");

        assertTrue(result.isPresent());
        assertEquals("City1", result.get().getName());
        verify(cityRepository, times(1)).findByName("City1");
    }

    @Test
    void getCityById_throwsExceptionWhenCityNotFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<City> result = cityService.getCityById(1L);

        assertFalse(result.isPresent());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void createCity_savesCity() {
        City mockCity = new City("City1", 1.0f, 1.0f, "Country1");
        when(cityRepository.save(mockCity)).thenReturn(mockCity);

        City result = cityService.createCity(mockCity);

        assertNotNull(result);
        assertEquals("City1", result.getName());
        verify(cityRepository, times(1)).save(mockCity);
    }

    @Test
    void updateCity_updatesAndReturnsCity() {
        City existingCity = new City("City1", 1.0f, 1.0f, "Country1");
        City updatedCity = new City("UpdatedCity", 2.0f, 2.0f, "UpdatedCountry");

        when(cityRepository.findById(1L)).thenReturn(Optional.of(existingCity));
        when(cityRepository.save(any(City.class))).thenReturn(updatedCity);

        City result = cityService.updateCity(1L, updatedCity);

        assertNotNull(result);
        assertEquals("UpdatedCity", result.getName());
        verify(cityRepository, times(1)).findById(1L);
        verify(cityRepository, times(1)).save(existingCity);
    }

    @Test
    void updateCity_throwsExceptionWhenCityNotFound() {
        City updatedCity = new City("UpdatedCity", 2.0f, 2.0f, "UpdatedCountry");
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> cityService.updateCity(1L, updatedCity));

        assertEquals("City not found with id: 1", exception.getMessage());
        verify(cityRepository, times(1)).findById(1L);
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void deleteCity_deletesCity() {
        when(cityRepository.existsById(1L)).thenReturn(true);

        cityService.deleteCity(1L);

        verify(cityRepository, times(1)).existsById(1L);
        verify(cityRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCity_throwsExceptionWhenCityNotFound() {
        when(cityRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> cityService.deleteCity(1L));

        assertEquals("City not found with id: 1", exception.getMessage());
        verify(cityRepository, times(1)).existsById(1L);
        verify(cityRepository, never()).deleteById(1L);
    }
}
