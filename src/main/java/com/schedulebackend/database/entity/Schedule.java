package com.schedulebackend.database.entity;

import com.schedulebackend.database.entity.enums.LessonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode()
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Time startTime;
    private Time endTime;
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    public Schedule(Date date, Time startTime, Time endTime, Lesson lesson, Teacher teacher, Classroom classroom, Group group, LessonType lessonType) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lesson = lesson;
        this.teacher = teacher;
        this.classroom = classroom;
        this.group = group;
        this.lessonType = lessonType;
    }
}
