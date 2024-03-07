package com.schedulebackend.database.DTO.ScheduleParseDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.schedulebackend.database.entity.Classroom;
import com.schedulebackend.database.entity.Group;
import com.schedulebackend.database.entity.Teacher;
import lombok.Data;

import java.util.List;
@Data
public class LessonDTO {
    @Expose
    @SerializedName("subject_name")
    String subjectName;
    @Expose
    @SerializedName("subject_id")
    int subjectId;
    @Expose
    @SerializedName("type")
    String lessonType;
    @Expose
    @SerializedName("teachers")
    List<Teacher> teacherList;
    @Expose
    @SerializedName("groups")
    List<Group> groupList;
    @Expose
    @SerializedName("rooms")
    List<Classroom> classroomList;
}