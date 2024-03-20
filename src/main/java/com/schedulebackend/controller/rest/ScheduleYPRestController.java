package com.schedulebackend.controller.rest;

import com.schedulebackend.database.DTO.ErrorResponseDTO;
import com.schedulebackend.database.entity.News;
import com.schedulebackend.database.repository.NewsRepository;
import com.schedulebackend.service.ScheduleYPService;
import com.schedulebackend.service.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="Контроллер расписания Я.Практикума")
public class ScheduleYPRestController {
    private final ScheduleYPService scheduleYPService;
    private final NewsRepository newsRepository;
    private final SchedulerService schedulerService;

    @Operation(
            summary = "Обновить расписание по id новости"
    )
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Неверный id новости", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/admin/scheduleyp/update")
    public ResponseEntity<?> updateSchedule(@RequestParam Long newsId) throws IOException, InterruptedException, TelegramApiException {
        try {
            scheduleYPService.updateSchedule(newsRepository.findById(newsId).orElseThrow());
        }
        catch(NoSuchElementException e){
            return ResponseEntity.status(500).body(new ErrorResponseDTO("Неверный id новости"));
        }
        return ResponseEntity.ok().body("");
    }

    @Operation(
            summary = "Ручной вызов обновления новостей и расписания. Не вызывать лишний раз"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/admin/scheduleyp/test")
    public void test() throws TelegramApiException, IOException, InterruptedException {
        schedulerService.updateNewsAndSchedule();
    }

    @Operation(
            summary = "Получить расписание на неделю"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = News.class)))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/scheduleyp/week")
    public ResponseEntity<?> weekSchedule() {
        return ResponseEntity.ok().body(scheduleYPService.getWeekSchedule());
    }

    @Operation(
            summary = "Получить расписание на сегодня"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = News.class)))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/scheduleyp/today")
    public ResponseEntity<?> todaySchedule() {
        return ResponseEntity.ok().body(scheduleYPService.getTodaySchedule());
    }

}