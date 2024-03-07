package com.schedulebackend.service;

import com.schedulebackend.database.entity.News;
import com.schedulebackend.database.entity.ScheduledTask;
import com.schedulebackend.database.repository.ScheduledTaskRepository;
import com.schedulebackend.service.TelegramBot.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class SchedulerService implements ApplicationListener<ApplicationReadyEvent> {
    private final NewsService newsService;
    private final TelegramBotService telegramBotService;
    private ScheduleYPService scheduleYPService;
    private final ScheduledTaskRepository scheduledTaskRepository;
    private final ScheduledTaskService scheduledTaskService;
    private final TaskScheduler taskScheduler;

    // Карта для хранения ссылок на запланированные задачи
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    //Сеттер для того чтобы Spring не ругался на зацикливание
    @Autowired
    @Lazy
    public void setScheduleYPService(ScheduleYPService scheduleYPService){
        this.scheduleYPService=scheduleYPService;
    }

    //Таски на обновление новостей и расписания через ChatGPT в 16 и 22 по МСК
    @Scheduled(cron = "0 0 22 * * *", zone = "Europe/Moscow")
    @Scheduled(cron = "0 0 16 * * *", zone = "Europe/Moscow")
    public void updateNewsAndSchedule() throws IOException, TelegramApiException, InterruptedException {
        List<News> newsList = new ArrayList<>(newsService.updateNews());
        for (News news : newsList) {
            try{
                telegramBotService.sendNews(news);
            }catch (TelegramApiException e){
                telegramBotService.sendToOwnerMessage("Не удалось отправить сообщение.\nid новости: "+news.getId());
            }
            scheduleYPService.updateSchedule(news);
        }
    }

    //Создать таски при перезапуске
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<ScheduledTask> tasks = scheduledTaskRepository.findByActive(true);
        tasks.forEach(this::scheduleTask);
    }

    //Метод для создания тасок
    public void scheduleTask(ScheduledTask task) {
        scheduledTasks.put(task.getId(), taskScheduler.schedule(() -> {
            StringBuilder message = new StringBuilder();
            message.append("Сегодня пара через 30 минут: \n").append(task.getDescription()).append("\n");
            if (task.getLessonLink() != null) {
                message.append("[Ссылка](").append(task.getLessonLink()).append(") на пару");
            } else {
                message.append("Ссылки на пару нет");
            }
            try {
                telegramBotService.sendNotifications(message.toString());
                scheduledTaskService.setTaskActiveFalse(task.getId());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Выполнение задачи: " + task.getDescription());
        }, new CronTrigger(task.getCronExpression(), TimeZone.getTimeZone("Europe/Moscow"))));
    }

    //Удаление таски
    public void cancelScheduledTask(Long taskId) {
        ScheduledFuture<?> future = scheduledTasks.get(taskId);
        if (future != null) {
            future.cancel(true);
            scheduledTasks.remove(taskId);
            scheduledTaskService.deleteTask(taskId);
        }
    }
}
