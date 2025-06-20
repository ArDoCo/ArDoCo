/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.measures.equality.EqualityMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.strategy.AverageStrategy;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.strategy.ComparisonStrategy;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.strategy.SimilarityStrategy;

/**
 * Provides utility methods for calculating similarity between objects, with configurable strategies and measures.
 * Not thread-safe.
 */
public class WordSimUtils {

    private MutableList<WordSimMeasure> measures = Lists.mutable.withAll(WordSimLoader.loadUsingProperties());
    private ComparisonStrategy strategy = ComparisonStrategy.AT_LEAST_ONE;
    private SimilarityStrategy similarityStrategy = new AverageStrategy();

    /**
     * Sets which measures should be used for similarity comparison.
     *
     * @param measures the measures to use
     */
    public void setMeasures(Collection<WordSimMeasure> measures) {
        this.measures = Lists.mutable.withAll(measures);
    }

    /**
     * Adds the specified measure to the measures used for similarity comparison.
     *
     * @param measure the measure to add
     * @return true if the measure was added successfully
     */
    public boolean addMeasure(WordSimMeasure measure) {
        return this.measures.add(measure);
    }

    /**
     * Sets the default comparison strategy. The specified strategy will be used for all subsequent comparisons that themselves do not specify a strategy.
     *
     * @param strategy the new default strategy
     */
    public void setStrategy(ComparisonStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Sets the default similarity strategy. The specified strategy will be used for all subsequent comparisons that themselves do not specify a strategy.
     *
     * @param strategy the new default strategy
     */
    public void setStrategy(SimilarityStrategy strategy) {
        this.similarityStrategy = strategy;
    }

    /**
     * Evaluates whether the words from the given {@link ComparisonContext} are similar using the specified comparison strategy.
     *
     * @param ctx      the context
     * @param strategy the strategy
     * @return true if the given strategy considers the words similar enough
     */
    public boolean areWordsSimilar(ComparisonContext ctx, ComparisonStrategy strategy) {
        Objects.requireNonNull(ctx);
        Objects.requireNonNull(strategy);

        if (!this.splitLengthTest(ctx)) {
            return false;
        }

        return strategy.areWordsSimilar(ctx, this.measures.toList());
    }

    private boolean splitLengthTest(ComparisonContext ctx) {
        var first = ctx.firstTerm().toLowerCase();
        var second = ctx.secondTerm().toLowerCase();
        return (first.split(" ").length == second.split(" ").length);
    }

    /**
     * Evaluates whether the words from the given {@link ComparisonContext} are similar using the default comparison strategy.
     *
     * @param ctx the context
     * @return true if the default strategy considers the words similar enough
     */
    public boolean areWordsSimilar(ComparisonContext ctx) {
        Objects.requireNonNull(ctx);
        return this.areWordsSimilar(ctx, this.strategy);
    }

    /**
     * Evaluates whether the given words are similar using the default comparison strategy.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @return true if the default strategy considers the words similar enough
     */
    public boolean areWordsSimilar(String firstWord, String secondWord) {
        return this.areWordsSimilar(new ComparisonContext(firstWord, secondWord, false), this.strategy);
    }

    /**
     * Evaluates whether the given words are similar using the specified comparison strategy.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @param strategy   the strategy to use
     * @return true if the given strategy considers the words similar enough
     */
    public boolean areWordsSimilar(String firstWord, String secondWord, ComparisonStrategy strategy) {
        return this.areWordsSimilar(new ComparisonContext(firstWord, secondWord, false), strategy);
    }

    /**
     * Evaluates whether the given words are similar using the default comparison strategy.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @return true if the default strategy considers the words similar enough
     */
    public boolean areWordsSimilar(Word firstWord, Word secondWord) {
        return this.areWordsSimilar(new ComparisonContext(firstWord, secondWord, false), this.strategy);
    }

    /**
     * Evaluates whether the given words are similar using the specified comparison strategy.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @param strategy   the strategy to use
     * @return true if the given strategy considers the words similar enough
     */
    public boolean areWordsSimilar(Word firstWord, Word secondWord, ComparisonStrategy strategy) {
        return this.areWordsSimilar(new ComparisonContext(firstWord, secondWord, false), strategy);
    }

    /**
     * Evaluates whether the given words are similar using the default comparison strategy.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @return true if the default strategy considers the words similar enough
     */
    public boolean areWordsSimilar(String firstWord, Word secondWord) {
        return this.areWordsSimilar(new ComparisonContext(firstWord, secondWord.getText(), null, secondWord, false), this.strategy);
    }

    /**
     * Evaluates whether the given words are similar using the specified comparison strategy.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @param strategy   the strategy to use
     * @return true if the given strategy considers the words similar enough
     */
    public boolean areWordsSimilar(String firstWord, Word secondWord, ComparisonStrategy strategy) {
        return this.areWordsSimilar(new ComparisonContext(firstWord, secondWord.getText(), null, secondWord, false), strategy);
    }

    /**
     * Evaluates the similarity of the given words using the specified similarity strategy.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @param strategy   the strategy to use
     * @param ignoreCase whether to ignore the case during comparison
     * @return similarity in range [0,1]
     */
    public double getSimilarity(String firstWord, String secondWord, SimilarityStrategy strategy, boolean ignoreCase) {
        var allMeasuresExceptDefault = this.measures.stream().filter(m -> !(m instanceof EqualityMeasure)).collect(Collectors.toCollection(ArrayList::new));
        if (allMeasuresExceptDefault.isEmpty()) {
            allMeasuresExceptDefault.add(new EqualityMeasure());
        }

        return strategy.getSimilarity(new ComparisonContext(ignoreCase ? firstWord.toLowerCase() : firstWord, ignoreCase ?
                secondWord.toLowerCase() :
                secondWord, null, null, false), allMeasuresExceptDefault);
    }

    /**
     * Evaluates the similarity of the given words.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @return similarity in range [0,1]
     */
    public double getSimilarity(String firstWord, String secondWord) {
        return this.getSimilarity(firstWord, secondWord, false);
    }

    /**
     * Evaluates the similarity of the given words.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @param ignoreCase whether to ignore the case during comparison
     * @return similarity in range [0,1]
     */
    public double getSimilarity(String firstWord, String secondWord, boolean ignoreCase) {
        return this.getSimilarity(firstWord, secondWord, this.similarityStrategy, ignoreCase);
    }

    /**
     * Configures SQLite settings for read-only, exclusive locking, and no journal mode.
     *
     * @return configured SQLiteConfig instance
     */
    public static SQLiteConfig getSqLiteConfig() {
        var cfg = new SQLiteConfig();
        cfg.setReadOnly(true);
        cfg.setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE);
        cfg.setJournalMode(SQLiteConfig.JournalMode.OFF);
        cfg.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
        cfg.setOpenMode(SQLiteOpenMode.NOMUTEX);
        return cfg;
    }
}
