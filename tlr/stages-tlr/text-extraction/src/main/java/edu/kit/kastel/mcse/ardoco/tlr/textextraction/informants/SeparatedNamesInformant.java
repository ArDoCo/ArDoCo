/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction.informants;

import java.util.SortedMap;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.text.POSTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;

/**
 * This analyzer classifies all nodes, containing separators, as names and adds them as mappings to the current text
 * extraction state.
 */

public class SeparatedNamesInformant extends TextExtractionInformant {

    @Configurable
    private double probability = 0.8;

    /**
     * Prototype constructor.
     *
     * @param dataRepository the {@link DataRepository}
     */
    public SeparatedNamesInformant(DataRepository dataRepository) {
        super(SeparatedNamesInformant.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        for (var word : DataRepositoryHelper.getAnnotatedText(this.getDataRepository()).words()) {
            this.exec(word);
        }
    }

    /***
     * Checks if Node Value contains separator. If true, it is split and added separately to the names of the text
     * extraction state.
     */
    private void exec(Word word) {
        this.checkForSeparatedNode(word);
    }

    /**
     * Checks if Node Value contains separator. If true, it is split and added separately to the names of the text
     * extraction state.
     */
    private void checkForSeparatedNode(Word word) {
        if (word.getPosTag() != POSTag.FOREIGN_WORD && CommonUtilities.containsSeparator(word.getText())) {
            this.getTextStateStrategy().addNounMapping(word, MappingKind.NAME, this, this.probability);
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        // emtpy
    }

}
