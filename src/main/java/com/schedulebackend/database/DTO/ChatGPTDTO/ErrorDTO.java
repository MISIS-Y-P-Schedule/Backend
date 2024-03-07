package com.schedulebackend.database.DTO.ChatGPTDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO {
    String message;
    String type;
}
