package com.codecool.solarwatchmvc.repository;

import com.codecool.solarwatchmvc.model.SunTimes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SunTimesRepository extends JpaRepository<SunTimes, Long> {
    List<SunTimes> findByDate(LocalDate date);
}
