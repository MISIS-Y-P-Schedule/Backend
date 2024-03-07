package com.schedulebackend.database.DTO.ScheduleParseDTOOLD;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleParseDTO {
    String status;
    String startDate;
    @JsonProperty("schedule")
    ScheduleListParseDTO scheduleEntity;

}
