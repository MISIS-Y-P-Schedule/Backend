package com.schedulebackend.database.mapper;

import com.schedulebackend.database.entity.Schedule;
import com.schedulebackend.database.entity.ScheduleChangelog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleChangelogCreateMapper implements Mapper<Schedule, ScheduleChangelog> {

    @Override
    public ScheduleChangelog map(Schedule schedule) {
        ScheduleChangelog scheduleChangelog = new ScheduleChangelog();
        copy(scheduleChangelog, schedule);
        return scheduleChangelog;
    }

    public ScheduleChangelog map(ScheduleChangelog scheduleChangelog, Schedule schedule) {
        copy(scheduleChangelog, schedule);
        return scheduleChangelog;
    }

    private void copy(ScheduleChangelog scheduleChangelog, Schedule schedule) {
        scheduleChangelog.setDate(schedule.getDate());
        scheduleChangelog.setStartTime(schedule.getStartTime());
        scheduleChangelog.setEndTime(schedule.getEndTime());
        scheduleChangelog.setLesson(schedule.getLesson());
        scheduleChangelog.setTeacher(schedule.getTeacher());
        scheduleChangelog.setClassroom(schedule.getClassroom());
        scheduleChangelog.setGroup(schedule.getGroup());
        scheduleChangelog.setLessonType(schedule.getLessonType());
    }
}
