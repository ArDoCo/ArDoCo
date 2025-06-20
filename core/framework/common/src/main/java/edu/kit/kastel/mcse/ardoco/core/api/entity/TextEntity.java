/* Licensed under MIT 2024-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.entity;

import java.io.Serial;

/**
 * Represents an entity that is part of the text model.
 */
public non-sealed class TextEntity extends Entity {
    @Serial
    private static final long serialVersionUID = 7693834560590279832L;

    /**
     * Creates a new text entity with the specified name.
     *
     * @param name the name of the text entity
     */
    protected TextEntity(String name) {
        super(name);
    }

    /**
     * Creates a new text entity with the specified name and id.
     *
     * @param name the name of the text entity
     * @param id   the unique identifier
     */
    protected TextEntity(String name, String id) {
        super(name, id);
    }

}
