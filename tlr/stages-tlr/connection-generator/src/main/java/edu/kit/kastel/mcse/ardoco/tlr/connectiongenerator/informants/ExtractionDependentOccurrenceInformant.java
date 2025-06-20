/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.connectiongenerator.informants;

import java.util.SortedMap;

import edu.kit.kastel.mcse.ardoco.core.api.models.ModelStates;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.Model;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextStateStrategy;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;
import edu.kit.kastel.mcse.ardoco.tlr.textextraction.TextStateStrategies;

/**
 * This analyzer searches for the occurrence of instance names and types of the extraction state and adds them as names and types to the text extraction state.
 */
public class ExtractionDependentOccurrenceInformant extends Informant {

    @Configurable
    private double probability = 1.0;
    private static final TextStateStrategies STRATEGY = TextStateStrategies.DEFAULT;

    public ExtractionDependentOccurrenceInformant(DataRepository dataRepository) {
        super(ExtractionDependentOccurrenceInformant.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        DataRepository dataRepository = this.getDataRepository();
        var text = DataRepositoryHelper.getAnnotatedText(dataRepository);
        var textStateStrategy = STRATEGY.apply(this.getDataRepository());
        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        for (var word : text.words()) {
            this.exec(textStateStrategy, modelStates, word);
        }
    }

    private void exec(TextStateStrategy tss, ModelStates modelStates, Word word) {
        for (var metamodel : modelStates.getMetamodels()) {
            var model = modelStates.getModel(metamodel);
            if (model == null) {
                continue;
            }
            this.searchForNameInModel(model, tss, word);
            this.searchForType(model, tss, word);
        }
    }

    /**
     * This method checks whether a given node is a name of an instance given in the model extraction state. If it appears to be a name this is stored in the
     * text extraction state.
     */
    private void searchForNameInModel(Model model, TextStateStrategy tss, Word word) {
        if (this.posTagIsUndesired(word) && !this.wordStartsWithCapitalLetter(word)) {
            return;
        }

        var instanceNameIsSimilar = model.getEndpoints().stream().anyMatch(i -> SimilarityUtils.getInstance().isWordSimilarToEntity(word, i));
        if (instanceNameIsSimilar) {
            tss.addNounMapping(word, MappingKind.NAME, this, this.probability);
        }
    }

    private boolean wordStartsWithCapitalLetter(Word word) {
        return Character.isUpperCase(word.getText().charAt(0));
    }

    private boolean posTagIsUndesired(Word word) {
        return !word.getPosTag().getTag().startsWith("NN");
    }

    /**
     * This method checks whether a given node is a type of an instance given in the model extraction state. If it appears to be a type this is stored in the
     * text extraction state. If multiple options are available the node value is taken as reference.
     */

    private void searchForType(Model model, TextStateStrategy tss, Word word) {
        var instanceTypeIsSimilar = model.getEndpoints()
                .stream()
                .filter(modelEntity -> modelEntity.getType().isPresent())
                .anyMatch(modelEntity -> SimilarityUtils.getInstance().isWordSimilarToModelInstanceType(word, modelEntity));
        if (instanceTypeIsSimilar) {
            tss.addNounMapping(word, MappingKind.TYPE, this, this.probability);
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        // handle additional config
    }
}
