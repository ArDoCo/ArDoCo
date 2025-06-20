/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a computational object in the code model.
 * Serves as a base for control elements.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = ControlElement.class, name = "ControlElement") })
@JsonTypeName("ComputationalObject")
public sealed class ComputationalObject extends CodeItem permits ControlElement {

    @Serial
    private static final long serialVersionUID = -6879811567216500291L;

    /**
     * Default constructor for Jackson.
     */
    ComputationalObject() {
        // Jackson
    }

    /**
     * Creates a new computational object with the specified name.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the computational object
     */
    public ComputationalObject(CodeItemRepository codeItemRepository, String name) {
        super(codeItemRepository, name);
    }

}
