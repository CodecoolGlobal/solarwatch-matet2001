package com.codecool.solarwatch;

import static org.assertj.core.api.Assertions.assertThat;

import com.codecool.solarwatch.controller.AuthController;
import com.codecool.solarwatch.controller.CityController;
import com.codecool.solarwatch.controller.SolarWatchController;
import com.codecool.solarwatch.controller.SunTimesController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class SmokeTest {

    private final AuthController authController;
    private final CityController cityController;
    private final SolarWatchController solarWatchController;
    private final SunTimesController sunTimesController;

    @Autowired
    public SmokeTest(AuthController authController, CityController cityController, SolarWatchController solarWatchController, SunTimesController sunTimesController) {
        this.authController = authController;
        this.cityController = cityController;
        this.solarWatchController = solarWatchController;
        this.sunTimesController = sunTimesController;
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(authController).isNotNull();
        assertThat(cityController).isNotNull();
        assertThat(solarWatchController).isNotNull();
        assertThat(sunTimesController).isNotNull();
    }
}
