package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.service.CityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public City getCityById(@PathVariable long id) {
        return cityService.getCityById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found with id: " + id));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('USER')")
    public City getCityByName(@PathVariable String name) {
        return cityService.getCityByName(name)
                .orElseThrow(() -> new EntityNotFoundException("City not found with name: " + name));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<City> createCity(@RequestBody City city) {
        City createdCity = cityService.createCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCity);
    }


    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public City updateCity(@PathVariable long id, @RequestBody City cityDetails) {
        return cityService.updateCity(id, cityDetails);
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCity(@PathVariable long id) {
        cityService.deleteCity(id);
    }
}