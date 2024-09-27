package com.application.apigateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
public class Utility {
    public static final String notificationTopic = "NotificationTopic";
    public static final String notificationConsumerGrpId = "NotificationConsumerGrpId";


    public static String getJsonString(Object object) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return getJsonStringObjectMapper(object);
        }
    }

    public static String getJsonStringObjectMapper(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
