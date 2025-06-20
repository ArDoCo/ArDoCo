/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.strategy;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.ComparisonContext;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.WordSimMeasure;

/**
 * A similarity strategy that returns the average of all similarity scores from the provided word similarity measures.
 */
public class AverageStrategy implements SimilarityStrategy {
    @Override
    public double getSimilarity(ComparisonContext ctx, List<WordSimMeasure> measures) {
        double sum = 0.0;
        int successful = 0;

        for (WordSimMeasure measure : measures) {
            var similarity = measure.getSimilarity(ctx);
            if (!Double.isNaN(similarity)) {
                successful++;
                sum += similarity;
            }
        }

        return successful == 0 ? 0.0 : sum / successful;
    }
}
