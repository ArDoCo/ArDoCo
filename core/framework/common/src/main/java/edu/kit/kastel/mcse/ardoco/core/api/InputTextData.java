/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api;

import java.io.Serial;

import edu.kit.kastel.mcse.ardoco.core.data.PipelineStepData;

/**
 * Container for input text data to be used as {@link PipelineStepData} in the pipeline.
 */
public final class InputTextData implements PipelineStepData {

    @Serial
    private static final long serialVersionUID = -5404851121533249349L;

    public static final String ID = "InputTextData";

    private final String text;

    /**
     * Constructs a new InputTextData instance.
     *
     * @param text the input text
     */
    public InputTextData(String text) {
        this.text = text;
    }

    /**
     * Returns the input text.
     *
     * @return the input text
     */
    public String getText() {
        return this.text;
    }
}
