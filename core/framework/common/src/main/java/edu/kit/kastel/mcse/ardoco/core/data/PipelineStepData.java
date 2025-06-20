/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.data;

import java.io.Serializable;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.common.JsonHandling;

/**
 * Interface for data used in pipeline steps, supporting serialization and conversion between data types.
 */
public interface PipelineStepData extends Serializable {

    /**
     * Logger for pipeline step data operations.
     */
    Logger logger = LoggerFactory.getLogger(PipelineStepData.class);

    /**
     * Converts this data into a given {@link PipelineStepData} class and returns the converted class, packed in an
     * {@link Optional}. If conversion is impossible, returns an empty {@link Optional}.
     *
     * @param clazz class this should be converted to
     * @param <T>   {@link PipelineStepData} type
     * @return Optional containing the converted class or that is empty, if conversion failed.
     */
    default <T extends PipelineStepData> Optional<T> asPipelineStepData(Class<T> clazz) {
        if (!clazz.isAssignableFrom(this.getClass())) {
            return Optional.empty();
        }
        return Optional.of(clazz.cast(this));
    }

    /**
     * Serializes this data into a JSON string.
     *
     * @return JSON string representation of this data or null if serialization fails.
     */
    default String serialize() {
        var oom = JsonHandling.createObjectMapper();
        try {
            return oom.writeValueAsString(this);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Deserializes the given JSON string into an instance of this data type.
     *
     * @param data JSON string to deserialize
     * @return Deserialized instance of this data type or null if deserialization fails.
     */
    default PipelineStepData deserialize(String data) {
        var oom = JsonHandling.createObjectMapper();
        try {
            return oom.readValue(data, this.getClass());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
