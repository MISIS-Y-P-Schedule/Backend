package com.schedulebackend.database.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskLink;

    @Column(length = 2500)
    private String taskDescription;

    private LocalDate deadline;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lessonyp_id")
    private LessonYP lessonSync;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taskId")
    private List<UserTask> userTasks = new ArrayList<>();

}
