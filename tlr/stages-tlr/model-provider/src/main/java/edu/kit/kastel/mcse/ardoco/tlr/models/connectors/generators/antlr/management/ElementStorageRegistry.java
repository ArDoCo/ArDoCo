/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Comment;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.commentmatching.CommentMatcher;

/**
 * This class is used to store elements of different types. It is used to store
 * the specific elements of the same
 * type in a ElementStorage and retrieve them based on their identifier and/or
 * parent identifier.
 * The valid Element types and their corresponding ElementStorage are registered
 * in the registerStorage method, which
 * subclasses of this class have to implement.
 * This class also contains methods to access the elements stored in the
 * ElementStorages as well as to add elements.
 * Additionally, it is responsible for adding comments extracted from the source
 * code to the corresponding elements
 * via the CommentMatcher.
 */
@Deterministic
public abstract class ElementStorageRegistry {
    private final CommentMatcher commentMatcher;
    private final EnumMap<Type, ElementStorage<?>> storages = new EnumMap<>(Type.class);
    private final EnumMap<Type, Class<?>> typeOfClass = new EnumMap<>(Type.class);

    protected ElementStorageRegistry(CommentMatcher commentMatcher) {
        registerStorage();
        createTypeMap();
        this.commentMatcher = commentMatcher;
    }

    public void addComments(List<Comment> comments) {
        commentMatcher.matchComments(comments, getAllElements());
    }

    public List<ElementIdentifier> getRootIdentifiers() {
        List<ElementIdentifier> identifiers = new ArrayList<>();
        List<Element> elements = getAllElements();

        for (Element element : elements) {
            ElementIdentifier identifier = element.getParentIdentifier();
            if (hasNotBeenAdded(identifier, identifiers) && checkIfElementWithIdentifierIsRoot(identifier)) {
                identifiers.add(new ElementIdentifier(identifier.name(), identifier.path(), identifier.type()));
            }
        }
        return identifiers;
    }

    public boolean hasStorage(Type type) {
        return typeOfClass.containsKey(type) && storages.containsKey(type);
    }

    public List<Element> getContentOfIdentifier(ElementIdentifier identifier) {
        List<Element> elements = new ArrayList<>();
        for (ElementStorage<?> storage : storages.values()) {
            for (Element element : storage.getContentOfIdentifier(identifier)) {
                elements.add(new Element(element));
            }
        }
        return elements;
    }

    public boolean checkIfElementWithIdentifierIsRoot(ElementIdentifier identifier) {
        ElementStorage<?> storage = getTypedStorage(identifier.type());
        if (storage != null) {
            return storage.getElement(identifier) != null && storage.getElement(identifier).getParentIdentifier() == null;
        }
        return false;
    }

    public List<Element> getAllElements() {
        List<Element> elements = new ArrayList<>();
        for (ElementStorage<?> storage : storages.values()) {
            for (Element element : storage.getElements()) {
                elements.add(new Element(element));
            }
        }
        return elements;
    }

    public Element getElement(ElementIdentifier identifier) {
        for (ElementStorage<?> storage : storages.values()) {
            Element element = storage.getElement(identifier);
            if (element != null) {
                return new Element(element);
            }
        }
        return null;
    }

    protected abstract void registerStorage();

    protected <T extends Element> void registerStorage(Type type, ElementStorage<T> storage) {
        if (hasStorage(type)) {
            return;
        }
        storages.put(type, storage);
    }

    protected <T extends Element> void addElement(Type type, T element) {
        if (hasStorage(type, element)) {
            ElementStorage<T> storage = getTypedStorage(type);
            if (storage != null) {
                storage.addElement(element);
            }
        }
    }

    protected <T extends Element> void addElements(Type type, Iterable<T> elements) {
        for (T element : elements) {
            addElement(type, element);
        }
    }

    protected <T extends Element> T getElement(ElementIdentifier identifier, Class<T> clazz) {
        if (identifier.type() == null || !verifyAllowed(identifier.type(), clazz)) {
            return null;
        }
        ElementStorage<T> storage = getTypedStorage(identifier.type());
        return storage.getElement(identifier);
    }

    protected <T extends Element> List<T> getElements(Type type, Class<T> clazz) {
        if (hasStorage(type, clazz)) {
            ElementStorage<T> storage = getStorage(type, clazz);
            return storage.getElements();
        }
        return new ArrayList<>();
    }

    protected <T extends Element> boolean containsElement(Type type, T element) {
        if (hasStorage(type)) {
            ElementStorage<T> storage = getTypedStorage(type);
            return storage.contains(element);
        }
        return false;
    }

    protected <T extends Element> List<T> getContentOfIdentifier(Type type, ElementIdentifier identifier) {
        if (hasStorage(type)) {
            ElementStorage<T> storage = getTypedStorage(type);
            return storage.getContentOfIdentifier(identifier);
        }
        return new ArrayList<>();
    }

    protected List<Element> getElementsWithoutParentidentifier() {
        List<Element> roots = new ArrayList<>();
        for (ElementStorage<?> storage : storages.values()) {
            roots.addAll(storage.getElementsWithoutParentIdentifier());
        }
        return roots;
    }

    @SuppressWarnings("unchecked")
    private <T extends Element> ElementStorage<T> getTypedStorage(Type type) {
        Class<T> clazz = (Class<T>) typeOfClass.get(type);
        return (ElementStorage<T>) getStorage(type, clazz);
    }

    @SuppressWarnings("unchecked")
    private <T extends Element> ElementStorage<T> getStorage(Type type, Class<T> clazz) {
        if (hasStorage(type, clazz)) {
            return (ElementStorage<T>) storages.get(type);
        }
        return null;
    }

    private <T extends Element> boolean verifyAllowed(Type type, Class<T> clazz) {
        return typeOfClass.get(type) != null && typeOfClass.get(type).equals(clazz);
    }

    private <T extends Element> boolean hasStorage(Type type, T element) {
        return element != null && storages.containsKey(type) && storages.get(type).getClassType().equals(element.getClass());
    }

    private <T extends Element> boolean hasStorage(Type type, Class<T> clazz) {
        return storages.containsKey(type) && storages.get(type).getClassType().equals(clazz);
    }

    private void createTypeMap() {
        for (var typeXstorage : storages.entrySet()) {
            typeOfClass.put(typeXstorage.getKey(), typeXstorage.getValue().getClassType());
        }
    }

    private boolean hasNotBeenAdded(ElementIdentifier identifier, List<ElementIdentifier> identifiers) {
        return identifier != null && !identifiers.contains(identifier);
    }
}
