/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction.informants;

import java.util.SortedMap;
import java.util.StringJoiner;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextState;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepositorySyncer;
import edu.kit.kastel.mcse.ardoco.tlr.textextraction.NounMappingImpl;
import edu.kit.kastel.mcse.ardoco.tlr.textextraction.TextStateImpl;

public class CompoundAgentInformant extends TextExtractionInformant {
    @Configurable
    private double compoundConfidence = 0.6;
    @Configurable
    private double specialNamedEntityConfidence = 0.6;

    public CompoundAgentInformant(DataRepository dataRepository) {
        super(CompoundAgentInformant.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        var text = DataRepositoryHelper.getAnnotatedText(this.getDataRepository());
        var textState = this.getDataRepository().getData(TextState.ID, TextStateImpl.class).orElseThrow();
        for (var word : text.words()) {
            this.createNounMappingIfCompoundWord(word, textState);
            this.createNounMappingIfSpecialNamedEntity(word);
        }
    }

    private void createNounMappingIfCompoundWord(Word word, TextState textState) {
        var compoundWords = CommonUtilities.getCompoundWords(word);

        // if compoundWords is empty then it is no compoundWords
        if (compoundWords.isEmpty()) {
            return;
        }
        // add the full compoundWords
        this.addCompoundNounMapping(compoundWords, textState);

        // filter NounMappings that are types and add the rest of the compoundWords (if it changed)
        var filteredCompoundWords = this.filterWordsOfTypeMappings(compoundWords, textState);
        if (filteredCompoundWords.size() != compoundWords.size() && filteredCompoundWords.size() > 1) {
            this.addCompoundNounMapping(filteredCompoundWords, textState);
        }
    }

    private ImmutableList<Word> filterWordsOfTypeMappings(ImmutableList<Word> words, TextState textState) {
        MutableList<Word> filteredWords = Lists.mutable.empty();
        for (var word : words) {
            if (!textState.isWordContainedByMappingKind(word, MappingKind.TYPE)) {
                filteredWords.add(word);
            }
        }
        return filteredWords.toImmutable();
    }

    private void addCompoundNounMapping(ImmutableList<Word> compoundWords, TextState textState) {
        var reference = CommonUtilities.createReferenceForCompound(compoundWords);
        var similarReferenceNounMappings = textState.getNounMappingsWithSimilarReference(reference);
        if (similarReferenceNounMappings.isEmpty()) {

            var nounMapping = this.getTextStateStrategy()
                    .addNounMapping(compoundWords.toImmutableSortedSet(), MappingKind.NAME, this, this.compoundConfidence, compoundWords.toImmutableList(),
                            compoundWords.collect(Word::getText).toImmutableList(), createReferenceForCompound(compoundWords));
            ((NounMappingImpl) nounMapping).setIsDefinedAsCompound(true);
        } else {
            for (var nounMapping : similarReferenceNounMappings) {

                textState.removeNounMapping(this.getDataRepository(), nounMapping, null, true);

                var newWords = nounMapping.getWords().toSortedSet();
                newWords.addAllIterable(compoundWords);

                var compoundMapping = this.getTextStateStrategy()
                        .addNounMapping(newWords.toImmutable(), nounMapping.getDistribution(), nounMapping.getReferenceWords(), nounMapping.getSurfaceForms(),
                                nounMapping.getReference());
                DataRepositorySyncer.onNounMappingDeletion(this.dataRepository, nounMapping, compoundMapping);
                ((NounMappingImpl) compoundMapping).setIsDefinedAsCompound(true);
            }
        }
    }

    private static String createReferenceForCompound(ImmutableList<Word> comoundWords) {
        var sortedCompoundWords = comoundWords.toSortedListBy(Word::getPosition);
        var referenceJoiner = new StringJoiner(" ");
        for (var w : sortedCompoundWords) {
            referenceJoiner.add(w.getText());
        }
        return referenceJoiner.toString();
    }

    private void createNounMappingIfSpecialNamedEntity(Word word) {
        var text = word.getText();
        if (CommonUtilities.isCamelCasedWord(text) || CommonUtilities.nameIsSnakeCased(text)) {
            this.getTextStateStrategy().addNounMapping(word, MappingKind.NAME, this, this.specialNamedEntityConfidence);
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> map) {
        // none
    }
}
