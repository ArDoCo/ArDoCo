/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.strategy;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.ComparisonContext;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.WordSimMeasure;

/**
 * A strategy for combining the verdicts of multiple word similarity measures.
 */
public interface ComparisonStrategy {

    ComparisonStrategy AT_LEAST_ONE = new AtLeastOneStrategy();

    ComparisonStrategy MAJORITY = new MajorityStrategy();

    /**
     * Evaluates whether the words from the given context are similar by combining the verdicts of the specified measures.
     *
     * @param ctx      the context containing the words
     * @param measures the measures to use
     * @return true if the words are similar
     */
    boolean areWordsSimilar(ComparisonContext ctx, List<WordSimMeasure> measures);

}
