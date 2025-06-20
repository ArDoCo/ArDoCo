/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.recommendationgenerator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.AggregationFunctions;
import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;
import edu.kit.kastel.mcse.ardoco.core.data.Confidence;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

/**
 * This class represents recommended instances. These instances should be contained by the model. The likelihood is measured by the probability. Every
 * recommended instance has a unique name.
 */
public final class RecommendedInstanceImpl extends RecommendedInstance implements Claimant {

    private static final AggregationFunctions GLOBAL_AGGREGATOR = AggregationFunctions.AVERAGE;
    @Serial
    private static final long serialVersionUID = -8664363203506956742L;
    /**
     * Meaning (Weights): <br/> 0,-1,1 => Balanced <br/> 2 => InternalConfidence: 2 / mappingConfidence: 1<br/> -2 => InternalConfidence: 1 / mappingConfidence:
     * 2<br/> ...
     */
    private int weightInternalConfidence = 0;

    private final String type;
    private final String name;
    private Confidence internalConfidence;
    private final MutableList<NounMapping> typeMappings;
    private final MutableList<NounMapping> nameMappings;

    private RecommendedInstanceImpl(String name, String type) {
        super(name, UUID.randomUUID().toString());
        this.type = type;
        this.name = name;
        this.internalConfidence = new Confidence(AggregationFunctions.AVERAGE);
        this.nameMappings = Lists.mutable.empty();
        this.typeMappings = Lists.mutable.empty();
    }

    @Override
    public void onNounMappingDeletion(NounMapping deletedNounMapping, NounMapping replacement) {
        if (this.nameMappings.remove(deletedNounMapping)) {
            if (replacement == null) {
                throw new IllegalArgumentException("Replacement cannot be null");
            }
            this.nameMappings.add(replacement);
        } else if (this.typeMappings.remove(deletedNounMapping)) {
            if (replacement == null) {
                throw new IllegalArgumentException("Replacement cannot be null");
            }
            this.typeMappings.add(replacement);
        }
    }

    /**
     * Creates a new recommended instance.
     *
     * @param name        the name of the instance
     * @param type        the type of the instance
     * @param probability the probability that this instance should be found in the model
     * @param nameNodes   the involved name mappings
     * @param typeNodes   the involved type mappings
     */
    public RecommendedInstanceImpl(String name, String type, Claimant claimant, double probability, ImmutableList<NounMapping> nameNodes,
            ImmutableList<NounMapping> typeNodes) {
        this(name, type);
        this.internalConfidence.addAgentConfidence(claimant, probability);

        this.nameMappings.addAll(nameNodes.castToCollection());
        this.typeMappings.addAll(typeNodes.castToCollection());
    }

    private static double calculateMappingProbability(ImmutableList<NounMapping> nameMappings, ImmutableList<NounMapping> typeMappings) {
        var highestNameProbability = nameMappings.collectDouble(nm -> nm.getProbabilityForKind(MappingKind.NAME)).maxIfEmpty(0);
        var highestTypeProbability = typeMappings.collectDouble(nm -> nm.getProbabilityForKind(MappingKind.TYPE)).maxIfEmpty(0);

        return CommonUtilities.rootMeanSquare(highestNameProbability, highestTypeProbability);
    }

    /**
     * Returns the involved name mappings.
     *
     * @return the name mappings of this recommended instance
     */
    @Override
    public ImmutableList<NounMapping> getNameMappings() {
        return Lists.immutable.withAll(this.nameMappings);
    }

    /**
     * Returns the involved type mappings.
     *
     * @return the type mappings of this recommended instance
     */
    @Override
    public ImmutableList<NounMapping> getTypeMappings() {
        return Lists.immutable.withAll(this.typeMappings);
    }

