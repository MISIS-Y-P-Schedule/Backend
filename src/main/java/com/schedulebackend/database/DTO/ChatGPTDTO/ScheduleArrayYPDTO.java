package com.schedulebackend.database.DTO.ChatGPTDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleArrayYPDTO {
    String name;
    String start;
    String end;
    String date;
    String type;
    String link;
}
