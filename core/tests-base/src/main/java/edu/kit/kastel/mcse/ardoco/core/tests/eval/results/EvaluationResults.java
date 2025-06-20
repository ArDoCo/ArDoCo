/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.tests.eval.results;

import java.util.List;
import java.util.Locale;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.metrics.result.SingleClassificationResult;

public record EvaluationResults<T>(SingleClassificationResult<T> classificationResult) {

    public String toRow() {
        return String.format(Locale.ENGLISH, """
                %4s & %4s & %4s & %4s & %4s & %4s & %4s
                %4.2f & %4.2f & %4.2f & %4.2f & %4.2f & %4.2f & %4.2f""", "P", "R", "F1", "Acc", "Spec", "Phi", "PhiN", precision(), recall(), f1(), accuracy(),
                specificity(), phiCoefficient(), phiOverPhiMax());
    }

    public String toRow(String headerKey, String headerVal) {
        return String.format(Locale.ENGLISH, """
                %10s & %4s & %4s & %4s & %4s & %4s & %4s & %4s
                %10s & %4.2f & %4.2f & %4.2f & %4.2f & %4.2f & %4.2f & %4.2f""", headerKey, "P", "R", "F1", "Acc", "Spec", "Phi", "PhiN", headerVal,
                precision(), recall(), f1(), accuracy(), specificity(), phiCoefficient(), phiOverPhiMax());
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, """
                \tPrecision:%8.2f
                \tRecall:%11.2f
                \tF1:%15.2f
                \tAccuracy:%9.2f
                \tSpecificity:%6.2f
                \tPhi Coef.:%8.2f
                \tPhi/PhiMax:%7.2f (Phi Max: %.2f)
                %s""", precision(), recall(), f1(), accuracy(), specificity(), phiCoefficient(), phiOverPhiMax(), phiCoefficientMax(), toRow());
    }

    public String getExtendedResultStringWithExpected(ExpectedResults expectedResults) {
        return String.format(Locale.ENGLISH, """
                \tPrecision:%8.2f (min. expected: %.2f)
                \tRecall:%11.2f (min. expected: %.2f)
                \tF1:%15.2f (min. expected: %.2f)""", precision(), expectedResults.precision(), recall(), expectedResults.recall(), f1(), expectedResults
                .f1()) + String.format(Locale.ENGLISH, """

                        \tAccuracy:%9.2f (min. expected: %.2f)
                        \tSpecificity:%6.2f (min. expected: %.2f)""", accuracy(), expectedResults.accuracy(), specificity(), expectedResults
                        .specificity()) + String.format(Locale.ENGLISH, """

                                \tPhi Coef.:%8.2f (min. expected: %.2f)
                                \tPhi/PhiMax:%7.2f (Phi Max: %.2f)
                                %s""", phiCoefficient(), expectedResults.phiCoefficient(), phiOverPhiMax(), phiCoefficientMax(), toRow());
    }

    public ImmutableList<T> getFound() {
        MutableList<T> found = Lists.mutable.empty();
        found.addAll(classificationResult.getTruePositives());
        found.addAll(classificationResult.getFalsePositives());
        return found.toImmutable();
    }

    public double precision() {
        return classificationResult.getPrecision();
    }

    public double recall() {
        return classificationResult.getRecall();
    }

    public double f1() {
        return classificationResult.getF1();
    }

    public double accuracy() {
        return classificationResult.getAccuracy();
    }

    public double specificity() {
        return classificationResult.getSpecificity();
    }

    public double phiCoefficient() {
        return classificationResult.getPhiCoefficient();
    }

    public double phiOverPhiMax() {
        return classificationResult.getPhiOverPhiMax();
    }

    public double phiCoefficientMax() {
        return classificationResult.getPhiCoefficientMax();
    }

    public List<T> truePositives() {
        return classificationResult.getTruePositives().stream().toList();
    }

    public List<T> falsePositives() {
        return classificationResult.getFalsePositives().stream().toList();
    }

    public List<T> falseNegatives() {
        return classificationResult.getFalseNegatives().stream().toList();
    }
}
