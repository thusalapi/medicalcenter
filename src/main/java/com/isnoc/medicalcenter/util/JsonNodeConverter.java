package com.isnoc.medicalcenter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Converter(autoApply = true) // Automatically apply to JsonNode fields
@Component // Allows ObjectMapper injection
@RequiredArgsConstructor // Injects ObjectMapper via constructor
@Slf4j
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    private final ObjectMapper objectMapper; // Inject Spring's configured ObjectMapper

    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Error converting JsonNode to String", e);
            // Consider throwing a runtime exception or returning a specific error string
            return null;
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readTree(dbData);
        } catch (IOException e) {
            log.error("Error converting String to JsonNode", e);
            // Consider throwing a runtime exception or returning null/empty node
            return null;
        }
    }
}