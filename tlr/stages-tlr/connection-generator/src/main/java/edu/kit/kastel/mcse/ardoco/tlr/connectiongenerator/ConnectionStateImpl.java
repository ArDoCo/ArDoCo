/* Licensed under MIT 2021-2024. */
package edu.kit.kastel.mcse.ardoco.tlr.connectiongenerator;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.legacy.ModelInstance;
import edu.kit.kastel.mcse.ardoco.core.api.stage.connectiongenerator.ConnectionState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.connectiongenerator.InstanceLink;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.data.AbstractState;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

/**
 * The connection state encapsulates all connections between the model extraction state and the recommendation state.
 * These connections are stored in instance and relation links.
 */
public class ConnectionStateImpl extends AbstractState implements ConnectionState {

    private MutableList<TraceLink<RecommendedInstance, ModelInstance>> instanceLinks;

    /**
     * Creates a new connection state.
     */
    public ConnectionStateImpl() {
        super();
        this.instanceLinks = Lists.mutable.empty();
    }

    /**
     * Returns all instance links.
     *
     * @return all instance links
     */
    @Override
    public ImmutableList<TraceLink<RecommendedInstance, ModelInstance>> getInstanceLinks() {
        return Lists.immutable.withAll(this.instanceLinks);
    }

    /**
     * Returns all instance links with a model instance containing the given name.
     *
     * @param name the name of a model instance
     * @return all instance links with a model instance containing the given name as list
     */
    @Override
    public ImmutableList<TraceLink<RecommendedInstance, ModelInstance>> getInstanceLinksByName(String name) {
        return Lists.immutable.fromStream(this.instanceLinks.stream().filter(imapping -> imapping.getSecondEndpoint().getNameParts().contains(name)));
    }

    /**
     * Returns all instance links with a model instance containing the given type.
     *
     * @param type the type of a model instance
     * @return all instance links with a model instance containing the given type as list
     */
    @Override
    public ImmutableList<TraceLink<RecommendedInstance, ModelInstance>> getInstanceLinksByType(String type) {
        return Lists.immutable.fromStream(this.instanceLinks.stream().filter(ilink -> ilink.getSecondEndpoint().getTypeParts().contains(type)));
    }

    @Override
    public ImmutableList<TraceLink<RecommendedInstance, ModelInstance>> getInstanceLinksByRecommendedInstance(RecommendedInstance recommendedInstance) {
        return Lists.immutable.fromStream(this.instanceLinks.stream().filter(il -> il.getFirstEndpoint().equals(recommendedInstance)));
    }

    /**
     * Returns all instance links with a model instance containing the given name and type.
     *
     * @param type the type of a model instance
     * @param name the name of a model instance
     * @return all instance links with a model instance containing the given name and type as list
     */
    @Override
    public ImmutableList<TraceLink<RecommendedInstance, ModelInstance>> getInstanceLinks(String name, String type) {
        return Lists.immutable.fromStream(this.instanceLinks.stream()
                .filter(imapping -> imapping.getSecondEndpoint().getNameParts().contains(name))//
                .filter(imapping -> imapping.getSecondEndpoint().getTypeParts().contains(type)));
    }

    /**
     * Adds the connection of a recommended instance and a model instance to the state. If the model instance is already
     * contained by the state it is extended. Elsewhere a new instance link is created
     *
     * @param recommendedModelInstance the recommended instance
     * @param instance                 the model instance
     * @param probability              the probability of the link
     */
    @Override
    public void addToLinks(RecommendedInstance recommendedModelInstance, ModelInstance instance, Claimant claimant, double probability) {

        var newInstanceLink = new InstanceLink(recommendedModelInstance, instance, claimant, probability);
        if (!this.isContainedByInstanceLinks(newInstanceLink)) {
            this.instanceLinks.add(newInstanceLink);
        } else {
            var optionalInstanceLink = this.instanceLinks.stream().filter(il -> il.equals(newInstanceLink)).findFirst();
            if (optionalInstanceLink.isPresent()) {
                var existingInstanceLink = optionalInstanceLink.get();
                var newNameMappings = newInstanceLink.getFirstEndpoint().getNameMappings();
                var newTypeMappings = newInstanceLink.getFirstEndpoint().getTypeMappings();
                existingInstanceLink.getFirstEndpoint().addMappings(newNameMappings, newTypeMappings);
            }
        }
    }

    /**
     * Checks if an instance link is already contained by the state.
     *
     * @param instanceLink the given instance link
     * @return true if it is already contained
     */
    @Override
    public boolean isContainedByInstanceLinks(TraceLink<RecommendedInstance, ModelInstance> instanceLink) {
        return this.instanceLinks.contains(instanceLink);
    }

    /**
     * Removes an instance link from the state
     *
     * @param instanceMapping the instance link to remove
     */
    @Override
    public void removeFromMappings(TraceLink<RecommendedInstance, ModelInstance> instanceMapping) {
        this.instanceLinks.remove(instanceMapping);
    }

    /**
     * Removes all instance links containing the given instance
     *
     * @param instance the given instance
     */
    @Override
    public void removeAllInstanceLinksWith(ModelInstance instance) {
        this.instanceLinks.removeIf(mapping -> mapping.getSecondEndpoint().equals(instance));
    }

    /**
     * Removes all instance links containing the given recommended instance
     *
     * @param instance the given recommended instance
     */
    @Override
    public void removeAllInstanceLinksWith(RecommendedInstance instance) {
        this.instanceLinks.removeIf(mapping -> mapping.getFirstEndpoint().equals(instance));
    }

}
