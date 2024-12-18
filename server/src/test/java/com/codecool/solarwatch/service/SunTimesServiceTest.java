package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.SunTimes;
import com.codecool.solarwatch.repository.SunTimesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SunTimesServiceTest {

    @Mock
    private SunTimesRepository sunTimesRepository;

    @InjectMocks
    private SunTimesService sunTimesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSunTimes_returnsAllSunTimes() {
        List<SunTimes> mockSunTimes = List.of(
                new SunTimes("5:10:21 AM", "3:01:47 PM", LocalDate.of(2024, 11, 21)),
                new SunTimes("5:34:47 AM", "3:17:49 PM", LocalDate.of(2024, 11, 7))
        );
        when(sunTimesRepository.findAll()).thenReturn(mockSunTimes);

        List<SunTimes> result = sunTimesService.getAllSunTimes();

        assertNotNull(result);
        assertEquals(mockSunTimes.size(), result.size());
        verify(sunTimesRepository, times(1)).findAll();
    }

    @Test
    void getSunTimesById_returnsSunTimesById() {
        SunTimes mockSunTimes = new SunTimes("5:10:21 AM", "3:01:47 PM", LocalDate.of(2024, 11, 21));

        when(sunTimesRepository.findById(1L)).thenReturn(Optional.of(mockSunTimes));

        assertTrue(sunTimesService.getSunTimesById(1L).isPresent());
        assertEquals(mockSunTimes, sunTimesService.getSunTimesById(1L).get());
        verify(sunTimesRepository, times(2)).findById(1L);
    }

    @Test
    void getSunTimesByDate_returnsSunTimesByDate() {
        LocalDate mockDate = LocalDate.of(2024, 11, 21);
        SunTimes mockSunTimes = new SunTimes("5:10:21 AM", "3:01:47 PM", mockDate);

        when(sunTimesRepository.findByDate(mockDate)).thenReturn(List.of(mockSunTimes));

        List<SunTimes> result = sunTimesService.getSunTimesByDate(mockDate);

        assertNotNull(result);
        assertEquals(mockSunTimes.getDate(), result.get(0).getDate());
        verify(sunTimesRepository, times(1)).findByDate(mockDate);

    }

    @Test
    void createSunTimes_savesCity() {
        SunTimes mockSunTimes = new SunTimes("5:10:21 AM", "3:01:47 PM", LocalDate.of(2024, 11, 21));
        when(sunTimesRepository.save(mockSunTimes)).thenReturn(mockSunTimes);

        SunTimes result = sunTimesService.createSunTimes(mockSunTimes);

        assertNotNull(result);
        assertEquals(mockSunTimes.getDate(), result.getDate());
        verify(sunTimesRepository, times(1)).save(mockSunTimes);
    }

    @Test
    void updateSunTimes_updatesAndReturnsSunTimes() {
        SunTimes existingSunTimes = new SunTimes("5:10:21 AM", "3:01:47 PM", LocalDate.of(2024, 11, 21));
        SunTimes updatedSunTimes = new SunTimes("5:10:00 AM", "3:01:00 PM", LocalDate.of(2024, 10, 21));

        when(sunTimesRepository.findById(1L)).thenReturn(Optional.of(existingSunTimes));
        when(sunTimesRepository.save(updatedSunTimes)).thenReturn(updatedSunTimes);

        SunTimes updateResult = sunTimesService.updateSunTimes(1L, updatedSunTimes);

        assertNotNull(updateResult);
        assertEquals(updatedSunTimes.getDate(), updateResult.getDate());
        verify(sunTimesRepository, times(1)).findById(1L);
        verify(sunTimesRepository, times(1)).save(updatedSunTimes);
    }

    @Test
    void updateSunTimes_throwsExceptionWhenCityNotFound() {
        SunTimes updatedSunTimes = new SunTimes("5:10:00 AM", "3:01:00 PM", LocalDate.of(2024, 10, 21));
        when(sunTimesRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> sunTimesService.updateSunTimes(1L, updatedSunTimes));

        assertEquals("SunTimes not found with id: 1", exception.getMessage());
        verify(sunTimesRepository, times(1)).findById(1L);
        verify(sunTimesRepository, never()).save(any(SunTimes.class));
    }

    @Test
    void deleteSunTimes_deletesSunTimes() {
        when(sunTimesRepository.existsById(1L)).thenReturn(true);

        sunTimesService.deleteSunTimes(1L);

        verify(sunTimesRepository, times(1)).existsById(1L);
        verify(sunTimesRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSunTimes_throwsExceptionWhenCityNotFound() {
        when(sunTimesRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> sunTimesService.deleteSunTimes(1L));

        assertEquals("SunTimes not found with id: 1", exception.getMessage());
        verify(sunTimesRepository, times(1)).existsById(1L);
        verify(sunTimesRepository, times(0)).deleteById(1L);
    }
}