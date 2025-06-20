/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.aggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.Confidence;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.computationtree.AggregationNode;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.computationtree.Node;

/**
 * Aggregates by using the average. Can use weights to calculate the weighted
 * average.
 */
public class Average extends ConfidenceAggregator {

    private final List<Double> weights;

    private Average() {
        weights = null;
    }

    private Average(List<Double> weights) {
        if (weights.isEmpty()) {
            throw new IllegalArgumentException("The number of weights must not be zero");
        }
        for (double weight : weights) {
            if (weight < 0) {
                throw new IllegalArgumentException("Weights must not be smaller than 0");
            }
        }
        this.weights = new ArrayList<>(weights);
    }

    /**
     * Returns a new aggregation node with the specified children nodes to be aggregated. The aggregation function aggregates by using
     * the weighted average with the specified weights. The number of children nodes and weights
     * must each be at least one, the number of children nodes
     * must be equal to the number of the weights and the weights must not be negative, or an {@code IllegalArgumentException} gets
     * thrown.
     *
     * @param weights  the weights that are used for the weighted average
     * @param children the node's children to be aggregated
     * @throws IllegalArgumentException if the number of children nodes or weights is zero or
     *                                  if the number of weights is not equal to the
     *                                  number of children nodes or if a weight is negative
     */
    public static AggregationNode getAverageNode(List<Double> weights, List<Node> children) {
        if (weights.size() != children.size()) {
            throw new IllegalArgumentException("The number of weights is not equal to the number of children nodes");
        }
        Average average = new Average(weights);
        return new AggregationNode(average, children);
    }

    @Override
    protected Confidence aggregateConfidences(List<Confidence> confidences) {
        double average = 0.0;
        double weightSum = 0;
        for (int i = 0; i < confidences.size(); i++) {
            Confidence confidence = confidences.get(i);
            if (confidence.hasValue()) {
                average += (getWeight(i) * confidence.getValue());
            }
            weightSum += getWeight(i);
        }
        if (0 == average) {
            return new Confidence();
        }
        return new Confidence(average / weightSum);
    }

    private double getWeight(int index) {
        if (weights.isEmpty()) {
            return 1.0;
        }
        return weights.get(index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), weights);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Average other = (Average) obj;
        return Objects.equals(weights, other.weights);
    }

    @Override
    public String toString() {
        String weightsString = "";
        if (weights != null) {
            weightsString = "-" + weights;
        }
        return "Average" + weightsString;
    }
}
