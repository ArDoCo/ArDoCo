/* Licensed under MIT 2024-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.tracelink;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;

/**
 * A trace link representing a transitive relationship between two entities over multiple intermediate entities.
 * This class combines a sequence of trace links to create a single link from the first endpoint to the last endpoint.
 *
 * @param <A> the type of the first entity
 * @param <B> the type of the last entity
 */
public final class MultiHopTransitiveTraceLink<A extends Entity, B extends Entity> extends TraceLink<A, B> {

    @Serial
    private static final long serialVersionUID = 850583103292008395L;

    private static final Logger logger = LoggerFactory.getLogger(MultiHopTransitiveTraceLink.class);

    private final TraceLink<A, ?> firstTraceLink;
    private final TraceLink<?, B> lastTraceLink;

    private final List<? extends TraceLink<?, ?>> allLinks;

    private MultiHopTransitiveTraceLink(TraceLink<A, ?> firstTraceLink, TraceLink<?, B> lastTraceLink, List<? extends TraceLink<?, ?>> allLinks) {
        super(firstTraceLink.getFirstEndpoint(), lastTraceLink.getSecondEndpoint());
        this.firstTraceLink = firstTraceLink;
        this.lastTraceLink = lastTraceLink;
        this.allLinks = allLinks;
    }

    public static <A extends Entity, B extends Entity> Optional<MultiHopTransitiveTraceLink<A, B>> createTransitiveTraceLink(TraceLink<A, ?> firstTraceLink,
            TraceLink<?, B> lastTraceLink, List<? extends TraceLink<?, ?>> intermediateLinks) {

        if (intermediateLinks == null || intermediateLinks.isEmpty()) {
            throw new IllegalArgumentException("Please use " + TransitiveTraceLink.class.getSimpleName());
        }

        Objects.requireNonNull(firstTraceLink);
        Objects.requireNonNull(lastTraceLink);

        List<TraceLink<?, ?>> completeList = new ArrayList<>();
        completeList.add(firstTraceLink);
        completeList.addAll(intermediateLinks);
        completeList.add(lastTraceLink);

        for (int i = 1; i < completeList.size(); i++) {
            if (!TransitiveTraceLink.isValidTransitiveTraceLink(completeList.get(i - 1), completeList.get(i))) {
                MultiHopTransitiveTraceLink.logger.debug("Invalid transition: {} -> {}", completeList.get(i - 1), completeList.get(i));
                return Optional.empty();
            }
        }

        return Optional.of(new MultiHopTransitiveTraceLink<>(firstTraceLink, lastTraceLink, completeList));
    }

    public TraceLink<A, ?> getFirstTraceLink() {
        return this.firstTraceLink;
    }

    public TraceLink<?, B> getLastTraceLink() {
        return this.lastTraceLink;
    }

    public List<? extends TraceLink<?, ?>> getAllLinks() {
        return new ArrayList<TraceLink<?, ?>>(this.allLinks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.allLinks);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MultiHopTransitiveTraceLink<?, ?> other)) {
            return false;
        }
        return Objects.equals(this.allLinks, other.allLinks);
    }
}
