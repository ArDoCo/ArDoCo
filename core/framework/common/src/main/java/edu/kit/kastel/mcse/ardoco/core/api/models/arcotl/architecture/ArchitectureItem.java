/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture;

import java.io.Serial;
import java.util.Optional;

import edu.kit.kastel.mcse.ardoco.core.api.entity.ArchitectureEntity;

/**
 * Represents an item in the architecture model.
 * Serves as a base class for architecture model elements.
 */
public abstract sealed class ArchitectureItem extends ArchitectureEntity permits ArchitectureComponent, ArchitectureInterface, ArchitectureMethod {

    @Serial
    private static final long serialVersionUID = -216185356134452192L;

    /**
     * Creates a new architecture item with the specified name.
     *
     * @param name the name of the architecture item
     */
    protected ArchitectureItem(String name) {
        super(name);
    }

    /**
     * Creates a new architecture item with the specified name and identifier.
     *
     * @param name the name of the architecture item
     * @param id   the identifier of the architecture item
     */
    protected ArchitectureItem(String name, String id) {
        super(name, id);
    }

    /**
     * Returns the type of the architecture item as a string.
     *
     * @return the type of the architecture item
     */
    public abstract Optional<String> getType();
}
