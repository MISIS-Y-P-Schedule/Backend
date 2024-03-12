package com.schedulebackend.database.DTO.AuthDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWTResponseDTO {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}