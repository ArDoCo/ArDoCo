/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.heuristics;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;
import edu.kit.kastel.mcse.ardoco.core.api.models.architecture.ArchitectureComponent;
import edu.kit.kastel.mcse.ardoco.core.api.models.architecture.ArchitectureInterface;
import edu.kit.kastel.mcse.ardoco.core.api.models.code.CodeCompilationUnit;
import edu.kit.kastel.mcse.ardoco.core.api.models.code.CodePackage;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.NameComparisonUtils;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.Confidence;

public class PathResemblance extends StandaloneHeuristic {

    @Override
    protected Confidence calculateConfidence(ArchitectureComponent archComponent, CodeCompilationUnit compUnit) {
        return calculatePathResemblance(archComponent, compUnit);
    }

    @Override
    protected Confidence calculateConfidence(ArchitectureInterface archInterface, CodeCompilationUnit compUnit) {
        if (!archInterface.getMethodSignatures().isEmpty()) {
            return new Confidence();
        }
        return calculatePathResemblance(archInterface, compUnit);
    }

    private Confidence calculatePathResemblance(Entity archEndpoint, CodeCompilationUnit compUnit) {
        List<String> codeNames = NameComparisonUtils.getProcessedSplit(compUnit.getPathElements());
        Set<String> allPackageNames = getAllPackageNames();
        if (compUnit.hasParent()) {
            codeNames.removeAll(allPackageNames);
        }
        double similarity = NameComparisonUtils.getContainedRatio(archEndpoint, codeNames, NameComparisonUtils.PreprocessingMethod.NONE);
        if (similarity == 0) {
            return new Confidence();
        }
        return new Confidence(similarity);
    }

    private SortedSet<String> getAllPackageNames() {
        List<? extends CodePackage> packages = getCodeModel().getAllPackages();
        SortedSet<String> allPackageNames = new TreeSet<>();
        for (CodePackage codePackage : packages) {
            allPackageNames.add(codePackage.getName());
        }
        return allPackageNames;
    }

    @Override
    public String toString() {
        return "PathResemblance";
    }
}
