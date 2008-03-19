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
 * $Header: /src/voyager/com/volantis/mcs/dom/Node.java,v 1.6 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 09-May-02    Ian             VBM:2002031203 - Changed log4j to use string.
 * 19-Sep-02    Phil W-S        VBM:2002091910 - Ensure that reset clears the
 *                              object attribute.
 * 27-Jan-02    Geoff           VBM:2003012302 - Added overload of promote
 *                              which takes boolean parameter trimEmptyNodes.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.NodeSequence;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class is the base class for all the elements which can appear in a
 * dom.
 * <p>
 * These are doubly linked as it is likely that they will be rearranged quite
 * a lot and the extra link makes it much easier to remove them and add them
 * at arbitrary positions in the list.
 * </p>
 */
public abstract class NodeImpl
        implements Node {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(NodeImpl.class);

    protected final boolean CHECK_INVARIANTS;

    protected final boolean DEBUG;

    protected final DOMFactory factory;

    /**
     * The parent element, is null if this is the root node.
     * <p>
     * This is package private so it can be accessed by other classes in this
     * package.
     * </p>
     */
    ElementImpl parent;

    /**
     * The next node, is null if this is the last node in the list.
     * <p>
     * This is package private so it can be accessed by other classes in this
     * package.
     * </p>
     */
    NodeImpl next;

    /**
     * The previous node, is null if this is the first node in the list.
     * <p>
     * This is package private so it can be accessed by other classes in this
     * package.
     * </p>
     */
    NodeImpl previous;

    /**
     * User data associated with the node. This data is just for use within
     * a single processing stage, and can be overwritten at any time. Therefore
     * it cannot be guaranteed to persist across processing stages.
     */
    Object object;

    /**
     * Annotation data associated with the node. This data is longer-lived
     * than the user data and persists across processing stages. This data can
     * only be set once.
     */
    Object annotation;

    /**
     * Create a new <code>Node</code>.
     */
    NodeImpl(DOMFactory factory) {
        this.factory = factory;
        this.CHECK_INVARIANTS = factory.isDebug();
        this.DEBUG = logger.isDebugEnabled();
    }

    // Javadoc inherited.
    public DOMFactory getDOMFactory() {
        return factory;
    }

    public Element getParent() {
        return parent;
    }

    public Node getPrevious() {
        return previous;
    }

    public Node getNext() {
        return next;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setAnnotation(Object annotation) {
        if (this.annotation != null) {
            throw new IllegalStateException("annotation has already been set.");
        }
        this.annotation = annotation;
    }

    public Object getAnnotation() {
        return annotation;
    }

    public void insertAfter(Node node) {

        NodeImpl nodeImpl = (NodeImpl) node;
        if (logger.isDebugEnabled()) {
            logger.debug("Inserting " + this + " after " + nodeImpl);
        }

        if (CHECK_INVARIANTS) {
            checkIsolated();
            nodeImpl.checkInvariants();
        }

        // Update this node's links.
        previous = nodeImpl;
        next = nodeImpl.next;
        parent = nodeImpl.parent;

        // Update the links in the tree.
        if (next == null) {
            // This node is the last in the list so we need to make this node
            // the tail.
            parent.tail = this;
        } else {
            // Add a link back to this node.
            next.previous = this;
        }

        // Make this node the next node after the one in the tree.
        nodeImpl.next = this;

        if (CHECK_INVARIANTS) {
            nodeImpl.checkInvariants();
        }
    }

    public void insertBefore(Node node) {

        NodeImpl nodeImpl = (NodeImpl) node;
        if (logger.isDebugEnabled()) {
            logger.debug("Inserting " + this + " before " + nodeImpl);
        }

        if (CHECK_INVARIANTS) {
            checkIsolated();
            nodeImpl.checkInvariants();
        }

        // Update this node's links.
        previous = nodeImpl.previous;
        next = nodeImpl;
        parent = nodeImpl.parent;

        // Update the links in the tree.
        if (previous == null) {
            // This node is the first in the list so we need to make it the head.
            parent.head = this;
        } else {
            // Add a link forward to this node.
            previous.next = this;
        }

        // Make this node the previous node before the one in the tree.
        nodeImpl.previous = this;

        if (CHECK_INVARIANTS) {
            nodeImpl.checkInvariants();
        }
    }

    public void addToTail(Element element) {

        if (logger.isDebugEnabled()) {
            logger.debug("Adding " + this + " to tail of " + element);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        element.addTail(this);

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }
    }

    public void addToHead(Element element) {

        if (logger.isDebugEnabled()) {
            logger.debug("Adding " + this + " to head of " + element);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        element.addHead(this);

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }
    }

    public void replace(Node node) {
        NodeImpl nodeImpl = (NodeImpl) node;
        nodeImpl.replaceWith(this);
    }

    /**
     * Replace this node with the specified node.
     * @param node The node which will replace this one.
     */
    private void replaceWith(NodeImpl node) {

        if (logger.isDebugEnabled()) {
            logger.debug("Replacing " + this + " with " + node);
        }

        if (CHECK_INVARIANTS) {
            node.checkIsolated();
            checkInvariants();
        }

        if (previous == null) {
            parent.head = node;
        } else {
            previous.next = node;
        }

        if (next == null) {
            parent.tail = node;
        } else {
            next.previous = node;
        }

        node.parent = parent;
        node.next = next;
        node.previous = previous;

        parent = null;
        next = null;
        previous = null;

        if (CHECK_INVARIANTS) {
            node.checkInvariants();
            checkIsolated();
        }
    }

    public void remove() {

        if (logger.isDebugEnabled()) {
            logger.debug("Removing " + this);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        if (previous == null) {
            // This node is the first in the list so update the parent element's
            // head to reference the next node.
            parent.head = next;
        } else {
            // Update the previous node's next link so that it references the node
            // after this one.
            previous.next = next;
        }

        if (next == null) {
            // This node is the last in the list so update the parent element's
            // tail to reference the previous node.
            parent.tail = previous;
        } else {
            // Update the next node's previous link so that it references the node
            // before this one.
            next.previous = previous;
        }

        ElementImpl oldParent = parent;

        // Reset this node.
        previous = null;
        parent = null;
        next = null;

        if (CHECK_INVARIANTS) {
            oldParent.checkInvariants();
            checkIsolated();
        }
    }

    public boolean promote() {
        return parent.promoteNode(this);
    }

    public boolean promote(boolean trimEmptyNodes) {
        return parent.promoteNode(this, trimEmptyNodes);
    }

    // Javadoc inherited.
    public void replaceWith(NodeSequence sequence) {

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        PrivateNodeSequence s = (PrivateNodeSequence) sequence;

        // If the sequence is empty then simply remove this node and return
        // immediately.
        NodeImpl first = (NodeImpl) s.getFirst();
        if (first == null) {
            remove();
            return;
        }

        // Make sure that the node being replaced is not in the sequence as
        // that would corrupt the DOM.
        Node end = s.getEnd();
        for (NodeImpl node = first; node != end; node = node.next) {
            if (this == node) {
                throw new IllegalStateException(
                        "Cannot replace node with sequence containing node");
            }

            // Update the parent.
            node.parent = parent;
        }

        NodeImpl last = (NodeImpl) s.getLast();

        if (previous == null) {
            parent.head = first;
        } else {
            previous.next = first;
        }
        first.previous = previous;

        if (next == null) {
            parent.tail = last;
        } else {
            next.previous = last;
        }
        last.next = next;

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }
    }

    void checkIsolated() {
        if (parent != null) {
            throw new IllegalStateException
                    (this + ": Isolated node should have a null parent but it is "
                    + parent);
        }
        if (next != null) {
            throw new IllegalStateException
                    (this + ": Isolated node should have a null next but it is "
                    + next);
        }
        if (previous != null) {
            throw new IllegalStateException
                    (this + ": Isolated node should have a null previous but it is "
                    + previous);
        }

        checkInvariants();
    }

    void checkWholeTree() {
        NodeImpl root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        root.checkInvariants();
    }

    void checkInvariants() {

        if (parent != null) {
            checkWholeTree();
        } else {
            try {
                if (logger.isDebugEnabled()) {
                    debug();
                }

                checkInvariantsImpl();
            } catch (IllegalStateException e) {
                if (logger.isDebugEnabled()) {
                    debug();
                }

                throw e;
            }
        }
    }

    void checkInvariantsImpl() {
        if (parent == null) {
            if (previous != null) {
                throw new IllegalStateException
                        (this + ": previous should be null when not in tree but is "
                        + previous);
            }
            if (next != null) {
                throw new IllegalStateException
                        (this + ": next should be null when not in tree but is "
                        + next);
            }
        } else {
            if (previous == null && parent.head != this) {
                throw new IllegalStateException
                        (this + ": previous is null but this is not head of the list");
            }
            if (next == null && parent.tail != this) {
                throw new IllegalStateException
                        (this + ": next is null but this is not tail of the list");
            }
        }
    }

    void debug() {
        //logger.debug ("Who is calling this", new Throwable ());
        if (logger.isDebugEnabled()) {
            logger.debug("Starting");
        }
        debug("  ");
        if (logger.isDebugEnabled()) {
            logger.debug("Ending");
        }
    }

    void debug(String indent) {
        if (logger.isDebugEnabled()) {
            logger.debug(indent + "node " + this);
        }
        debugLinks(indent);
    }

    void debugLinks(String indent) {
        if (logger.isDebugEnabled()) {
            logger.debug(indent + "previous " + previous);
            logger.debug(indent + "parent " + parent);
            logger.debug(indent + "next " + next);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 14-Mar-05	7357/4	pcameron	VBM:2005030906 Fixed node annotation for dissection

 11-Mar-05	7357/2	pcameron	VBM:2005030906 Fixed node annotation for dissection

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
