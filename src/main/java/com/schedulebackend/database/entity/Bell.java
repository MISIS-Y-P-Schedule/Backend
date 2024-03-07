package com.schedulebackend.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bells")
public class Bell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Time startTime;
    private Time endTime;

    public Bell(Time startTime, Time endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}