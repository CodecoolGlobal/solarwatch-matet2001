package com.codecool.solarwatchmvc.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class SunTimes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String sunrise;
    private String sunset;
    private LocalDate date;

    public SunTimes(String sunrise, String sunset, LocalDate date) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = date;
    }
}
