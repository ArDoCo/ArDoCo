/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a control element in the code model.
 * Extends {@link ComputationalObject}.
 */
@JsonTypeName("ControlElement")
public final class ControlElement extends ComputationalObject {

    @Serial
    private static final long serialVersionUID = -2733651783905632198L;

    /**
     * Default constructor for Jackson.
     */
    @SuppressWarnings("unused")
    private ControlElement() {
        // Jackson
    }

    /**
     * Creates a new control element with the specified name.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the control element
     */
    public ControlElement(CodeItemRepository codeItemRepository, String name) {
        super(codeItemRepository, name);
    }
}
