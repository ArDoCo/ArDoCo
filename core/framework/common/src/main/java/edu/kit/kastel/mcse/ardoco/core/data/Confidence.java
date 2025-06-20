/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.common.AggregationFunctions;
import edu.kit.kastel.mcse.ardoco.core.common.tuple.Triple;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

/**
 * Represents a confidence value for a result, aggregating confidences from multiple claimants using a specified aggregation function.
 */
@Deterministic
public final class Confidence implements Comparable<Confidence>, Serializable {

    @Serial
    private static final long serialVersionUID = 4307327201754195030L;

    private final AggregationFunctions confidenceAggregator;

    // Claimant, Confidence, MethodName
    private List<Triple<Claimant, Double, String>> agentConfidences;

    /**
     * Constructor for the confidence with a given aggregator function.
     *
     * @param confidenceAggregator the aggregation function for the confidence
     */
    public Confidence(AggregationFunctions confidenceAggregator) {
        this.confidenceAggregator = confidenceAggregator;
        this.agentConfidences = new ArrayList<>();
    }

    /**
     * Constructor for the confidence with a given aggregator function and an initial claimant with a certain probability (confidence).
     *
     * @param claimant             the claimant
     * @param probability          the probability
     * @param confidenceAggregator the aggregation function
     */
    public Confidence(Claimant claimant, double probability, AggregationFunctions confidenceAggregator) {
        this(confidenceAggregator);
        this.addAgentConfidence(claimant, probability);
    }

    private Confidence(AggregationFunctions confidenceAggregator, List<Triple<Claimant, Double, String>> agentConfidence) {
        this(confidenceAggregator);
        this.agentConfidences = new ArrayList<>(agentConfidence);
    }

    /**
     * Returns the set of claimants that contribute to this confidence
     *
     * @return the claimants
     */
    public Set<Claimant> getClaimants() {
        Set<Claimant> identitySet = Collections.newSetFromMap(new IdentityHashMap<>());
        for (var confidence : this.agentConfidences) {
            identitySet.add(confidence.first());
        }
        return identitySet;
    }

    /**
     * Creates a copy of this confidence object.
     *
     * @return a new instance of Confidence with the same properties
     */
    public Confidence createCopy() {
        return new Confidence(this.confidenceAggregator, this.agentConfidences);
    }

    /**
     * Add a confidence of an agent ({@link Claimant}.
     *
     * @param claimant   the claimant
     * @param confidence the confidence
     */
    public void addAgentConfidence(Claimant claimant, double confidence) {
        String method = this.getMethodInClaimant(claimant);
        this.agentConfidences.add(new Triple<>(claimant, confidence, method));
    }

    private String getMethodInClaimant(Claimant claimant) {
        var trace = new Exception().getStackTrace();
        for (var te : trace) {
            if (te.getClassName().equals(claimant.getClass().getName())) {
                return te.getMethodName();
            }
        }
        return "Unknown Method";
    }

    @Override
    public int compareTo(Confidence o) {
        if (this.equals(o)) {
            return 0;
        }
        return Double.compare(this.getConfidence(), o.getConfidence());
    }

    @Override
    public String toString() {
        return "Confidence{" + this.confidenceAggregator + "=>" + this.getConfidence() + '}';
    }

    /**
     * Returns the (aggregated) confidence value
     *
     * @return the (aggregated) confidence value
     */
    public double getConfidence() {
        if (this.agentConfidences.isEmpty()) {
            return 0;
        }
        if (this.confidenceAggregator == AggregationFunctions.ROLLING_AVERAGE) {
            // No aggregate
            return this.confidenceAggregator.applyAsDouble(this.agentConfidences.stream().map(Triple::second).toList());
        }
        var groupAggregator = AggregationFunctions.MAX;
        var claimantGroupings = this.agentConfidences.stream().collect(Collectors.groupingBy(Triple::first)).values();
        var claimantConfidences = claimantGroupings.stream().map(l -> l.stream().map(Triple::second).toList()).map(groupAggregator::applyAsDouble).toList();
        return this.confidenceAggregator.applyAsDouble(claimantConfidences);
    }

    /**
     * Merges two confidences two one w.r.t. the aggregators
     *
     * @param a                first confidence
     * @param b                second confidence
     * @param globalAggregator aggregator for merging different claimant confidences
     * @param localAggregator  aggregator for merging confidences of the same claimant
     * @return the combined confidence
     */
    public static Confidence merge(Confidence a, Confidence b, AggregationFunctions globalAggregator, AggregationFunctions localAggregator) {
        var result = new Confidence(globalAggregator);

        for (var aConf : a.agentConfidences) {
            var bConf = b.agentConfidences.stream().filter(p -> p.first().equals(aConf.first())).findFirst().orElse(null);
            if (bConf == null) {
                result.addAgentConfidence(aConf.first(), aConf.second());
            } else {
                result.addAgentConfidence(aConf.first(), localAggregator.applyAsDouble(List.of(aConf.second(), bConf.second())));
            }
        }

        for (var bConf : b.agentConfidences) {
            var aConf = a.agentConfidences.stream().anyMatch(p -> p.first().equals(bConf.first()));
            if (!aConf) {
                result.addAgentConfidence(bConf.first(), bConf.second());
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.agentConfidences, this.confidenceAggregator);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        var other = (Confidence) obj;
        return Objects.equals(this.agentConfidences, other.agentConfidences) && this.confidenceAggregator == other.confidenceAggregator;
    }

    public void addAllConfidences(Confidence other) {
        this.agentConfidences.addAll(other.agentConfidences);
    }
}
