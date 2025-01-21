package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.SolarUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SolarUser, Long> {
    Optional<SolarUser> findByUsername(String username);
    Boolean existsByUsername(String username);
}
