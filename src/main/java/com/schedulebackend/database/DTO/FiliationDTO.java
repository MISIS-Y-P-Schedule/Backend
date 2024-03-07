package com.schedulebackend.database.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FiliationDTO {
    @JsonProperty("response")
    ResponseFiliationFromAPIDTO responseFiliationFromAPIDTO;
}