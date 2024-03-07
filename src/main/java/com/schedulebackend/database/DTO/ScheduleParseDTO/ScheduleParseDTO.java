package com.schedulebackend.database.DTO.ScheduleParseDTO;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.Map;
@Data
public class ScheduleParseDTO {
    @Expose
    String status;
    @Expose
    Map<String,Map<String, DayDTO>> schedule;
}
