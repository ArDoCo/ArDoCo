/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureItem;

/**
 * Represents an architecture model that is an AMTL instance.
 * Provides access to architecture items and their type identifiers.
 */
public final class ArchitectureModelWithComponentsAndInterfaces extends ArchitectureModel {

    private final List<ArchitectureItem> content;

    /**
     * Creates a new architecture model that is an AMTL instance.
     *
     * @param content the content of the architecture model
     */
    public ArchitectureModelWithComponentsAndInterfaces(List<ArchitectureItem> content) {
        this.content = content;
    }

    /**
     * Returns the content of the architecture model.
     *
     * @return list of architecture items
     */
    @Override
    public List<ArchitectureItem> getContent() {
        return this.content;
    }

    /**
     * Returns the endpoints of this model.
     *
     * @return list of architecture items
     */
    @Override
    public List<ArchitectureItem> getEndpoints() {
        return this.getContent();
    }

    /**
     * Returns the metamodel of this architecture model.
     *
     * @return the metamodel
     */
    @Override
    public Metamodel getMetamodel() {
        return Metamodel.ARCHITECTURE_WITH_COMPONENTS_AND_INTERFACES;
    }

    /**
     * Returns the type identifiers of the architecture items in this model.
     *
     * @return sorted set of type identifiers
     */
    @Override
    public SortedSet<String> getTypeIdentifiers() {

        SortedSet<String> identifiers = new TreeSet<>();

        for (var entity : getContent()) {
            if (entity.getType().isPresent()) {
                identifiers.add(entity.getType().orElseThrow());
                identifiers.addAll(entity.getTypeParts().orElseThrow().toList());
            }
        }
        return identifiers;

    }

    /**
     * Checks equality with another object.
     *
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchitectureModelWithComponentsAndInterfaces that) || !super.equals(o)) {
            return false;
        }
        return this.content.equals(that.content);
    }

    /**
     * Returns the hash code for this architecture model.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        return 31 * result + this.content.hashCode();
    }

}
