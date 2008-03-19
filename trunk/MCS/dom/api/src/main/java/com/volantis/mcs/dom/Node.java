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

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate 
 */
public interface Node {

    DOMFactory getDOMFactory();

    /**
     * Get the parent element.
     * @return The parent element.
     */
    Element getParent();

    /**
     * Get the previous node.
     * @return The previous node.
     */
    Node getPrevious();

    /**
     * Get the next node.
     * @return The next node.
     */
    Node getNext();

    /**
     * Sets some user data associated with this node. This data is just for use
     * within a single processing stage, and can be overwritten at any time.
     * Therefore it cannot be guaranteed to persist across processing stages.
     *
     * @param object the user data
     */
    void setObject(Object object);

    /**
     * Gets the user data associated with this node.
     * @return the user data
     */
    Object getObject();

    /**
     * Sets the annotation of this node. The annotation may only be set once.
     * This can be used, for example, to set a processing instruction on the
     * node which a subsequent processing stage can then use.
     * @param annotation the annotation to set
     * @throws java.lang.IllegalStateException if the annotation has already been set
     */
    void setAnnotation(Object annotation);

    /**
     * Gets the annotation of this node.
     * @return the annotation
     */
    Object getAnnotation();

    /**
     * Insert this node after the specified node.
     * @param node The node to insert this one after.
     */
    void insertAfter(Node node);

    /**
     * Insert this node before the specified node.
     * @param node The node to insert this one before.
     */
    void insertBefore(Node node);

    /**
     * Add this node to the tail of the elements children.
     * @param element The element to add this node to.
     *
     * @deprecated Use {@link Element#addTail(Node)}
     */
    void addToTail(Element element);

    /**
     * Add this node at the head of the elements children.
     * @param element The element to add this node to.
     *
     * @deprecated Use {@link Element#addTail(Node)}
     */
    void addToHead(Element element);

    /**
     * Replace the specified node with this node.
     * @param node The node which will be replace by this one.
     */
    void replace(Node node);

    /**
     * Remove this node from the list.
     */
    void remove();

    /**
     * Promote this node.
     * <p>
     * If this node is the only child of the parent element then the parent
     * element is replaced by this node.
     * </p>
     * <p>
     * If this node is the first child of the parent element then it is
     * inserted before the parent element.
     * </p>
     * <p>
     * If this node is the last child of the parent element then it is
     * inserted after the parent element.
     * </p>
     * <p>
     * Otherwise the parent element is split into two, the first of which
     * contains all the children which precede this node and the second of which
     * contains all the children which follow this node. This node is then
     * inserted between the two elements.
     * </p>
     * @return True if the parent element was replaced and can be discarded.
     */
    boolean promote();

    /**
     * A variant of {@link #promote()} which allows the client to control
     * how "empty" elements which may be generated as a side affect of promotion
     * are dealt with (empty here includes containing only whitespace).
     * <p>
     * If trimEmptyNodes is specified as true, and if the element ends up being
     * split into two, with the newly promoted node in the middle, and if either
     * of those are empty or have only whitespace as children, they will be
     * trimmed from the DOM tree. Note that in this case the return value will
     * vary depending on whether it was the new or cloned element which was
     * empty.
     *
     * @param trimEmptyNodes Set to true if you want "empty" nodes removed.
     * @return True if the parent node was replaced and therefore can
     * be discarded.
     */
    boolean promote(boolean trimEmptyNodes);

    /**
     * An "accept" method for objects which may visit the node.
     * (see GOF book).
     */
    void accept(DOMVisitor visitor);

    /**
     * Replace this node with the specified sequence.
     *
     * @param sequence The sequence whose nodes should be inserted in place of
     *                 this node.
     */
    void replaceWith(NodeSequence sequence);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jun-05	8856/3	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 ===========================================================================
*/
