/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.codetraceability;

import java.util.Collection;

import org.eclipse.collections.api.set.ImmutableSet;

import edu.kit.kastel.mcse.ardoco.core.api.entity.ArchitectureEntity;
import edu.kit.kastel.mcse.ardoco.core.api.entity.ModelEntity;
import edu.kit.kastel.mcse.ardoco.core.api.text.SentenceEntity;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.data.PipelineStepData;

/**
 * State interface for code traceability.
 * Stores trace links between sentences, architecture, and code entities.
 */
@Deterministic
public interface CodeTraceabilityState extends PipelineStepData {
    /**
     * The ID for this state.
     */
    String ID = "CodeTraceabilityState";

    /**
     * Add a collection of trace links between sentences and code entities to this state.
     *
     * @param traceLinks the trace links to add
     * @return whether the operation was successful
     */
    boolean addSadCodeTraceLinks(Collection<? extends TraceLink<SentenceEntity, ? extends ModelEntity>> traceLinks);

    /**
     * Return a set of stored trace links between sentences and code entities.
     *
     * @return set of stored trace links
     */
    ImmutableSet<TraceLink<SentenceEntity, ? extends ModelEntity>> getSadCodeTraceLinks();

    /**
     * Add a collection of trace links between architecture and code entities to this state.
     *
     * @param traceLinks the trace links to add
     * @return whether the operation was successful
     */
    boolean addSamCodeTraceLinks(Collection<? extends TraceLink<? extends ArchitectureEntity, ? extends ModelEntity>> traceLinks);

    /**
     * Return a set of stored trace links between architecture and code entities.
     *
     * @return set of stored trace links
     */
    ImmutableSet<TraceLink<? extends ArchitectureEntity, ? extends ModelEntity>> getSamCodeTraceLinks();
}
