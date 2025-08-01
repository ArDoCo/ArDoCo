/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.execution.runner;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.models.ModelFormat;
import edu.kit.kastel.mcse.ardoco.core.execution.ConfigurationHelper;
import edu.kit.kastel.mcse.ardoco.core.execution.RunnerBaseTest;
import edu.kit.kastel.mcse.ardoco.tlr.execution.Swattr;
import edu.kit.kastel.mcse.ardoco.tlr.models.agents.ArchitectureConfiguration;

@Disabled("Disabled as other (integration) tests cover the same functionality. Enable for debugging/local development.")
class SwattrTest extends RunnerBaseTest {

    @Test
    @DisplayName("Test ArDoCo for SAD-SAM-TLR (PCM)")
    void testSadSamTlrPcm() {
        var runner = new Swattr(projectName);
        var additionalConfigsMap = ConfigurationHelper.loadAdditionalConfigs(new File(additionalConfigs));
        runner.setUp(inputText, new ArchitectureConfiguration(new File(inputModelArchitecture), ModelFormat.PCM), additionalConfigsMap, outputDir);

        testRunnerAssertions(runner);
        Assertions.assertNotNull(runner.run());
    }

    @Disabled("Disabled for faster builds. Enable if you need to check UML models.")
    @Test
    @DisplayName("Test ArDoCo for SAD-SAM-TLR (UML)")
    void testSadSamTlrUml() {
        var runner = new Swattr(projectName);
        var additionalConfigsMap = ConfigurationHelper.loadAdditionalConfigs(new File(additionalConfigs));
        runner.setUp(inputText, new ArchitectureConfiguration(new File(inputModelArchitectureUml), ModelFormat.UML), additionalConfigsMap, outputDir);

        testRunnerAssertions(runner);
        Assertions.assertNotNull(runner.run());
    }

}
