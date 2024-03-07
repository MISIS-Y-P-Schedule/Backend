package com.schedulebackend.database.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

@Value
public class LessonYPCreateDTO {
    @NotEmpty
    String name;
    String lessonLink;
}
