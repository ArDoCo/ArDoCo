/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.id.tests.eval.baseline;

import java.util.SortedMap;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.MutableSet;

import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistencyState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistencyStates;
import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;
import edu.kit.kastel.mcse.ardoco.id.InconsistencyStatesImpl;
import edu.kit.kastel.mcse.ardoco.id.types.MissingModelInstanceInconsistency;

/**
 * Informant for {@link InconsistencyBaseline}
 */
public class InconsistencyBaselineInformant extends Informant {
    protected InconsistencyBaselineInformant(DataRepository dataRepository) {
        super(InconsistencyBaselineInformant.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        var inconsistencyStates = InconsistencyStatesImpl.build();
        DataRepository dataRepository = this.getDataRepository();
        dataRepository.addData(InconsistencyStates.ID, inconsistencyStates);

        var text = DataRepositoryHelper.getAnnotatedText(dataRepository);
        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        var connectionStates = DataRepositoryHelper.getConnectionStates(dataRepository);

        var sentences = Sets.mutable.fromStream(text.getSentences().stream().map(Sentence::getSentenceNumber));
        for (var metamodel : modelStates.getMetamodels()) {
            var traceLinks = connectionStates.getConnectionState(metamodel).getTraceLinks();
            var sentencesWithTraceLinks = traceLinks.collect(it -> it.getFirstEndpoint().getSentence().getSentenceNumber()).toSet();
            MutableSet<Integer> sentencesWithoutTraceLinks = sentences.withoutAll(sentencesWithTraceLinks);

            InconsistencyState inconsistencyState = inconsistencyStates.getInconsistencyState(metamodel);
            for (var sentence : sentencesWithoutTraceLinks) {
                inconsistencyState.addInconsistency(new MissingModelInstanceInconsistency("", sentence + 1, 0.69, null));
            }
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> map) {
        // empty
    }
}
