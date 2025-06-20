/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.aggregation;

import java.util.List;
import java.util.Objects;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.NodeResult;

public abstract class Matcher extends Aggregation {

    protected enum EndpointType {
        ARCHITECTURE("Architecture"), CODE("Code");

        private final String name;

        EndpointType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private final EndpointType endpointTypeToMatch;

    protected Matcher(EndpointType endpointsToUse) {
        this.endpointTypeToMatch = endpointsToUse;
    }

    @Override
    public NodeResult calculateConfidences(ArchitectureModel archModel, CodeModel codeModel, List<NodeResult> childrenResults) {
        NodeResult matchResult = new NodeResult(archModel, codeModel);
        List<? extends Entity> endpoints = switch (endpointTypeToMatch) {
            case ARCHITECTURE -> archModel.getEndpoints();
            case CODE -> codeModel.getEndpoints();
        };
        for (Entity endpointToMatch : endpoints) {
            NodeResult partialMatchResult = matchEndpoint(endpointToMatch, childrenResults);
            matchResult.addAll(partialMatchResult);
        }
        return matchResult;
    }

    protected abstract NodeResult matchEndpoint(Entity archEndpoint, List<NodeResult> childrenResults);

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), endpointTypeToMatch);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Matcher other)) {
            return false;
        }
        return Objects.equals(endpointTypeToMatch, other.endpointTypeToMatch);
    }

    @Override
    public String toString() {
        return endpointTypeToMatch.getName() + "Matcher";
    }
}
