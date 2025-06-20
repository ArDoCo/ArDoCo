/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.id.informants;

import java.util.List;
import java.util.SortedMap;
import java.util.regex.Pattern;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;
import edu.kit.kastel.mcse.ardoco.core.api.entity.ModelEntity;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistencyState;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;
import edu.kit.kastel.mcse.ardoco.id.agents.UndocumentedModelElementInconsistencyAgent;
import edu.kit.kastel.mcse.ardoco.id.types.MissingTextForModelElementInconsistency;

/**
 * This informant for the {@link UndocumentedModelElementInconsistencyAgent} implements the logic to find model elements that are undocumented. To do so, it
 * checks for each model element whether it is mentioned a minimum number of times (default: 1).
 */
public class UndocumentedModelElementInconsistencyInformant extends Informant {

    @Configurable
    private int minimumNeededTraceLinks = 1;
    @Configurable
    private List<String> whitelist = Lists.mutable.of();
    @Configurable
    private List<String> types = Lists.mutable.of("Component", "BasicComponent", "CompositeComponent");

    public UndocumentedModelElementInconsistencyInformant(DataRepository dataRepository) {
        super(UndocumentedModelElementInconsistencyInformant.class.getSimpleName(), dataRepository);
    }

    public static boolean modelInstanceHasTargetedType(ModelEntity modelEntity, List<String> types) {
        var entityType = modelEntity.getType();
        if (entityType.isPresent()) {
            if (types.contains(entityType.orElseThrow())) {
                return true;
            }
            for (var instanceType : modelEntity.getTypeParts().orElseThrow()) {
                if (types.contains(instanceType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static MutableList<ModelEntity> filterWithWhitelist(MutableList<ModelEntity> candidateModelInstances, List<String> whitelist) {
        var filteredCandidates = Lists.mutable.ofAll(candidateModelInstances);
        for (var whitelisting : whitelist) {
            var pattern = Pattern.compile(whitelisting);
            var whitelistedCandidates = filteredCandidates.select(c -> {
                if (pattern.matcher(c.getName()).matches()) {
                    return true;
                }
                for (var name : c.getNameParts()) {
                    if (pattern.matcher(name).matches()) {
                        return true;
                    }
                }
                return false;
            });
            filteredCandidates.removeAll(whitelistedCandidates);
        }
        return filteredCandidates;
    }

    @Override
    public void process() {
        var dataRepository = this.getDataRepository();
        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        var connectionStates = DataRepositoryHelper.getConnectionStates(dataRepository);
        var inconsistencyStates = DataRepositoryHelper.getInconsistencyStates(dataRepository);

        for (var metamodel : modelStates.getMetamodels()) {
            var model = modelStates.getModel(metamodel);
            var connectionState = connectionStates.getConnectionState(metamodel);
            var inconsistencyState = inconsistencyStates.getInconsistencyState(metamodel);

            var linkedModelInstances = connectionState.getInstanceLinks().collect(TraceLink::getSecondEndpoint).distinct();

            // find model instances of given types that are not linked and, thus, are candidates
            MutableList<ModelEntity> candidateEntities = Lists.mutable.empty();
            for (ModelEntity entity : model.getEndpoints()) {
                if (modelInstanceHasTargetedType(entity, this.types) && !this.modelInstanceHasMinimumNumberOfAppearances(linkedModelInstances, entity)) {
                    candidateEntities.add(entity);
                }
            }

            // further filtering
            candidateEntities = filterWithWhitelist(candidateEntities, this.whitelist);

            // create Inconsistencies
            this.createInconsistencies(candidateEntities, inconsistencyState);
        }
    }

    private boolean modelInstanceHasMinimumNumberOfAppearances(ImmutableList<ModelEntity> linkedModelEntities, Entity modelInstance) {
        return linkedModelEntities.count(modelInstance::equals) >= this.minimumNeededTraceLinks;
    }

    private void createInconsistencies(MutableList<ModelEntity> candidateEntities, InconsistencyState inconsistencyState) {
        for (var candidate : candidateEntities) {
            var inconsistency = new MissingTextForModelElementInconsistency(candidate);
            inconsistencyState.addInconsistency(inconsistency);
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> map) {
        // empty
    }
}
