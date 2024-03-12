package com.schedulebackend.controller.rest;

import com.schedulebackend.service.TelegramBot.TelegramBotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tg")
@Tag(name="Контроллер оповещений в тг бота")
public class TgBotRestController {
    private final TelegramBotService telegramBotService;

    @Operation(
            summary = "Отправить оповещение"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("sendtoall")
    @ResponseStatus(HttpStatus.OK)
    public void sendNotifications(@RequestParam String password, @RequestParam String message) throws TelegramApiException {
        if(password.equals("19021902")) {
            telegramBotService.sendNotifications(message);
        }
    }
    @Operation(
            summary = "Отправить оповещение определенному юзеру"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("sendtouser/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public void sendNotificationToUser(@PathVariable Long user_id, @RequestParam String password, @RequestParam String message) throws TelegramApiException {
        if(password.equals("19021902")) {
            telegramBotService.sendToUserMessage(message, user_id);
        }
    }

}
