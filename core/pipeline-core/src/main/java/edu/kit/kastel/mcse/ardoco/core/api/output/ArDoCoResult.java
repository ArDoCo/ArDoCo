/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.output;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.PreprocessingData;
import edu.kit.kastel.mcse.ardoco.core.api.entity.ArchitectureEntity;
import edu.kit.kastel.mcse.ardoco.core.api.entity.ModelEntity;
import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelStates;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.Model;
import edu.kit.kastel.mcse.ardoco.core.api.stage.codetraceability.CodeTraceabilityState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.connectiongenerator.ConnectionState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.Inconsistency;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistencyState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistentSentence;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.ModelInconsistency;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.TextInconsistency;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendationState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextState;
import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;
import edu.kit.kastel.mcse.ardoco.core.api.text.SentenceEntity;
import edu.kit.kastel.mcse.ardoco.core.api.text.Text;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;

/**
 * This record represents the result of running ArDoCo. It is backed by a {@link DataRepository} and provides access to data from it. Besides accessing all data
 * from the
 * calculation steps, this record also provides convenience methods to directly access results such as found trace links and detected inconsistencies.
 */
@Deterministic
public record ArDoCoResult(DataRepository dataRepository) {
    private static final Logger logger = LoggerFactory.getLogger(ArDoCoResult.class);

    private static String formatTraceLinksHumanReadable(TraceLink<SentenceEntity, ModelEntity> traceLink) {
        String modelElementName = traceLink.getSecondEndpoint().getName();
        String modelElementUid = traceLink.getSecondEndpoint().getId();
        String modelInfo = String.format("%s (%s)", modelElementName, modelElementUid);

        var sentence = traceLink.getFirstEndpoint().getSentence();
        int sentenceNumber = sentence.getSentenceNumberForOutput();
        String sentenceInfo = String.format("S%3d: \"%s\"", sentenceNumber, sentence.getText());

        return String.format("%-42s <--> %s", modelInfo, sentenceInfo);
    }

    /**
     * Returns the name of the project the results are based on.
     *
     * @return the name of the project the results are based on
     */
    public String getProjectName() {
        return DataRepositoryHelper.getProjectPipelineData(this.dataRepository).getProjectName();
    }

    /**
     * Returns the set of {@link TraceLink TraceLinks} that were found for the model with the given metamodel.
     *
     * @param metamodel the metamodel to get trace links for
     * @return {@link TraceLink TraceLinks} for the model with the given metamodel
     */
    public ImmutableSet<TraceLink<SentenceEntity, ModelEntity>> getTraceLinksForModel(Metamodel metamodel) {
        ConnectionState connectionState = this.getConnectionState(metamodel);
        if (connectionState != null) {
            return connectionState.getTraceLinks();
        }
        return Sets.immutable.empty();
    }

    /**
     * Returns the set of {@link TraceLink TraceLinks} for architecture models.
     *
     * @return set of {@link TraceLink TraceLinks} for architecture models
     */
    public ImmutableList<TraceLink<SentenceEntity, ModelEntity>> getArchitectureTraceLinks() {
        MutableSet<TraceLink<SentenceEntity, ModelEntity>> traceLinks = Sets.mutable.empty();

        for (var metamodel : this.getMetamodels()) {
            if (metamodel == Metamodel.ARCHITECTURE_ONLY_COMPONENTS) {
                traceLinks.addAll(this.getTraceLinksForModel(metamodel).castToCollection());
            }
        }
        return traceLinks.toImmutableList();
    }

    /**
     * Returns the set of {@link TraceLink TraceLinks} for architecture models as formatted strings. The strings are formatted to be human-readable.
     *
     * @return trace links as formatted strings
     */
    public List<String> getAllTraceLinksAsBeautifiedStrings() {
        return this.getArchitectureTraceLinks()
                .toSortedList(Comparator.comparingInt(tl -> tl.getFirstEndpoint().getSentence().getSentenceNumber()))
                .collect(ArDoCoResult::formatTraceLinksHumanReadable);
    }

    /**
     * Returns the list of {@link TraceLink TraceLinks} between architecture entities and code entities. If there are none, it returns an empty list.
     *
     * @return the list of {@link TraceLink TraceLinks} between architecture and code entities
     */
    public List<TraceLink<? extends ArchitectureEntity, ? extends ModelEntity>> getSamCodeTraceLinks() {
        var samCodeTraceabilityState = this.getCodeTraceabilityState();
        if (samCodeTraceabilityState != null) {
            return samCodeTraceabilityState.getSamCodeTraceLinks().toList();
        }
        return List.of();
    }

