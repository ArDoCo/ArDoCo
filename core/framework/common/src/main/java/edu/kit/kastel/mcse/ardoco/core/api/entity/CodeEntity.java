/* Licensed under MIT 2024-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.entity;

import java.io.Serial;
import java.util.Optional;

import org.eclipse.collections.api.list.ImmutableList;

/**
 * Represents an entity that is part of the code model.
 */
public non-sealed class CodeEntity extends ModelEntity {
    @Serial
    private static final long serialVersionUID = 5520572653996476974L;

    /**
     * Creates a new code entity with the specified name.
     *
     * @param name the name of the code entity
     */
    protected CodeEntity(String name) {
        super(name);
    }

    /**
     * Creates a new code entity with the specified name and id.
     *
     * @param name the name of the code entity
     * @param id   the unique identifier
     */
    protected CodeEntity(String name, String id) {
        super(name, id);
    }

    @Override
    public Optional<String> getType() {
        return Optional.empty();
    }

    @Override
    public Optional<ImmutableList<String>> getTypeParts() {
        if (this.getType().isPresent()) {
            return Optional.of(splitIdentifierIntoParts(this.getType().get()).toImmutable());
        }
        return Optional.empty();
    }

}
