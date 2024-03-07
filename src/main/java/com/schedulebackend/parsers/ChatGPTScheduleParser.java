package com.schedulebackend.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedulebackend.database.DTO.ChatGPTDTO.*;
import com.schedulebackend.database.entity.News;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ChatGPTScheduleParser {

    private final HttpClient httpClient;

    @Value("${variables.openai-assistant}")
    private String assistantId;
    @Value("${variables.openai-token}")
    private String apiKey;

    String apiUrl = "https://api.openai.com/v1/threads";

    public String sendPostRequest(RequestBody body, String url) throws IOException {
        Request postRequest = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("OpenAI-Beta", "assistants=v1")
                    .build();
        return httpClient.sendPostRequest(postRequest);

    }

    public String sendGetRequest(String url) {
        Request getRequest = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("OpenAI-Beta", "assistants=v1")
                    .build();
        return httpClient.sendGetRequest(getRequest);

    }

    public ScheduleParseYPResponseDTO parseResponse(News news) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();

        //Создаем Thread, для того чтобы отправить и получить сообщение
        ThreadAndRunResponseDTO threadResponseDTO = mapper.readValue(sendPostRequest(RequestBody.create("", null), apiUrl), new TypeReference<>() {
        });

        //Отправляем сообщение
        MediaType mediaType = MediaType.parse("application/json");
        String messageBodyJson = "{\n" +
                "  \"role\": \"user\",\n" +
                "  \"content\": \"" + news.getContent() + "\"\n" +
                "}";
        RequestBody messageBody = RequestBody.create(messageBodyJson, mediaType);
        try {
            sendPostRequest(messageBody, apiUrl + "/" + threadResponseDTO.getId() + "/messages");
        } catch (RuntimeException e) {
            return new ScheduleParseYPResponseDTO();
        }

        //Отправляем id созданного ассистента и запускаем Thread
        String assistantBodyJson = "{"
                    + "\"assistant_id\": \"" + assistantId + "\""
                    + "}";
        RequestBody assistantBody = RequestBody.create(assistantBodyJson, mediaType);
        ThreadAndRunResponseDTO runResponseDTO = mapper.readValue(sendPostRequest(assistantBody, apiUrl + "/" + threadResponseDTO.getId() + "/runs"), new TypeReference<>() {
        });

        //Ждем выполнения Thread
        RunStatusResponseDTO statusResponseDTO = mapper.readValue(sendGetRequest(apiUrl + "/" + threadResponseDTO.getId() + "/runs/" + runResponseDTO.getId()), new TypeReference<>() {
        });
        int counter = 0;
        while (!(Objects.equals(statusResponseDTO.getStatus(), "completed") || counter == 9)) {
            System.out.println(runResponseDTO.getId() + "  " + counter);
            statusResponseDTO = mapper.readValue(sendGetRequest(apiUrl + "/" + threadResponseDTO.getId() + "/runs/" + runResponseDTO.getId()), new TypeReference<>() {
            });
            counter++;
            Thread.sleep(10000);//5000
        }
        if (counter < 9) {
            String response = null;
            String getResponse = sendGetRequest(apiUrl + "/" + threadResponseDTO.getId() + "/messages?limit=100");
            ResponseFromChatGPTDTO responseFromChatGPTDTO = mapper.readValue(getResponse, new TypeReference<>() {
            });

            //Проверяем на ошибку сервера
            while (!(responseFromChatGPTDTO.getError() == null)) {
                Thread.sleep(6000);//3000
                System.out.println("server_error");
                getResponse = sendGetRequest(apiUrl + "/" + threadResponseDTO.getId() + "/messages?limit=100");
                responseFromChatGPTDTO = mapper.readValue(getResponse, new TypeReference<>() {
                });
            }
            counter++;
            DataFromChatGPTDTO test = responseFromChatGPTDTO.getData().get(0);
            response = test.getContent().get(0).getText().getValue();
            return mapper.readValue(response, new TypeReference<>() {
            });

        } else {
            return new ScheduleParseYPResponseDTO();
        }
        }
    }
