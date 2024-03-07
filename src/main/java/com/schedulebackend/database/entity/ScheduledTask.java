package com.schedulebackend.database.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Entity
@NoArgsConstructor
@Table(name = "scheduled_tasks")
public class ScheduledTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String description;
    private String lessonLink;
    private String cronExpression;
    private boolean active;

    public ScheduledTask(Date date, String description, String lessonLink, String cronExpression, boolean active) {
        this.date=date;
        this.description = description;
        this.lessonLink = lessonLink;
        this.cronExpression = cronExpression;
        this.active = active;
    }
}