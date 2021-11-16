package ca.bazlur.visualizer.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class JsonHelper {
    public static <T> String toJson(ObjectMapper objectMapper, T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T fromJson(ObjectMapper objectMapper, String payload, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(payload, clazz);
    }

    public static <T> T fromJson(ObjectMapper objectMapper, String payload, TypeReference<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(payload, clazz);
    }
    public static <T> T fromJsonWithOffsetDateTimeAndDateTimeFormatter(ObjectMapper objectMapper, DateTimeFormatter dateTimeFormatter, String payload, TypeReference<T> clazz) throws JsonProcessingException {
        var simpleModule = new SimpleModule();
        simpleModule.addDeserializer(OffsetDateTime.class, new JsonDeserializer<>() {
            @Override
            public OffsetDateTime deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
                return OffsetDateTime.parse(p.getText(), dateTimeFormatter);
            }
        });
        objectMapper.registerModule(simpleModule);

        return objectMapper.readValue(payload, clazz);
    }
}
