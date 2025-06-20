/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity;

import static edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils.cosineSimilarity;
import static edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils.uniqueDot;

import java.util.function.ToDoubleBiFunction;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.PhraseMapping;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;

/**
 * Aggregation strategies for comparing two {@link PhraseMapping} objects using different similarity metrics.
 * Provides maximum, minimum, and average cosine similarity between phrase vectors.
 */
@Deterministic
public enum PhraseMappingAggregatorStrategy implements ToDoubleBiFunction<PhraseMapping, PhraseMapping> {
    /**
     * Uses the maximum cosine similarity between phrase vectors.
     */
    MAX_SIMILARITY((a, b) -> uniqueDot(a.getPhrases().toImmutableList(), b.getPhrases().toImmutableList()).stream()
            .mapToDouble(p -> cosineSimilarity(p.first().getPhraseVector().toSortedMap(), p.second().getPhraseVector().toSortedMap()))
            .max()
            .orElse(Double.NaN)), //

    /**
     * Uses the minimum cosine similarity between phrase vectors.
     */
    MIN_SIMILARITY((a, b) -> uniqueDot(a.getPhrases().toImmutableList(), b.getPhrases().toImmutableList()).stream()
            .mapToDouble(p -> cosineSimilarity(p.first().getPhraseVector().toSortedMap(), p.second().getPhraseVector().toSortedMap()))
            .min()
            .orElse(Double.NaN)), //

    /**
     * Uses the average cosine similarity between phrase vectors.
     */
    AVG_SIMILARITY((a, b) -> uniqueDot(a.getPhrases().toImmutableList(), b.getPhrases().toImmutableList()).stream()
            .mapToDouble(p -> cosineSimilarity(p.first().getPhraseVector().toSortedMap(), p.second().getPhraseVector().toSortedMap()))
            .average()
            .orElse(Double.NaN));

    private final ToDoubleBiFunction<PhraseMapping, PhraseMapping> mapper;

    /**
     * Creates a strategy with the given mapping function.
     *
     * @param mapper the function to aggregate similarity
     */
    PhraseMappingAggregatorStrategy(ToDoubleBiFunction<PhraseMapping, PhraseMapping> mapper) {
        this.mapper = mapper;
    }

    /**
     * Applies the aggregation strategy to two {@link PhraseMapping} objects.
     *
     * @param phraseMapping  the first phrase mapping
     * @param phraseMapping2 the second phrase mapping
     * @return the aggregated similarity value
     */
    @Override
    public double applyAsDouble(PhraseMapping phraseMapping, PhraseMapping phraseMapping2) {
        return this.mapper.applyAsDouble(phraseMapping, phraseMapping2);
    }
}
