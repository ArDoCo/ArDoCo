/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.core.api.entity;

import java.io.Serial;
import java.util.Optional;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;

/**
 * Abstract base class for entities that are part of a model, such as architecture or code entities.
 */
public abstract sealed class ModelEntity extends Entity permits ArchitectureEntity, CodeEntity {

    @Serial
    private static final long serialVersionUID = -3169247087879811254L;

    /**
     * Default constructor for deserialization frameworks.
     */
    protected ModelEntity() {
        // Jackson
        super(null);
    }

    /**
     * Creates a new entity with the specified name.
     *
     * @param name the name of the entity to be created
     */
    protected ModelEntity(String name) {
        super(name);
    }

    /**
     * Creates a new entity with the specified name and id.
     *
     * @param name the name of the entity
     * @param id   the unique identifier
     */
    protected ModelEntity(String name, String id) {
        super(name, id);
    }

    /**
     * Returns the type of the entity, if available.
     *
     * @return an Optional containing the type, or empty if not available
     */
    public abstract Optional<String> getType();

    /**
     * Returns the type parts of the entity, if available.
     *
     * @return an Optional containing the type parts, or empty if not available
     */
    public abstract Optional<ImmutableList<String>> getTypeParts();

    /**
     * Returns the parts of the entity's name.
     *
     * @return an immutable list of name parts
     */
    public ImmutableList<String> getNameParts() {
        return splitIdentifierIntoParts(this.getName()).toImmutable();
    }

    protected MutableList<String> splitIdentifierIntoParts(String identifier) {
        String splitName = CommonUtilities.splitCases(identifier);
        var names = Lists.mutable.with(splitName.split(" "));
        if (names.size() > 1) {
            names.add(identifier);
        }
        return names;
    }

}
