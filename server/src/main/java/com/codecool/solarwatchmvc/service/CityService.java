package com.codecool.solarwatchmvc.service;

import com.codecool.solarwatchmvc.model.City;
import com.codecool.solarwatchmvc.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public Optional<City> getCityById(long id) {
        return cityRepository.findById(id);
    }

    public Optional<City> getCityByName(String name) {
        return cityRepository.findByName(name);
    }

    public City createCity(City city) {
        return cityRepository.save(city);
    }

    public City updateCity(long id, City cityDetails) {
        return cityRepository.findById(id).map(city -> {
            city.setName(cityDetails.getName());
            city.setLat(cityDetails.getLat());
            city.setLon(cityDetails.getLon());
            city.setCountry(cityDetails.getCountry());
            city.setSunTimes(cityDetails.getSunTimes());
            return cityRepository.save(city);
        }).orElseThrow(() -> new EntityNotFoundException("City not found with id: " + id));
    }

    public void deleteCity(long id) {
        if (cityRepository.existsById(id)) {
            cityRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("City not found with id: " + id);
        }
    }
}

