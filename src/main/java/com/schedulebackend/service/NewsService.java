package com.schedulebackend.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.schedulebackend.database.entity.News;
import com.schedulebackend.database.entity.NewsLinkAttachments;
import com.schedulebackend.database.entity.enums.NewsLinksType;
import com.schedulebackend.database.repository.NewsRepository;
import com.schedulebackend.parsers.NewsParser;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsParser newsParser;
    private final NewsRepository newsRepository;

    // Метод сохранения новостей
    public News saveNews(JsonElement response) {
        News presavedNews = new News(Date.from(Instant.parse(response.getAsJsonObject().get("created_at").toString().replace("\"", ""))), String.valueOf(response.getAsJsonObject().get("content")).replace("\"", ""));
        for (JsonElement files : response.getAsJsonObject().get("files").getAsJsonArray()) {
            String fileType = response.getAsJsonObject().get("files").getAsJsonArray().get(0).getAsJsonObject().get("file_type").toString().replace("\"", "");
            switch (fileType) {
                case ("image") ->
                        presavedNews.getNewsLinkList().add(new NewsLinkAttachments(presavedNews, NewsLinksType.IMAGES, String.valueOf(files.getAsJsonObject().get("versions").getAsJsonObject().get("full")).replace("\"", "")));
                case ("file") ->
                        presavedNews.getNewsLinkList().add(new NewsLinkAttachments(presavedNews, NewsLinksType.FILES, String.valueOf(files.getAsJsonObject().get("url")).replace("\"", "")));
            }
        }
        return newsRepository.save(presavedNews);
    }

    // Обновление новостей
    public List<News> updateNews() throws IOException, AuthException {
        List<News> newsBuffer = new ArrayList<>();
        JsonArray responseBody;
        if (newsRepository.findAll().isEmpty()) {
            responseBody = newsParser.requestNews(1000);
            for (JsonElement response : responseBody) {
                saveNews(response);
            }
        } else {
            responseBody = newsParser.requestNews(300);
            for (JsonElement response : responseBody) {
                if (Date.from(Instant.parse(response.getAsJsonObject().get("created_at").toString().replace("\"", "")))
                        .after(newsRepository.findFirstByOrderByCreatedAtDesc().getCreatedAt())) {
                    newsBuffer.add(saveNews(response));
                }
            }
        }
        return newsBuffer;
    }
}
