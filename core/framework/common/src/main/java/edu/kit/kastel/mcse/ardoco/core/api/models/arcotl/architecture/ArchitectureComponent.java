/* Licensed under MIT 2023. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture;

import java.util.SortedSet;

/**
 * A representation of the model object <i>Component</i> from AMTL. Components
 * are building blocks of a software architecture. A component can contain
 * subcomponents but doesn't have to. A component can provide and require
 * interfaces. Provided interfaces are implemented by the component. Required
 * interfaces specify some functionality that is needed by the component.
 */
public final class ArchitectureComponent extends ArchitectureItem {

    private static final long serialVersionUID = -7349058662425121364L;

    private final SortedSet<ArchitectureComponent> subcomponents;
    private final SortedSet<ArchitectureInterface> providedInterfaces;

    private final SortedSet<ArchitectureInterface> requiredInterfaces;
    private final String type;

    public ArchitectureComponent(String name, String id, SortedSet<ArchitectureComponent> subcomponents, SortedSet<ArchitectureInterface> providedInterfaces,
            SortedSet<ArchitectureInterface> requiredInterfaces, String type) {
        super(name, id);
        this.subcomponents = subcomponents;
        this.providedInterfaces = providedInterfaces;
        this.requiredInterfaces = requiredInterfaces;
        this.type = type;
    }

    /**
     * Returns the subcomponents of this component.
     *
     * @return the subcomponents of this component
     */
    public SortedSet<ArchitectureComponent> getSubcomponents() {
        return this.subcomponents;
    }

    /**
     * Returns the provided interfaces of this component. Provided interfaces are
     * implemented by this component.
     *
     * @return the provided interfaces of this component
     */
    public SortedSet<ArchitectureInterface> getProvidedInterfaces() {
        return this.providedInterfaces;
    }

    /**
     * Returns the required interfaces of this component. Required interfaces
     * specify some functionality that is needed by this component.
     *
     * @return the required interfaces of this component
     */
    public SortedSet<ArchitectureInterface> getRequiredInterfaces() {
        return this.requiredInterfaces;
    }

    /**
     * Returns the type of this component as specified in the meta model.
     *
     * @return the type of this component
     */
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "Component: " + this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchitectureComponent that) || !super.equals(o) || !this.subcomponents.equals(that.subcomponents) || !this.providedInterfaces.equals(that.providedInterfaces)) {
            return false;
        }
        return this.requiredInterfaces.equals(that.requiredInterfaces);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.subcomponents.hashCode();
        result = 31 * result + this.providedInterfaces.hashCode();
        return 31 * result + this.requiredInterfaces.hashCode();
    }
}
