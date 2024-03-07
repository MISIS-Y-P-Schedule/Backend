package com.schedulebackend.service;

import com.schedulebackend.database.DTO.ChatGPTDTO.ScheduleArrayYPDTO;
import com.schedulebackend.database.DTO.LessonYPCreateDTO;
import com.schedulebackend.database.entity.News;
import com.schedulebackend.database.entity.ScheduleYP;
import com.schedulebackend.database.entity.ScheduleYPChangelog;
import com.schedulebackend.database.entity.ScheduledTask;
import com.schedulebackend.database.entity.enums.ChangeType;
import com.schedulebackend.database.entity.enums.LessonTypeYP;
import com.schedulebackend.database.mapper.ScheduleYPChangelogCreateMapper;
import com.schedulebackend.database.repository.ScheduleYPChangelogRepository;
import com.schedulebackend.database.repository.ScheduleYPRepository;
import com.schedulebackend.database.repository.ScheduledTaskRepository;
import com.schedulebackend.parsers.ChatGPTScheduleParser;
import com.schedulebackend.service.TelegramBot.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleYPService {
    private final ChatGPTScheduleParser chatGPTScheduleParser;
    private final ScheduleYPChangelogRepository scheduleYPChangelogRepository;
    private final ScheduleYPChangelogCreateMapper scheduleYPChangelogCreateMapper;
    private final ScheduleYPRepository scheduleYPRepository;
    private final LessonYPService lessonYPService;
    private final ScheduledTaskService scheduledTaskService;
    private final SchedulerService schedulerService;
    private final ScheduledTaskRepository scheduledTaskRepository;
    private final TelegramBotService telegramBotService;

    @Transactional
    public void createSchedule(ScheduleYP schedule, Timestamp changeDateTime) {
        scheduleYPRepository.save(schedule);
        ScheduleYPChangelog scheduleYPChangelog = new ScheduleYPChangelog();
        scheduleYPChangelog.setChangeType(ChangeType.CREATE);
        scheduleYPChangelog.setChangeTime(changeDateTime);
        scheduleYPChangelogRepository.save(scheduleYPChangelogCreateMapper.map(scheduleYPChangelog, schedule));
    }

    @Transactional
    public void deleteSchedule(ScheduleYP scheduleYP, Timestamp changeDateTime) {
        ScheduleYPChangelog scheduleChangelog = new ScheduleYPChangelog();
        scheduleChangelog.setChangeType(ChangeType.DELETE);
        scheduleChangelog.setChangeTime(changeDateTime);
        scheduleYPChangelogRepository.save(scheduleYPChangelogCreateMapper.map(scheduleChangelog, scheduleYP));
        scheduleYPRepository.deleteAllByDateAndLesson(scheduleYP.getDate(), scheduleYP.getLesson());
    }

    @Transactional
    public List<ScheduleYP> getTodaySchedule() {
        LocalDate localdate = LocalDate.now();
        return scheduleYPRepository.findAllByDate(java.sql.Date.valueOf(localdate));
    }

    @Transactional
    public List<ScheduleYP> getWeekSchedule() {
        LocalDate localdate = LocalDate.now();
        localdate = localdate.minusDays(localdate.getDayOfWeek().getValue());
        return scheduleYPRepository.findAllByDateBetween(java.sql.Date.valueOf(localdate), java.sql.Date.valueOf(localdate.plusDays(6)));
    }

    @Transactional
    public void updateSchedule(News news) throws IOException, InterruptedException, TelegramApiException {
        List<ScheduleArrayYPDTO> scheduleArray = chatGPTScheduleParser.parseResponse(news).getSchedule();
        if(scheduleArray != null) {
            Timestamp changeDateTime = Timestamp.from(Instant.from(ZonedDateTime.now()));
            String lessonNameBuf = "";
            for (ScheduleArrayYPDTO scheduleYP : scheduleArray) {
                System.out.println(scheduleYP);
                LocalDate localdate = LocalDate.of(1,1, 1);
                if(!scheduleYP.getDate().isEmpty()){
                    localdate = LocalDate.parse(scheduleYP.getDate());
                }
                LocalTime startTime = LocalTime.of(0, 0);
                if(!scheduleYP.getStart().isEmpty()) {
                    startTime = LocalTime.parse(scheduleYP.getStart());
                }
                LocalTime endTime = LocalTime.of(0, 0);
                if(!scheduleYP.getEnd().isEmpty()) {
                    endTime = LocalTime.parse(scheduleYP.getEnd());
                }
                LessonYPCreateDTO lessonYPCreateDTO = new LessonYPCreateDTO(scheduleYP.getName(), scheduleYP.getLink() == null ? null : scheduleYP.getLink());
                ScheduleYP schedule = new ScheduleYP(java.sql.Date.valueOf(localdate), java.sql.Time.valueOf(startTime), java.sql.Time.valueOf(endTime), lessonYPService.createOrReturnLesson(lessonYPCreateDTO), LessonTypeYP.valueOf(scheduleYP.getType()));
                if (scheduleYP.getType().equals(LessonTypeYP.DELETE.toString())) {
                    this.deleteSchedule(schedule, changeDateTime);
                    if (!schedule.getLessonType().equals(LessonTypeYP.ASYNC)) {
                        ScheduledTask scheduledTask = scheduledTaskRepository.findByDateAndDescriptionAndActive(java.sql.Date.valueOf(localdate), scheduleYP.getName(), true);
                        if(scheduledTask != null) {
                            schedulerService.cancelScheduledTask(scheduledTask.getId());
                        }else{
                            telegramBotService.sendToOwnerMessage("Не удален урок из расписания:"+scheduleYP.getName());
                        }
                    }
                } else {
                    this.createSchedule(schedule, changeDateTime);
                    LocalTime startTimeCron = startTime.minusMinutes(30);
                    System.out.println("0 " + startTimeCron.getMinute() + " " + startTimeCron.getHour() + " " + localdate.getDayOfMonth() + " " + localdate.getMonth().getValue() + " *");
                    if (!lessonNameBuf.equals(scheduleYP.getName()) && !schedule.getLessonType().equals(LessonTypeYP.ASYNC)) {
                        schedulerService.scheduleTask(scheduledTaskService.createTask(new ScheduledTask(java.sql.Date.valueOf(localdate),scheduleYP.getName(), schedule.getLesson().getLessonLink(), "0 " + startTimeCron.getMinute() + " " + startTimeCron.getHour() + " " + localdate.getDayOfMonth() + " " + localdate.getMonth().getValue() + " *", true)));
                        lessonNameBuf = scheduleYP.getName();
                    }
                }
            }
        }
    }

}
