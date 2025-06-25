/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.python3;

import org.antlr.v4.runtime.*;

public abstract class Python3ParserBase extends Parser {
    protected Python3ParserBase(TokenStream input) {
        super(input);
    }

    public boolean CannotBePlusMinus() {
        return true;
    }

    public boolean CannotBeDotLpEq() {
        return true;
    }
}
