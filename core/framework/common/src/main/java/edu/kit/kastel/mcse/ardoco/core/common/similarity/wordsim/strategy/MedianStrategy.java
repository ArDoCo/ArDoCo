/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.strategy;

import java.util.ArrayList;
import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.ComparisonContext;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.WordSimMeasure;

/**
 * A similarity strategy that returns the median of all similarity scores from the provided word similarity measures.
 */
public class MedianStrategy implements SimilarityStrategy {
    @Override
    public double getSimilarity(ComparisonContext ctx, List<WordSimMeasure> measures) {
        var values = new ArrayList<Double>();

        for (WordSimMeasure measure : measures) {
            var similarity = measure.getSimilarity(ctx);
            if (!Double.isNaN(similarity)) {
                values.add(similarity);
            }
        }
        values.sort(Double::compare);

        var array = values.toArray(new Double[0]);
        if (array.length % 2 == 0) {
            return (array[array.length / 2] + array[array.length / 2 - 1]) / 2;
        }
        return array[array.length / 2];
    }
}
