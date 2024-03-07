package com.schedulebackend.database.DTO.ScheduleParseDTO;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.Map;
@Data
public class BellDTO {

//    @Expose
//    @SerializedName("header")
//    BellHeaderDTO header;

    @Expose
    Map<String, DayDTO> test;
}
