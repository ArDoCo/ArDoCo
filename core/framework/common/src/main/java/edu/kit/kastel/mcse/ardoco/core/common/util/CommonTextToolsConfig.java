/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.util;

import org.eclipse.collections.api.list.ImmutableList;

/**
 * Configuration class for the common text tools, providing constants for similarity and text processing settings.
 */
public final class CommonTextToolsConfig {

    private CommonTextToolsConfig() {
        throw new IllegalAccessError();
    }

    private static final ResourceAccessor CONFIG = loadParameters("/configs/CommonTextToolsConfig.properties");

    /**
     * List of separators used for containing by a word in SimilarityUtils.
     */
    public static final ImmutableList<String> SEPARATORS_TO_CONTAIN = CONFIG.getPropertyAsList("separators_ToContain");
    /**
     * List of separators used for splitting a word in SimilarityUtils.
     */
    public static final ImmutableList<String> SEPARATORS_TO_SPLIT = CONFIG.getPropertyAsList("separators_ToSplit");

    /**
     * Decides whether the levenshtein similarity measure should be used.
     */
    public static final boolean LEVENSHTEIN_ENABLED = CONFIG.isPropertyEnabled("levenshtein_Enabled");
    /**
     * Int for the minimal length of word similarity for methods in SimilarityUtils.
     */
    public static final int LEVENSHTEIN_MIN_LENGTH = CONFIG.getPropertyAsInt("levenshtein_MinLength");
    /**
     * Int for the maximal levensthein distance for two words to be similar for methods in SimilarityUtils.
     */
    public static final int LEVENSHTEIN_MAX_DISTANCE = CONFIG.getPropertyAsInt("levenshtein_MaxDistance");
    /**
     * The levenshtein distance threshold which, multiplied with the length of the shortest word of a comparison, acts as a dynamic distance limit.
     */
    public static final double LEVENSHTEIN_THRESHOLD = CONFIG.getPropertyAsDouble("levenshtein_Threshold");

    /**
     * Decides whether the JaroWinkler similarity measure should be used.
     */
    public static final boolean JAROWINKLER_ENABLED = CONFIG.isPropertyEnabled("jaroWinkler_Enabled");
    /**
     * The default threshold for similarity in methods of SimilarityUtils.
     */
    public static final double JAROWINKLER_SIMILARITY_THRESHOLD = CONFIG.getPropertyAsDouble("jaroWinkler_SimilarityThreshold");

    /**
     * The minimal propotion of two lists that need to be similar, that both are similar. Used in SimilarityUtils.
     */
    public static final double GET_MOST_RECOMMENDED_I_BY_REF_MIN_PROPORTION = CONFIG.getPropertyAsDouble("getMostRecommendedIByRef_MinProportion");
    /**
     * The increase for the method getMostRecommendedInstancesByReference in SimilarityUtils.
     */
    public static final double GET_MOST_RECOMMENDED_I_BY_REF_INCREASE = CONFIG.getPropertyAsDouble("getMostRecommendedIByRef_Increase");

    /**
     * Decides whether the NGram similarity measure should be used.
     */
    public static final boolean NGRAM_ENABLED = CONFIG.isPropertyEnabled("ngram_Enabled");
    /**
     * The length of ngrams for the N-gram word similarity measure.
     */
    public static final int NGRAM_MEASURE_NGRAM_LENGTH = CONFIG.getPropertyAsInt("ngram_NgramLength");
    /**
     * The threshold for the ngram similarity measure above which words are considered similar.
     */
    public static final double NGRAM_SIMILARITY_THRESHOLD = CONFIG.getPropertyAsDouble("ngram_SimilarityThreshold");

    /**
     * Decides whether the SEWordSim similarity measure should be used.
     */
    public static final boolean SEWORDSIM_ENABLED = CONFIG.isPropertyEnabled("sewordsim_Enabled");
    /**
     * The threshold for the SEWordSim similarity measure above which words are considered similar.
     */
    public static final double SEWORDSIM_SIMILARITY_THRESHOLD = CONFIG.getPropertyAsDouble("sewordsim_SimilarityThreshold");
    /**
     * The path to the sqlite database file used by the SEWordSim word similarity measure.
     */
    public static final String SEWORDSIM_DB_FILE_PATH = CONFIG.getProperty("sewordsim_DatabaseFilePath");

    /**
     * Decides whether the GloVe similarity measure should be used.
     */
    public static final boolean GLOVE_ENABLED = CONFIG.isPropertyEnabled("glove_Enabled");
    /**
     * The threshold for the GloVe similarity measure above which words are considered similar.
     */
    public static final double GLOVE_SIMILARITY_THRESHOLD = CONFIG.getPropertyAsDouble("glove_SimilarityThreshold");
    /**
     * The path to the sqlite database file used by the GloVe word similarity measure.
     */
    public static final String GLOVE_DB_FILE_PATH = CONFIG.getProperty("glove_DatabaseFilePath");

    private static ResourceAccessor loadParameters(String filePath) {
        return new ResourceAccessor(filePath, true);
    }

}
