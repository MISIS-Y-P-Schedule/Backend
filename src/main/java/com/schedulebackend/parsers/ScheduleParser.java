package com.schedulebackend.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.schedulebackend.database.DTO.ScheduleParseDTO.ScheduleParseDTO;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ScheduleParser {

    public ScheduleParseDTO getScheduleFromAPI(int groupId, String date) throws IOException, InterruptedException {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("group", String.valueOf(groupId))
                .add("start_date", date)
                .build();
        Request request = new Request.Builder()
                .url("https://lk.misis.ru/method/schedule.get")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();

        String serrializedJson = response.body().string();
        if (!response.isSuccessful()) {
            if (response.code() == 503) {
                Thread.sleep(3000);
                System.out.println("Expected code " + response);
                response = client.newCall(request).execute();
            }
        } else {
            System.out.println(serrializedJson);
        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        return gson.fromJson(serrializedJson, ScheduleParseDTO.class);
    }
}
