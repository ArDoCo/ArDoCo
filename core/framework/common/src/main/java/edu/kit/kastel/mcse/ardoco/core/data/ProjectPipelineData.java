/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.data;

/**
 * Represents data about the overall project, such as the project name.
 */
public interface ProjectPipelineData extends PipelineStepData {

    /**
     * Identifier for project pipeline data.
     */
    String ID = "ProjectPipelineData";

    /**
     * Returns the project name.
     *
     * @return the project name
     */
    String getProjectName();
}