    /**
     * Returns the list of {@link TraceLink TraceLinks} between sentences and code entities. If there are none, it returns an empty list.
     *
     * @return the list of {@link TraceLink TraceLinks} between sentences and code entities
     */
    public List<TraceLink<SentenceEntity, ? extends ModelEntity>> getSadCodeTraceLinks() {
        var samCodeTraceabilityState = this.getCodeTraceabilityState();
        if (samCodeTraceabilityState != null) {
            return samCodeTraceabilityState.getSadCodeTraceLinks().toList();
        }
        return List.of();
    }

    /**
     * Returns all {@link Inconsistency inconsistencies} that were found for the model with the given metamodel.
     *
     * @param metamodel the metamodel to get inconsistencies for
     * @return inconsistencies for the model
     */
    public ImmutableList<Inconsistency> getAllInconsistenciesForModel(Metamodel metamodel) {
        InconsistencyState inconsistencyState = this.getInconsistencyState(metamodel);
        if (inconsistencyState != null) {
            return inconsistencyState.getInconsistencies();
        }
        return Lists.immutable.empty();
    }

    /**
     * Returns a list of {@link Inconsistency inconsistencies} that were found for the model with the given metamodel and that are of the specified
     * inconsistency class.
     *
     * @param metamodel         the metamodel to get inconsistencies for
     * @param inconsistencyType the type of inconsistency to filter for
     * @return inconsistencies for the model with the given type
     */
    public <T extends Inconsistency> ImmutableList<T> getInconsistenciesOfTypeForModel(Metamodel metamodel, Class<T> inconsistencyType) {
        return this.getAllInconsistenciesForModel(metamodel).select(i -> inconsistencyType.isAssignableFrom(i.getClass())).collect(inconsistencyType::cast);
    }

    /**
     * Returns a list of all {@link Inconsistency inconsistencies} that were found.
     *
     * @return all found inconsistencies
     */
    public ImmutableList<Inconsistency> getAllInconsistencies() {
        MutableList<Inconsistency> inconsistencies = Lists.mutable.empty();
        for (var model : this.getMetamodels()) {
            inconsistencies.addAll(this.getAllInconsistenciesForModel(model).castToCollection());
        }
        return inconsistencies.toImmutable();
    }

    /**
     * Returns all {@link TextInconsistency TextInconsistencies} that were found.
     *
     * @return all found TextInconsistencies
     */
    public ImmutableList<TextInconsistency> getAllTextInconsistencies() {
        var inconsistencies = this.getAllInconsistencies();
        return inconsistencies.select(i -> TextInconsistency.class.isAssignableFrom(i.getClass())).collect(TextInconsistency.class::cast);
    }

    /**
     * Returns all {@link ModelInconsistency ModelInconsistencies} that were found.
     *
     * @return all found ModelInconsistencies
     */
    public ImmutableList<ModelInconsistency> getAllModelInconsistencies() {
        var inconsistencies = this.getAllInconsistencies();
        return inconsistencies.select(i -> ModelInconsistency.class.isAssignableFrom(i.getClass())).collect(ModelInconsistency.class::cast);
    }

    /**
     * Returns a list of {@link InconsistentSentence InconsistentSentences}.
     *
     * @return all InconsistentSentences
     */
    public ImmutableList<InconsistentSentence> getInconsistentSentences() {
        Map<Integer, InconsistentSentence> incSentenceMap = new LinkedHashMap<>();

        var inconsistencies = this.getAllTextInconsistencies();
        for (var inconsistency : inconsistencies) {
            int sentenceNo = inconsistency.getSentenceNumber();
            var incSentence = incSentenceMap.get(sentenceNo);
            if (incSentence != null) {
                incSentence.addInconsistency(inconsistency);
            } else {
                var sentence = this.getSentence(sentenceNo);
                incSentence = new InconsistentSentence(sentence, inconsistency);
                incSentenceMap.put(sentenceNo, incSentence);
            }
        }

        var sortedInconsistentSentences = Lists.mutable.withAll(incSentenceMap.values()).sortThisByInt(i -> i.sentence().getSentenceNumberForOutput());
        return sortedInconsistentSentences.toImmutable();
    }

