package com.schedulebackend.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.schedulebackend.database.repository.NewsRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class NewsParser {

    private final NewsRepository newsRepository;

    //private static final String AUTH_FILE_PATH = "app/authFiles/auth.json";
    private static final String AUTH_FILE_PATH = "src/main/resources/authFiles/auth.json";

    @Setter
    @Getter
    private static class CookieHeader {
        private String cookie;
        private URI apiURI;
    }

    // Обновление токена в файле
    public void updateCookie(String cookie) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); // Получение токена из файла
        File file = new File(AUTH_FILE_PATH);
        //this.getClass().getClassLoader().getResourceAsStream("authFiles/auth.json");
        CookieHeader cookieClass = mapper.readValue(file, CookieHeader.class);
        cookieClass.setCookie(cookie);
        mapper.writeValue(file, cookieClass); // Запись токена
    }

    // Запрос в API пачки
    public JsonArray requestNews(int per) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); // Получение токена из файла
        File file = new File(AUTH_FILE_PATH);
        CookieHeader cookie = mapper.readValue(file, CookieHeader.class);

        CloseableHttpClient httpClient = HttpClients.createDefault(); //Запрос на получение сообщений
        HttpGet request = new HttpGet(cookie.getApiURI().toString() + per);
        request.setHeader("Cookie", cookie.getCookie());
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            return (JsonArray) JsonParser.parseString(EntityUtils.toString(response.getEntity()));
        }
        return null;
    }
}
