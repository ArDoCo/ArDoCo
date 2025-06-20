/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction.informants;

import java.util.SortedMap;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.text.DependencyTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.common.util.WordHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;

/**
 * The analyzer examines the outgoing arcs of the current node.
 */

public class OutDepArcsInformant extends TextExtractionInformant {

    @Configurable
    private double nameOrTypeWeight = 0.5;

    @Configurable
    private double probability = 0.8;

    /**
     * Prototype constructor.
     *
     * @param dataRepository the {@link DataRepository}
     */
    public OutDepArcsInformant(DataRepository dataRepository) {
        super(OutDepArcsInformant.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        for (var word : DataRepositoryHelper.getAnnotatedText(this.getDataRepository()).words()) {
            this.exec(word);
        }
    }

    private void exec(Word word) {

        var nodeValue = word.getText();
        if (nodeValue.length() == 1 && !Character.isLetter(nodeValue.charAt(0))) {
            return;
        }
        this.examineOutgoingDepArcs(word);
    }

    /**
     * Examines the outgoing dependencies of a node.
     */
    private void examineOutgoingDepArcs(Word word) {

        var outgoingDepArcs = WordHelper.getOutgoingDependencyTags(word);

        for (DependencyTag shortDepTag : outgoingDepArcs) {

            if (DependencyTag.AGENT == shortDepTag || DependencyTag.RCMOD == shortDepTag) {
                this.getTextStateStrategy().addNounMapping(word, MappingKind.NAME, this, this.probability * this.nameOrTypeWeight);
                this.getTextStateStrategy().addNounMapping(word, MappingKind.TYPE, this, this.probability * this.nameOrTypeWeight);
            } else if (DependencyTag.NUM == shortDepTag || DependencyTag.PREDET == shortDepTag) {
                this.getTextStateStrategy().addNounMapping(word, MappingKind.TYPE, this, this.probability);
            }
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        // emtpy
    }

}
