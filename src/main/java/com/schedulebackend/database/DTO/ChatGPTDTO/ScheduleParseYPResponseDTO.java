package com.schedulebackend.database.DTO.ChatGPTDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleParseYPResponseDTO {
    List<ScheduleArrayYPDTO> schedule;
}
