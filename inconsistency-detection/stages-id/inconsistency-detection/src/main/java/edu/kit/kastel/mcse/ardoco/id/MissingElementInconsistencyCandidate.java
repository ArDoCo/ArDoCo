/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.id;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import org.eclipse.collections.api.factory.SortedSets;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;

public class MissingElementInconsistencyCandidate implements Serializable {

    @Serial
    private static final long serialVersionUID = 6718278829646931607L;
    private final RecommendedInstance recommendedInstance;
    private final MutableSortedSet<MissingElementSupport> supports = SortedSets.mutable.empty();

    public MissingElementInconsistencyCandidate(RecommendedInstance recommendedInstance, MissingElementSupport support) {
        this.recommendedInstance = recommendedInstance;
        supports.add(support);
    }

    /**
     * Adds support for this candidate. If the support was already added before, it will not add it again.
     *
     * @param support the support that should be added
     * @return true if this candidate did not already contain the specified support
     */
    public boolean addSupport(MissingElementSupport support) {
        return supports.add(support);
    }

    /**
     * Returns an integer to show how much support this candidate has
     *
     * @return the amount of support
     */
    public int getAmountOfSupport() {
        return supports.size();
    }

    /**
     * Returns the {@link RecommendedInstance}
     *
     * @return the recommendedInstance
     */
    public RecommendedInstance getRecommendedInstance() {
        return recommendedInstance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recommendedInstance);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MissingElementInconsistencyCandidate other)) {
            return false;
        }
        return Objects.equals(recommendedInstance, other.recommendedInstance);
    }

    @Override
    public String toString() {
        return "MissingElementInconsistencyCandidate [" + (recommendedInstance != null ? "recommendedInstance=" + recommendedInstance : "") + "]";
    }

}
