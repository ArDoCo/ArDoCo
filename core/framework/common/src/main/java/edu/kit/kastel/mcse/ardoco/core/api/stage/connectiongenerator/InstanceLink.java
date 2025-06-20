/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.connectiongenerator;

import java.io.Serial;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.entity.ArchitectureEntity;
import edu.kit.kastel.mcse.ardoco.core.api.entity.CodeEntity;
import edu.kit.kastel.mcse.ardoco.core.api.entity.ModelEntity;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.common.AggregationFunctions;
import edu.kit.kastel.mcse.ardoco.core.data.Confidence;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

/**
 * Defines a link between a {@link RecommendedInstance} and a {@link ModelEntity}.
 */
@Deterministic
public final class InstanceLink extends TraceLink<RecommendedInstance, ModelEntity> {

    @Serial
    private static final long serialVersionUID = -8630933950725516269L;
    private final Confidence confidence;

    /**
     * Create a new instance link.
     *
     * @param textualInstance the recommended instance
     * @param entity          the model instance
     */
    public InstanceLink(RecommendedInstance textualInstance, ModelEntity entity) {
        super(textualInstance, entity);
        this.confidence = new Confidence(AggregationFunctions.AVERAGE);
    }

    /**
     * Creates a new instance link with a claimant and probability.
     *
     * @param textualInstance the recommended instance
     * @param entity          the model instance
     * @param claimant        the claimant
     * @param probability     the probability of this link
     */
    public InstanceLink(RecommendedInstance textualInstance, ModelEntity entity, Claimant claimant, double probability) {
        this(textualInstance, entity);
        this.confidence.addAgentConfidence(claimant, probability);
    }

    /**
     * Add confidence to this link.
     *
     * @param claimant   the claimant
     * @param confidence the confidence value to add
     */
    public final void addConfidence(Claimant claimant, double confidence) {
        this.confidence.addAgentConfidence(claimant, confidence);
    }

    /**
     * Returns the probability of the correctness of this link.
     *
     * @return the probability of this link
     */
    public final double getConfidence() {
        return this.confidence.getConfidence();
    }

    /**
     * Returns a string representation of this instance link.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        Set<String> names = new LinkedHashSet<>();
        MutableList<Integer> namePositions = Lists.mutable.empty();
        Set<String> types = new LinkedHashSet<>();
        MutableList<Integer> typePositions = Lists.mutable.empty();

        for (NounMapping nameMapping : this.getFirstEndpoint().getNameMappings()) {
            names.addAll(nameMapping.getSurfaceForms().castToCollection());
            namePositions.addAll(nameMapping.getMappingSentenceNo().castToCollection());
        }
        for (NounMapping typeMapping : this.getFirstEndpoint().getTypeMappings()) {
            types.addAll(typeMapping.getSurfaceForms().castToCollection());
            typePositions.addAll(typeMapping.getMappingSentenceNo().castToCollection());
        }

        String typeInfo;
        switch (this.getSecondEndpoint()) {
            case ArchitectureEntity architectureEntity -> typeInfo = architectureEntity.getType().orElseThrow();
            case CodeEntity ignored -> typeInfo = "";
        }

        return "InstanceMapping [ uid=" + this.getSecondEndpoint().getId() + ", name=" + this.getSecondEndpoint().getName() + //
                ", as=" + String.join(", ", typeInfo) + ", probability=" + this.getConfidence() + ", FOUND: " + //
                this.getFirstEndpoint().getName() + " : " + this.getFirstEndpoint().getType() + ", occurrences= " + //
                "NameVariants: " + names.size() + ": " + names + " sentences{" + Arrays.toString(namePositions.toArray()) + "}" + //
                ", TypeVariants: " + types.size() + ": " + types + "sentences{" + Arrays.toString(typePositions.toArray()) + "}" + "]";
    }

}
