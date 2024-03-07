package com.schedulebackend.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedulebackend.database.DTO.FiliationDTO;
import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FiliationParserJSON {
    private final HttpClient httpClient;

    public FiliationDTO parseJSON() throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("filiation_id", "880")
                .build();
        Request request = new Request.Builder()
                .url("https://lk.misis.ru/method/filiation_info.get")
                .post(formBody)
                .build();
        String serrializedJson = httpClient.sendPostRequest(request);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(serrializedJson, new TypeReference<>() {
        });
    }
}
