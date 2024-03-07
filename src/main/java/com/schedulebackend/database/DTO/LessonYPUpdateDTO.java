package com.schedulebackend.database.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class LessonYPUpdateDTO {
    @NotNull
    Long id;
    Long teacherId;
    String lessonLink;
}
