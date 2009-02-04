/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.dom;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jdom.Attribute;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An IStructuredContentProvider for JDOM Elements.
 * ElementChildrenTreeContentProvider supports the concepts of skipElements and
 * stopElements. A skipElement is the name of element that should not be
 * included in the content but its children may be. A stopElement will not
 * have its children included in the content.
 *
 * NOTE: This content provider will not provide content for
 * the schemaLocation attributes.
 */
public class ElementChildrenTreeContentProvider
        implements IStructuredContentProvider, ITreeContentProvider {

    /**
     * The name of the schemaLocation attribute.
     */
    private static final String SCHEMA_LOCATION =
                "schemaLocation"; //$NON-NLS-1$

    /**
     * The elements in the tree to be skipped by this ODOMOutlinePage.
     */
    private final String[] skipElements;

    /**
     * The elements whose children should not be shown in the tree.
     */
    private final String[] stopElements;

    /**
     * Flag indicating whether or not element attributes should be provided
     * as child content of elements rather than assumed to be contained
     * only with elements.
     */
    private final boolean attributesAreChildren;

    /**
     * Flag indicating that the root element should be made available as
     * content.
     */
    private final boolean provideRootElement;

    /**
     * An array for use when there are no children.
     */
    private static final Object EMPTY_ARRAY [] = new Object[0];

    /**
     * Construct a new default ElementChildrenTreeContentProvider that has no
     * skip or stop elements and has attributes as part of the elements
     * rather than children of elements. Additionally, attributes will
     * not be considered children and the root element will not be
     * provided as content.
     */
    public ElementChildrenTreeContentProvider() {
        this(null, null, false, false);
    }

    /**
     * Construct a new ElementChildrenTreeContentProvider.
     * @param skipElements Any elements that should be skipped in the
     * content though their children may be available. Can be null.
     * @param stopElements Any elements that should not have their child
     * elements in the content. Can be null.
     * @param attributesAreChildren If true will provide element attributes
     * as child content of elements; otherwise element attributes will not
     * be provided separately from their associated elements.
     * @param provideRootElement If true will provide the root element as
     * content; otherwise only root element children will be provided.
     * @todo using provideRootElement = true doesn't provide the expected behaviour (eclipse displays the children of node 'rootNode' to be 'rootNode', etc. etc.)
     */
    public ElementChildrenTreeContentProvider(String[] skipElements,
                                              String[] stopElements,
                                              boolean attributesAreChildren,
                                              boolean provideRootElement) {
        this.skipElements = skipElements;
        this.stopElements = stopElements;
        this.attributesAreChildren = attributesAreChildren;
        this.provideRootElement = provideRootElement;
    }

    /**
     * Get the child elements of the given object which must be a JDOM Element.
     * @param parentElement The JDOM Element whose children to get.
     * @throws IllegalArgumentException If parentElement is not a JDOM Element
     * or is null. This method is only ever called once - the first time
     * the tree is displayed. The parameter is the Object used in the
     * setInput() call to the Viewer.
     */
    // rest of javadoc inherited
    public Object[] getElements(Object parentElement) {
        if (parentElement == null) {
            throw new IllegalArgumentException(
                        "Cannot be null: parentElement"); //$NON-NLS-1$
        }

        if (!(parentElement instanceof Element)) {
            throw new IllegalArgumentException(
                        "Expected a JDOM Element " + //$NON-NLS-1$
                        "for parentElement but was: " + //$NON-NLS-1$
                        parentElement.getClass().getName());
        }

        List childrenList = new ArrayList(0);

        if (provideRootElement) {
            childrenList.add(parentElement);
        } else {
            childrenList.addAll(((Element) parentElement).getChildren());
        }

        return childrenList.toArray();
    }

    // javadoc inherited
    public void dispose() {
    }

    // javadoc inherited
    public void inputChanged(Viewer viewer, Object o, Object o1) {
    }

    /**
     * Remove and Element from a List and add its children. This method
     * does not care about ordering since that is the responsibility of the
     * LabelProvider.
     * @param element The Element to replace with its children.
     * @param elements The List of Elements to act upon.
     */
    private void replaceElementWithChildren(Element element, List elements) {
        if (elements.remove(element)) {
            List children = element.getChildren();
            elements.addAll(children);
        }
    }

    /**
     * Determine if the given element is a stopElement.
     * @param element The element.
     * @return true If element is named in the stopElements array; false
     * otherwise.
     */
    private boolean isStopElement(Element element) {
        return arrayContains(stopElements, element.getName());
    }

    /**
     * Determine if the given element is a skipElement.
     * @param element The element.
     * @return true If element is named in the skipElements array; false
     * otherwise.
     */
    private boolean isSkipElement(Element element) {
        return arrayContains(skipElements, element.getName());
    }

    /**
     * Determine if a given Object is contained within a given array of Objects.
     * @param array The array of Objects.
     * @param object The Object.
     * @return true If the given Object is contained within the array of Objects
     * using Object.equals() as the comparator; false otherwise.
     */
    private boolean arrayContains(Object[] array, Object object) {
        boolean contained = false;
        if (array != null) {
            for (int i = 0; i < array.length && !contained; i++) {
                contained = object.equals(array[i]);
            }
        }

        return contained;
    }

    // javadoc inherited
    public Object[] getChildren(Object o) {
        Object children [] = EMPTY_ARRAY;
        if (o instanceof Element) {
            Element parentElement = (Element) o;
            // Declare a new List. It is possible that this list will remain
            // empty and therefore unecessary but this is unlikely and we
            // may not know until quite late in this method so it is far
            // simpler to do this declaration.
            List childrenList = new ArrayList(0);

            // If element attributes are supposed to be provided as children
            // then add them first.
            if (attributesAreChildren) {
                // The list returned from getAttributes() is not an ordinary
                // List but a specialization that allows users to add and
                // remove attributes from the parent element by using the
                // equivalent List api methods. The problem with this is that
                // it means we cannot move the attributes around in a way that
                // would happen in a sort so we need a new list.
                List attributes =
                        new ArrayList(parentElement.getAttributes());
                if (attributes != null && attributes.size() > 0) {
                    // Attributes need to be added in their natural (i.e.
                    // alphabetical order.
                    Collections.sort(attributes, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            Attribute a1 = (Attribute) o1;
                            Attribute a2 = (Attribute) o2;
                            return a1.getName().compareTo(a2.getName());
                        }
                    });
                    childrenList.addAll(attributes);

                    // Remove schemaLocation attributes
                    for (int i = 0; i < childrenList.size(); i++) {
                        Attribute attribute = (Attribute) childrenList.get(i);
                        if (attribute.getName().equals(SCHEMA_LOCATION)) {
                            childrenList.remove(i);
                            i--;
                        }
                    }
                }
            }

            if (!isStopElement(parentElement)) {
                // The list returned from getChildren() is not an ordinary List
                // but a specialization that allows users to add and remove
                // child elements from the parent element by using the
                // equivalent List api methods. The problem with this is that
                // it means we cannot add arbitrary elements (e.g child
                // elements) to the list because they have parents which will
                // cause a problem when JDOM tries to add them to another
                // parent because this is what their List implementation does.
                List elementChildren =
                        new ArrayList(parentElement.getChildren());

                // Elements should be sorted in their natural (i.e. alphabetical
                // order)
                Collections.sort(elementChildren, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        Element e1 = (Element) o1;
                        Element e2 = (Element) o2;
                        return e1.getName().compareTo(e2.getName());
                    }
                });

                if (childrenList == null) {
                    childrenList = new ArrayList(elementChildren);
                } else {
                    childrenList.addAll(elementChildren);
                }

                if (skipElements != null) {
                    // If any of the children are skipElements then they must be
                    // replaced by their children. Note that attributes are
                    // never included in the children in this case.
                    for (int i = 0; i < elementChildren.size(); i++) {
                        Element child = (Element) elementChildren.get(i);
                        if (isSkipElement(child)) {
                            // If the child is both a stopElement and a
                            // skipElement then neither it nor its children
                            // should appear in the children returned.
                            if (isStopElement(child)) {
                                childrenList.remove(child);
                            } else {
                                replaceElementWithChildren(child, childrenList);
                            }
                        }
                    }
                }
            }
            children = childrenList.toArray();
        }

        return children;
    }

    // javadoc inherited
    public Object getParent(Object o) {
        Object parent = null;
        if (o instanceof Attribute) {
            Attribute attribute = (Attribute) o;
            parent = attribute.getParent();
        } else {
            // Must be an Element
            parent = ((Element) o).getParent();
        }
        return parent;
    }

    // javadoc inherited
    public boolean hasChildren(Object o) {
        boolean hasChildren = false;
        if (o instanceof Element) {
            Element element = (Element) o;

            hasChildren = !isStopElement(element) &&
                    element.getChildren().size() > 0;
            if (!hasChildren && attributesAreChildren) {
                hasChildren = element.getAttributes().size() > 0;
            }
        }

        return hasChildren;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Feb-04	3192/1	byron	VBM:2004021303 Image asset wizard - windows - cannot handle typing in C:\ or choosing C:

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Jan-04	2659/1	allan	VBM:2003112801 The ThemeDesign Editor

 18-Jan-04	2562/1	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
