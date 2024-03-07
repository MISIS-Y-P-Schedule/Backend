package com.schedulebackend.service;

import com.schedulebackend.database.entity.Bell;
import com.schedulebackend.database.repository.BellRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BellService {
    private final BellRepository bellRepository;

    @Transactional
    public void createBells() {
        if (bellRepository.findAll().isEmpty()) {
            bellRepository.save(new Bell(Time.valueOf(LocalTime.parse("09:00")), Time.valueOf(LocalTime.parse("10:35"))));
            bellRepository.save(new Bell(Time.valueOf(LocalTime.parse("10:50")), Time.valueOf(LocalTime.parse("12:25"))));
            bellRepository.save(new Bell(Time.valueOf(LocalTime.parse("12:40")), Time.valueOf(LocalTime.parse("14:15"))));
            bellRepository.save(new Bell(Time.valueOf(LocalTime.parse("14:30")), Time.valueOf(LocalTime.parse("16:05"))));
            bellRepository.save(new Bell(Time.valueOf(LocalTime.parse("16:20")), Time.valueOf(LocalTime.parse("17:55"))));
            bellRepository.save(new Bell(Time.valueOf(LocalTime.parse("18:00")), Time.valueOf(LocalTime.parse("19:25"))));
            bellRepository.save(new Bell(Time.valueOf(LocalTime.parse("19:35")), Time.valueOf(LocalTime.parse("21:00"))));
        }
        //return  bellRepository.findAll().isEmpty() ? classroomRepository.save(classroom) : new Classroom();
    }
}
