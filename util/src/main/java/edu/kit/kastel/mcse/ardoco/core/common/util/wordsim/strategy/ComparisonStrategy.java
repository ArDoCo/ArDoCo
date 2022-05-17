package edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.strategy;

import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.ComparisonContext;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.WordSimMeasure;

import java.util.List;

/**
 * A comparison strategy determines how the verdicts of multiple WSMs regarding a specific comparison are combined.
 */
public interface ComparisonStrategy {

	ComparisonStrategy AT_LEAST_ONE = new AtleastOneStrategy();

	ComparisonStrategy MAJORITY = new MajorityStrategy();

	/**
	 * Evaluates whether the words from the given {@link ComparisonContext} are similar by combining the verdicts
	 * of the specified word similarity measures.
	 *
	 * @param ctx      the context containing the words
	 * @param measures the measures to use
	 * @return Returns {@code true} if the words are similar.
	 */
	boolean areWordsSimilar(ComparisonContext ctx, List<WordSimMeasure> measures);

}
