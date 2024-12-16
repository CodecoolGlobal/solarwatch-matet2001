package com.codecool.solarwatchmvc.controller;

import com.codecool.solarwatchmvc.DTO.CityDTO;
import com.codecool.solarwatchmvc.DTO.SolarWatchRequestDTO;
import com.codecool.solarwatchmvc.DTO.SolarWatchResponseDTO;
import com.codecool.solarwatchmvc.DTO.SunTimesDTO;
import com.codecool.solarwatchmvc.model.City;
import com.codecool.solarwatchmvc.model.SunTimes;
import com.codecool.solarwatchmvc.security.UserDetailsServiceImpl;
import com.codecool.solarwatchmvc.service.GeocodingService;
import com.codecool.solarwatchmvc.service.SunTimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

