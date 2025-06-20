/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.connectiongenerator;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;

import edu.kit.kastel.mcse.ardoco.core.api.entity.ModelEntity;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.api.text.SentenceEntity;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.configuration.IConfigurable;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

/**
 * State interface for connection generation.
 * Provides access to instance links and trace links.
 */
@Deterministic
public interface ConnectionState extends IConfigurable {

    /**
     * Returns all instance links.
     *
     * @return all instance links
     */
    ImmutableList<TraceLink<RecommendedInstance, ModelEntity>> getInstanceLinks();

    /**
     * Returns a list of trace links that are contained within this connection state.
     *
     * @return list of trace links within this connection state
     */
    default ImmutableSet<TraceLink<SentenceEntity, ModelEntity>> getTraceLinks() {
        MutableSet<TraceLink<SentenceEntity, ModelEntity>> traceLinks = Sets.mutable.empty();
        for (var instanceLink : this.getInstanceLinks()) {
            var textualInstance = instanceLink.getFirstEndpoint();
            for (var nm : textualInstance.getNameMappings()) {
                for (var word : nm.getWords()) {
                    var traceLink = new SadModelTraceLink(word.getSentence(), instanceLink.getSecondEndpoint());
                    traceLinks.add(traceLink);
                }
            }
        }
        return traceLinks.toImmutable();
    }

    /**
     * Adds the connection of a recommended instance and a model instance to the state.
     * If the model instance is already contained by the state it is extended, otherwise a new instance link is created.
     *
     * @param recommendedModelInstance the recommended instance
     * @param modelEntity              the model instance
     * @param claimant                 the claimant
     * @param probability              the probability of the link
     */
    void addToLinks(RecommendedInstance recommendedModelInstance, ModelEntity modelEntity, Claimant claimant, double probability);

    /**
     * Checks if an instance link is already contained by the state.
     *
     * @param instanceLink the given instance link
     * @return true if it is already contained
     */
    boolean isContainedByInstanceLinks(TraceLink<RecommendedInstance, ModelEntity> instanceLink);
}
