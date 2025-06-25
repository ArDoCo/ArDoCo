/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements;

/**
 * Represents a comment in the code. Contains information about the text, the
 * line in which the comment starts and ends, and the path of the file the
 * comment is in.
 */
public record Comment(String text, int startLine, int endLine, String path) {
}
