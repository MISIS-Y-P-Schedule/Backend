package com.schedulebackend.parsers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class HttpClient {
    OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(180, TimeUnit.SECONDS) // Тайм-аут подключения
            .readTimeout(180, TimeUnit.SECONDS)    // Тайм-аут чтения
            .writeTimeout(180, TimeUnit.SECONDS)   // Тайм-аут записи
            .build();

    public String sendPostRequest(Request postRequest) {
        try {
            // Выполняем запрос и получаем ответ
            Response postResponse = client.newCall(postRequest).execute();

            // Выводим тело ответа
            if (postResponse.isSuccessful() && postResponse.body() != null) {
                return postResponse.body().string();
            } else {
                System.out.println("Ошибка при запросе: " + postRequest.url());
                if (postResponse.body() != null) {
                    System.out.println(postResponse.body().string());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendGetRequest(Request getRequest) {
        try {
            // Выполняем запрос и получаем ответ
            Response getResponse = client.newCall(getRequest).execute();

            // Выводим тело ответа (убедитесь, что обрабатываете возможные ошибки)
            if (getResponse.isSuccessful() && getResponse.body() != null) {
                return getResponse.body().string();
            } else {
                System.out.println("Ошибка при запросе: " + getRequest.url());
                if (getResponse.body() != null) {
                    System.out.println(getResponse.body().string());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
