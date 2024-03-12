package com.schedulebackend.database.DTO.AuthDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JWTRequestDTO {

    private String login;
    private String password;

}