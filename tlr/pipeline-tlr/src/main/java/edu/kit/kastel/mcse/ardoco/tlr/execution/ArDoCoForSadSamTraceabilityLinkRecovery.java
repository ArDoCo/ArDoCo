/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.execution;

import java.io.File;
import java.util.SortedMap;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelFormat;
import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.execution.runner.ArDoCoRunner;
import edu.kit.kastel.mcse.ardoco.tlr.connectiongenerator.ConnectionGenerator;
import edu.kit.kastel.mcse.ardoco.tlr.models.agents.ArCoTLModelProviderAgent;
import edu.kit.kastel.mcse.ardoco.tlr.models.agents.ArchitectureConfiguration;
import edu.kit.kastel.mcse.ardoco.tlr.recommendationgenerator.RecommendationGenerator;
import edu.kit.kastel.mcse.ardoco.tlr.text.providers.TextPreprocessingAgent;
import edu.kit.kastel.mcse.ardoco.tlr.textextraction.TextExtraction;

public class ArDoCoForSadSamTraceabilityLinkRecovery extends ArDoCoRunner {
    public ArDoCoForSadSamTraceabilityLinkRecovery(String projectName) {
        super(projectName);
    }

    public void setUp(File inputText, File inputArchitectureModel, ModelFormat modelFormat, SortedMap<String, String> additionalConfigs, File outputDir) {
        definePipeline(inputText, inputArchitectureModel, modelFormat, additionalConfigs);
        setOutputDirectory(outputDir);
        isSetUp = true;
    }

    public void setUp(String inputTextLocation, String inputArchitectureModelLocation, ModelFormat modelFormat, SortedMap<String, String> additionalConfigs,
            String outputDirectory) {
        setUp(new File(inputTextLocation), new File(inputArchitectureModelLocation), modelFormat, additionalConfigs, new File(outputDirectory));
    }

    private void definePipeline(File inputText, File inputArchitectureModel, ModelFormat modelFormat, SortedMap<String, String> additionalConfigs) {
        var dataRepository = this.getArDoCo().getDataRepository();
        var text = CommonUtilities.readInputText(inputText);
        if (text.isBlank()) {
            throw new IllegalArgumentException("Cannot deal with empty input text. Maybe there was an error reading the file.");
        }
        DataRepositoryHelper.putInputText(dataRepository, text);

        this.getArDoCo().addPipelineStep(TextPreprocessingAgent.get(additionalConfigs, dataRepository));

        // TODO: Phi: Right here?
        var architectureConfiguration = new ArchitectureConfiguration(inputArchitectureModel, modelFormat, Metamodel.ARCHITECTURE_ONLY_COMPONENTS);
        ArCoTLModelProviderAgent arCoTLModelProviderAgent = //
                ArCoTLModelProviderAgent.getArCoTLModelProviderAgent(dataRepository, additionalConfigs, architectureConfiguration, null);
        this.getArDoCo().addPipelineStep(arCoTLModelProviderAgent);

        this.getArDoCo().addPipelineStep(TextExtraction.get(additionalConfigs, dataRepository));
        this.getArDoCo().addPipelineStep(RecommendationGenerator.get(additionalConfigs, dataRepository));
        this.getArDoCo().addPipelineStep(ConnectionGenerator.get(additionalConfigs, dataRepository));
    }
}
