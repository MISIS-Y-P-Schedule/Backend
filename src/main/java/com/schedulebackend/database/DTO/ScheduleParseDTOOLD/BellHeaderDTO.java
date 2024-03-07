package com.schedulebackend.database.DTO.ScheduleParseDTOOLD;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BellHeaderDTO {

    @Expose
    @SerializedName("type")
    String type;

    @Expose
    @SerializedName("start_lesson")
    String startTime;

    @Expose
    @SerializedName("end_lesson")
    String endTime;
}