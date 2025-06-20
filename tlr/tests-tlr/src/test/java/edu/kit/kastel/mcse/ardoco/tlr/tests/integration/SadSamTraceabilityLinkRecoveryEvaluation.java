/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.tests.integration;

import static edu.kit.kastel.mcse.ardoco.tlr.tests.integration.TraceLinkEvaluationIT.OUTPUT;

import java.io.File;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import edu.kit.kastel.mcse.ardoco.core.api.models.ModelFormat;
import edu.kit.kastel.mcse.ardoco.core.api.output.ArDoCoResult;
import edu.kit.kastel.mcse.ardoco.core.common.util.TraceLinkUtilities;
import edu.kit.kastel.mcse.ardoco.core.execution.ConfigurationHelper;
import edu.kit.kastel.mcse.ardoco.core.execution.runner.ArDoCoRunner;
import edu.kit.kastel.mcse.ardoco.core.tests.eval.GoldStandardProject;
import edu.kit.kastel.mcse.ardoco.core.tests.eval.results.ExpectedResults;
import edu.kit.kastel.mcse.ardoco.tlr.execution.ArDoCoForSadSamTraceabilityLinkRecovery;

/**
 * Integration test that evaluates the traceability link recovery capabilities of ArDoCo.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SadSamTraceabilityLinkRecoveryEvaluation<T extends GoldStandardProject> extends TraceabilityLinkRecoveryEvaluation<T> {

    @Override
    protected ArDoCoRunner getAndSetupRunner(T project) {
        var additionalConfigsMap = ConfigurationHelper.loadAdditionalConfigs(project.getAdditionalConfigurationsFile());

        String name = project.getProjectName();
        File inputModel = project.getModelFile();
        File inputText = project.getTextFile();
        File outputDir = new File(OUTPUT);

        var runner = new ArDoCoForSadSamTraceabilityLinkRecovery(name);
        runner.setUp(inputText, inputModel, ModelFormat.PCM, additionalConfigsMap, outputDir);
        return runner;
    }

    @Override
    protected ExpectedResults getExpectedResults(T project) {
        return project.getExpectedTraceLinkResults();
    }

    @Override
    protected ImmutableList<String> getGoldStandard(T project) {
        return project.getTlrGoldStandard();
    }

    @Override
    protected ImmutableList<String> enrollGoldStandard(ImmutableList<String> goldStandard, ArDoCoResult result) {
        return goldStandard;
    }

    @Override
    protected ImmutableList<String> createTraceLinkStringList(ArDoCoResult arDoCoResult) {
        var sadSamTls = Lists.immutable.ofAll(arDoCoResult.getArchitectureTraceLinks());
        return TraceLinkUtilities.getSadSamTraceLinksAsStringList(sadSamTls);
    }

    @Override
    protected int getConfusionMatrixSum(ArDoCoResult arDoCoResult) {
        int sentences = arDoCoResult.getText().getSentences().size();
        int modelElements = 0;
        for (var model : arDoCoResult.getMetamodels()) {
            modelElements += arDoCoResult.getModelState(model).getEndpoints().size();
        }

        return sentences * modelElements;
    }

    protected ArDoCoResult getArDoCoResult(String name, File inputText, File inputModel, ModelFormat modelFormat, File additionalConfigurations) {
        var additionalConfigsMap = ConfigurationHelper.loadAdditionalConfigs(additionalConfigurations);
        File outputDir = new File(OUTPUT);

        var runner = new ArDoCoForSadSamTraceabilityLinkRecovery(name);
        runner.setUp(inputText, inputModel, modelFormat, additionalConfigsMap, outputDir);
        return runner.run();
    }
}
