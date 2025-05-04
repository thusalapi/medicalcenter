package com.isnoc.medicalcenter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * Utility class for converting between JSON strings and objects
 */
@Component
public class JsonConverter {

    private final ObjectMapper objectMapper;

    public JsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Convert a JSON string to a JsonNode object
     * 
     * @param json JSON string
     * @return JsonNode object
     */
    public JsonNode fromString(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON: " + json, e);
        }
    }

    /**
     * Convert an object to a JsonNode
     * 
     * @param object Object to convert
     * @return JsonNode representation of the object
     */
    public JsonNode fromObject(Object object) {
        return objectMapper.valueToTree(object);
    }

    /**
     * Convert a JsonNode to a JSON string
     * 
     * @param jsonNode JsonNode object
     * @return JSON string
     */
    public String toString(JsonNode jsonNode) {
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JsonNode to string", e);
        }
    }

    /**
     * Convert a JsonNode to an object of the specified class
     * 
     * @param jsonNode JsonNode object
     * @param valueType Class to convert to
     * @return Object of the specified class
     */
    public <T> T toObject(JsonNode jsonNode, Class<T> valueType) {
        try {
            return objectMapper.treeToValue(jsonNode, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JsonNode to object", e);
        }
    }

    /**
     * Validate that a JSON string conforms to a JSON schema
     * 
     * @param json JSON string to validate
     * @param schema JSON schema to validate against
     * @return true if valid, false otherwise
     */
    public boolean validateAgainstSchema(String json, String schema) {
        // Implementation for JSON schema validation
        // This would typically use a library like json-schema-validator
        // For now, returning true as a placeholder
        return true;
    }
}