/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.tests;

import java.util.Locale;
import java.util.Set;

import org.eclipse.collections.api.collection.ImmutableCollection;
import org.slf4j.Logger;

import edu.kit.kastel.mcse.ardoco.core.api.output.ArDoCoResult;
import edu.kit.kastel.mcse.ardoco.core.tests.eval.results.EvaluationResults;
import edu.kit.kastel.mcse.ardoco.core.tests.eval.results.ExpectedResults;
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator;

/**
 * This utility class provides methods for running the tests, especially regarding the evaluations.
 */
public class TestUtil {

    private TestUtil() {
        throw new IllegalAccessError("This constructor should not be called!");
    }

    /**
     * compares the tlr results with the expected results and creates a new {@link EvaluationResults}.
     *
     * @param arDoCoResult the ArDoCoResult
     * @param results      Collection representing the results
     * @param goldStandard Collection representing the gold standard
     * @return the result of the comparison
     */
    public static <T> EvaluationResults<T> compareTLR(ArDoCoResult arDoCoResult, ImmutableCollection<T> results, ImmutableCollection<T> goldStandard) {

        Set<T> distinctTraceLinks = new java.util.LinkedHashSet<>(results.castToCollection());
        Set<T> distinctGoldStandard = new java.util.LinkedHashSet<>(goldStandard.castToCollection());

        int sentences = arDoCoResult.getText().getSentences().size();
        int modelElements = 0;
        for (var model : arDoCoResult.getMetamodels()) {
            modelElements += arDoCoResult.getModelState(model).getEndpoints().size();
        }

        int confusionMatrixSum = sentences * modelElements;

        var calculator = ClassificationMetricsCalculator.getInstance();

        var classification = calculator.calculateMetrics(distinctTraceLinks, distinctGoldStandard, confusionMatrixSum);
        return new EvaluationResults<>(classification);
    }

    /**
     * compares the inconsistencies results with the expected results and creates a new {@link EvaluationResults}.
     *
     * @param arDoCoResult the ArDoCoResult
     * @param results      Collection representing the results
     * @param goldStandard Collection representing the gold standard
     * @return the result of the comparison
     */
    public static <T> EvaluationResults<T> compareInconsistencies(ArDoCoResult arDoCoResult, ImmutableCollection<T> results,
            ImmutableCollection<T> goldStandard) {

        Set<T> distinctTraceLinks = new java.util.LinkedHashSet<>(results.castToCollection());
        Set<T> distinctGoldStandard = new java.util.LinkedHashSet<>(goldStandard.castToCollection());

        int confusionMatrixSum = arDoCoResult.getText().getSentences().size();

        var calculator = ClassificationMetricsCalculator.getInstance();
        var classification = calculator.calculateMetrics(distinctTraceLinks, distinctGoldStandard, confusionMatrixSum);
        return new EvaluationResults<>(classification);
    }

    /**
     * Log the provided {@link EvaluationResults} using the provided logger and name. The log put out the result string provided by the
     * {@link EvaluationResults}.
     *
     * @param logger  Logger to use
     * @param name    Name to show in the output
     * @param results the results
     */
    public static void logResults(Logger logger, String name, EvaluationResults<?> results) {
        if (logger.isInfoEnabled())
            logger.info(createResultLogString(name, results));
    }

    /**
     * Creates a string from the given results that can be used, e.g., for logging. Extracts the name as well as precision, recall and F1-score and displays
     * them line by line.
     *
     * @param name    the name that should be displayed
     * @param results the results
     * @return a String containing the name and the results (precision, recall, F1) line by line
     */
    public static String createResultLogString(String name, EvaluationResults<?> results) {
        return String.format(Locale.ENGLISH, "%n%s:%n%s", name, results);
    }

    /**
     * Logs extended results as a row format using the provided logger.
     *
     * @param logger    the logger to use
     * @param headerVal the header value
     * @param headerKey the header key
     * @param results   the results to log
     */
    public static void logExtendedResultsAsRow(Logger logger, String headerVal, String headerKey, EvaluationResults<?> results) {
        var txt = String.format("%n%s", results.toRow(headerVal, headerKey));
        logger.info(txt);
    }

    /**
     * Log the provided {@link EvaluationResults} using the provided logger and name. Additionally, logs TP, FP, TN and FN used to calculate the metrics.
     *
     * @param logger  Logger to use
     * @param name    Name to show in the output
     * @param results the results
     */
    public static void logExplicitResults(Logger logger, String name, EvaluationResults<?> results) {
        var tp = results.truePositives().size();
        var fp = results.falsePositives().size();
        var fn = results.falseNegatives().size();
        var precisionDenominator = tp + fp;
        var recallDenominator = tp + fn;
        var logString = String.format(Locale.ENGLISH, "%n%s:%n\tPrecision:%7d/%d = %.3f%n\tRecall:%10d/%d = %.3f", name, tp, precisionDenominator, results
                .precision(), tp, recallDenominator, results.recall());
        logger.info(logString);
    }

    /**
     * Logs extended results with expected results comparison using the provided logger.
     *
     * @param logger          the logger to use
     * @param testClass       the test class object
     * @param name            the name to show in the output
     * @param results         the results to log
     * @param expectedResults the expected results for comparison
     */
    public static void logExtendedResultsWithExpected(Logger logger, Object testClass, String name, EvaluationResults<?> results,
            ExpectedResults expectedResults) {
        var infoString = String.format(Locale.ENGLISH, """

                %s (%s):
                %s""", name, testClass.getClass().getSimpleName(), results.getExtendedResultStringWithExpected(expectedResults));
        logger.info(infoString);
    }

}
