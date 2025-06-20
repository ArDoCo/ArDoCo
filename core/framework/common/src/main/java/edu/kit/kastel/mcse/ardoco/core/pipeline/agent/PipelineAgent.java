/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.pipeline.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import edu.kit.kastel.mcse.ardoco.core.configuration.ChildClassConfigurable;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.AbstractExecutionStage;
import edu.kit.kastel.mcse.ardoco.core.pipeline.Pipeline;

/**
 * Represents a pipeline agent that calculates results for an {@link AbstractExecutionStage}.
 * Subclasses should override and may override {@link #initializeState()} for custom initialization.
 */
public abstract class PipelineAgent extends Pipeline implements Agent {

    private final List<? extends Informant> informants;

    @Configurable
    @ChildClassConfigurable
    private List<String> enabledInformants;

    /**
     * Creates a new pipeline agent with the specified id. Runs informants sequentially on the data repository.
     *
     * @param informants     the informants in order of execution (all enabled by default)
     * @param id             the id
     * @param dataRepository the data repository
     */
    protected PipelineAgent(List<? extends Informant> informants, String id, DataRepository dataRepository) {
        super(id, dataRepository);
        this.informants = new ArrayList<>(informants);
        this.enabledInformants = informants.stream().map(Informant::getId).toList();
    }

    /**
     * Prepares pipeline steps and initializes the agent.
     */
    @Override
    protected final void preparePipelineSteps() {
        super.preparePipelineSteps();
        this.initialize();
    }

    /**
     * Called before all informants. Override to add custom behavior.
     */
    @Override
    protected void before() {
        //Nothing by default
    }

    /**
     * Called after all informants. Override to add custom behavior.
     */
    @Override
    protected void after() {
        //Nothing by default
    }

    /**
     * Initializes the execution and adds enabled informants as pipeline steps.
     */
    protected final void initialize() {
        this.initializeState();
        for (var informant : this.informants) {
            if (this.enabledInformants.contains(informant.getId())) {
                this.addPipelineStep(informant);
            }
        }
    }

    /**
     * Override to initialize state before processing, if necessary.
     */
    protected void initializeState() {
        // do nothing here
    }

    /**
     * Returns the informants, including disabled ones.
     *
     * @return the list of informants
     */
    public List<Informant> getInformants() {
        return List.copyOf(this.informants);
    }

    /**
     * Applies additional configuration to internal objects and informants.
     */
    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        super.delegateApplyConfigurationToInternalObjects(additionalConfiguration);
        this.informants.forEach(filter -> filter.applyConfiguration(additionalConfiguration));
    }

}
