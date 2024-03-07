package com.schedulebackend.database.DTO.ScheduleParseDTO;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.List;
@Data
public class DayDTO {

    @Expose
    List<LessonDTO> lessons;

}
