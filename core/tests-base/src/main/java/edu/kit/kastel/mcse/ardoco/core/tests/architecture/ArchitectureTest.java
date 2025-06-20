/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.tests.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;
import edu.kit.kastel.mcse.ardoco.core.common.JsonHandling;
import edu.kit.kastel.mcse.ardoco.core.configuration.AbstractConfigurable;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;

@AnalyzeClasses(packages = "edu.kit.kastel.mcse.ardoco")
public class ArchitectureTest {

    // Package constants
    private static final String PACKAGE_EXECUTION = "..execution..";
    private static final String PACKAGE_TESTS = "..tests..";
    private static final String PACKAGE_MODELS = "..models..";
    private static final String PACKAGE_RECOMMENDATION_GENERATOR = "..recommendationgenerator..";
    private static final String PACKAGE_CONNECTION_GENERATOR = "..connectiongenerator..";
    private static final String PACKAGE_INCONSISTENCY = "..inconsistency..";
    private static final String PACKAGE_INCONSISTENCY_DETECTION = "..id.."; // InconsistencyDetection
    private static final String PACKAGE_PIPELINE = "..pipeline..";
    private static final String PACKAGE_COMMON = "..common..";
    private static final String PACKAGE_OUTPUT = "..output..";
    private static final String PACKAGE_CODE_TRACEABILITY = "..codetraceability..";
    private static final String PACKAGE_TRACE_LINKS = "..tracelinks..";
    private static final String PACKAGE_API = "..api..";
    private static final String PACKAGE_TRACE_LINK = "..tracelink..";
    private static final String PACKAGE_DATA = "..data..";
    private static final String PACKAGE_TEXT_EXTRACTION = "..textextraction..";

    // Layer name constants
    private static final String LAYER_COMMON = "Common";
    private static final String LAYER_TEXT_EXTRACTOR = "TextExtractor";
    private static final String LAYER_MODEL_EXTRACTOR = "ModelExtractor";
    private static final String LAYER_RECOMMENDATION_GENERATOR = "RecommendationGenerator";
    private static final String LAYER_CONNECTION_GENERATOR = "ConnectionGenerator";
    private static final String LAYER_INCONSISTENCY_DETECTION = "InconsistencyDetection";
    private static final String LAYER_CODE_TRACEABILITY = "CodeTraceability";
    private static final String LAYER_PIPELINE = "Pipeline";
    private static final String LAYER_EXECUTION = "Execution";

    @ArchTest
    public static final ArchRule noDependencyOnExecution = classes().that()
            .resideInAPackage(PACKAGE_EXECUTION)
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAnyPackage(PACKAGE_EXECUTION, PACKAGE_TESTS);
    @ArchTest
    public static final ArchRule modelInstancesOnlyAfterModelExtraction = classes().that()
            .haveSimpleName("Model")
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAnyPackage(PACKAGE_MODELS, PACKAGE_RECOMMENDATION_GENERATOR, PACKAGE_CONNECTION_GENERATOR, PACKAGE_INCONSISTENCY,
                    PACKAGE_INCONSISTENCY_DETECTION, PACKAGE_PIPELINE, PACKAGE_COMMON, PACKAGE_OUTPUT, PACKAGE_TESTS);

    @ArchTest
    public static final ArchRule linksOnlyAfterConnectionGenerator = classes().that()
            .haveSimpleNameEndingWith("Link")
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAnyPackage(PACKAGE_CONNECTION_GENERATOR, PACKAGE_CODE_TRACEABILITY, PACKAGE_TRACE_LINKS, PACKAGE_INCONSISTENCY,
                    PACKAGE_INCONSISTENCY_DETECTION, PACKAGE_PIPELINE, PACKAGE_COMMON, PACKAGE_API, PACKAGE_TESTS);

    @ArchTest
    public static final ArchRule usingLinkAsNamingOnlyInConnectionGenerator = classes().that()
            .haveSimpleNameEndingWith("Link")
            .should()
            .resideInAnyPackage(PACKAGE_TRACE_LINK, PACKAGE_CODE_TRACEABILITY, PACKAGE_CONNECTION_GENERATOR);

