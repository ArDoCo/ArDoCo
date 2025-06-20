/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureComponent;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureItem;

/**
 * Represents a model containing only architecture components.
 * Provides access to architecture components and their type identifiers.
 */
public final class ArchitectureComponentModel extends ArchitectureModel {

    private final ArchitectureModel architectureModel;

    /**
     * Creates a new ArchitectureComponentModel.
     *
     * @param architectureModel the architecture model
     */
    public ArchitectureComponentModel(ArchitectureModel architectureModel) {
        this.architectureModel = Objects.requireNonNull(architectureModel);
    }

    /**
     * Returns the architecture components in this model.
     *
     * @return list of architecture components
     */
    @Override
    public List<ArchitectureComponent> getContent() {
        List<ArchitectureComponent> entities = new ArrayList<>();
        for (ArchitectureItem entity : architectureModel.getContent()) {
            if (entity instanceof ArchitectureComponent component) {
                entities.add(component);
            }
        }
        return entities;
    }

    /**
     * Returns the endpoints of this model.
     *
     * @return list of model entities
     */
    @Override
    public List<? extends ArchitectureItem> getEndpoints() {
        return this.getContent();
    }

    /**
     * Returns the metamodel of this model.
     *
     * @return the metamodel
     */
    @Override
    public Metamodel getMetamodel() {
        return Metamodel.ARCHITECTURE_ONLY_COMPONENTS;
    }

    /**
     * Returns the type identifiers of the architecture components in this model.
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
}
