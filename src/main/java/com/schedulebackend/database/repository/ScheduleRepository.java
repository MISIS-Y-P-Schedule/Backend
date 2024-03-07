package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.Group;
import com.schedulebackend.database.entity.Lesson;
import com.schedulebackend.database.entity.Schedule;
import com.schedulebackend.database.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
//    @Query(value = "SELECT * FROM schedule WHERE date=?1 AND start_time=?2 AND group_id=?3 AND lesson_id=?4 ",nativeQuery = true)
//    Schedule findSchedule(Date date, Time start_time, Long, Long group_id, Long lesson_id);

    @Query
    Schedule findByDateAndStartTimeAndTeacherAndGroupAndLesson(Date date, Time startTime, Teacher Teacher, Group group, Lesson lesson);

    @Query
    Schedule findByDateAndStartTimeAndGroup(Date date, Time startTime, Group group);
}