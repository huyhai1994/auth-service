package com.homelab.authservice.shared.json;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JacksonUtils {

    private final ObjectMapper objectMapper;

    public <T> T convertFromJson(
            String json,
            TypeReference<T> typeReference
    ) {
        return objectMapper.readValue(json, typeReference);
    }

    public <T> String convertObjectToJson(T object) {
        return objectMapper.writeValueAsString(object);
    }
}
