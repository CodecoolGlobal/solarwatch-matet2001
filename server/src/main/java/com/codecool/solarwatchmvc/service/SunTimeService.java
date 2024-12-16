package com.codecool.solarwatchmvc.service;

import com.codecool.solarwatchmvc.model.City;
import com.codecool.solarwatchmvc.model.SunTimeResponse;
import com.codecool.solarwatchmvc.model.SunTimes;
import com.codecool.solarwatchmvc.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class SunTimeService {
    private static final Logger logger = LoggerFactory.getLogger(SunTimeService.class);

    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;

    @Autowired
    public SunTimeService(RestTemplate restTemplate, CityRepository cityRepository) {
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
    }

    public SunTimes getCitySunTimesByDateConvertedToHungarianTimeZone(City city, LocalDate date) {
        Optional<SunTimes> sunTimes = city.getSunTimesByDate(date);

        return sunTimes.map(this::convertToLocalSunTimes).orElseGet(() -> convertToLocalSunTimes(fetchCitySunTimesByDate(city, date)));

    }

    public SunTimes fetchCitySunTimesByDate(City city, LocalDate date) {
        try {
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", city.getLat(), city.getLon(), formattedDate);

            logger.info("Url for sun time api: {}", url);

            SunTimeResponse response = restTemplate.getForObject(url, SunTimeResponse.class);

            logger.info("Response from Sun Time api: {}", response);

            if (response != null) {
                SunTimes sunTimes = new SunTimes();
                sunTimes.setSunrise(response.results().getSunrise());
                sunTimes.setSunset(response.results().getSunset());
                sunTimes.setDate(date);
                city.addSunTimes(sunTimes);
                cityRepository.save(city);

                return sunTimes;
            } else {
                throw new RuntimeException("No sun times found for: " + city + ", " + formattedDate);
            }
        } catch (DateTimeException e) {
            throw new DateTimeException("Invalid date format: " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }

    private SunTimes convertToLocalSunTimes(SunTimes sunTimes) {
        // Define a formatter that matches the API time format (e.g., 5:11:51 AM)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm:ss a");

        // Parse sunrise and sunset times as LocalTime, assuming they are in UTC
        LocalTime sunriseUtc = LocalTime.parse(sunTimes.getSunrise(), formatter);
        LocalTime sunsetUtc = LocalTime.parse(sunTimes.getSunset(), formatter);

        // Convert to ZonedDateTime (UTC) and then shift to local time zone (GMT+2 or Europe/Budapest)
        ZonedDateTime sunriseLocal = sunriseUtc.atDate(LocalDate.now()).atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Europe/Budapest"));
        ZonedDateTime sunsetLocal = sunsetUtc.atDate(LocalDate.now()).atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Europe/Budapest"));

        // Convert back to LocalTime for output (if needed)
        String sunriseFormatted = sunriseLocal.format(formatter);
        String sunsetFormatted = sunsetLocal.format(formatter);

        // Return the new SunTimes object with local times
        sunTimes.setSunrise(sunriseFormatted);
        sunTimes.setSunset(sunsetFormatted);
        return sunTimes;
    }
}
