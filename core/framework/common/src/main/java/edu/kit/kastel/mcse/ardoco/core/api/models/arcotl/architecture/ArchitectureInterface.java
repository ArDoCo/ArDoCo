/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture;

import java.io.Serial;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;

import org.eclipse.collections.api.list.ImmutableList;

/**
 * Represents an interface in the architecture model.
 * Contains method signatures and models interfaces in the software architecture.
 */
public final class ArchitectureInterface extends ArchitectureItem {

    @Serial
    private static final long serialVersionUID = 2232013345166120690L;

    private final SortedSet<ArchitectureMethod> signatures;

    /**
     * Creates a new ArchitectureInterface.
     *
     * @param name       the name of the interface
     * @param id         the unique identifier
     * @param signatures the method signatures of this interface
     */
    public ArchitectureInterface(String name, String id, SortedSet<ArchitectureMethod> signatures) {
        super(name, id);
        this.signatures = signatures;

    }

    /**
     * Returns the method signatures of this interface.
     *
     * @return method signatures
     */
    public SortedSet<ArchitectureMethod> getSignatures() {
        return this.signatures;
    }

    @Override
    public Optional<String> getType() {
        // Not implemented yet. Interfaces currently have no specified type.
        return Optional.empty();
    }

    @Override
    public Optional<ImmutableList<String>> getTypeParts() {
        // Not implemented yet. Interfaces currently have no specified type.
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Interface: " + this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchitectureInterface that) || !super.equals(o)) {
            return false;
        }
        return Objects.equals(this.signatures, that.signatures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.signatures);
    }

}
