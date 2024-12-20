/* Licensed under MIT 2022-2024. */
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
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.PreprocessingData;
import edu.kit.kastel.mcse.ardoco.core.api.entity.ArchitectureEntity;
import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelStates;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.legacy.LegacyModelExtractionState;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.legacy.ModelInstance;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeCompilationUnit;
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
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.SadSamTraceLink;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.SamCodeTraceLink;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TransitiveTraceLink;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;

/**
 * This record represents the result of running ArDoCo. It is backed by a {@link DataRepository} and grabs data from it. Besides accessing all data from the
 * calculation steps, this record also provides some convenience methods to directly access results such as found trace links and detected inconsistencies.
 */
@Deterministic
public record ArDoCoResult(DataRepository dataRepository) {
    private static final Logger logger = LoggerFactory.getLogger(ArDoCoResult.class);

    /**
     * Returns the name of the project the results are based on.
     *
     * @return the name of the project the results are based on.
     */
    public String getProjectName() {
        return DataRepositoryHelper.getProjectPipelineData(this.dataRepository).getProjectName();
    }

    /**
     * Returns the set of {@link SadSamTraceLink}s that were found for the Model with the given ID.
     *
     * @param modelId the ID of the model that should be traced
     * @return Trace links for the model with the given id
     */
    public ImmutableSet<TraceLink<SentenceEntity, ArchitectureEntity>> getTraceLinksForModel(Metamodel modelId) {
        ConnectionState connectionState = this.getConnectionState(modelId);
        if (connectionState != null) {
            return connectionState.getTraceLinks();
        }
        return Sets.immutable.empty();
    }

    /**
     * Returns the set of {@link SadSamTraceLink}s that were found for the Model with the given ID as strings in the format "ModelElementId,SentenceNo".
     *
     * @param modelId the ID of the model that should be traced
     * @return Trace links for the model with the given id as Strings
     */
    public ImmutableSortedSet<String> getTraceLinksForModelAsStrings(Metamodel modelId) {
        var formatString = "%s,%d";
        return this.getTraceLinksForModel(modelId)
                .collect(tl -> String.format(formatString, tl.getSecondEndpoint().getId(), tl.getFirstEndpoint().getSentence().getSentenceNumber() + 1))
                .toImmutableSortedSet();
    }

    /**
     * Returns the set of {@link SadSamTraceLink}s
     *
     * @return set of Trace links
     */
    public ImmutableList<TraceLink<SentenceEntity, ArchitectureEntity>> getAllTraceLinks() {
        MutableSet<TraceLink<SentenceEntity, ArchitectureEntity>> traceLinks = Sets.mutable.empty();

        for (var modelId : this.getModelIds()) {
            if (modelId == Metamodel.ARCHITECTURE) {
                traceLinks.addAll(this.getTraceLinksForModel(modelId).castToCollection());
            }
        }
        return traceLinks.toImmutableList();
    }

    /**
     * Returns the set of {@link SadSamTraceLink SadSamTraceLinks} as strings. The strings are beautified to have a human-readable format
     *
     * @return Trace links as Strings
     */
    public List<String> getAllTraceLinksAsBeautifiedStrings() {
        return this.getAllTraceLinks()
                .toSortedList(Comparator.comparingInt(tl -> tl.getFirstEndpoint().getSentence().getSentenceNumber()))
                .collect(ArDoCoResult::formatTraceLinksHumanReadable);
    }

    private static String formatTraceLinksHumanReadable(TraceLink<SentenceEntity, ArchitectureEntity> traceLink) {
        String modelElementName = ((ModelInstance) traceLink.getSecondEndpoint()).getFullName();
        String modelElementUid = traceLink.getSecondEndpoint().getId();
        String modelInfo = String.format("%s (%s)", modelElementName, modelElementUid);

        var sentence = traceLink.getFirstEndpoint().getSentence();
        int sentenceNumber = sentence.getSentenceNumberForOutput();
        String sentenceInfo = String.format("S%3d: \"%s\"", sentenceNumber, sentence.getText());

        return String.format("%-42s <--> %s", modelInfo, sentenceInfo);
    }

    /**
     * Return the list of {@link SamCodeTraceLink SamCodeTraceLinks}. If there are none, it will return an empty list.
     *
     * @return the list of {@link SamCodeTraceLink SamCodeTraceLinks}.
     */
    public List<TraceLink<ArchitectureEntity, CodeCompilationUnit>> getSamCodeTraceLinks() {
        var samCodeTraceabilityState = this.getCodeTraceabilityState();
        if (samCodeTraceabilityState != null) {
            return samCodeTraceabilityState.getSamCodeTraceLinks().toList();
        }
        return List.of();
    }

    /**
     * Return the list of {@link TransitiveTraceLink TransitiveTraceLinks}. If there are none, it will return an empty list.
     *
     * @return the list of {@link TransitiveTraceLink TransitiveTraceLinks}.
     */
    public List<TraceLink<SentenceEntity, CodeCompilationUnit>> getSadCodeTraceLinks() {
        var samCodeTraceabilityState = this.getCodeTraceabilityState();
        if (samCodeTraceabilityState != null) {
            return samCodeTraceabilityState.getSadCodeTraceLinks().toList();
        }
        return List.of();
    }

