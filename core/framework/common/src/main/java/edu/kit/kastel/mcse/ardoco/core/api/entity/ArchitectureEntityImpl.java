/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.core.api.entity;

import java.io.Serial;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;

/**
 * Implementation of an architecture entity, representing a concrete instance in the architecture model.
 */
public final class ArchitectureEntityImpl extends ArchitectureEntity {

    @Serial
    private static final long serialVersionUID = 9185325416212743266L;

    private final String fullName;
    private final String fullType;
    private final MutableList<String> names;
    private final MutableList<String> types;
    private final String uid;

    /**
     * Creates a new instance.
     *
     * @param name name of the instance
     * @param type type of the instance
     * @param uid  unique identifier of the instance needed for trace linking
     */
    public ArchitectureEntityImpl(String name, String type, String uid) {
        super(name, uid);

        String splitName = CommonUtilities.splitCases(name);
        this.names = Lists.mutable.with(splitName.split(" "));
        if (this.names.size() > 1) {
            this.names.add(name);
        }

        String splitType = CommonUtilities.splitCases(type);
        this.types = Lists.mutable.with(splitType.split(" "));
        if (this.types.size() > 1) {
            this.types.add(type);
        }
        this.uid = uid;
        this.fullName = name;
        this.fullType = type;
    }

    @Override
    public String getName() {
        return this.fullName;
    }

    @Override
    public Optional<String> getType() {
        return Optional.of(this.fullType);
    }

    @Override
    public Optional<ImmutableList<String>> getTypeParts() {
        return Optional.of(this.types.toImmutable());
    }

    @Override
    public String getId() {
        return this.uid;
    }

    @Override
    public String toString() {
        return "Instance [names=" + String.join(", ", this.names) + ", type=" + String.join(", ", this.types) + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fullName, this.fullType, this.uid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ArchitectureEntityImpl other)) {
            return false;
        }
        return Objects.equals(this.fullName, other.fullName) && Objects.equals(this.fullType, other.fullType) && Objects.equals(this.uid, other.uid);
    }
}
