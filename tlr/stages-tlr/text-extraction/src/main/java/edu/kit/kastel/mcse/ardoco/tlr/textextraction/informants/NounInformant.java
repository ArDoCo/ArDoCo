/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction.informants;

import java.util.SortedMap;

import org.eclipse.collections.api.list.ImmutableList;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.text.POSTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;

/**
 * The analyzer classifies nouns.
 */
public class NounInformant extends TextExtractionInformant {
    @Configurable
    private double nameOrTypeWeight = 0.5;

    @Configurable
    private double probability = 0.2;

    /**
     * Prototype constructor.
     *
     * @param data the {@link DataRepository}
     */
    public NounInformant(DataRepository data) {
        super(NounInformant.class.getSimpleName(), data);
    }

    @Override
    public void process() {
        ImmutableList<Word> words = DataRepositoryHelper.getAnnotatedText(this.getDataRepository()).words();
        for (var word : words) {
            var text = word.getText();
            if (text.length() > 1 && Character.isLetter(text.charAt(0))) {
                this.findSingleNouns(word);
            }
        }
    }

    /**
     * Finds all nouns and adds them as name-or-type mappings (and types) to the text extraction state.
     */
    private void findSingleNouns(Word word) {
        var pos = word.getPosTag();
        if (POSTag.NOUN_PROPER_SINGULAR == pos || POSTag.NOUN == pos || POSTag.NOUN_PROPER_PLURAL == pos) {
            this.getTextStateStrategy().addNounMapping(word, MappingKind.NAME, this, this.probability * this.nameOrTypeWeight);
            this.getTextStateStrategy().addNounMapping(word, MappingKind.TYPE, this, this.probability * this.nameOrTypeWeight);
        }
        if (POSTag.NOUN_PLURAL == pos) {
            this.getTextStateStrategy().addNounMapping(word, MappingKind.TYPE, this, this.probability);
        }

    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        // empty
    }
}
