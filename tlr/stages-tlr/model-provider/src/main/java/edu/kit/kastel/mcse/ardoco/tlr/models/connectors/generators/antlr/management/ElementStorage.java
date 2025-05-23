/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management;

import java.util.ArrayList;
import java.util.List;

import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;

/**
 * This class is used to store elements of a specific element object. It is used to store
 * elements of the same element object in a list and to retrieve them based on their
 * identifier and/or parent identifier.
 */
public class ElementStorage<T extends Element> {
    private final Class<T> classType;
    private List<T> elements;

    public ElementStorage(List<T> elements, Class<T> classType) {
        this.elements = elements;
        this.classType = classType;
    }

    public ElementStorage(Class<T> classType) {
        this.classType = classType;
        elements = new ArrayList<>();
    }

    public Class<T> getClassType() {
        return classType;
    }

    public void addElement(T element) {
        if (element == null || elements.contains(element)) {
            return;
        }
        this.elements.add(element);
    }

    public void addElements(List<T> elements) {
        for (T element : elements) {
            addElement(element);
        }
    }

    public T getElement(ElementIdentifier identifier) {
        for (T element : elements) {
            if (isElementWithIdentifier(element, identifier)) {
                return element;
            }
        }
        return null;
    }

    public List<T> getElements() {
        return elements;
    }

    public boolean contains(Element element) {
        return elements.contains(element);
    }

    /**
     * Returns all elements that have the given parent identifier.
     * 
     * @param identifier The parent identifier to search for.
     * @return A list of elements that have the given parent identifier.
     */
    public List<T> getContentOfIdentifier(ElementIdentifier identifier) {
        List<T> elementsWithMatchingParentIdentifier = new ArrayList<>();

        for (T element : elements) {
            if (isElementWithParentIdentifier(element, identifier)) {
                elementsWithMatchingParentIdentifier.add(element);
            }
        }
        return elementsWithMatchingParentIdentifier;
    }

    public List<T> getElementsWithoutParentIdentifier() {
        List<T> elementsWithoutParentIdentifier = new ArrayList<>();

        for (T element : elements) {
            if (element.getParentIdentifier() == null) {
                elementsWithoutParentIdentifier.add(element);
            }
        }
        return elementsWithoutParentIdentifier;
    }

    private boolean isElementWithIdentifier(T element, ElementIdentifier identifier) {
        return identifier != null && element.getIdentifier().equals(identifier);
    }

    private boolean isElementWithParentIdentifier(T element, ElementIdentifier parentIdentifier) {
        return (parentIdentifier == null && element.getParentIdentifier() == null) || (element.getParentIdentifier() != null && element.getParentIdentifier()
                .equals(parentIdentifier));
    }

}
