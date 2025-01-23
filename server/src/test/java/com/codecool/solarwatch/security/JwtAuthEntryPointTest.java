package com.codecool.solarwatch.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class JwtAuthEntryPointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHandleUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/secure-endpoint")) // An endpoint requiring authentication
                .andExpect(status().isUnauthorized()); // Expect SC_BAD_GATEWAY (502)
    }
}