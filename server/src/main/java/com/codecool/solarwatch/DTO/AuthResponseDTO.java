package com.codecool.solarwatch.DTO;


public record AuthResponseDTO(String username, String jwtToken, java.util.List<String> roles, String message) { }
