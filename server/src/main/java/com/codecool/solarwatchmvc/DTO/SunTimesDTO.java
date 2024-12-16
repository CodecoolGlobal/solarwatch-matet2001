package com.codecool.solarwatchmvc.DTO;

import java.time.LocalDate;

public record SunTimesDTO(String sunrise, String sunset, LocalDate date) {
}
