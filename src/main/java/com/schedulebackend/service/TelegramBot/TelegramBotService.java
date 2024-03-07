package com.schedulebackend.service.TelegramBot;

import com.schedulebackend.database.entity.News;
import com.schedulebackend.database.entity.NewsLinkAttachments;
import com.schedulebackend.database.entity.TgUser;
import com.schedulebackend.database.entity.enums.NewsLinksType;
import com.schedulebackend.database.entity.enums.UserState;
import com.schedulebackend.service.ScheduleYPService;
import com.schedulebackend.service.TgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Component
public class TelegramBotService extends AbilityBot {
    private final ResponseHandler responseHandler;
    private final TgUserService tgUserService;

    @Autowired
    public TelegramBotService(Environment environment, TgUserService tgUserService, @Lazy ScheduleYPService scheduleYPService) {
        super(environment.getProperty("variables.telegram-token"), "YPScheduleBot");
        this.tgUserService = tgUserService;
        responseHandler = new ResponseHandler(tgUserService, scheduleYPService, silent, db);
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> responseHandler.replyToStart(ctx.chatId()))
                .build();
    }

    public Reply replyToButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> responseHandler.replyToButtons(getChatId(upd), upd.getMessage());
        return Reply.of(action, Flag.TEXT, upd -> responseHandler.userIsActive(getChatId(upd)));
    }

    //Метод для отправки новостей из пачки
    public void sendNews(News news) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        StringBuilder newsContent = new StringBuilder(news.getContent().replace("\\r", "\r").replace("\\n", "\n"));
        List<InputMedia> medias = new ArrayList<>();
        SendPhoto sendPhoto = new SendPhoto();
        int photoCounter = 0;
        for (NewsLinkAttachments newsLinkAttachments : news.getNewsLinkList()) {
            if (newsLinkAttachments.getNewsLinksType().equals(NewsLinksType.FILES)) {
                newsContent.append("<a href=\"").append(newsLinkAttachments.getNewsLink()).append("\">Ссылка</a> на файл").append("\n");
                //newsContent.append("[Ссылка](").append(newsLinkAttachments.getNewsLink()).append(") на файл").append("\n");
            } else {
                if (news.getNewsLinkList().size() > 1) {
                    medias.add(new InputMediaPhoto(newsLinkAttachments.getNewsLink()));
                }
                else {
                    sendPhoto.setPhoto(new InputFile(newsLinkAttachments.getNewsLink()));
                }
                photoCounter++;
            }
        }

        String html = newsContent.toString().replaceAll("\\*\\*\\*(.*?)\\*\\*(.*?)\\*", "<i><b>$1</b>$2</i>")// Жирный текст внутри курсива
                .replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>") // Жирный текст
                .replaceAll("\\*(.+?)\\*", "<i>$1</i>")// Курсив
                .replaceAll("\\[(.+?)\\]\\((.+?)\\)", "<a href=\"$2\">$1</a>"); // Ссылки

        System.out.println("tg "+html);
        sendMessage.setText(html);
        sendMessage.enableHtml(true);
        sendMessage.disableWebPagePreview();
        for (TgUser tgUser : tgUserService.getAllUsers()) {
            sendMessage.setChatId(tgUser.getTgId());
            sendPhoto.setChatId(tgUser.getTgId());
            if (!newsContent.isEmpty()) {
                sender.execute(sendMessage);
            }
            SendMediaGroup sendMediaGroup = new SendMediaGroup(tgUser.getTgId().toString(), medias);
            if (photoCounter > 1) execute(sendMediaGroup);
            if (photoCounter == 1) sender.sendPhoto(sendPhoto);
        }
    }

    //Методы для отправки уведомлений
    public void sendNotifications(String message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        for (TgUser tgUser : tgUserService.getUsersByState(UserState.ALL_NOTIFICATIONS)) {
            sendMessage.setChatId(tgUser.getTgId());
            sendMessage.enableMarkdown(true);
            sendMessage.disableWebPagePreview();
            sendMessage.setText(message);
            sender.execute(sendMessage);
        }
    }

    public void sendToUserMessage(String message, Long id) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(id);
        sendMessage.setText(message);
        sender.execute(sendMessage);
    }

    public void sendToOwnerMessage(String message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(creatorId());
        sendMessage.setText(message);
        sender.execute(sendMessage);
    }
    @Override
    public long creatorId() {
        return 191833583;
    }
}