    @ArchTest
    public static final ArchRule inconsistencyOnlyAfterInconsistencyDetection = classes().that()
            .haveSimpleNameContaining("Inconsistency")
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAnyPackage(PACKAGE_INCONSISTENCY, PACKAGE_INCONSISTENCY_DETECTION, PACKAGE_EXECUTION, PACKAGE_API, PACKAGE_COMMON, PACKAGE_TESTS);

    @ArchTest
    public static final ArchRule layerRule = layeredArchitecture().consideringAllDependencies()
            // Layer definition
            .layer(LAYER_COMMON)
            .definedBy(PACKAGE_COMMON, PACKAGE_DATA, PACKAGE_API, PACKAGE_TESTS)
            .layer(LAYER_TEXT_EXTRACTOR)
            .definedBy(PACKAGE_TEXT_EXTRACTION)
            .layer(LAYER_MODEL_EXTRACTOR)
            .definedBy(PACKAGE_MODELS)
            .layer(LAYER_RECOMMENDATION_GENERATOR)
            .definedBy(PACKAGE_RECOMMENDATION_GENERATOR)
            .layer(LAYER_CONNECTION_GENERATOR)
            .definedBy(PACKAGE_CONNECTION_GENERATOR)
            .layer(LAYER_INCONSISTENCY_DETECTION)
            .definedBy(PACKAGE_INCONSISTENCY, PACKAGE_INCONSISTENCY_DETECTION)
            .layer(LAYER_CODE_TRACEABILITY)
            .definedBy(PACKAGE_CODE_TRACEABILITY)
            .layer(LAYER_PIPELINE)
            .definedBy(PACKAGE_PIPELINE)
            .layer(LAYER_EXECUTION)
            .definedBy(PACKAGE_EXECUTION)
            // rule definition
            .whereLayer(LAYER_EXECUTION)
            .mayOnlyBeAccessedByLayers(LAYER_COMMON) // Needed for tests
            .whereLayer(LAYER_INCONSISTENCY_DETECTION)
            .mayOnlyBeAccessedByLayers(LAYER_PIPELINE, LAYER_COMMON, LAYER_EXECUTION)
            .whereLayer(LAYER_CONNECTION_GENERATOR)
            .mayOnlyBeAccessedByLayers(LAYER_CODE_TRACEABILITY, LAYER_INCONSISTENCY_DETECTION, LAYER_PIPELINE, LAYER_COMMON, LAYER_EXECUTION)
            .whereLayer(LAYER_RECOMMENDATION_GENERATOR)
            .mayOnlyBeAccessedByLayers(LAYER_CONNECTION_GENERATOR, LAYER_INCONSISTENCY_DETECTION, LAYER_PIPELINE, LAYER_COMMON, LAYER_EXECUTION)
            .whereLayer(LAYER_TEXT_EXTRACTOR)
            .mayOnlyBeAccessedByLayers(LAYER_RECOMMENDATION_GENERATOR, LAYER_CONNECTION_GENERATOR, LAYER_INCONSISTENCY_DETECTION, LAYER_PIPELINE, LAYER_COMMON,
                    LAYER_EXECUTION)
            .whereLayer(LAYER_MODEL_EXTRACTOR)
            .mayOnlyBeAccessedByLayers(LAYER_RECOMMENDATION_GENERATOR, LAYER_CONNECTION_GENERATOR, LAYER_CODE_TRACEABILITY, LAYER_INCONSISTENCY_DETECTION,
                    LAYER_PIPELINE, LAYER_COMMON, LAYER_EXECUTION);

    @ArchTest
    public static final ArchRule configurableFieldsOnlyInConfigurableClasses = fields().that()
            .areAnnotatedWith(Configurable.class)
            .should()
            .beDeclaredInClassesThat()
            .areAssignableTo(AbstractConfigurable.class);

    @ArchTest
    public static final ArchRule traceLinksShouldBeFinal = classes().that()
            .areAssignableTo(TraceLink.class)
            .and()
            .doNotHaveFullyQualifiedName(TraceLink.class.getName())
            .should()
            .haveModifier(JavaModifier.FINAL);

    @ArchTest
    public static final ArchRule jacksonIsConfiguredGlobally = noClasses().that()
            .doNotHaveFullyQualifiedName(JsonHandling.class.getName())
            .and()
            .doNotHaveFullyQualifiedName("edu.kit.kastel.mcse.ardoco.magika.Configuration")
            .should()
            .callConstructor(ObjectMapper.class);

}
