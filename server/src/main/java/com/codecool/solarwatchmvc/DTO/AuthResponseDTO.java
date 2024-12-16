package com.codecool.solarwatchmvc.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String tokenType = "Bearer ";
    private String message;

    public AuthResponseDTO(String message) {
        this.message = message;
    }

    public AuthResponseDTO(String token, String message) {
        this.token = token;
        this.message = message;
    }
}
