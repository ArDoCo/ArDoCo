/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Extracts the path or last part of the path from a ParserRuleContext ANTLR
 * node.
 */
public final class PathExtractor {

    /**
     * Extracts the path from the given ParserRuleContext
     * 
     * @param ctx, the ANTLR ParserRuleContext
     *             @return, the path as string
     */
    public static String extractPath(ParserRuleContext ctx) {
        String path = ctx.getStart().getInputStream().getSourceName();
        String normalizedPath = path.replace("\\", "/");
        return normalizedPath;
    }

    /**
     * Extracts the name from the path of the given ParserRuleContext
     * 
     * @param ctx, the ANTLR ParserRuleContext
     *             @return, the name as string
     */
    public static String extractNameFromPath(ParserRuleContext ctx) {
        String path = extractPath(ctx);
        String normalizedPath = path.replace("\\", "/");
        String name = normalizedPath.substring(normalizedPath.lastIndexOf("/") + 1, normalizedPath.lastIndexOf("."));
        return name;
    }

}
