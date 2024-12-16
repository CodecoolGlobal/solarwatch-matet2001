package com.codecool.solarwatchmvc;

import static org.assertj.core.api.Assertions.assertThat;

import com.codecool.solarwatchmvc.controller.AuthController;
import com.codecool.solarwatchmvc.controller.CityController;
import com.codecool.solarwatchmvc.controller.SolarWatchController;
import com.codecool.solarwatchmvc.controller.SunTimesController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
