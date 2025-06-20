/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency;

import java.util.List;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.configuration.IConfigurable;

/**
 * State holding data and information about inconsistencies.
 */
public interface InconsistencyState extends IConfigurable {

    /**
     * Returns a list of inconsistencies held by this state.
     *
     * @return list of inconsistencies
     */
    ImmutableList<Inconsistency> getInconsistencies();

    /**
     * Add an inconsistency to this state.
     *
     * @param inconsistency the inconsistency to add
     * @return true if added successfully
     */
    boolean addInconsistency(Inconsistency inconsistency);

    /**
     * Remove an inconsistency from this state.
     *
     * @param inconsistency the inconsistency to remove
     * @return true if removed successfully
     */
    boolean removeInconsistency(Inconsistency inconsistency);

    /**
     * Add multiple recommended instances to this state.
     *
     * @param recommendedInstances the recommended instances to add
     * @return true if all were added successfully
     */
    default boolean addRecommendedInstances(List<RecommendedInstance> recommendedInstances) {
        var success = true;
        for (var recommendedInstance : recommendedInstances) {
            success &= addRecommendedInstance(recommendedInstance);
        }
        return success;
    }

    /**
     * Add a single recommended instance to this state.
     *
     * @param recommendedInstance the recommended instance to add
     * @return true if added successfully
     */
    boolean addRecommendedInstance(RecommendedInstance recommendedInstance);

    /**
     * Remove multiple recommended instances from this state.
     *
     * @param recommendedInstances the recommended instances to remove
     * @return true if all were removed successfully
     */
    default boolean removeRecommendedInstances(List<RecommendedInstance> recommendedInstances) {
        var success = true;
        for (var recommendedInstance : recommendedInstances) {
            success &= removeRecommendedInstance(recommendedInstance);
        }
        return success;
    }

    /**
     * Remove a single recommended instance from this state.
     *
     * @param recommendedInstance the recommended instance to remove
     * @return true if removed successfully
     */
    boolean removeRecommendedInstance(RecommendedInstance recommendedInstance);

    /**
     * Sets the recommended instances.
     *
     * @param recommendedInstances the recommended instances to set
     */
    void setRecommendedInstances(List<RecommendedInstance> recommendedInstances);

    /**
     * Returns the recommended instances.
     *
     * @return the recommended instances
     */
    MutableList<RecommendedInstance> getRecommendedInstances();

}
