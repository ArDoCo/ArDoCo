/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.aggregation;

import java.util.ArrayList;
import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeCompilationUnit;
import edu.kit.kastel.mcse.ardoco.core.common.tuple.Pair;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.Confidence;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.EndpointTupleRepo;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.NodeResult;

public abstract class ConfidenceAggregator extends Aggregation {

    @Override
    public NodeResult calculateConfidences(ArchitectureModel archModel, CodeModel codeModel, List<NodeResult> childrenResults) {
        NodeResult nodeResult = new NodeResult();
        EndpointTupleRepo endpointTupleRepo = new EndpointTupleRepo(archModel, codeModel);
        for (Pair<ArchitectureItem, CodeCompilationUnit> endpointTuple : endpointTupleRepo.getEndpointTuples()) {
            Confidence confidence = this.aggregateConfidences(this.getConfidences(childrenResults, endpointTuple));
            nodeResult.add(endpointTuple, confidence);
        }
        return nodeResult;
    }

    /**
     * Returns from each of the specified node results the confidence of the specified endpoint tuple. The returned confidences have the same order as the
     * specified node results. Throws an {@code IllegalStateException} if not all of the specified node results have a calculated confidence for the specified
     * endpoint tuple.
     *
     * @param results       the node results for which confidences are to be returned
     * @param endpointTuple the endpoint tuple for which confidences are to be returned
     * @return the specified endpoint tuple's confidences from all of the specified node results
     * @throws IllegalStateException if not all of the specified node results have a calculated confidence for the specified endpoint tuple
     */
    private List<Confidence> getConfidences(List<NodeResult> results, Pair<ArchitectureItem, CodeCompilationUnit> endpointTuple) {
        List<Confidence> confidences = new ArrayList<>();
        for (NodeResult result : results) {
            Confidence confidence = result.getConfidence(endpointTuple);
            if (null == confidence) {
                throw new IllegalStateException("For all of the nodes the endpoint tuple's confidence must have been calculated");
            }
            confidences.add(confidence);
        }
        return confidences;
    }

    /**
     * Aggregates the specified {@link Confidence confidences}.
     *
     * @param confidences the confidences to be aggregated
     * @return the aggregated confidence
     */
    protected abstract Confidence aggregateConfidences(List<Confidence> confidences);
}
