package com.schedulebackend.database.DTO.ScheduleParseDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BellHeaderDTO {
    @Expose
    @SerializedName("start_lesson")
    String startTime;
    @Expose
    @SerializedName("end_lesson")
    String endTime;
}