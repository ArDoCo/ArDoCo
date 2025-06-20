/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.tuple;

import java.io.Serializable;

/**
 * A record representing a triple of three elements, where the second and third elements are serializable.
 *
 * @param <T> the type of the first element
 * @param <U> the type of the second element (must be serializable)
 * @param <V> the type of the third element (must be serializable)
 */
public record Triple<T, U extends Serializable, V extends Serializable>(T first, U second, V third) implements Serializable {
}
