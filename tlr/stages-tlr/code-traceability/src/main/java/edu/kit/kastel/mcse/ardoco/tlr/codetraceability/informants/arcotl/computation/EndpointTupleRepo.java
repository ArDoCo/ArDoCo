/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation;

import java.util.ArrayList;
import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeCompilationUnit;
import edu.kit.kastel.mcse.ardoco.core.common.tuple.Pair;

/**
 * A repository of endpoint tuples. An endpoint tuple consists of an architecture endpoint and a code endpoint. Contains every possible combination of endpoints
 * of an architecture model and a code model.
 */
//TODO: Phi Rename
public class EndpointTupleRepo {

    private final List<Pair<ArchitectureItem, CodeCompilationUnit>> endpointTuples;

    /**
     * Creates a new repository of endpoint tuples. Contains every possible combination of endpoints of the specified architecture model and the specified code
     * model.
     *
     * @param archModel the architecture model whose endpoints are to be part of the repository
     * @param codeModel the code model whose endpoints are to be part of the repository
     */
    public EndpointTupleRepo(ArchitectureModel archModel, CodeModel codeModel) {
        this.endpointTuples = new ArrayList<>();
        for (var architectureEndpoint : archModel.getEndpoints()) {
            for (var codeEndpoint : codeModel.getEndpoints()) {
                if (codeEndpoint instanceof CodeCompilationUnit codeCompilationUnit) {
                    // Currently only supported for codeCompilationUnits due to Heuristics
                    this.endpointTuples.add(new Pair<>(architectureEndpoint, codeCompilationUnit));
                }
            }
        }
    }

    /**
     * Returns all endpoint tuples.
     *
     * @return all endpoint tuples
     */
    public List<Pair<ArchitectureItem, CodeCompilationUnit>> getEndpointTuples() {
        return new ArrayList<>(this.endpointTuples);
    }
}
