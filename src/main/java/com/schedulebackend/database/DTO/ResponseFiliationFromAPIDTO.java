package com.schedulebackend.database.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.schedulebackend.database.entity.Classroom;
import com.schedulebackend.database.entity.Group;
import com.schedulebackend.database.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseFiliationFromAPIDTO {
    @JsonProperty("groups")
    List<Group> groupList;
    @JsonProperty("rooms")
    List<Classroom> classroomList;
    @JsonProperty("teachers")
    List<Teacher> teacherList;

}
