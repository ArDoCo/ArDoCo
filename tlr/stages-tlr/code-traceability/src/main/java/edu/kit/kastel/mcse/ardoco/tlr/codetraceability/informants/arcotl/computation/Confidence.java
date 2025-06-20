/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The calculated confidence of an endpoint tuple. The confidence has a value
 * between 0 and 1 if a value exists. The value represents how likely a trace
 * link between the two specific endpoints of the endpoint tuple would be
 * correct. A value of 0 indicates that a trace link likely would be incorrect
 * and a value of 1 indicates that a trace link likely would be correct. The
 * calculated confidence can also not have a value, as not every computation
 * node calculates a value for each endpoint tuple. A nonexistent value
 * indicates that the confidence has been calculated, but the calculation didn't
 * result in a value. A nonexistent value does not make any statement about how
 * likely a trace link between the two specific endpoints would be correct.
 */
public class Confidence implements Comparable<Confidence> {

    private final Double confidenceValue;

    /**
     * Creates a new confidence that doesn't have a value.
     */
    public Confidence() {
        confidenceValue = null;
    }

    /**
     * Creates a new confidence with the specified value. The value must be between
     * 0 and 1, or an {@code IllegalArgumentException} gets thrown.
     *
     * @param confidenceValue the value of the confidence to be created, must be
     *                        between 0 and 1
     * @throws IllegalArgumentException if the specified value is smaller than 0 or
     *                                  bigger than 1
     */
    public Confidence(double confidenceValue) {
        if (!(confidenceValue >= 0 && confidenceValue <= 1)) {
            throw new IllegalArgumentException("Confidence value must not be smaller than 0 or bigger than 1");
        }
        this.confidenceValue = confidenceValue;
    }

    /**
     * Returns the value of this confidence if it exists, otherwise throws an exception.
     *
     * @return the value of this confidence
     */
    public double getValue() {
        if (confidenceValue == null) {
            throw new NoSuchElementException("Confidence has no value");
        }
        return confidenceValue;
    }

    /**
     * Returns true if and only if this confidence has a value.
     *
     * @return true if this confidence has a value; false otherwise
     */
    public boolean hasValue() {
        return confidenceValue != null;
    }

    @Override
    public int compareTo(Confidence other) {
        if (equals(other)) {
            return 0;
        }
        if (!hasValue()) {
            return -1;
        }
        if (!other.hasValue()) {
            return 1;
        }
        if (getValue() < other.getValue()) {
            return -1;
        }
        return 1;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(confidenceValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Confidence other)) {
            return false;
        }
        return Objects.equals(confidenceValue, other.confidenceValue);
    }

    @Override
    public String toString() {
        if (confidenceValue != null) {
            return Double.toString(confidenceValue);
        }
        return "no value";
    }
}
