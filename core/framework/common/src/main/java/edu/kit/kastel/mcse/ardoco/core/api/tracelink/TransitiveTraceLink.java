/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.tracelink;

import java.io.Serial;
import java.util.Objects;
import java.util.Optional;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;

/**
 * A trace link that represents a transitive relationship between two entities through an intermediate entity.
 * This class combines two trace links to create a single trace link from the first endpoint of the first link
 * to the second endpoint of the second link.
 *
 * @param <A> the type of the first entity
 * @param <B> the type of the second entity
 */
public final class TransitiveTraceLink<A extends Entity, B extends Entity> extends TraceLink<A, B> {

    @Serial
    private static final long serialVersionUID = 3781827633038556211L;

    private final TraceLink<A, ?> firstTraceLink;
    private final TraceLink<?, B> secondTraceLink;

    private TransitiveTraceLink(TraceLink<A, ?> firstTraceLink, TraceLink<?, B> secondTraceLink) {
        super(firstTraceLink.getFirstEndpoint(), secondTraceLink.getSecondEndpoint());
        this.firstTraceLink = firstTraceLink;
        this.secondTraceLink = secondTraceLink;
    }

    public static <A extends Entity, B extends Entity> Optional<TransitiveTraceLink<A, B>> createTransitiveTraceLink(
            TraceLink<A, ? extends Entity> firstTraceLink, TraceLink<? extends Entity, B> secondTraceLink) {
        if (TransitiveTraceLink.isValidTransitiveTraceLink(firstTraceLink, secondTraceLink)) {
            return Optional.of(new TransitiveTraceLink<>(firstTraceLink, secondTraceLink));
        }
        return Optional.empty();
    }

    public static boolean isValidTransitiveTraceLink(TraceLink<?, ?> firstTraceLink, TraceLink<?, ?> secondTraceLink) {
        var secondEndpointOfFirstTl = firstTraceLink.getSecondEndpoint().getId();
        var firstEndpointOfSecondTl = secondTraceLink.getFirstEndpoint().getId();
        return secondEndpointOfFirstTl.equals(firstEndpointOfSecondTl);
    }

    public TraceLink<A, ?> getFirstTraceLink() {
        return this.firstTraceLink;
    }

    public TraceLink<?, B> getSecondTraceLink() {
        return this.secondTraceLink;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.asPair());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TransitiveTraceLink<?, ?> other)) {
            return false;
        }
        return Objects.equals(this.getFirstTraceLink(), other.getFirstTraceLink()) && //
                Objects.equals(this.getSecondTraceLink(), other.getSecondTraceLink()) && //
                Objects.equals(this.asPair(), other.asPair());
    }
}
