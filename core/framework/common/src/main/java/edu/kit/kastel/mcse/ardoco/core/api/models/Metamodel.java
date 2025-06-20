/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models;

/**
 * Enum representing the supported metamodel types.
 */
public enum Metamodel {
    /** Code model with only compilation units. */
    CODE_ONLY_COMPILATION_UNITS,
    /** Architecture model with components and interfaces. */
    ARCHITECTURE_WITH_COMPONENTS_AND_INTERFACES,
    /** Code model with compilation units and packages. */
    CODE_WITH_COMPILATION_UNITS_AND_PACKAGES,
    /** Architecture model with only components. */
    ARCHITECTURE_ONLY_COMPONENTS;

    /**
     * Checks if the given metamodel is a code model.
     *
     * @param metamodel the metamodel to check
     * @return true if it is a code model, false otherwise
     */
    public static boolean isACodeModel(Metamodel metamodel) {
        return switch (metamodel) {
            case CODE_WITH_COMPILATION_UNITS_AND_PACKAGES, CODE_ONLY_COMPILATION_UNITS -> true;
            case ARCHITECTURE_ONLY_COMPONENTS, ARCHITECTURE_WITH_COMPONENTS_AND_INTERFACES -> false;
        };
    }

    /**
     * Checks if the given metamodel is an architecture model.
     *
     * @param metamodel the metamodel to check
     * @return true if it is an architecture model, false otherwise
     */
    public static boolean isAnArchitectureModel(Metamodel metamodel) {
        return switch (metamodel) {
            case ARCHITECTURE_WITH_COMPONENTS_AND_INTERFACES, ARCHITECTURE_ONLY_COMPONENTS -> true;
            case CODE_ONLY_COMPILATION_UNITS, CODE_WITH_COMPILATION_UNITS_AND_PACKAGES -> false;
        };
    }
}
