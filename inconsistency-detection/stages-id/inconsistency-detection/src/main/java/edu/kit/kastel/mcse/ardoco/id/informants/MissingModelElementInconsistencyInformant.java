/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.id.informants;

import java.util.SortedMap;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.Model;
import edu.kit.kastel.mcse.ardoco.core.api.stage.connectiongenerator.ConnectionStates;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistencyState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistencyStates;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;
import edu.kit.kastel.mcse.ardoco.id.MissingElementInconsistencyCandidate;
import edu.kit.kastel.mcse.ardoco.id.MissingElementSupport;
import edu.kit.kastel.mcse.ardoco.id.types.MissingModelInstanceInconsistency;

public class MissingModelElementInconsistencyInformant extends Informant {

    @Configurable
    private double minSupport = 1;

    public MissingModelElementInconsistencyInformant(DataRepository dataRepository) {
        super(MissingModelElementInconsistencyInformant.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        var dataRepository = this.getDataRepository();
        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        var connectionStates = DataRepositoryHelper.getConnectionStates(dataRepository);
        var inconsistencyStates = DataRepositoryHelper.getInconsistencyStates(dataRepository);

        for (var metamodel : modelStates.getMetamodels()) {
            var model = modelStates.getModel(metamodel);
            this.findMissingModelElementInconsistencies(connectionStates, inconsistencyStates, model);
        }
    }

    private void findMissingModelElementInconsistencies(ConnectionStates connectionStates, InconsistencyStates inconsistencyStates, Model model) {
        Metamodel metamodel = model.getMetamodel();
        var inconsistencyState = inconsistencyStates.getInconsistencyState(metamodel);
        var connectionState = connectionStates.getConnectionState(metamodel);

        var candidates = Lists.mutable.<MissingElementInconsistencyCandidate>empty();

        var candidateElements = Lists.mutable.ofAll(inconsistencyState.getRecommendedInstances());
        var linkedRecommendedInstances = connectionState.getInstanceLinks().collect(TraceLink::getFirstEndpoint);

        // find recommendedInstances with no trace link (also not sharing words with linked RIs)
        candidateElements.removeAllIterable(linkedRecommendedInstances);
        this.filterCandidatesCoveredByRecommendedInstance(candidateElements, linkedRecommendedInstances);

        for (var candidate : candidateElements) {
            this.addToCandidates(candidates, candidate, MissingElementSupport.ELEMENT_WITH_NO_TRACE_LINK);
        }

        // methods for other kinds of support
        // NONE

        // finally create inconsistencies
        this.createInconsistencies(candidates, inconsistencyState);
    }

    /**
     * Filter those that are covered by other RecommendedInstances. covered means that they share at least one word
     *
     * @param candidateElements          candidate RecommendedInstances
     * @param linkedRecommendedInstances already linked RecommendedInstances
     */
    private void filterCandidatesCoveredByRecommendedInstance(MutableList<RecommendedInstance> candidateElements,
            ImmutableList<RecommendedInstance> linkedRecommendedInstances) {
        for (var linkedRecommendedInstance : linkedRecommendedInstances) {
            var linkedWords = linkedRecommendedInstance.getNameMappings().flatCollect(NounMapping::getWords);
            var candidatesToRemove = Lists.mutable.<RecommendedInstance>empty();
            for (var candidate : candidateElements) {
                if (CommonUtilities.wordListContainsAnyWordFromRecommendedInstance(linkedWords, candidate)) {
                    candidatesToRemove.add(candidate);
                }
            }
            candidateElements.removeAll(candidatesToRemove);
        }
    }

    private void addToCandidates(MutableList<MissingElementInconsistencyCandidate> candidates, RecommendedInstance recommendedInstance,
            MissingElementSupport support) {
        for (var candidate : candidates) {
            var candidateRecommendedInstance = candidate.getRecommendedInstance();
            if (candidateRecommendedInstance.equals(recommendedInstance)) {
                candidate.addSupport(MissingElementSupport.MULTIPLE_OVERLAPPING_RECOMMENDED_INSTANCES);
                return;
            }
            var candidateWords = candidateRecommendedInstance.getNameMappings().flatCollect(NounMapping::getWords);
            if (CommonUtilities.wordListContainsAnyWordFromRecommendedInstance(candidateWords, recommendedInstance)) {
                candidate.addSupport(MissingElementSupport.MULTIPLE_OVERLAPPING_RECOMMENDED_INSTANCES);
                // TODO what to do here?
                // A) return here, but for sure miss some correct sentences
                // B) do not return and do nothing else, this causes a lot of candidates
                // C) merge candidates? or merge the underlying recommendedInstances?
            }
        }

        var candidate = new MissingElementInconsistencyCandidate(recommendedInstance, support);
        candidates.add(candidate);
    }

    private void createInconsistencies(MutableList<MissingElementInconsistencyCandidate> candidates, InconsistencyState inconsistencyState) {
        for (var candidate : candidates) {
            var support = candidate.getAmountOfSupport();
            if (support >= this.minSupport) {
                RecommendedInstance recommendedInstance = candidate.getRecommendedInstance();
                double confidence = recommendedInstance.getProbability();
                for (var word : recommendedInstance.getNameMappings().flatCollect(NounMapping::getWords).distinct()) {
                    var sentenceNo = word.getSentenceNumber() + 1;
                    var wordText = word.getText();
                    inconsistencyState.addInconsistency(new MissingModelInstanceInconsistency(wordText, sentenceNo, confidence, candidate));
                }
            }
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> map) {
        // empty
    }
}
