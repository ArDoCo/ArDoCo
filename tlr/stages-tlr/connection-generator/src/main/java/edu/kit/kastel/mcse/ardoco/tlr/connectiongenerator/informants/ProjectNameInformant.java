/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.connectiongenerator.informants;

import java.util.Objects;
import java.util.SortedMap;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendationState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;

/**
 * This informant looks for (parts of) the project's name within RecommendedInstances and if it finds the project's name, influences the probability of the
 * RecommendedInstance negatively because it then should not be a recommended instance.
 */
public class ProjectNameInformant extends Informant {
    private static final String ERROR_EMPTY_LIST = "List cannot be empty";

    @Configurable
    private double penalty = Double.NEGATIVE_INFINITY;

    /**
     * Constructs a new instance of the {@link ProjectNameInformant} with the given data repository.
     *
     * @param dataRepository the data repository
     */
    public ProjectNameInformant(DataRepository dataRepository) {
        super("ProjectNameExtractor", dataRepository);
    }

    private static String getEditedProjectName(String projectName) {
        // remove white spaces from project name
        return projectName.replace(" ", "");
    }

    @Override
    public void process() {
        DataRepository dataRepository = this.getDataRepository();
        var projectName = DataRepositoryHelper.getProjectPipelineData(dataRepository).getProjectName();
        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        var recommendationStates = DataRepositoryHelper.getRecommendationStates(dataRepository);
        for (var metamodel : modelStates.getMetamodels()) {
            var recommendationState = recommendationStates.getRecommendationState(metamodel);

            this.checkForProjectNameInRecommendedInstances(projectName, recommendationState);
            this.checkForProjectNameInRecommendedInstances(projectName.toLowerCase(), recommendationState);
        }
    }

    private void checkForProjectNameInRecommendedInstances(String projectName, RecommendationState recommendationState) {
        for (var recommendedInstance : recommendationState.getRecommendedInstances()) {
            this.checkForProjectNameInNounMappingsOfRecommendedInstance(projectName, recommendedInstance);
        }
    }

    private void checkForProjectNameInNounMappingsOfRecommendedInstance(String projectName, RecommendedInstance recommendedInstance) {
        for (var nm : recommendedInstance.getNameMappings()) {
            this.checkWordsInNounMapping(projectName, recommendedInstance, nm);
        }
    }

    private void checkWordsInNounMapping(String projectName, RecommendedInstance recommendedInstance, NounMapping nm) {
        for (var word : nm.getWords()) {
            String wordText = word.getText().toLowerCase();
            if (projectName.contains(wordText)) {
                var words = this.expandWordForName(projectName, word);
                var expandedWord = this.concatenateWords(words);
                if (SimilarityUtils.getInstance().areWordsSimilar(projectName, expandedWord)) {
                    recommendedInstance.addProbability(this, this.penalty);
                }
            }
        }
    }

    private String concatenateWords(MutableList<Word> words) {
        var sortedWords = words.sortThisByInt(Word::getPosition);
        StringBuilder concatenatedWords = new StringBuilder();
        for (var word : sortedWords) {
            concatenatedWords.append(word.getText().toLowerCase());
        }
        return concatenatedWords.toString();
    }

    private MutableList<Word> expandWordForName(String projectName, Word word) {
        MutableList<Word> words = Lists.mutable.with(word);
        var editedProjectName = getEditedProjectName(projectName);

        this.expandWordForNameLeft(editedProjectName, words);
        this.expandWordForNameRight(editedProjectName, words);

        return words.distinct().sortThisByInt(Word::getPosition);
    }

    private void expandWordForNameLeft(String name, MutableList<Word> words) {
        Objects.requireNonNull(name);
        if (words.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY_LIST);
        }

        Word currWord = words.sortThisByInt(Word::getPosition).getFirstOptional().orElseThrow(IllegalArgumentException::new);
        this.expandWordForName(name, currWord, words, Word::getPreWord, (text, addition) -> addition + text);
    }

    private void expandWordForNameRight(String name, MutableList<Word> words) {
        Objects.requireNonNull(name);
        if (words.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY_LIST);
        }

        var currWord = words.sortThisByInt(Word::getPosition).getLastOptional().orElseThrow(IllegalArgumentException::new);
        this.expandWordForName(name, currWord, words, Word::getNextWord, (text, addition) -> text + addition);
    }

    private void expandWordForName(String name, Word currWord, MutableList<Word> words, UnaryOperator<Word> wordExpansion,
            BinaryOperator<String> concatenation) {
        Objects.requireNonNull(name);
        if (words.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY_LIST);
        }

        var testWordText = this.concatenateWords(words);
        while (name.contains(testWordText)) {
            words.add(currWord);

            currWord = wordExpansion.apply(currWord);
            var wordText = "NON-MATCHING";
            if (currWord != null) {
                wordText = currWord.getText().toLowerCase();
            }
            testWordText = concatenation.apply(testWordText, wordText);
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> map) {
        // handle additional configuration
    }
}
