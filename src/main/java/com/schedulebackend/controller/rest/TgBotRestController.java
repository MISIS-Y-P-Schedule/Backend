package com.schedulebackend.controller.rest;

import com.schedulebackend.service.TelegramBot.TelegramBotService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tg")
public class TgBotRestController {
    private final TelegramBotService telegramBotService;
    @PostMapping
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public void sendNotifications(@RequestParam String password, @RequestParam String message) throws TelegramApiException {
        if(password.equals("19021902")) {
            telegramBotService.sendNotifications(message);
        }
    }
}
