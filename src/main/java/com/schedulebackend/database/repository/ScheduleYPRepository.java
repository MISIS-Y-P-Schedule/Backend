package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.LessonYP;
import com.schedulebackend.database.entity.ScheduleYP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;
@Repository
public interface ScheduleYPRepository extends JpaRepository<ScheduleYP, Long> {
//    @Query(value = "SELECT * FROM schedule WHERE date=?1 AND start_time=?2 AND group_id=?3 AND lesson_id=?4 ",nativeQuery = true)
//    Schedule findSchedule(Date date, Time start_time, Long, Long group_id, Long lesson_id);

    @Query
    ScheduleYP findByDateAndStartTimeAndLesson(Date date, Time startTime, LessonYP lesson);
    @Query
    List<ScheduleYP> findAllByDateBetween(Date firstDate, Date secondDate);
    @Query
    List<ScheduleYP> findAllByDate(Date date);

    @Query
    void deleteAllByDateAndLesson(Date date, LessonYP lessonYP);
}