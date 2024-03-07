package com.schedulebackend.service.TelegramBot;

import com.schedulebackend.database.entity.ScheduleYP;
import com.schedulebackend.database.entity.enums.UserState;
import com.schedulebackend.service.ScheduleYPService;
import com.schedulebackend.service.TgUserService;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.schedulebackend.service.TelegramBot.Keyboard.getMainKeyboardWithNotifications;
import static com.schedulebackend.service.TelegramBot.Keyboard.getMainKeyboardWithoutNotifications;

public class ResponseHandler {
    private final TgUserService tgUserService;
    private final ScheduleYPService scheduleYPService;
    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;

    public ResponseHandler(TgUserService tgUserService, ScheduleYPService scheduleYPService, SilentSender sender, DBContext db) {
        this.tgUserService = tgUserService;
        this.scheduleYPService = scheduleYPService;
        this.sender = sender;
        chatStates = db.getMap(Constants.CHAT_STATES);
    }

    public void replyToStart(long chatId) {
        tgUserService.createTgUser(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет!");
        message.setReplyMarkup(getMainKeyboardWithNotifications());
        sender.execute(message);
        chatStates.put(chatId, UserState.ALL_NOTIFICATIONS);
    }

    private void stopChat(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Бот выключен.\nНапишите /start чтобы запустить снова");
        chatStates.remove(chatId);
        tgUserService.deleteTgUser(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sender.execute(sendMessage);
    }

    public void replyToButtons(long chatId, Message message) {
        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
        }
        switch (chatStates.get(chatId)) {
            case ALL_NOTIFICATIONS, WITHOUT_NOTIFICATIONS -> replyToSchedule(chatId, message);
            default -> unexpectedMessage(chatId);
        }
    }

    private String scheduleStringCollector(List<ScheduleYP> scheduleYPList){
        StringBuilder response = new StringBuilder();
        Date datebuf = new Date();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM, EEEE").withLocale(Locale.forLanguageTag("ru"));
        for(ScheduleYP scheduleYP : scheduleYPList) {
            if (!datebuf.equals(scheduleYP.getDate())) {
                response.append("-------------------------------").append("\n");
                response.append(dateFormatter.format(LocalDateTime.ofInstant(scheduleYP.getDate().toInstant(), ZoneId.of("Europe/Moscow")))).append("\n\n");
            }
            response.append(scheduleYP.getStartTime().toLocalTime()).append(" - ").append(scheduleYP.getEndTime().toLocalTime()).append("\n");
            response.append(scheduleYP.getLesson()).append("\n");
            response.append(scheduleYP.getLessonType().getName()).append("\n\n");
            datebuf = scheduleYP.getDate();
        }
        return response.toString();
    }

    private void replyToSchedule(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        switch (message.getText()) {
//            case "Расписание на неделю" -> {
//                StringBuilder response = new StringBuilder();
//                Date datebuf = new Date();
//                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM, EEEE");
//                for(ScheduleYP scheduleYP : scheduleYPService.getWeekSchedule()) {
//                    if (!datebuf.equals(scheduleYP.getDate())) {
//                        response.append("-------------------------------").append("\n");
//                        response.append(dateFormatter.format(LocalDateTime.ofInstant(scheduleYP.getDate().toInstant(), UTC))).append("\n");
//                    }
//                    response.append(scheduleYP.getStartTime().toLocalTime()).append(" - ").append(scheduleYP.getEndTime().toLocalTime()).append("\n");
//                    response.append(scheduleYP.getLesson()).append("\n");
//                    response.append(scheduleYP.getLessonType().getName()).append("\n\n");
//                    datebuf = scheduleYP.getDate();
//                }
//                sendMessage.setText(response.toString());
//                sender.execute(sendMessage);
//            }
//            case "Расписание на сегодня" -> {
//                StringBuilder response = new StringBuilder();
//                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM, EEEE");
//                response.append("----------------------------").append("\n");
//                response.append(dateFormatter.format(LocalDate.now())).append("\n");
//                for (ScheduleYP scheduleYP : scheduleYPService.getTodaySchedule()) {
//                    response.append(scheduleYP.getStartTime().toLocalTime()).append(" - ").append(scheduleYP.getEndTime().toLocalTime()).append("\n");
//                    response.append(scheduleYP.getLesson()).append("\n");
//                    response.append(scheduleYP.getLessonType().getName()).append("\n\n");
//                }
//                sendMessage.setText(response.toString());
//                sender.execute(sendMessage);
//            }
            case "Расписание на неделю" -> {
                sendMessage.setText(scheduleStringCollector(scheduleYPService.getWeekSchedule()));
                sendMessage.disableWebPagePreview();
                sender.execute(sendMessage);
            }
            case "Расписание на сегодня" -> {
                sendMessage.setText(scheduleStringCollector(scheduleYPService.getTodaySchedule()));
                sendMessage.disableWebPagePreview();
                sender.execute(sendMessage);
            }
            case "Выключить уведомления" -> {
                sendMessage.setReplyMarkup(getMainKeyboardWithoutNotifications());
                sendMessage.setText("Уведомления о начале пар выключены");
                sender.execute(sendMessage);
                tgUserService.updateUserState(chatId, UserState.WITHOUT_NOTIFICATIONS);
                chatStates.put(chatId, UserState.WITHOUT_NOTIFICATIONS);
            }
            case "Включить уведомления" -> {
                sendMessage.setReplyMarkup(getMainKeyboardWithNotifications());
                sendMessage.setText("Уведомления о начале пар включены");
                sender.execute(sendMessage);
                tgUserService.updateUserState(chatId, UserState.ALL_NOTIFICATIONS);
                chatStates.put(chatId, UserState.ALL_NOTIFICATIONS);
            }
            case "Есть предложение" -> {
                sendMessage.setText("Напишите @DELTO0000 если есть предложения по улучшению сервиса");
                sender.execute(sendMessage);
            }
            default -> unexpectedMessage(chatId);
        }
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }

    private void unexpectedMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Неправильная команда");
        sender.execute(sendMessage);
    }
}
