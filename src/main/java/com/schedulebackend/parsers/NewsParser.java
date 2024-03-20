package com.schedulebackend.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import jakarta.security.auth.message.AuthException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class NewsParser {

    //private static final String AUTH_FILE_PATH = "app/authFiles/auth.json";
    @Value("${variables.auth-file-path}")
    private String AUTH_FILE_PATH;

    @Setter
    @Getter
    private static class CookieHeader {
        private String cookie;
        private URI apiURI;
        private String userId;
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
    public JsonArray requestNews(int per) throws IOException, AuthException {
        ObjectMapper mapper = new ObjectMapper(); // Получение токена из файла
        File file = new File(AUTH_FILE_PATH);
        CookieHeader cookie = mapper.readValue(file, CookieHeader.class);

        CloseableHttpClient httpClient = HttpClients.createDefault(); //Запрос на получение сообщений
        HttpGet request = new HttpGet(cookie.getApiURI().toString() + per);
        System.out.println(request.getURI());
        request.setHeader("Cookie", cookie.getCookie());
        request.setHeader("user-id", cookie.getUserId());
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            return (JsonArray) JsonParser.parseString(EntityUtils.toString(response.getEntity()));
        } else if (response.getStatusLine().getStatusCode() == 401) {
            throw new AuthException("Обновите токен пачки");
        } else {
            throw new RuntimeException(String.valueOf(response.getStatusLine().getStatusCode()));
        }
    }
}
