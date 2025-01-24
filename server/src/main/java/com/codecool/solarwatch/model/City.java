package com.codecool.solarwatch.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class City {
    @Id

    private long id;
    private UUID publicId;
    private String name;
    private double lat;
    private double lon;
    private String country;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<SunTimes> sunTimes = new HashSet<>();

    public City(String name, double lat, double lon, String country) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    public void addSunTimes(SunTimes sunTimes) {
        this.sunTimes.add(sunTimes);
    }

    public Optional<SunTimes> getSunTimesByDate(LocalDate date) {
        if (sunTimes == null) return Optional.empty();
        return sunTimes.stream().
                filter(sunTime -> sunTime.getDate().equals(date))
                .findFirst();
    }
}
