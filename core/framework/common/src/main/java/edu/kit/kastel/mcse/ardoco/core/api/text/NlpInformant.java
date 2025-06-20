/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.text;

import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;

/**
 * Provides annotated text for NLP processing.
 */
public abstract class NlpInformant extends Informant {

    /**
     * Creates a new NLP informant.
     *
     * @param id             the informant ID
     * @param dataRepository the data repository
     */
    protected NlpInformant(String id, DataRepository dataRepository) {
        super(id, dataRepository);
    }

    /**
     * Returns the annotated text.
     *
     * @return the annotated text
     */
    public abstract Text getAnnotatedText();

    /**
     * Returns the annotated text with the given name.
     *
     * @param textName the name of the text
     * @return the annotated text
     */
    public abstract Text getAnnotatedText(String textName);
}
