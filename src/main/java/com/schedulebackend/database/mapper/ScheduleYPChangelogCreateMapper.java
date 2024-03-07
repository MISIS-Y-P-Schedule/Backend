package com.schedulebackend.database.mapper;

import com.schedulebackend.database.entity.ScheduleYP;
import com.schedulebackend.database.entity.ScheduleYPChangelog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleYPChangelogCreateMapper implements Mapper<ScheduleYP, ScheduleYPChangelog> {

    @Override
    public ScheduleYPChangelog map(ScheduleYP schedule) {
        ScheduleYPChangelog scheduleChangelog = new ScheduleYPChangelog();
        copy(scheduleChangelog, schedule);
        return scheduleChangelog;
    }

    public ScheduleYPChangelog map(ScheduleYPChangelog scheduleChangelog, ScheduleYP schedule) {
        copy(scheduleChangelog, schedule);
        return scheduleChangelog;
    }

    private void copy(ScheduleYPChangelog scheduleChangelog, ScheduleYP schedule) {
        scheduleChangelog.setDate(schedule.getDate());
        scheduleChangelog.setStartTime(schedule.getStartTime());
        scheduleChangelog.setEndTime(schedule.getEndTime());
        scheduleChangelog.setLesson(schedule.getLesson());
        scheduleChangelog.setLessonType(schedule.getLessonType());
    }
}
