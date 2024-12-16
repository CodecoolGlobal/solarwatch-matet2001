package com.codecool.solarwatchmvc.DTO;

import java.time.LocalDate;

public record SolarWatchRequestDTO(String cityName, LocalDate date) {
}
