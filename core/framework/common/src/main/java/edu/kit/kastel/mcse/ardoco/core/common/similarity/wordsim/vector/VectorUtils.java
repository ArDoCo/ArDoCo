/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.vector;

import java.util.Objects;

/**
 * Utility functions for double and float vectors.
 */
public class VectorUtils {

    /**
     * Calculates the cosine similarity between two vectors. If both are zero, returns 1.0; if only one is zero, returns 0.0.
     *
     * @param firstVec  the first vector
     * @param secondVec the second vector
     * @return the cosine similarity
     * @throws IllegalArgumentException if the vectors have different lengths
     */
    public static double cosineSimilarity(double[] firstVec, double[] secondVec) {
        Objects.requireNonNull(firstVec);
        Objects.requireNonNull(secondVec);
        if (firstVec.length != secondVec.length) {
            throw new IllegalArgumentException("vector length does not match!");
        }

        if (isZero(firstVec) && isZero(secondVec)) {
            return 1.0; // similarity between 0 and 0 should probably be 1.0
        }

        if (isZero(firstVec) || isZero(secondVec)) {
            return 0.0; // similarity between 0 and something else should probably be 0.0
        }

        double dotProduct = 0.0;
        double firstNorm = 0.0;
        double secondNorm = 0.0;

        for (int i = 0; i < firstVec.length; i++) {
            dotProduct += firstVec[i] * secondVec[i];
            firstNorm += Math.pow(firstVec[i], 2);
            secondNorm += Math.pow(secondVec[i], 2);
        }

        return dotProduct / (Math.sqrt(firstNorm) * Math.sqrt(secondNorm));
    }

    /**
     * Calculates the cosine similarity between two vectors. The two vectors must have the same length.
     *
     * @param firstVec  the first vector
     * @param secondVec the second vector
     * @return the cosine similarity
     * @throws IllegalArgumentException if the vectors have different lengths
     */
    public static double cosineSimilarity(float[] firstVec, float[] secondVec) {
        Objects.requireNonNull(firstVec);
        Objects.requireNonNull(secondVec);

        double[] firstVecDouble = new double[firstVec.length];
        double[] secondVecDouble = new double[secondVec.length];
        for (int i = 0; i < firstVec.length; i++) {
            firstVecDouble[i] = firstVec[i];
            secondVecDouble[i] = secondVec[i];
        }

        return cosineSimilarity(firstVecDouble, secondVecDouble);
    }

    /**
     * Checks whether the given vector contains any non-zero numbers.
     *
     * @param vector the vector to check
     * @return {@code true} if the given vector either has no entries or if the only entries are zero
     */
    public static boolean isZero(double[] vector) {
        Objects.requireNonNull(vector);

        for (double entry : vector) {
            if (entry != 0.0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether the given vector contains any non-zero numbers.
     *
     * @param vector the vector to check
     * @return {@code true} if the given vector either has no entries or if the only entries are zero
     */
    public static boolean isZero(float[] vector) {
        Objects.requireNonNull(vector);

        for (float entry : vector) {
            if (entry != 0.0f) {
                return false;
            }
        }

        return true;
    }

    /**
     * Adds the given vectors together and stores the resulting vector in the first argument. The two vectors must
     * have the same length.
     *
     * @param result the first vector that will also be the result vector after the addition
     * @param toAdd  the second vector
     */
    public static void add(double[] result, double[] toAdd) {
        Objects.requireNonNull(result);
        Objects.requireNonNull(toAdd);

        if (result.length != toAdd.length) {
            throw new IllegalArgumentException("vectors of different lengths: " + result.length + " != " + toAdd.length);
        }

        for (int i = 0; i < result.length; i++) {
            result[i] += toAdd[i];
        }
    }

    /**
     * Scales the given vector by the given scalar.
     *
     * @param vector the vector to scale
     * @param scalar the scalar
     */
    public static void scale(double[] vector, double scalar) {
        Objects.requireNonNull(vector);

        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i] * scalar;
        }
    }

    private VectorUtils() {
    }

}
