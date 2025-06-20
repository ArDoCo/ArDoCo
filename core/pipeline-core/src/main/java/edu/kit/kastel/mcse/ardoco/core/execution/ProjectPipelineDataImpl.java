/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.execution;

import java.io.Serial;

import edu.kit.kastel.mcse.ardoco.core.data.ProjectPipelineData;

/**
 * Implementation of {@link ProjectPipelineData} that stores the project's name provided in the constructor.
 */
public class ProjectPipelineDataImpl implements ProjectPipelineData {

    @Serial
    private static final long serialVersionUID = -993634357212795104L;
    private final String projectName;

    /**
     * Constructs this class using the project's name.
     *
     * @param projectName the project's name
     */
    ProjectPipelineDataImpl(String projectName) {
        super();
        this.projectName = projectName;
    }

    @Override
    public String getProjectName() {
        return this.projectName;
    }
}
