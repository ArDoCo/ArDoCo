/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.measures.equality;

import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.ComparisonContext;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.WordSimMeasure;

/**
 * A word similarity measure that checks if the string representations of the terms are equal, ignoring case.
 */
public class EqualityMeasure implements WordSimMeasure {
    /**
     * Checks if the terms in the given context are similar by comparing their string representations, ignoring case.
     *
     * @param comparisonContext the comparison context
     * @return true if the terms are equal ignoring case, false otherwise
     */
    @Override
    public boolean areWordsSimilar(ComparisonContext comparisonContext) {
        return comparisonContext.firstTerm().equalsIgnoreCase(comparisonContext.secondTerm());
    }

    /**
     * Returns 1.0 if the terms are similar, 0.0 otherwise.
     *
     * @param comparisonContext the comparison context
     * @return the similarity score (1.0 or 0.0)
     */
    @Override
    public double getSimilarity(ComparisonContext comparisonContext) {
        return this.areWordsSimilar(comparisonContext) ? 1 : 0;
    }
}
