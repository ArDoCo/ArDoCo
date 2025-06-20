/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 * A record representing a pair of two serializable elements.
 *
 * @param <T> the type of the first element
 * @param <U> the type of the second element
 */
public record Pair<T extends Serializable, U extends Serializable>(T first, U second) implements Serializable {

    /**
     * Checks if the pair contains the specified element.
     *
     * @param element the element to check for
     * @return true if the pair contains the element, false otherwise
     */
    public boolean hasElement(Serializable element) {
        return Objects.equals(this.first, element) || Objects.equals(this.second, element);
    }

    /**
     * Returns the other element in the pair when given one element.
     *
     * @param element one of the elements in the pair
     * @return the other element in the pair
     * @throws IllegalArgumentException if the given element is not part of this pair
     */
    public Serializable getOtherElement(Serializable element) {
        if (Objects.equals(this.first, element)) {
            return this.second;
        }
        if (Objects.equals(this.second, element)) {
            return this.first;
        }
        throw new IllegalArgumentException("Unknown element: " + element);
    }

}
