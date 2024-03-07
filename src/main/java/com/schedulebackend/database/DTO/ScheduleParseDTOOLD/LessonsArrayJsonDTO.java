package com.schedulebackend.database.DTO.ScheduleParseDTOOLD;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.schedulebackend.database.entity.Classroom;
import com.schedulebackend.database.entity.Group;
import com.schedulebackend.database.entity.Teacher;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LessonsArrayJsonDTO {
    @JsonProperty("subject_name")
    String subjectName;
    @JsonProperty("subject_id")
    int subjectId;
    @JsonProperty("type")
    String lessonType;
    @JsonProperty("teachers")
    List<Teacher> teacherList;
    @JsonProperty("groups")
    List<Group> groupList;
    @JsonProperty("rooms")
    List<Classroom> classroomList;
}
