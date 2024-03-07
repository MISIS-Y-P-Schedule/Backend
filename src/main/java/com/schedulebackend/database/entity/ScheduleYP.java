package com.schedulebackend.database.entity;

import com.schedulebackend.database.entity.enums.LessonTypeYP;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.time.ZoneOffset.UTC;

@Data
@Entity
@NoArgsConstructor
@Table(name = "scheduleyp")
public class ScheduleYP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lessonRecordLink;
    private Time startTime;
    private Time endTime;
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lessonyp_id")
    private LessonYP lesson;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "group_id")
//    private Group group;

    @Enumerated(EnumType.STRING)
    private LessonTypeYP lessonType;

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM, EEEE");
        //LocalDate test = LocalDate.from(date.toInstant());
        return  startTime.toLocalTime() + " - " + endTime.toLocalTime() + " " + dateFormatter.format(LocalDateTime.ofInstant(date.toInstant(),UTC)) +
                "\n" + lesson +
                "\n" + lessonType.getName() +
                "\n" + "--------------------------------" + "\n";
    }

    public ScheduleYP(Date date, Time startTime, Time endTime, LessonYP lesson, LessonTypeYP lessonType) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lesson = lesson;
        this.lessonType = lessonType;
    }
}
