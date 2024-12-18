package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.SunTimes;
import com.codecool.solarwatch.service.SunTimesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sun-times")
public class SunTimesController {

    private final SunTimesService sunTimesService;

    @Autowired
    public SunTimesController(SunTimesService sunTimesService) {
        this.sunTimesService = sunTimesService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<SunTimes> getAllSunTimes() {
        return sunTimesService.getAllSunTimes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public SunTimes getSunTimesById(@PathVariable long id) {
        return sunTimesService.getSunTimesById(id)
                .orElseThrow(() -> new EntityNotFoundException("SunTimes not found with id: " + id));
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('USER')")
    public List<SunTimes> getSunTimesByDate(@PathVariable LocalDate date) {
        return sunTimesService.getSunTimesByDate(date);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public SunTimes createSunTimes(@RequestBody SunTimes sunTimes) {
        return sunTimesService.createSunTimes(sunTimes);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public SunTimes updateSunTimes(@PathVariable long id, @RequestBody SunTimes sunTimesDetails) {
        return sunTimesService.updateSunTimes(id, sunTimesDetails);
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSunTimes(@PathVariable long id) {
        sunTimesService.deleteSunTimes(id);
    }
}

