package com.schedulebackend.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessonsyp")
public class LessonYP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lessonLink;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacheryp_id")
    private TeacherYP teacher;

    public LessonYP(String name) {
        this.name = name;
    }

    public LessonYP(String name, String lessonLink) {
        this.name = name;
        this.lessonLink = lessonLink;
    }

    @Override
    public String toString() {
        return  name +
                "\nСсылка на пару: " + lessonLink +
                "\n" + teacher;
    }
}