    /**
     * Returns the probability being an instance of the model.
     *
     * @return the probability to be found in the model
     */
    @Override
    public double getProbability() {
        var mappingProbability = RecommendedInstanceImpl.calculateMappingProbability(this.getNameMappings(), this.getTypeMappings());
        var ownProbability = this.internalConfidence.getConfidence();
        List<Double> probabilities = new ArrayList<>();
        probabilities.add(mappingProbability);
        probabilities.add(ownProbability);

        if (Math.abs(this.weightInternalConfidence) > 1) {
            var element = this.weightInternalConfidence > 0 ? ownProbability : mappingProbability;
            for (int i = 0; i < Math.abs(this.weightInternalConfidence) - 1; i++) {
                probabilities.add(element);
            }
        }

        return RecommendedInstanceImpl.GLOBAL_AGGREGATOR.applyAsDouble(probabilities);
    }

    /**
     * Adds a name and type mapping to this recommended instance.
     *
     * @param nameMapping the name mapping to add
     * @param typeMapping the type mapping to add
     */
    @Override
    public void addMappings(NounMapping nameMapping, NounMapping typeMapping) {
        this.addName(nameMapping);
        this.addType(typeMapping);
    }

    /**
     * Adds name and type mappings to this recommended instance.
     *
     * @param nameMapping the name mappings to add
     * @param typeMapping the type mappings to add
     */
    @Override
    public void addMappings(ImmutableList<NounMapping> nameMapping, ImmutableList<NounMapping> typeMapping) {
        nameMapping.forEach(this::addName);
        typeMapping.forEach(this::addType);
    }

    /**
     * Adds a name mapping to this recommended instance.
     *
     * @param nameMapping the name mapping to add
     */
    @Override
    public void addName(NounMapping nameMapping) {
        if (this.nameMappings.contains(nameMapping)) {
            return;
        }
        this.nameMappings.add(nameMapping);
    }

    /**
     * Adds a type mapping to this recommended instance.
     *
     * @param typeMapping the type mapping to add
     */
    @Override
    public void addType(NounMapping typeMapping) {
        if (this.typeMappings.contains(typeMapping)) {
            return;
        }
        this.typeMappings.add(typeMapping);
    }

    /**
     * Returns the type as string from this recommended instance.
     *
     * @return the type as string
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Returns the name as string from this recommended instance.
     *
     * @return the name as string
     */
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addProbability(Claimant claimant, double probability) {
        this.internalConfidence.addAgentConfidence(claimant, probability);
    }

    @Override
    public ImmutableSortedSet<Integer> getSentenceNumbers() {
        MutableSortedSet<Integer> sentenceNos = this.getNameMappings().flatCollect(nm -> nm.getWords().collect(Word::getSentenceNumber)).toSortedSet();
        return sentenceNos.toImmutableSortedSet();
    }

    @Override
    public String toString() {
        var separator = "\n\t\t\t\t\t";
        MutableList<String> typeNodeVals = Lists.mutable.empty();
        for (NounMapping typeMapping : this.typeMappings) {
            typeNodeVals.add(typeMapping.toString());
        }

        MutableList<String> nameNodeVals = Lists.mutable.empty();
        for (NounMapping nameMapping : this.nameMappings) {
            nameNodeVals.add(nameMapping.toString());
        }
        return "RecommendationInstance [" + " name=" + this.name + ", type=" + this.type + ", probability=" + this.getProbability() + //
                ", mappings:]= " + separator + String.join(separator, nameNodeVals) + separator + String.join(separator, typeNodeVals) + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RecommendedInstanceImpl other)) {
            return false;
        }
        return Objects.equals(this.name, other.name) && Objects.equals(this.type, other.type);
    }

    @Override
    public int compareTo(Entity o) {
        if (this == o) {
            return 0;
        }
        if (o instanceof RecommendedInstance ri) {
            return Comparator.comparing(RecommendedInstance::getName).thenComparing(RecommendedInstance::getType).compare(this, ri);
        }
        return super.compareTo(o);
    }

    @Override
    public ImmutableList<Claimant> getClaimants() {
        return Lists.immutable.withAll(this.internalConfidence.getClaimants());
    }

}
