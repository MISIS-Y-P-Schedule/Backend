package com.schedulebackend.database.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class TeacherYPUpdateDTO {
    @NotNull
    Long id;
    String firstname;
    String midname;
    String lastname;

}