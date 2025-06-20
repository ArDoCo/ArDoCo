/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim;

import java.util.ArrayList;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.measures.equality.EqualityMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.measures.glove.GloveMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.measures.jarowinkler.JaroWinklerMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.measures.levenshtein.LevenshteinMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.measures.ngram.NgramMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.measures.sewordsim.SEWordSimMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.util.CommonTextToolsConfig;

/**
 * Loads the word similarity measures enabled according to {@link CommonTextToolsConfig}.
 */
public class WordSimLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordSimLoader.class);

    /**
     * Loads and returns the word similarity measures enabled according to {@link CommonTextToolsConfig}.
     *
     * @return a list of word similarity measures
     */
    public static ImmutableList<WordSimMeasure> loadUsingProperties() {
        try {
            var list = new ArrayList<WordSimMeasure>();

            list.add(new EqualityMeasure());

            if (CommonTextToolsConfig.LEVENSHTEIN_ENABLED) {
                list.add(new LevenshteinMeasure());
            }

            if (CommonTextToolsConfig.JAROWINKLER_ENABLED) {
                list.add(new JaroWinklerMeasure());
            }

            if (CommonTextToolsConfig.NGRAM_ENABLED) {
                list.add(new NgramMeasure());
            }

            if (CommonTextToolsConfig.SEWORDSIM_ENABLED) {
                list.add(new SEWordSimMeasure());
            }

            if (CommonTextToolsConfig.GLOVE_ENABLED) {
                list.add(new GloveMeasure());
            }

            return Lists.immutable.withAll(list);
        } catch (Exception e) {
            LOGGER.error("Failed to load word similarity measures", e);
            return Lists.immutable.empty();
        }
    }

    private WordSimLoader() {
    }

}
