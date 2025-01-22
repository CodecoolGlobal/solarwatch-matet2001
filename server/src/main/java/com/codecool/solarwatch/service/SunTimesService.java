package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.model.SunTimes;
import com.codecool.solarwatch.repository.SunTimesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SunTimesService {

    private final SunTimesRepository sunTimesRepository;

    @Autowired
    public SunTimesService(SunTimesRepository sunTimesRepository) {
        this.sunTimesRepository = sunTimesRepository;
    }

    public List<SunTimes> getAllSunTimes() {
        return sunTimesRepository.findAll();
    }

    public Optional<SunTimes> getSunTimesById(long id) {
        return sunTimesRepository.findById(id);
    }

    public List<SunTimes> getSunTimesByDate(LocalDate date) {
        return sunTimesRepository.findByDate(date);
    }

    public SunTimes createSunTimes(SunTimes sunTimes) {
        return sunTimesRepository.save(sunTimes);
    }

    public SunTimes updateSunTimes(long id, SunTimes sunTimesDetails) {
        return sunTimesRepository.findById(id).map(sunTimes -> {
            sunTimes.setSunrise(sunTimesDetails.getSunrise());
            sunTimes.setSunset(sunTimesDetails.getSunset());
            sunTimes.setDate(sunTimesDetails.getDate());
            return sunTimesRepository.save(sunTimes);
        }).orElseThrow(() -> new EntityNotFoundException("SunTimes not found with id: " + id));
    }
    public void deleteSunTimes(long id) {
        if(sunTimesRepository.existsById(id)) {
            sunTimesRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("SunTimes not found with id: " + id);
        }
    }

    public void deleteAllSunTimes() {
        sunTimesRepository.deleteAllInBatch();
    }
}

