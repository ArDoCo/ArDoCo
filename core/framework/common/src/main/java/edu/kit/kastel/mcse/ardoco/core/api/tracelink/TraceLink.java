/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.tracelink;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;
import edu.kit.kastel.mcse.ardoco.core.common.tuple.Pair;

/**
 * Abstract base class for trace links between two entities. A {@link TraceLink} connects two endpoints of types {@code E1} and {@code E2}.
 *
 * @param <E1> the type of the first entity
 * @param <E2> the type of the second entity
 */
public abstract class TraceLink<E1 extends Entity, E2 extends Entity> implements Serializable {
    @Serial
    private static final long serialVersionUID = -2363643561606530433L;

    private final E1 endpoint1;
    private final E2 endpoint2;

    protected TraceLink(E1 firstEndpoint, E2 secondEndpoint) {
        this.endpoint1 = firstEndpoint;
        this.endpoint2 = secondEndpoint;
    }

    /**
     * Returns the endpoint tuple of this trace link.
     *
     * @return the endpoint tuple of this trace link
     */
    public Pair<E1, E2> asPair() {
        return new Pair<>(this.endpoint1, this.endpoint2);
    }

    public E1 getFirstEndpoint() {
        return this.endpoint1;
    }

    public E2 getSecondEndpoint() {
        return this.endpoint2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.endpoint1, this.endpoint2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TraceLink<?, ?> other)) {
            return false;
        }
        return Objects.equals(this.endpoint1, other.endpoint1) && Objects.equals(this.endpoint2, other.endpoint2);
    }

    @Override
    public String toString() {
        return this.asPair().toString();
    }
}
