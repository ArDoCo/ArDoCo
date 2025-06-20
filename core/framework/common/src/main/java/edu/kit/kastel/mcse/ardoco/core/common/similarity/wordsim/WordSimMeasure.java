/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim;

/**
 * A measure that determines whether two words from a {@link ComparisonContext} are similar.
 */
public interface WordSimMeasure {

    /**
     * Evaluates whether the words from the given {@link ComparisonContext} are similar.
     *
     * @param ctx the context containing the words
     * @return true if the words are similar
     */
    boolean areWordsSimilar(ComparisonContext ctx);

    /**
     * Evaluates how similar the words from the given {@link ComparisonContext} are.
     *
     * @param ctx the context containing the words
     * @return similarity in range [0,1]
     */
    double getSimilarity(ComparisonContext ctx);
}
