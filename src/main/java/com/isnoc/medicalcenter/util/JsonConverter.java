package com.isnoc.medicalcenter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isnoc.medicalcenter.exception.InvalidJsonException;
import org.springframework.stereotype.Component;

/**
 * Utility class to handle JSON conversion and manipulation
 */
@Component
public class JsonConverter {

    private final ObjectMapper objectMapper;

    public JsonConverter() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Convert a string to a JsonNode
     * 
     * @param jsonString JSON string to convert
     * @return JsonNode representation
     * @throws InvalidJsonException if the string is not valid JSON
     */
    public JsonNode parseJson(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException("Invalid JSON format: " + e.getMessage());
        }
    }

    /**
     * Convert an object to a JsonNode
     * 
     * @param object Object to convert
     * @return JsonNode representation
     */
    public JsonNode toJsonNode(Object object) {
        return objectMapper.valueToTree(object);
    }

    /**
     * Convert a JsonNode to a specific type
     * 
     * @param <T> Target type
     * @param jsonNode JsonNode to convert
     * @param valueType Class of target type
     * @return Object of target type
     * @throws InvalidJsonException if conversion fails
     */
    public <T> T fromJsonNode(JsonNode jsonNode, Class<T> valueType) {
        try {
            return objectMapper.treeToValue(jsonNode, valueType);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException("Error converting JSON to " + valueType.getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Convert JsonNode to a JSON string
     * 
     * @param jsonNode JsonNode to convert
     * @return JSON string representation
     * @throws InvalidJsonException if conversion fails
     */
    public String toJsonString(JsonNode jsonNode) {
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException("Error converting JsonNode to string: " + e.getMessage());
        }
    }

    /**
     * Merge two JSON objects
     * 
     * @param mainNode Primary JSON object
     * @param updateNode JSON object with values to merge in
     * @return Merged JSON object
     */
    public JsonNode mergeJsonNodes(JsonNode mainNode, JsonNode updateNode) {
        if (!(mainNode instanceof ObjectNode) || !(updateNode instanceof ObjectNode)) {
            throw new InvalidJsonException("Both nodes must be object nodes for merging");
        }
        
        ObjectNode result = ((ObjectNode) mainNode).deepCopy();
        updateNode.fields().forEachRemaining(field -> 
            result.set(field.getKey(), field.getValue())
        );
        
        return result;
    }

    /**
     * Create an empty JSON object
     * 
     * @return Empty ObjectNode
     */
    public ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    /**
     * Get the ObjectMapper instance for direct use
     * 
     * @return ObjectMapper instance
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}