/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.entity;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.kastel.mcse.ardoco.core.common.IdentifierProvider;

/**
 * An entity defines the smallest unit of a trace link.
 */
public abstract sealed class Entity implements Serializable, Comparable<Entity> permits TextEntity, ModelEntity {

    private static final long serialVersionUID = 5916408204883918465L;

    @JsonProperty
    private final String id;

    @JsonProperty
    private String name;

    protected Entity() {
        // Jackson
        this(null);
    }

    /**
     * Creates a new entity with the specified name.
     *
     * @param name the name of the entity to be created
     */
    protected Entity(String name) {
        this(name, IdentifierProvider.createId());
    }

    protected Entity(String name, String id) {
        this.name = name;
        this.id = Objects.requireNonNull(id);
    }

    public String getId() {
        return this.id;
    }

    /**
     * Returns the entity's name.
     *
     * @return the entity's name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Entity other)) {
            return false;
        }
        return Objects.equals(this.id, other.id) && Objects.equals(this.name, other.name);
    }

    @Override
    public int compareTo(Entity o) {
        if (this.equals(o)) {
            return 0;
        }
        return this.id.compareTo(o.id);
    }

}
