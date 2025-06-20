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
 * The analyzer examines the incoming dependency arcs of the current node.
 */
public class InDepArcsInformant extends TextExtractionInformant {

    @Configurable
    private double nameOrTypeWeight = 0.5;

    @Configurable
    private double probability = 1.0;

    /**
     * Prototype constructor.
     *
     * @param data the {@link DataRepository}
     */
    public InDepArcsInformant(DataRepository data) {
        super(InDepArcsInformant.class.getSimpleName(), data);
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
        this.examineIncomingDepArcs(word);
    }

    /**
     * Examines the incoming dependency arcs.
     */
    private void examineIncomingDepArcs(Word word) {

        var incomingDepArcs = WordHelper.getIncomingDependencyTags(word);

        for (DependencyTag depTag : incomingDepArcs) {
            if (hasNameOrTypeDependencies(depTag)) {
                this.getTextStateStrategy().addNounMapping(word, MappingKind.NAME, this, this.probability * this.nameOrTypeWeight);
                this.getTextStateStrategy().addNounMapping(word, MappingKind.TYPE, this, this.probability * this.nameOrTypeWeight);
            } else if (hasTypeOrNameOrTypeDependencies(depTag)) {
                if (WordHelper.hasIndirectDeterminerAsPreWord(word)) {
                    this.getTextStateStrategy().addNounMapping(word, MappingKind.TYPE, this, this.probability);
                }

                this.getTextStateStrategy().addNounMapping(word, MappingKind.NAME, this, this.probability * this.nameOrTypeWeight);
                this.getTextStateStrategy().addNounMapping(word, MappingKind.TYPE, this, this.probability * this.nameOrTypeWeight);
            }
        }
    }

    private static boolean hasTypeOrNameOrTypeDependencies(DependencyTag depTag) {
        var hasObjectDependencies = DependencyTag.OBJ == depTag || DependencyTag.IOBJ == depTag || DependencyTag.POBJ == depTag;
        return hasObjectDependencies || DependencyTag.NMOD == depTag || DependencyTag.NSUBJPASS == depTag;
    }

    private static boolean hasNameOrTypeDependencies(DependencyTag depTag) {
        return DependencyTag.APPOS == depTag || DependencyTag.NSUBJ == depTag || DependencyTag.POSS == depTag;
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        // emtpy
    }
}
