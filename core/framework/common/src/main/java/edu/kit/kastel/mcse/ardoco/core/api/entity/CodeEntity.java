/* Licensed under MIT 2024. */
package edu.kit.kastel.mcse.ardoco.core.api.entity;

public non-sealed class CodeEntity extends Entity {
    private static final long serialVersionUID = 5520572653996476974L;

    protected CodeEntity(String name) {
        super(name);
    }

    protected CodeEntity(String name, String id) {
        super(name, id);
    }
}
