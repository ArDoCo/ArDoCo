/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation;

import java.util.Set;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.stage.codetraceability.ModelCodeTraceLink;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.computationtree.Node;

/**
 * A computation. Calculates the {@link Confidence confidences} and trace links for a computation tree.
 */
@Deterministic
public class Computation {

    private final Node root;
    private final ComputationResult result;

    /**
     * Creates a new computation with the specified computation tree and the specified models between which trace links will be calculated.
     *
     * @param root      the root of the computation tree
     * @param archModel the architecture model for which trace links will be calculated
     * @param codeModel the code model for which trace links will be calculated
     */
    public Computation(Node root, ArchitectureModel archModel, CodeModel codeModel) {
        this.root = root;
        this.result = root.compute(archModel, codeModel, new ComputationResult());
    }

    /**
     * Returns the result of the computation.
     *
     * @return the result of the computation
     */
    public ComputationResult getResult() {
        return result;
    }

    /**
     * Returns the trace links that this computation has calculated. The computation's tree root's result maps all endpoint tuples to a confidence. Every
     * endpoint tuple whose confidence has a value gets returned.
     *
     * @return trace links for every endpoint tuple whose confidence in the root's result has a value
     */
    public Set<ModelCodeTraceLink> getTraceLinks() {
        return result.getTraceLinks(root);
    }
}
