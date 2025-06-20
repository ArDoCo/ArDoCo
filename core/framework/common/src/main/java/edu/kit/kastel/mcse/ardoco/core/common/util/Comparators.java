/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.util;

import java.util.Collection;

import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.ordered.SortedIterable;

/**
 * Provides functions to compare collections regardless of order or identity.
 */
public final class Comparators {
    private Comparators() {
        throw new IllegalAccessError();
    }

    /**
     * Returns whether both collections consist of equal elements (regardless of order).
     *
     * @param first  collection
     * @param second collection
     * @param <T>    Type of the collection
     * @return true if both collections have equal elements, false otherwise
     */
    public static <T> boolean collectionsEqualsAnyOrder(ImmutableCollection<T> first, ImmutableCollection<T> second) {
        return collectionsEqualsAnyOrder(first.castToCollection(), second.castToCollection());
    }

    /**
     * Returns whether both collections consist of equal elements (regardless of order).
     *
     * @param first  collection
     * @param second collection
     * @param <T>    Type of the collection
     * @return true if both collections have equal elements, false otherwise
     */
    public static <T> boolean collectionsEqualsAnyOrder(Collection<T> first, Collection<T> second) {
        return first.size() == second.size() && first.containsAll(second) && second.containsAll(first);
    }

    /**
     * Returns whether both sorted iterables consist of equal elements (regardless of order).
     *
     * @param first  collection
     * @param second collection
     * @param <T>    Type of the collection
     * @return true if both sorted iterables have equal elements, false otherwise
     */
    public static <T> boolean collectionsEqualsAnyOrder(SortedIterable<T> first, SortedIterable<T> second) {
        var f = first.iterator();
        var s = second.iterator();
        while (f.hasNext() || s.hasNext()) {
            if ((f.hasNext() != s.hasNext()) || !f.next().equals(s.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether both collections consist of the same references (regardless of order).
     *
     * @param first  collection
     * @param second collection
     * @param <T>    Type of the collection
     * @return true if both collections have the same references, false otherwise
     */
    public static <T> boolean collectionsIdentityAnyOrder(ImmutableCollection<T> first, ImmutableCollection<T> second) {
        return collectionsIdentityAnyOrder(first.castToCollection(), second.castToCollection());
    }

    /**
     * Returns whether both sorted iterables consist of the same references (regardless of order).
     *
     * @param first  collection
     * @param second collection
     * @param <T>    Type of the collection
     * @return true if both sorted iterables have the same references, false otherwise
     */
    public static <T> boolean collectionsIdentityAnyOrder(SortedIterable<T> first, SortedIterable<T> second) {
        var f = first.iterator();
        var s = second.iterator();
        while (f.hasNext() || s.hasNext()) {
            if ((f.hasNext() != s.hasNext()) || (f.next() != s.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether both collections consist of the same references (regardless of order).
     *
     * @param first  collection
     * @param second collection
     * @param <T>    Type of the collection
     * @return true if both collections have the same references, false otherwise
     */
    public static <T> boolean collectionsIdentityAnyOrder(Collection<T> first, Collection<T> second) {
        return first.size() == second.size() && first.stream().allMatch(f -> second.stream().anyMatch(s -> f == s));
    }
}
