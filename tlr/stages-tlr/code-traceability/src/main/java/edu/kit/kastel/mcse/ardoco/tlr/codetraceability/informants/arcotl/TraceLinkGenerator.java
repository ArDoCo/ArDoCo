/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.stage.codetraceability.ModelCodeTraceLink;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.Computation;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.computationtree.Node;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.aggregation.Filter;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.aggregation.MatchBest;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.aggregation.MatchSequentially;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.aggregation.Maximum;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.heuristics.*;

@Deterministic
public final class TraceLinkGenerator {

    private static final Node interfaceName = new ComponentNameResemblance(ComponentNameResemblance.NameConfig.INTERFACE,
            NameComparisonUtils.PreprocessingMethod.NONE).getNode();
    private static final Node interfaceMethod = new MethodResemblance().getNode();
    private static final Node interfaceBest = MatchBest.getMatchBestCodeNode(MatchBest.getMatchBestArchNode(Maximum.getMaximumNode(interfaceName,
            interfaceMethod)));

    private static final Node packageNodeStem = new PackageResemblance(NameComparisonUtils.PreprocessingMethod.STEMMING).getNode();
    private static final Node packageBest = MatchBest.getMatchBestCodeNode(MatchBest.getMatchBestArchNode(packageNodeStem));
    private static final Node packageFiltered = Filter.getFilterArchNode(packageBest, new SubpackageFilter().getNode(packageBest));

    private static final Node compName = new ComponentNameResemblance(ComponentNameResemblance.NameConfig.COMPONENT,
            NameComparisonUtils.PreprocessingMethod.NONE).getNode();
    private static final Node compNameBest = MatchBest.getMatchBestCodeNode(compName);
    private static final Node compNameInherited = Maximum.getMaximumNode(new InheritLinks().getNode(compNameBest), compNameBest);

    private static final Node compCombined = Maximum.getMaximumNode(MatchSequentially.getMatchSeqArchNode(packageFiltered, compNameInherited),
            new ComponentNameResemblance(ComponentNameResemblance.NameConfig.COMPONENT_WITHOUT_PACKAGE, NameComparisonUtils.PreprocessingMethod.NONE)
                    .getNode());

    private static final Node commonWords = Maximum.getMaximumNode(new ComponentNameResemblanceTest().getNode(compCombined), compCombined);

    private static final Node compFiltered = Filter.getFilterArchNode(commonWords, new Required().getNode(commonWords));

    private static final Node path = new PathResemblance().getNode();
    private static final Node pathBest = MatchBest.getMatchBestCodeNode(MatchBest.getMatchBestArchNode(path));
    private static final Node maxCompInterface = Maximum.getMaximumNode(pathBest, compFiltered, interfaceBest);

    private static final Node root = Filter.getFilterArchNode(maxCompInterface, new ProvidedInterfaceCorrespondence().getNode(maxCompInterface));

    private static final Map<Node, String> treeConfigs = new LinkedHashMap<>();

    static {
        treeConfigs.put(interfaceName, "interfaceName");
        treeConfigs.put(interfaceMethod, "interfaceMethod");
        treeConfigs.put(interfaceBest, "interfaceBest");

        treeConfigs.put(packageNodeStem, "packageStemming");
        treeConfigs.put(packageBest, "packageBest");
        treeConfigs.put(packageFiltered, "subpackageRemoval");

        treeConfigs.put(compName, "compName");
        treeConfigs.put(compNameBest, "compNameBest");
        treeConfigs.put(compNameInherited, "hintInheritance");
        treeConfigs.put(compCombined, "packageAndName");

        treeConfigs.put(commonWords, "commonWords");
        treeConfigs.put(compFiltered, "compLinks");

        treeConfigs.put(path, "path");
        treeConfigs.put(pathBest, "pathBest");
        treeConfigs.put(maxCompInterface, "combination");

        treeConfigs.put(root, "interfaceProvision");
    }

    private TraceLinkGenerator() {
        throw new IllegalStateException("No instantiation provided");
    }

    public static Node getRoot() {
        return root;
    }

    public static Node getRoot(NameComparisonUtils.PreprocessingMethod preprocessConfig) {
        Node interfaceName = new ComponentNameResemblance(ComponentNameResemblance.NameConfig.INTERFACE, preprocessConfig).getNode();
        Node interfaceMethod = new MethodResemblance().getNode();
        Node interfaceBest = MatchBest.getMatchBestCodeNode(MatchBest.getMatchBestArchNode(Maximum.getMaximumNode(interfaceName, interfaceMethod)));

        Node packageNodeStem = new PackageResemblance(NameComparisonUtils.PreprocessingMethod.STEMMING).getNode();
        Node packageBest = MatchBest.getMatchBestCodeNode(MatchBest.getMatchBestArchNode(packageNodeStem));
        Node packageFiltered = Filter.getFilterArchNode(packageBest, new SubpackageFilter().getNode(packageBest));

        Node compName = new ComponentNameResemblance(ComponentNameResemblance.NameConfig.COMPONENT, preprocessConfig).getNode();
        Node compNameBest = MatchBest.getMatchBestCodeNode(compName);
        Node compNameInherited = Maximum.getMaximumNode(new InheritLinks().getNode(compNameBest), compNameBest);

        Node compCombined = Maximum.getMaximumNode(MatchSequentially.getMatchSeqArchNode(packageFiltered, compNameInherited), new ComponentNameResemblance(
                ComponentNameResemblance.NameConfig.COMPONENT_WITHOUT_PACKAGE, NameComparisonUtils.PreprocessingMethod.NONE).getNode());

        Node commonWords = Maximum.getMaximumNode(new ComponentNameResemblanceTest().getNode(compCombined), compCombined);

        Node compFiltered = Filter.getFilterArchNode(commonWords, new Required().getNode(commonWords));

        Node path = new PathResemblance().getNode();
        Node pathBest = MatchBest.getMatchBestCodeNode(MatchBest.getMatchBestArchNode(path));
        Node maxCompInterface = Maximum.getMaximumNode(pathBest, compFiltered, interfaceBest);

        return Filter.getFilterArchNode(maxCompInterface, new ProvidedInterfaceCorrespondence().getNode(maxCompInterface));
    }

    public static Set<ModelCodeTraceLink> generateTraceLinks(Node root, ArchitectureModel archModel, CodeModel codeModel) {
        if (archModel == null || codeModel == null) {
            return new java.util.LinkedHashSet<>();
        }
        if (root == null) {
            root = getRoot();
        }

        Computation computation = new Computation(root, archModel, codeModel);
        return computation.getTraceLinks();
    }

    public static Set<ModelCodeTraceLink> generateTraceLinks(ArchitectureModel archModel, CodeModel codeModel) {
        return generateTraceLinks(getRoot(), archModel, codeModel);
    }
}
