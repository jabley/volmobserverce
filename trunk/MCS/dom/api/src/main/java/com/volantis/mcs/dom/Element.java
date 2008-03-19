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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

import com.volantis.styling.StyleContainer;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate base="Node"
 */
public interface Element
        extends Node, StyleContainer {
    
    /**
     * Make a shallow copy of the element.
     * <p>
     * The name, attributes and Styles are copied but the children are not.
     * The Styles should be considered immutable; they refer to the original
     * Styles rather than being a clone, and any modification will therefore
     * affect both.
     * </p>
     */
    void copy(Element element);

    /**
     * Return true if this element has no children and false otherwise.
     */
    boolean isEmpty();

    /**
     * Return true if this element has at least one attribute.
     */
    boolean hasAttributes();

    /**
     * Set the name.
     *
     * @param name The name.
     */
    void setName(String name);

    /**
     * Get the name.
     *
     * @return The name.
     */
    String getName();

    /**
     * Get the head node.
     *
     * @return The head node.
     */
    Node getHead();

    /**
     * Get the tail node.
     *
     * @return The tail node.
     */
    Node getTail();

    /**
     * Clear the children from the element.
     *
     * <p>This does not detach all the children from this element so they will
     * still incorrectly report that this element is their parent.</p>
     */
    void clearChildren();

    /**
     * Remove all the children from this element.
     *
     * <p>This does not detach all the children from this element so they will
     * still incorrectly report that this element is their parent.</p>
     *
     * <p>The returned sequence is not live, i.e. changes to the element
     * will not affect the sequence.</p>
     *
     * @return The sequence of nodes.
     */
    NodeSequence removeChildren();

    /**
     * Add the specified node at the head of the list of children.
     *
     * @param node The node to add.
     */
    void addHead(Node node);

    /**
     * Add the specified node at the tail of the list of children.
     *
     * @param node The node to add.
     */
    void addTail(Node node);

    /**
     * Add this element's children to the head of the specified element.
     * <p>
     * Afterwards this element is empty.
     * </p>
     */
    void addChildrenToHead(Element element);

    /**
     * Add this element's children to the tail of the specified element.
     * <p>
     * Afterwards this element is empty.
     * </p>
     */
    void addChildrenToTail(Element element);

    /**
     * Insert this element's children after the specified node.
     * <p>Afterwards this element is empty.</p>
     *
     * @deprecated This has a truly nasty, utterly confusing name.
     */
    void insertChildrenAfter(Node node);

    /**
     * Insert this element's children before the specified node.
     * <p>Afterwards this element is empty.</p>
     *
     * @deprecated This has a truly nasty, utterly confusing name.
     */
    void insertChildrenBefore(Node node);

    /**
     * Set the value of an attribute.
     * <p>
     * If the attribute already has a value then change it, otherwise add a new
     * attribute to the list whose name and value are those specified.
     * </p>
     *
     * @param name  The name of the attribute to set.
     * @param value The value of the attribute to set.
     * @throws java.lang.IllegalArgumentException
     *          If either the name or value is null.
     */
    void setAttribute(String name, String value);

    /**
     * Remove the specified attribute.
     * <p>
     * If the element does not have a setting for the attribute then returns
     * nothing.
     * </p>
     *
     * @param name The name of the attribute which should be removed.
     * @throws java.lang.IllegalArgumentException
     *          If the name is null.
     */
    void removeAttribute(String name);

    /**
     * Get the first attribute in a singly linked list of attributes.
     *
     * @return The first attribute in the linked list of attributes.
     */
    Attribute getAttributes();

    /**
     * Copy the attributes from the other element.
     */
    void copyAttributes(Element element);

    /**
     * Merge the attributes from the other element.
     *
     * @param element  The element whose attributes should be merged into this
     *                 one.
     * @param override If an attribute is present in both elements then this
     *                 controls which value has precedence. If true then the other elements
     *                 attribute values take precedence, otherwise existing attribute values take
     *                 precedence.
     */
    void mergeAttributes(Element element, boolean override);

    void clearAttributes();

    /**
     * Get the value for the specified attribute.
     * <p/>
     * If the element does not have a setting for the attribute then return null.
     * </p>
     *
     * @param name The name of the attribute whose value should be returned.
     * @return The value of the specified attribute, or null if it could not be
     *         found.
     * @throws java.lang.IllegalArgumentException
     *          If the name is null.
     */
    String getAttributeValue(String name);

    /**
     * Iterate over the child nodes of this element, calling the supplied
     * iteratee for each one.
     * <p>
     * This is an application of the Internal Iterator pattern in the GOF book.
     *
     * @param nodeIteratee called for each child node of this element.
     */
    void forEachChild(NodeIteratee nodeIteratee);

    void addText(String string);

    /**
     * Add the sequence of nodes as children after the child node.
     *
     * @param nodes The sequence of nodes to insert.
     * @param child The child node after which the nodes will be inserted, if
     *              this is null then the nodes will be inserted at the head of
     *              the children.
     * @return The last node that was inserted, or the original child if no
     *         nodes were inserted.
     */
    Node insertAfter(NodeSequence nodes, Node child);

    /**
     * Clear the styles from the element.
     */
    void clearStyles();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 22-Jun-05	8483/4	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 ===========================================================================
*/
