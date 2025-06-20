/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api;

import java.io.Serial;

import edu.kit.kastel.mcse.ardoco.core.api.text.Text;
import edu.kit.kastel.mcse.ardoco.core.data.PipelineStepData;

/**
 * Container for data after preprocessing, to be stored as {@link PipelineStepData}. Currently holds the preprocessed {@link Text}.
 */
public class PreprocessingData implements PipelineStepData {
    @Serial
    private static final long serialVersionUID = 8103545017098419675L;

    public static final String ID = "PreprocessingData";

    private final Text preprocessedText;

    /**
     * Constructs a new PreprocessingData instance.
     *
     * @param preprocessedText the preprocessed text
     */
    public PreprocessingData(Text preprocessedText) {
        this.preprocessedText = preprocessedText;
    }

    /**
     * Returns the preprocessed text.
     *
     * @return the preprocessed text
     */
    public Text getText() {
        return this.preprocessedText;
    }

}
