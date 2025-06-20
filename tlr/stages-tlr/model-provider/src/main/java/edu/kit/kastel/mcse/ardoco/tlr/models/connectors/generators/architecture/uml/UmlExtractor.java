/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture.uml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureComponentModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModelWithComponentsAndInterfaces;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureComponent;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureInterface;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureMethod;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture.ArchitectureExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture.uml.parser.UmlComponent;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture.uml.parser.UmlInterface;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture.uml.parser.UmlModel;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture.uml.parser.xmlelements.OwnedOperation;

/**
 * An extractor for UML. Extracts an AMTL instance.
 */
@Deterministic
public final class UmlExtractor extends ArchitectureExtractor {

    public UmlExtractor(String path, Metamodel metamodelToExtract) {
        super(path, metamodelToExtract);
    }

    /**
     * Extracts an architecture model, i.e. an AMTL instance, from a UML instance.
     *
     * @return the extracted architecture model
     */
    @Override
    public ArchitectureModel extractModel() {
        UmlModel originalModel = new UmlModel(new File(path));
        List<ArchitectureInterface> interfaces = extractInterfaces(originalModel);
        List<ArchitectureComponent> components = extractComponents(originalModel, interfaces);
        List<ArchitectureItem> endpoints = new ArrayList<>();
        endpoints.addAll(interfaces);
        endpoints.addAll(components);

        ArchitectureModel architectureModelWithComponentsAndInterfaces = new ArchitectureModelWithComponentsAndInterfaces(endpoints);
        ArchitectureModel architectureComponentModel = new ArchitectureComponentModel(architectureModelWithComponentsAndInterfaces);

        return switch (metamodelToExtract) {
            case Metamodel.ARCHITECTURE_WITH_COMPONENTS_AND_INTERFACES -> architectureModelWithComponentsAndInterfaces;
            case Metamodel.ARCHITECTURE_ONLY_COMPONENTS -> architectureComponentModel;
            default -> throw new IllegalArgumentException("Unsupported metamodel: " + metamodelToExtract);
        };
    }

    private static List<ArchitectureInterface> extractInterfaces(UmlModel originalModel) {
        List<ArchitectureInterface> interfaces = new ArrayList<>();
        for (UmlInterface originalInterface : originalModel.getModel().getInterfaces()) {
            SortedSet<ArchitectureMethod> signatures = new TreeSet<>();
            for (OwnedOperation originalMethod : originalInterface.getOperations()) {
                ArchitectureMethod signature = new ArchitectureMethod(originalMethod.getName());
                signatures.add(signature);
            }
            ArchitectureInterface modelInterface = new ArchitectureInterface(originalInterface.getName(), originalInterface.getId(), signatures);
            interfaces.add(modelInterface);
        }
        return interfaces;
    }

    private static List<ArchitectureComponent> extractComponents(UmlModel originalModel, List<ArchitectureInterface> interfaces) {
        List<ArchitectureComponent> components = new ArrayList<>();
        for (UmlComponent originalComponent : originalModel.getModel().getComponents()) {
            SortedSet<ArchitectureComponent> subcomponents = new TreeSet<>();
            SortedSet<ArchitectureInterface> providedInterfaces = new TreeSet<>();
            SortedSet<ArchitectureInterface> requiredInterfaces = new TreeSet<>();
            for (UmlInterface providedInterface : originalComponent.getProvided()) {
                ArchitectureInterface modelInterface = findInterface(providedInterface.getId(), interfaces);
                providedInterfaces.add(modelInterface);
            }
            for (UmlInterface requiredInterface : originalComponent.getRequired()) {
                ArchitectureInterface modelInterface = findInterface(requiredInterface.getId(), interfaces);
                requiredInterfaces.add(modelInterface);
            }
            ArchitectureComponent modelComponent = new ArchitectureComponent(originalComponent.getName(), originalComponent.getId(), subcomponents,
                    providedInterfaces, requiredInterfaces, originalComponent.getType());
            components.add(modelComponent);
        }
        return components;
    }

    private static ArchitectureInterface findInterface(String id, List<ArchitectureInterface> interfaces) {
        return interfaces.stream().filter(modelInterface -> modelInterface.getId().equals(id)).findFirst().orElseThrow();
    }
}
