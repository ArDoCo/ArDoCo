/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeCompilationUnit;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.stage.codetraceability.ModelCodeTraceLink;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.common.tuple.Pair;

/**
 * The result of a computation node. A computation node's final result are the calculated {@link Confidence confidences} of every endpoint tuple.
 */
@Deterministic
public class NodeResult {

    private final Map<Pair<ArchitectureItem, CodeCompilationUnit>, Confidence> confidenceMap;

    /**
     * Creates a new computation node result. It is initially empty, so the confidences of the endpoint tuples still need to be added after they have been
     * calculated.
     */
    public NodeResult() {
        this.confidenceMap = new LinkedHashMap<>();
    }

    public NodeResult(ArchitectureModel archModel, CodeModel codeModel) {
        this.confidenceMap = new LinkedHashMap<>();
        EndpointTupleRepo endpointTupleRepo = new EndpointTupleRepo(archModel, codeModel);
        for (var endpointTuple : endpointTupleRepo.getEndpointTuples()) {
            this.add(endpointTuple, new Confidence());
        }
    }

    /**
     * Returns the calculated {@link Confidence confidence} of the specified endpoint tuple. Returns null if no confidence for the specified endpoint tuple has
     * been calculated and added to this result yet.
     *
     * @param endpointTuple the endpoint tuple for which the confidence is to be returned
     * @return the confidence of the endpoint tuple, or null if it doesn't exist yet
     */
    public Confidence getConfidence(Pair<ArchitectureItem, CodeCompilationUnit> endpointTuple) {
        return this.confidenceMap.get(endpointTuple);
    }

    public Confidence getBestConfidence(Entity endpoint) {
        Confidence max = new Confidence();
        for (var entry : this.confidenceMap.entrySet()) {
            Pair<ArchitectureItem, CodeCompilationUnit> tuple = entry.getKey();
            Confidence confidence = entry.getValue();
            if (tuple.hasElement(endpoint) && confidence.compareTo(max) > 0) {
                max = confidence;
            }
        }
        return max;
    }

    public NodeResult getEndpointTuples(Entity endpoint, Confidence confidence) {
        NodeResult result = new NodeResult();
        for (var entry : this.confidenceMap.entrySet()) {
            Pair<ArchitectureItem, CodeCompilationUnit> endpointTuple = entry.getKey();
            Confidence otherConfidence = entry.getValue();
            if (endpointTuple.hasElement(endpoint) && confidence.equals(otherConfidence)) {
                result.add(endpointTuple, otherConfidence);
            }
        }
        return result;
    }

    public SortedSet<Entity> getLinkedEndpoints(Entity endpoint) {
        SortedSet<Entity> linkedEndpoints = new TreeSet<>();
        for (var entry : this.confidenceMap.entrySet()) {
            Pair<ArchitectureItem, CodeCompilationUnit> tuple = entry.getKey();
            Confidence confidence = entry.getValue();
            if (tuple.hasElement(endpoint) && confidence.hasValue()) {
                linkedEndpoints.add((Entity) tuple.getOtherElement(endpoint));
            }
        }
        return linkedEndpoints;
    }

    /**
     * Returns trace links based on this computation node result. Every endpoint tuple for which a confidence has been calculated and added to this result gets
     * considered. Only returns a trace link for an endpoint tuple if its confidence has a value.
     *
     * @return trace links for every endpoint tuple whose confidence has a value
     */
    public Set<ModelCodeTraceLink> getTraceLinks() {
        Set<ModelCodeTraceLink> traceLinks = new LinkedHashSet<>();
        for (var entry : this.confidenceMap.entrySet()) {
            Confidence confidence = entry.getValue();
            if (confidence.hasValue()) {
                Pair<ArchitectureItem, CodeCompilationUnit> endpointTuple = entry.getKey();
                ArchitectureItem architectureEndpoint = endpointTuple.first();
                CodeItem codeEndpoint = endpointTuple.second();
                traceLinks.add(new ModelCodeTraceLink(architectureEndpoint, codeEndpoint));
            }
        }
        return traceLinks;
    }

    public boolean hasTraceLink(Entity endpoint) {
        for (var entry : this.confidenceMap.entrySet()) {
            Pair<ArchitectureItem, CodeCompilationUnit> tuple = entry.getKey();
            Confidence confidence = entry.getValue();
            if (tuple.hasElement(endpoint) && confidence.hasValue()) {
                return true;
            }
        }
        return false;
    }

    public NodeResult getResultForEndpoint(Entity endpoint) {
        NodeResult result = new NodeResult();
        for (var entry : this.confidenceMap.entrySet()) {
            Pair<ArchitectureItem, CodeCompilationUnit> tuple = entry.getKey();
            Confidence confidence = entry.getValue();
            if (tuple.hasElement(endpoint)) {
                result.add(tuple, confidence);
            }
        }
        return result;
    }

    public NodeResult filter(NodeResult resultToFilter) {
        NodeResult result = new NodeResult();
        for (var entry : this.confidenceMap.entrySet()) {
            Pair<ArchitectureItem, CodeCompilationUnit> tuple = entry.getKey();
            Confidence confidence = entry.getValue();
            if (resultToFilter.getConfidence(tuple).hasValue()) {
                result.add(tuple, new Confidence());
            } else {
                result.add(tuple, confidence);
            }
        }
        return result;
    }

    /**
     * Adds the calculated {@link Confidence confidence} of the specified endpoint tuple.
     *
     * @param endpointTuple the endpoint tuple whose confidence is to be added
     * @param confidence    the confidence of the endpoint tuple
     */
    public void add(Pair<ArchitectureItem, CodeCompilationUnit> endpointTuple, Confidence confidence) {
        this.confidenceMap.put(endpointTuple, confidence);
    }

    public void addAll(NodeResult partialResult) {
        this.confidenceMap.putAll(partialResult.confidenceMap);
    }
}
