package com.schedulebackend.database.DTO.ScheduleParseDTOOLD;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleListParseDTO {
    @JsonProperty("bell_1")
    BellJsonDTO bellJsonDTO1;
    @JsonProperty("bell_2")
    BellJsonDTO bellJsonDTO2;
    @JsonProperty("bell_3")
    BellJsonDTO bellJsonDTO3;
    @JsonProperty("bell_4")
    BellJsonDTO bellJsonDTO4;
    @JsonProperty("bell_5")
    BellJsonDTO bellJsonDTO5;
    @JsonProperty("bell_6")
    BellJsonDTO bellJsonDTO6;
    @JsonProperty("bell_7")
    BellJsonDTO bellJsonDTO7;
}
