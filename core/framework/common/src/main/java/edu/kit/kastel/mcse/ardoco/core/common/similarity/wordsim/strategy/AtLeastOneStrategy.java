/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.strategy;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.ComparisonContext;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.WordSimMeasure;

/**
 * Comparison strategy: accepts a word pair as similar if at least one measure accepts it as similar.
 */
public class AtLeastOneStrategy implements ComparisonStrategy {

    /**
     * Returns true if at least one measure considers the words similar.
     *
     * @param ctx      the comparison context
     * @param measures the measures to use
     * @return true if at least one measure returns true
     */
    @Override
    public boolean areWordsSimilar(ComparisonContext ctx, List<WordSimMeasure> measures) {
        for (WordSimMeasure measure : measures) {
            if (measure.areWordsSimilar(ctx)) {
                return true;
            }
        }

        return false;
    }

}
