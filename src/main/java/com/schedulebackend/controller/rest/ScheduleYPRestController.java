package com.schedulebackend.controller.rest;

import com.schedulebackend.database.repository.NewsRepository;
import com.schedulebackend.service.ScheduleYPService;
import com.schedulebackend.service.SchedulerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ScheduleYPRestController {
    private final ScheduleYPService scheduleYPService;
    private final NewsRepository newsRepository;
    private final SchedulerService schedulerService;
    @PostMapping("/admin/scheduleyp/update")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateSchedule(@RequestParam Long newsId) throws IOException, InterruptedException, TelegramApiException {
        try {
            scheduleYPService.updateSchedule(newsRepository.findById(newsId).orElseThrow());
        }
        catch(NoSuchElementException e){
            return ResponseEntity.badRequest().body("Неверный id новости");
        }
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/user/scheduleyp/week")
    public ResponseEntity<?> weekSchedule() {
        return ResponseEntity.ok().body(scheduleYPService.getWeekSchedule());
    }

    @GetMapping("/user/scheduleyp/today")
    public ResponseEntity<?> todaySchedule() {
        return ResponseEntity.ok().body(scheduleYPService.getTodaySchedule());
    }

    @GetMapping("/admin/scheduleyp/test")
    @SecurityRequirement(name = "JWT")
    public void test() throws TelegramApiException, IOException, InterruptedException {
        schedulerService.updateNewsAndSchedule();
    }
}