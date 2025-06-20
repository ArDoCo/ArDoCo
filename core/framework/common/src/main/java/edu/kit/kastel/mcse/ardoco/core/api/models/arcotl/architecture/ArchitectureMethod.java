/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture;

import java.io.Serial;
import java.util.Optional;

import org.eclipse.collections.api.list.ImmutableList;

/**
 * Represents a method in the architecture model.
 * Used as a signature in interfaces or components.
 */
public final class ArchitectureMethod extends ArchitectureItem {

    @Serial
    private static final long serialVersionUID = 6560555992331464264L;

    /**
     * Creates a new ArchitectureMethod.
     *
     * @param name the name of the method
     */
    public ArchitectureMethod(String name) {
        super(name);
    }

    /**
     * Returns the type of this method (currently not specified).
     *
     * @return empty optional
     */
    @Override
    public Optional<String> getType() {
        // Not implemented yet. Methods currently have no specified type.
        return Optional.empty();
    }

    /**
     * Returns the type parts of this method (currently not specified).
     *
     * @return empty optional
     */
    @Override
    public Optional<ImmutableList<String>> getTypeParts() {
        // Not implemented yet. Methods currently have no specified type.
        return Optional.empty();
    }

}
