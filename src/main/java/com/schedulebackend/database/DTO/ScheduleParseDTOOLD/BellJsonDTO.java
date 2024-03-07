package com.schedulebackend.database.DTO.ScheduleParseDTOOLD;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BellJsonDTO {
    @JsonProperty("header")
    BellHeaderDTO bellHeaderDTO;
    @JsonProperty("day_1")
    DayJsonDTO dayJsonDTO1;
    @JsonProperty("day_2")
    DayJsonDTO dayJsonDTO2;
    @JsonProperty("day_3")
    DayJsonDTO dayJsonDTO3;
    @JsonProperty("day_4")
    DayJsonDTO dayJsonDTO4;
    @JsonProperty("day_5")
    DayJsonDTO dayJsonDTO5;
    @JsonProperty("day_6")
    DayJsonDTO dayJsonDTO6;

}
