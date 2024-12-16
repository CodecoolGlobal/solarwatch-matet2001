package com.codecool.solarwatchmvc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunTimeResponse(SunTimes results, String status) {
}
