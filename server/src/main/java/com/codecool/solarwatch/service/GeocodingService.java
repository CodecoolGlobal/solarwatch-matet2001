package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GeocodingService {
    private static final String API_KEY = "c828fd578e8452122384274a198de8c3";
    private static final Logger logger = LoggerFactory.getLogger(GeocodingService.class);

    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;

    @Autowired
    public GeocodingService(RestTemplate restTemplate, CityRepository cityRepository) {
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
    }

    public City getCityByName(String cityName) {
        Optional<City> cityFromDatabase = cityRepository.findByName(cityName);
        return cityFromDatabase.orElseGet(() -> fetchCityByName(cityName));
    }

    private City fetchCityByName(String cityName) {
        String url = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", cityName, API_KEY);

        City[] cities = restTemplate.getForObject(url, City[].class);
        logger.info("Response from Open Weather API for {}: {}", cityName, cities);

        if (cities != null && cities.length > 0) {
            return cities[0];
        } else {
            throw new RuntimeException("No city location found for: " + cityName);
        }
    }
}
