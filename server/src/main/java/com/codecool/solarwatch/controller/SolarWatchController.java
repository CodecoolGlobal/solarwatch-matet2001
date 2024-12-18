package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.DTO.CityDTO;
import com.codecool.solarwatch.DTO.SolarWatchResponseDTO;
import com.codecool.solarwatch.DTO.SunTimesDTO;
import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.model.SunTimes;
import com.codecool.solarwatch.service.GeocodingService;
import com.codecool.solarwatch.service.SunTimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class SolarWatchController {
    private final GeocodingService geocodingService;
    private final SunTimeService sunTimeService;

    private static final Logger logger = LoggerFactory.getLogger(SolarWatchController.class);

    public SolarWatchController(GeocodingService geocodingService, SunTimeService sunTimeService) {
        this.geocodingService = geocodingService;
        this.sunTimeService = sunTimeService;
    }

    @GetMapping("/api/solar-watch")
    public SolarWatchResponseDTO getSolarWatch(@RequestParam(required = false) LocalDate date, @RequestParam String cityName, HttpServletRequest request) {
        if (date == null) {
            date = LocalDate.now();
        }
        
        City city = geocodingService.getCityByName(cityName);
        CityDTO cityDTO = new CityDTO(city.getId(), city.getName(), city.getLat(), city.getLon(), city.getCountry());

        SunTimes sunTimes = sunTimeService.getCitySunTimesByDateConvertedToHungarianTimeZone(city, date);
        SunTimesDTO sunTimesDTO = new SunTimesDTO(sunTimes.getSunrise(), sunTimes.getSunset(), sunTimes.getDate());

        SolarWatchResponseDTO responseDTO = new SolarWatchResponseDTO(cityDTO, sunTimesDTO);
        logger.info(responseDTO.toString());

        return responseDTO;
    }
}

