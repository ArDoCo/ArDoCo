/* Licensed under MIT 2023-2024. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants;

import java.util.SortedMap;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;

import edu.kit.kastel.mcse.ardoco.core.api.codetraceability.CodeTraceabilityState;
import edu.kit.kastel.mcse.ardoco.core.api.connectiongenerator.ConnectionStates;
import edu.kit.kastel.mcse.ardoco.core.api.entity.ArchitectureEntity;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelStates;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeCompilationUnit;
import edu.kit.kastel.mcse.ardoco.core.api.models.tracelinks.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.api.models.tracelinks.TransitiveTraceLink;
import edu.kit.kastel.mcse.ardoco.core.api.text.SentenceEntity;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;

@Deterministic
public class TraceLinkCombiner extends Informant {

    public TraceLinkCombiner(DataRepository dataRepository) {
        super(TraceLinkCombiner.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        MutableSet<TraceLink<SentenceEntity, CodeCompilationUnit>> transitiveTraceLinks = Sets.mutable.empty();
        CodeTraceabilityState codeTraceabilityState = DataRepositoryHelper.getCodeTraceabilityState(this.getDataRepository());
        ModelStates modelStatesData = DataRepositoryHelper.getModelStatesData(this.getDataRepository());
        ConnectionStates connectionStates = DataRepositoryHelper.getConnectionStates(this.getDataRepository());

        if (codeTraceabilityState == null || modelStatesData == null || connectionStates == null) {
            return;
        }
        var samCodeTraceLinks = codeTraceabilityState.getSamCodeTraceLinks();
        for (var modelId : modelStatesData.modelIds()) {
            var metamodel = modelStatesData.getModelExtractionState(modelId).getMetamodel();
            var connectionState = connectionStates.getConnectionState(metamodel);
            var sadSamTraceLinks = connectionState.getTraceLinks();

            var combinedLinks = this.combineToTransitiveTraceLinks(sadSamTraceLinks, samCodeTraceLinks);
            transitiveTraceLinks.addAll(combinedLinks.toList());
        }

        codeTraceabilityState.addSadCodeTraceLinks(transitiveTraceLinks);
    }

    private ImmutableSet<TraceLink<SentenceEntity, CodeCompilationUnit>> combineToTransitiveTraceLinks(
            ImmutableSet<? extends TraceLink<SentenceEntity, ArchitectureEntity>> sadSamTraceLinks,
            ImmutableSet<? extends TraceLink<ArchitectureEntity, CodeCompilationUnit>> samCodeTraceLinks) {

        MutableSet<TraceLink<SentenceEntity, CodeCompilationUnit>> transitiveTraceLinks = Sets.mutable.empty();

        for (TraceLink<SentenceEntity, ArchitectureEntity> sadSamTraceLink : sadSamTraceLinks) {
            String modelElementUid = sadSamTraceLink.getSecondEndpoint().getId();
            for (TraceLink<ArchitectureEntity, CodeCompilationUnit> samCodeTraceLink : samCodeTraceLinks) {
                String samCodeTraceLinkModelElementId = samCodeTraceLink.asPair().first().getId();
                if (modelElementUid.equals(samCodeTraceLinkModelElementId)) {
                    var transitiveTraceLinkOptional = TransitiveTraceLink.createTransitiveTraceLink(sadSamTraceLink, samCodeTraceLink);
                    transitiveTraceLinkOptional.ifPresent(it -> transitiveTraceLinks.add(it));
                }
            }
        }
        return transitiveTraceLinks.toImmutable();
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        // empty
    }
}
