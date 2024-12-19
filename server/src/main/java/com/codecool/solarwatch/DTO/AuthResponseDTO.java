package com.codecool.solarwatch.DTO;

import java.util.List;

public record AuthResponseDTO(String username, String jwtToken, List<String> roles, String message) { }
