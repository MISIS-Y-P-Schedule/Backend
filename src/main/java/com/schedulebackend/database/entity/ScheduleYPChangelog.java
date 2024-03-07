package com.schedulebackend.database.entity;

import com.schedulebackend.database.entity.enums.ChangeType;
import com.schedulebackend.database.entity.enums.LessonTypeYP;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "scheduleyp_changelog")
public class ScheduleYPChangelog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
    private Timestamp changeTime;

    private String lessonRecordLink;
    private Time startTime;
    private Time endTime;
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lessonyp_id")
    private LessonYP lesson;

    @Enumerated(EnumType.STRING)
    private LessonTypeYP lessonType;
}
