/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.tests.integration.evaluation;

import static edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel.CODE_WITH_COMPILATION_UNITS;

import java.io.File;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.eclipse.collections.api.factory.SortedMaps;
import org.junit.jupiter.api.Assertions;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.Model;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelFormat;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelStates;
import edu.kit.kastel.mcse.ardoco.core.api.output.ArDoCoResult;
import edu.kit.kastel.mcse.ardoco.core.common.tuple.Pair;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.execution.runner.ArDoCoRunner;
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator;
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleClassificationResult;
import edu.kit.kastel.mcse.ardoco.tlr.execution.Arcotl;
import edu.kit.kastel.mcse.ardoco.tlr.models.agents.ArchitectureConfiguration;
import edu.kit.kastel.mcse.ardoco.tlr.models.agents.CodeConfiguration;
import edu.kit.kastel.mcse.ardoco.tlr.tests.approach.ArCoTLEvaluationProject;

public class ArcotlEvaluation extends AbstractEvaluation {

    private final ArCoTLEvaluationProject project;
    private final boolean useAcmFile;

    public ArcotlEvaluation(ArCoTLEvaluationProject project, boolean useAcmFile) {
        this.project = project;
        this.useAcmFile = useAcmFile;
    }

    public ArDoCoResult runTraceLinkEvaluation() {
        ArDoCoRunner arcotl = createArcotl();
        ArDoCoResult result = arcotl.run();
        Assertions.assertNotNull(result);

        var goldStandard = project.getTlrTask().getExpectedTraceLinks();
        var enrolledGoldStandard = enrollGoldStandard(goldStandard, result);
        var evaluationResults = this.calculateEvaluationResults(result, enrolledGoldStandard);
        var expectedResults = project.getExpectedResults();

        logExtendedResultsWithExpected(project.name(), evaluationResults, expectedResults);
        compareResults(evaluationResults, expectedResults);
        return result;
    }

    private ArDoCoRunner createArcotl() {
        String projectName = project.name().toLowerCase();
        ModelFormat architectureModelFormat = ModelFormat.PCM;
        File inputArchitectureModel = project.getTlrTask().getArchitectureModelFile(architectureModelFormat);
        CodeConfiguration inputCode = useAcmFile //
                ? new CodeConfiguration(project.getTlrTask().getCodeModelFromResources(), CodeConfiguration.CodeConfigurationType.ACM_FILE) //
                : new CodeConfiguration(project.getTlrTask().getCodeDirectory(), CodeConfiguration.CodeConfigurationType.DIRECTORY);
        File outputDirectory = new File("target", projectName + "-output");
        outputDirectory.mkdirs();

        var runner = new Arcotl(projectName);
        runner.setUp(new ArchitectureConfiguration(inputArchitectureModel, architectureModelFormat), inputCode, SortedMaps.immutable.empty(), outputDirectory);
        return runner;
    }

    private List<Pair<String, String>> enrollGoldStandard(List<Pair<String, String>> goldStandard, ArDoCoResult result) {
        return this.enrollGoldStandard(goldStandard, result, CODE_WITH_COMPILATION_UNITS);
    }

    private SingleClassificationResult<String> calculateEvaluationResults(ArDoCoResult result, List<Pair<String, String>> goldStandard) {
        var sadSamCodeTlsAsString = result.getSamCodeTraceLinks()
                .collect(tl -> tl.getFirstEndpoint().getId() + " -> " + tl.getSecondEndpoint().toString())
                .toSortedSet();
        var goldStandardAsStrings = goldStandard.stream().map(pair -> pair.first() + " -> " + pair.second()).collect(Collectors.toCollection(TreeSet::new));

        int confusionMatrixSum = getConfusionMatrixSum(result);
        var calculator = ClassificationMetricsCalculator.getInstance();
        return calculator.calculateMetrics(sadSamCodeTlsAsString, goldStandardAsStrings, confusionMatrixSum);
    }

    private int getConfusionMatrixSum(ArDoCoResult result) {
        ModelStates modelStatesData = DataRepositoryHelper.getModelStatesData(result.dataRepository());
        Model codeModel = modelStatesData.getModel(Metamodel.CODE_WITH_COMPILATION_UNITS);
        Model architectureModel = modelStatesData.getModel(Metamodel.ARCHITECTURE_WITH_COMPONENTS_AND_INTERFACES);
        var codeModelEndpoints = codeModel.getEndpoints().size();
        var architectureModelEndpoints = architectureModel.getEndpoints().size();
        return codeModelEndpoints * architectureModelEndpoints;
    }

}