    /**
     * Returns the {@link Sentence} with the given sentence number.
     *
     * @param sentenceNo the sentence number
     * @return sentence with the given number
     */
    public Sentence getSentence(int sentenceNo) {
        return this.getText().getSentences().detect(s -> s.getSentenceNumberForOutput() == sentenceNo);
    }

    /**
     * Returns the internal {@link ConnectionState} for the model with the given metamodel or null if there is none.
     *
     * @param metamodel the metamodel to get the connection state for
     * @return the connection state or null if there is no {@link ConnectionState} for the given metamodel
     */
    public ConnectionState getConnectionState(Metamodel metamodel) {
        if (DataRepositoryHelper.hasConnectionStates(this.dataRepository)) {
            var connectionStates = DataRepositoryHelper.getConnectionStates(this.dataRepository);
            return connectionStates.getConnectionState(metamodel);
        }
        logger.warn("No ConnectionState found.");
        return null;
    }

    /**
     * Returns the internal {@link InconsistencyState} for the model with the given metamodel or null if there is none.
     *
     * @param metamodel the metamodel to get the inconsistency state for
     * @return the inconsistency state or null if there is no {@link InconsistencyState} for the given metamodel
     */
    public InconsistencyState getInconsistencyState(Metamodel metamodel) {
        if (DataRepositoryHelper.hasInconsistencyStates(this.dataRepository)) {
            var inconsistencyStates = DataRepositoryHelper.getInconsistencyStates(this.dataRepository);
            return inconsistencyStates.getInconsistencyState(metamodel);
        }
        logger.warn("No InconsistencyState found.");
        return null;
    }

    /**
     * Returns the internal {@link CodeTraceabilityState} or null if there is none.
     *
     * @return the {@link CodeTraceabilityState} or null if there is no {@link CodeTraceabilityState}
     */
    public CodeTraceabilityState getCodeTraceabilityState() {
        if (DataRepositoryHelper.hasCodeTraceabilityState(this.dataRepository)) {
            return DataRepositoryHelper.getCodeTraceabilityState(this.dataRepository);
        }
        logger.warn("No SamCodeTraceabilityState found.");
        return null;
    }

    /**
     * Returns the internal {@link ModelStates}.
     *
     * @return the ModelStates
     */
    private ModelStates getModelStates() {
        return DataRepositoryHelper.getModelStatesData(this.dataRepository);
    }

    /**
     * Returns a list of all metamodels for all the models that were loaded.
     *
     * @return list of all metamodels
     */
    public List<Metamodel> getMetamodels() {
        ModelStates modelStates = this.getModelStates();
        return Lists.mutable.ofAll(modelStates.getMetamodels());
    }

    /**
     * Returns the internal {@link Model} for the model with the given metamodel.
     *
     * @param metamodel the metamodel to get the model for
     * @return the Model
     */
    public Model getModelState(Metamodel metamodel) {
        ModelStates modelStates = this.getModelStates();
        return modelStates.getModel(metamodel);
    }

    /**
     * Returns the internal {@link TextState}.
     *
     * @return the TextState
     */
    public TextState getTextState() {
        return DataRepositoryHelper.getTextState(this.dataRepository);
    }

    /**
     * Returns the internal {@link RecommendationState} for the given {@link Metamodel} or null if there is none.
     *
     * @param metamodel the metamodel
     * @return the recommendation state or null if there is none
     */
    public RecommendationState getRecommendationState(Metamodel metamodel) {
        if (DataRepositoryHelper.hasRecommendationStates(this.dataRepository)) {
            var recommendationStates = DataRepositoryHelper.getRecommendationStates(this.dataRepository);
            return recommendationStates.getRecommendationState(metamodel);
        }
        ArDoCoResult.logger.warn("No RecommendationState found");
        return null;
    }

    /**
     * Returns the internal {@link PreprocessingData}.
     *
     * @return the preprocessing data
     */
    public PreprocessingData getPreprocessingData() {
        return this.dataRepository.getData(PreprocessingData.ID, PreprocessingData.class).orElseThrow();
    }

    /**
     * Returns the {@link Text}.
     *
     * @return the Text
     */
    public Text getText() {
        var preprocessingData = this.getPreprocessingData();
        return preprocessingData.getText();
    }
}
