package com.codecool.solarwatch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class SolarWatchApplicationTests {

    @Test
    void contextLoads() {
        // Test to ensure that the application context loads successfully
    }
}
