/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.execution.runner;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.output.ArDoCoResult;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.execution.ArDoCo;

/**
 * Abstract runner for ArDoCo pipeline execution.
 */
public abstract class ArDoCoRunner {
    private static final Logger logger = LoggerFactory.getLogger(ArDoCoRunner.class);

    private final ArDoCo arDoCo;

    private File outputDirectory;
    protected boolean isSetUp = false;

    protected ArDoCoRunner(String projectName) {
        this.arDoCo = new ArDoCo(projectName);
        this.outputDirectory = null;
    }

    /**
     * Checks if the runner is properly set up and ready to run.
     *
     * @return true if the runner is set up, false otherwise
     */
    public boolean isSetUp() {
        return this.isSetUp;
    }

    /**
     * Runs the ArDoCo pipeline and saves the results to the output directory.
     *
     * @return the ArDoCo result, or null if the runner is not properly set up
     */
    public final ArDoCoResult run() {
        if (this.isSetUp() && this.outputDirectory != null) {
            return this.getArDoCo().runAndSave(this.outputDirectory);
        } else {
            logger.error("Cannot run ArDoCo because the runner is not properly set up (#run).");
            return null;
        }
    }

    /**
     * Returns the {@link DataRepository} produced by the run. The results are not saved to the output directory.
     *
     * @return the data repository produced by the run
     */
    public final DataRepository runWithoutSaving() {
        if (this.isSetUp()) {
            this.getArDoCo().run();
            return this.getArDoCo().getDataRepository();
        } else {
            logger.error("Cannot run ArDoCo because the runner is not properly set up (#runWithoutSaving).");
            return null;
        }
    }

    /**
     * Returns the ArDoCo instance used by this runner.
     *
     * @return the ArDoCo instance
     */
    public ArDoCo getArDoCo() {
        return this.arDoCo;
    }

    /**
     * Sets the output directory where results will be saved.
     *
     * @param outputDirectory the directory to save output files to
     */
    protected void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
