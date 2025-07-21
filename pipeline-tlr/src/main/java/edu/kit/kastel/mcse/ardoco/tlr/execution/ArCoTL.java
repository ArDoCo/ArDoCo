/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.execution;

import java.io.File;
import java.util.SortedMap;

import edu.kit.kastel.mcse.ardoco.core.api.model.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.model.ModelFormat;
import edu.kit.kastel.mcse.ardoco.core.execution.ArDoCo;
import edu.kit.kastel.mcse.ardoco.core.execution.runner.ArDoCoRunner;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.SamCodeTraceabilityLinkRecovery;
import edu.kit.kastel.mcse.ardoco.tlr.models.agents.ArCoTLModelProviderAgent;
import edu.kit.kastel.mcse.ardoco.tlr.models.agents.ArchitectureConfiguration;

public class ArCoTL extends ArDoCoRunner {

    public ArCoTL(String projectName) {
        super(projectName);
    }

    public void setUp(File inputArchitectureModel, ModelFormat modelFormat, File inputCode, SortedMap<String, String> additionalConfigs, File outputDir) {
        definePipeline(inputArchitectureModel, modelFormat, inputCode, additionalConfigs);
        setOutputDirectory(outputDir);
        isSetUp = true;
    }

    private void definePipeline(File inputArchitectureModel, ModelFormat modelFormat, File inputCode, SortedMap<String, String> additionalConfigs) {
        ArDoCo arDoCo = this.getArDoCo();
        var dataRepository = arDoCo.getDataRepository();

        var codeConfiguration = ArCoTLModelProviderAgent.getCodeConfiguration(inputCode, Metamodel.CODE_WITH_COMPILATION_UNITS);
        var architectureConfiguration = new ArchitectureConfiguration(inputArchitectureModel, modelFormat,
                Metamodel.ARCHITECTURE_WITH_COMPONENTS_AND_INTERFACES);

        ArCoTLModelProviderAgent arCoTLModelProviderAgent = ArCoTLModelProviderAgent.getArCoTLModelProviderAgent(dataRepository, additionalConfigs,
                architectureConfiguration, codeConfiguration);
        arDoCo.addPipelineStep(arCoTLModelProviderAgent);
        arDoCo.addPipelineStep(SamCodeTraceabilityLinkRecovery.get(additionalConfigs, dataRepository));
    }
}
