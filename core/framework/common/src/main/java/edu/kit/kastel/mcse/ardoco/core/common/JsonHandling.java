/* Licensed under MIT 2024-2025. */
package edu.kit.kastel.mcse.ardoco.core.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Utility class for creating and configuring Jackson ObjectMappers for JSON serialization and deserialization.
 */
public final class JsonHandling {
    private JsonHandling() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * Creates and configures a Jackson ObjectMapper for use with ArDoCo models.
     *
     * @return a configured ObjectMapper
     */
    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(objectMapper.getSerializationConfig()
                .getDefaultVisibilityChecker() //
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY) //
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE) //
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE) //
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
        return objectMapper;
    }
}