    /**
     * Returns all {@link Inconsistency inconsistencies} that were found for the model with the given ID.
     *
     * @param modelId the ID of the model
     * @return Inconsistencies for the model
     */
    public ImmutableList<Inconsistency> getAllInconsistenciesForModel(Metamodel modelId) {
        InconsistencyState inconsistencyState = this.getInconsistencyState(modelId);
        if (inconsistencyState != null) {
            return inconsistencyState.getInconsistencies();
        }
        return Lists.immutable.empty();
    }

    /**
     * Returns a list of {@link Inconsistency inconsistencies} that were found for the model with the given ID and that are of the given Inconsistency class.
     *
     * @param modelId           the ID of the model
     * @param inconsistencyType type of the Inconsistency that should be returned
     * @param <T>               Type-parameter of the inconsistency
     * @return Inconsistencies for the model with the given type
     */
    public <T extends Inconsistency> ImmutableList<T> getInconsistenciesOfTypeForModel(Metamodel modelId, Class<T> inconsistencyType) {
        return this.getAllInconsistenciesForModel(modelId).select(i -> inconsistencyType.isAssignableFrom(i.getClass())).collect(inconsistencyType::cast);
    }

    /**
     * Returns a list of all {@link Inconsistency inconsistencies} that were found.
     *
     * @return all found inconsistencies
     */
    public ImmutableList<Inconsistency> getAllInconsistencies() {
        MutableList<Inconsistency> inconsistencies = Lists.mutable.empty();
        for (var model : this.getModelIds()) {
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
     * @return Sentence with the given number
     */
    public Sentence getSentence(int sentenceNo) {
        return this.getText().getSentences().detect(s -> s.getSentenceNumberForOutput() == sentenceNo);
    }

    /**
     * Returns the internal {@link ConnectionState} for the modelId with the given ID or null, if there is none.
     *
     * @param modelId the ID of the model
     * @return the connection state or null, if there is no {@link ConnectionState} for the given model ID
     */
    public ConnectionState getConnectionState(Metamodel modelId) {
        if (DataRepositoryHelper.hasConnectionStates(this.dataRepository)) {
            var connectionStates = DataRepositoryHelper.getConnectionStates(this.dataRepository);
            return connectionStates.getConnectionState(modelId);
        }
        ArDoCoResult.logger.warn("No ConnectionState found.");
        return null;
    }

    /**
     * Returns the internal {@link InconsistencyState} for the modelId with the given ID or null, if there is none.
     *
     * @param modelId the ID of the model
     * @return the inconsistency state or null, if there is no {@link InconsistencyState} for the given model ID
     */
    public InconsistencyState getInconsistencyState(Metamodel modelId) {
        if (DataRepositoryHelper.hasInconsistencyStates(this.dataRepository)) {
            var inconsistencyStates = DataRepositoryHelper.getInconsistencyStates(this.dataRepository);
            return inconsistencyStates.getInconsistencyState(modelId);
        }
        ArDoCoResult.logger.warn("No InconsistencyState found.");
        return null;
    }

    /**
     * Returns the internal {@link CodeTraceabilityState} or null, if there is none.
     *
     * @return the {@link CodeTraceabilityState} state or null, if there is no {@link CodeTraceabilityState} for the given model ID
     */
    public CodeTraceabilityState getCodeTraceabilityState() {
        if (DataRepositoryHelper.hasCodeTraceabilityState(this.dataRepository)) {
            return DataRepositoryHelper.getCodeTraceabilityState(this.dataRepository);
        }
        ArDoCoResult.logger.warn("No SamCodeTraceabilityState found.");
        return null;
    }

    /**
     * Returns the internal {@link ModelStates}
     *
     * @return the ModelStates
     */
    private ModelStates getModelStates() {
        return DataRepositoryHelper.getModelStatesData(this.dataRepository);
    }

    /**
     * Returns a list of all IDs for all the models that were loaded in.
     *
     * @return list of all model IDs
     */
    public List<Metamodel> getModelIds() {
        ModelStates modelStates = this.getModelStates();
        return Lists.mutable.ofAll(modelStates.modelIds());
    }

    /**
     * Returns the internal {@link LegacyModelExtractionState} for the modelId with the given ID.
     *
     * @param modelId the ID of the model
     * @return the LegacyModelExtractionState
     */
    public LegacyModelExtractionState getModelState(Metamodel modelId) {
        ModelStates modelStates = this.getModelStates();
        return modelStates.getModelExtractionState(modelId);
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
     * Returns the internal {@link RecommendationState} for the given {@link Metamodel} or null, if there is none.
     *
     * @param metamodel the metamodel
     * @return the recommendation state or null, if there is none
     */
    public RecommendationState getRecommendationState(Metamodel metamodel) {
        if (DataRepositoryHelper.hasRecommendationStates(this.dataRepository)) {
            var recommendationStates = DataRepositoryHelper.getRecommendationStates(this.dataRepository);
            return recommendationStates.getRecommendationState(metamodel);
        }
        ArDoCoResult.logger.warn("No RecommendationState found");
        return null;
    }

    public PreprocessingData getPreprocessingData() {
        return this.dataRepository.getData(PreprocessingData.ID, PreprocessingData.class).orElseThrow();
    }

    /**
     * Returns the {@link Text}
     *
     * @return the Text
     */
    public Text getText() {
        var preprocessingData = this.getPreprocessingData();
        return preprocessingData.getText();
    }
}
