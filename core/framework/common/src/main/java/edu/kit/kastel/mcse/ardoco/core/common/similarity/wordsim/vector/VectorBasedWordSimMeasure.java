/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.vector;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.WordSimMeasure;

/**
 * Abstract base for word similarity measures using vector embeddings. Manages a cache for lookup speed.
 */
@Deterministic
public abstract class VectorBasedWordSimMeasure implements WordSimMeasure {

    private static final float[] ZERO_VECTOR = {};
    private final Map<String, float[]> vectorCache = new LinkedHashMap<>();

    protected abstract WordVectorDataSource getVectorDataSource();

    /**
     * Compares two words by computing the cosine similarity between their vector representations.
     * Returns 0.0 if a vector is not found.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @return the similarity score between the two words, between 0.0 and 1.0
     * @throws RetrieveVectorException if an error occurs while retrieving the vectors
     */
    public double compareVectors(String firstWord, String secondWord) throws RetrieveVectorException {
        Objects.requireNonNull(firstWord);
        Objects.requireNonNull(secondWord);

        if (firstWord.equals(secondWord)) {
            return 1.0;
        }

        float[] firstVec = this.getVectorFromCacheOrDatabase(firstWord);

        if (VectorUtils.isZero(firstVec)) {
            return 0.0; // no vector representation for the first word
        }

        float[] secondVec = this.getVectorFromCacheOrDatabase(secondWord);

        if (VectorUtils.isZero(secondVec)) {
            return 0.0; // no vector representation for the second word
        }

        return VectorUtils.cosineSimilarity(firstVec, secondVec);
    }

    private float[] getVectorFromCacheOrDatabase(String word) throws RetrieveVectorException {
        float[] vector = this.vectorCache.getOrDefault(word, null);

        if (vector == null) {
            vector = this.getVectorDataSource().getWordVector(word).orElse(ZERO_VECTOR);
            this.vectorCache.put(word, vector);
        }

        return vector;
    }

}
