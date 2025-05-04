package com.isnoc.medicalcenter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JPA Converter to automatically handle conversion between JsonNode and String (for database storage)
 */
@Converter
@Component
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JsonNode to JSON string", e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty()) {
            return objectMapper.createObjectNode(); // Return empty object
        }

        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON string to JsonNode", e);
        }
    }
}