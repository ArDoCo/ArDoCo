/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements;

/**
 * Represents a package in the code. Additionally to the information of an
 * element it contains a short name.
 * The short name is the part of the package name that is not a part of the
 * parent package.
 */
public class PackageElement extends Element {
    private static final Type type = Type.PACKAGE;
    private String shortName; // The part of the package name that is not a part of the parent package

    public PackageElement(String name, String path) {
        super(name, path, type);
        this.shortName = name;
    }

    public PackageElement(String name, String path, ElementIdentifier parentIdentifier) {
        super(name, path, type, parentIdentifier);
        this.shortName = name;
    }

    public PackageElement(PackageElement packageElement) {
        super(packageElement);
        this.shortName = packageElement.getShortName();
    }

    public String[] getPackageNameParts(String regex) {
        return this.identifier.name().split(regex);
    }

    public void updateShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void updateParentIdentifier(ElementIdentifier parentIdentifier) {
        this.identifierOfParent = parentIdentifier;
    }

    public boolean extendsPackage(PackageElement packageElement) {
        return this.identifier.path().startsWith(packageElement.identifier.path()) && !this.equals(packageElement);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PackageElement) {
            PackageElement packageElement = (PackageElement) obj;
            return packageElement.identifier.path().equals(this.identifier.path());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }

}
