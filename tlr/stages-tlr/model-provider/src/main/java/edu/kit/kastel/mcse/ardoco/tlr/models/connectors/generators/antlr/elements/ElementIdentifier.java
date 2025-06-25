/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements;

/**
 * Represents an identifier for an element. An element gets identified by its
 * name, path, and type.
 */
public record ElementIdentifier(String name, String path, Type type) {
}
