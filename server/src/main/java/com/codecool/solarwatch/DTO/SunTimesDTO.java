package com.codecool.solarwatch.DTO;

import java.time.LocalDate;

public record SunTimesDTO(String sunrise, String sunset, LocalDate date) {
}
