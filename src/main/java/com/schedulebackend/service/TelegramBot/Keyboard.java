package com.schedulebackend.service.TelegramBot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class Keyboard {
    public static ReplyKeyboard getMainKeyboardWithNotifications() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Расписание на неделю");
        row1.add("Расписание на сегодня");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Выключить уведомления");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Есть предложение");
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .keyboardRow(row3)
                .build();
    }
    public static ReplyKeyboard getMainKeyboardWithoutNotifications() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Расписание на неделю");
        row1.add("Расписание на сегодня");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Включить уведомления");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Есть предложение");
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .keyboardRow(row3)
                .build();
    }
}
