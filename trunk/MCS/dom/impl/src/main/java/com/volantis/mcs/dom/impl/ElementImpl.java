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
 * $Header: /src/voyager/com/volantis/mcs/dom/Element.java,v 1.13 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 24-Apr-02    Paul            VBM:2002042202 - Added addChildrenToHead
 *                              method.
 * 26-Apr-02    Paul            VBM:2002042205 - Added clearAttributes method.
 * 09-May-02    Ian             VBM:2002031203 - Changed log4j to use string.
 * 05-Jun-02    Adrian          VBM:2002021103 - Added method containsOnly..
 *                              ..WhiteSpace to test if a promoted node has
 *                              only whitespace content.  If so the node is
 *                              discarded.
 * 02-Jun-02    Mat             VBM:2002070203 - clearAttributes was empty, so
 *                              add the code to walk through the attributes
 *                              list to release the attributes.
 * 06-Aug-02    Paul            VBM:2002073008 - Fixed problem with promoteNode
 *                              which caused empty paragraphs to be created.
 * 19-Aug-02    Byron           VBM:2002081311 - Modified copyAttributes to
 *                              copy the attributes correctly.
 * 20-Aug-02    Byron           VBM:2002081311 - Modified copy to check for null
 *                              arguments, copyAttributes to clear attribute list
 *                              before the copy, and clearAttributes to set the
 *                              list to null
 * 16-Jan-03    Chris W         VBM:2002111508 - Removed containsOnlyWhiteSpace.
 *                              containsOnlyWhiteSpace is almost protocol
 *                              specific so it has been moved to the WMLDOM...
 *                              ..Transformer. This means that promoteNode now
 *                              just promote nodes. Previously it promoted them
 *                              and tried to discard unnecessary ones. This
 *                              'feature' is the cause of this vbm.
 * 24-Jan-03    Geoff           VBM:2003012302 - Back out chris's change above,
 *                              then added the boolean trimEmptyNodes to
 *                              promoteNode, renamed containsOnlyWhitespace to
 *                              emptyOrWhitespaceOnly, and changed it's impl
 *                              so that it no longer recurses.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.DOMVisitor;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.NodeSequence;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class represents an element in the dom.
 */
public class ElementImpl
        extends NodeImpl
        implements Element {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ElementImpl.class);

    /**
     * The name of this element.
     */
    private String name;

    /**
     * The Styles for this element.
     */
    private Styles styles;

    /**
     * The head of the list of child nodes.
     *
     * <p>This is package private so it can be accessed by other classes in this
     * package.</p>
     */
    NodeImpl head;

    /**
     * The tail of the list of child nodes.
     *
     * <p>This is package private so it can be accessed by other classes in this
     * package.</p>
     */
    NodeImpl tail;

    /**
     * A linked list of attributes, sorted in ascending order by name.
     */
    private AttributeImpl attributes;

    /**
     * Create a new <code>Element</code>.
     */
    public ElementImpl(DOMFactory factory) {
        super(factory);
    }

    // Javadoc inherited.
    public void copy(Element element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        ElementImpl elementImpl = (ElementImpl) element;
        name = elementImpl.name;
        copyAttributes(elementImpl);
        this.styles = element.getStyles();
    }

    public boolean isEmpty() {
        return (head == null);
    }

    public boolean hasAttributes() {
        return (attributes != null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    // Javadoc inherited.
    public void clearChildren() {

        if (logger.isDebugEnabled()) {
            logger.debug("Clearing children from " + this);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        head = null;
        tail = null;

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }
    }

    public void addHead(Node node) {

        NodeImpl nodeImpl = (NodeImpl) node;

        if (logger.isDebugEnabled()) {
            logger.debug("Adding " + nodeImpl + " to head of " + this);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        addHead(nodeImpl, nodeImpl);

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }
    }

    public void addTail(Node node) {
        NodeImpl nodeImpl = (NodeImpl) node;

        if (logger.isDebugEnabled()) {
            logger.debug("Adding " + nodeImpl + " to tail of " + this);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

//    // Update the node's links.
//    nodeImpl.previous = tail;
//    nodeImpl.next = null;
//    nodeImpl.parent = this;
//
//    if (tail == null) {
//      // This element has no children.
//      head = nodeImpl;
//    } else {
//      // Insert the node after the last node in the list.
//      tail.next = nodeImpl;
//    }
//
//    tail = nodeImpl;
        addTail(nodeImpl, nodeImpl);

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }
    }

    public void addChildrenToHead(Element element) {

        ElementImpl elementImpl = (ElementImpl) element;

        if (logger.isDebugEnabled()) {
            logger.debug("Adding children of " + this + " to the head of "
                    + elementImpl);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        // If this element is already empty then there is nothing to do.
        if (head == null) {
            return;
        }

        // Make sure that the nodes being added refer to the correct parent.
        updateParentReferences(head, elementImpl);

        // Update the links of the head and tail nodes of the list of nodes being
        // added.
        head.previous = null;
        tail.next = elementImpl.head;

        // Update the links in the tree.
        if (elementImpl.head == null) {
            // The element into which the nodes are being inserted is empty
            // so make the first node in the list the head.
            elementImpl.tail = tail;
        } else {
            elementImpl.head.previous = tail;
        }
        elementImpl.head = head;

        head = null;
        tail = null;

        if (CHECK_INVARIANTS) {
            elementImpl.checkInvariants();
        }
    }

    public void addChildrenToTail(Element element) {

        ElementImpl elementImpl = (ElementImpl) element;

        if (logger.isDebugEnabled()) {
            logger.debug("Adding children of " + this + " to the tail of "
                    + elementImpl);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        // If this element is already empty then there is nothing to do.
        if (head == null) {
            return;
        }

        // Make sure that the nodes being added refer to the correct parent.
        updateParentReferences(head, elementImpl);

        // Update the links of the head and tail nodes of the list of nodes being
        // added.
        head.previous = elementImpl.tail;
        tail.next = null;

        // Update the links in the tree.
        if (elementImpl.tail == null) {
            // The element into which the nodes are being inserted is empty
            // so make the first node in the list the head.
            elementImpl.head = head;
        } else {
            elementImpl.tail.next = head;
        }
        elementImpl.tail = tail;

        head = null;
        tail = null;

        if (CHECK_INVARIANTS) {
            elementImpl.checkInvariants();
        }
    }

    public void insertChildrenAfter(Node node) {

        NodeImpl nodeImpl = (NodeImpl) node;

        if (logger.isDebugEnabled()) {
            logger.debug("Inserting children of " + this + " after "
                    + nodeImpl);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        // If this element is already empty then there is nothing to do.
        if (head == null) {
            return;
        }

        // Make sure that the nodes being added refer to the correct parent.
        updateParentReferences(head, nodeImpl.parent);

        // Update the links of the head and tail nodes of the list of nodes being
        // added.
        head.previous = nodeImpl;
        tail.next = nodeImpl.next;

        // Update the links in the tree.
        if (nodeImpl.next == null) {
            // The nodes have been inserted at the end of the list so make the
            // last node in the list the tail.
            nodeImpl.parent.tail = tail;
        } else {
            nodeImpl.next.previous = tail;
        }
        nodeImpl.next = head;

        head = null;
        tail = null;

        if (CHECK_INVARIANTS) {
            nodeImpl.parent.checkInvariants();
        }
    }

    public void insertChildrenBefore(Node node) {

        NodeImpl nodeImpl = (NodeImpl) node;

        if (logger.isDebugEnabled()) {
            logger.debug("Inserting children of " + this + " before "
                    + nodeImpl);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        // If this element is already empty then there is nothing to do.
        if (head == null) {
            return;
        }

        // Make sure that the nodes being added refer to the correct parent.
        updateParentReferences(head, nodeImpl.parent);

        // Update the links of the head and tail nodes of the list of nodes being
        // added.
        head.previous = nodeImpl.previous;
        tail.next = nodeImpl;

        // Update the links in the tree.
        if (nodeImpl.previous == null) {
            // The nodes have been inserted at the start of the list so make the
            // first node in the list the head.
            nodeImpl.parent.head = head;
        } else {
            nodeImpl.previous.next = head;
        }
        nodeImpl.previous = tail;

        head = null;
        tail = null;

        if (CHECK_INVARIANTS) {
            nodeImpl.parent.checkInvariants();
        }
    }

    /**
     * Promote the specified node.
     * <p>
     * If the node is the only child of this element then this element
     * is replaced by the node.
     * </p>
     * <p>
     * If the node is the first child of this element then it is
     * inserted before this element.
     * </p>
     * <p>
     * If the node is the last child of this element then it is
     * inserted after this element.
     * </p>
     * <p>
     * Otherwise this element is split into two, the first of which contains
     * all the children which precede the node and the second of which contains
     * all the children which follow the node. The node is inserted between the
     * two elements.
     * </p>
     *
     * @param node The node to promote.
     * @return True if the parent node was replaced and therefore can
     *         be discarded.
     */
    boolean promoteNode(Node node) {
        return promoteNode(node, false);
    }

    /**
     * A variant of {@link #promoteNode(Node)} which allows the client to control
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
     * @param node           The node to promote.
     * @param trimEmptyNodes Set to true if you want "empty" nodes removed.
     * @return True if the parent node was replaced and therefore can
     *         be discarded.
     */
    boolean promoteNode(Node node, boolean trimEmptyNodes) {

        NodeImpl nodeImpl = (NodeImpl) node;

        boolean discard = false;

        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Promoting " + nodeImpl + " to the same level as " + this);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        if (nodeImpl.parent != this) {
            throw new IllegalStateException
                    ("Node being promoted is not a child of this");
        }

        if (head == nodeImpl) {
            // Remove the node before we add it anywhere else in the tree.
            nodeImpl.remove();

            if (tail == null) {
                // The node being promoted was the only child so replace its parent
                // with it.
                nodeImpl.replace(this);
                discard = true;
            } else {
                // The node being promoted is the first child so insert it before
                // this element
                nodeImpl.insertBefore(this);
            }
        } else if (tail == nodeImpl) {
            // Remove the node before we add it anywhere else in the tree.
            nodeImpl.remove();
            // The element being promoted is the last child so insert it after
            // this element.
            nodeImpl.insertAfter(this);
        } else {
            // The element being promoted is somewhere in the middle so we need to
            // split the parent element into two.

            // Create a new element with the same attributes.
            ElementImpl after = (ElementImpl) factory.createElement();
            after.copy(this);

            // Add all the nodes which follow the node being promoted to the new
            // element.
            after.head = nodeImpl.next;
            after.head.previous = null;
            after.tail = tail;

            // Make sure that all the nodes being added to the new element correctly
            // refer to it as their parent.
            updateParentReferences(after.head, after);

            // Discard all the nodes which follow the node being promoted from
            // this element, do this after creating the new element, as it relies
            // on the original value of tail.
            tail = nodeImpl.previous;
            tail.next = null;

            if (CHECK_INVARIANTS) {
                after.checkInvariants();
            }

            // Isolate the node, otherwise the insertAfter method will throw an
            // exception.
            nodeImpl.parent = null;
            nodeImpl.next = null;
            nodeImpl.previous = null;

            if (trimEmptyNodes && emptyOrWhitespaceOnly(this)) {
                nodeImpl.replace(this);
                discard = true;
            } else {
                // Insert the node after this element.
                nodeImpl.insertAfter(this);
            }

            if (!(trimEmptyNodes && emptyOrWhitespaceOnly(after))) {
                // Insert the new element after the node.
                after.insertAfter(nodeImpl);
            } else {
                // We created it but didn't need it after all.
                // Might be a bit quicker to check earlier to avoid creating after in
                // the first place, but this is simpler.
            }

            if (CHECK_INVARIANTS) {
                parent.checkInvariants();
            }
        }

        return discard;
    }

    /**
     * Check if an element is empty, or contains only whitespace.
     *
     * @param element The <code>Element</code> to check.
     * @return true if the <code>Element</code> contains only
     *         whitespace or is empty, otherwise false.
     */
    private boolean emptyOrWhitespaceOnly(Element element) {
        boolean success = true;
        for (Node x = element.getHead(); x != null; x = x.getNext()) {
            success =
                    success && (x instanceof Text) && ((Text) x).isWhitespace();
        }
        return success;
    }

    /**
     * Update the parent references for all the nodes in the sequence
     * from the first to the last node inclusive.
     *
     * @param first  The first node in the sequence.
     * @param last   The last node in the sequence.
     * @param parent The new parent for the nodes.
     */
    private void updateParentReferences(
            NodeImpl first, NodeImpl last,
            ElementImpl parent) {

        Node end = (last == null ? null : last.next);
        for (NodeImpl node = first; node != end; node = node.next) {
            node.parent = parent;
        }
    }

    /**
     * Update the parent references for all the nodes in the sequence
     * from the first to the end of the list inclusive.
     *
     * @param first  The first node in the sequence.
     * @param parent The new parent for the nodes.
     */
    private void updateParentReferences(NodeImpl first, ElementImpl parent) {
        updateParentReferences(first, null, parent);
    }

    // Javadoc inherited.
    public NodeSequence removeChildren() {

        if (logger.isDebugEnabled()) {
            logger.debug("Removing children from " + this);
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        NodeSequence children;
        if (head == null) {
            children = EmptyNodeSequence.INSTANCE;
        } else {
            children = new NodeSequenceImpl(head, tail);

            head = null;
            tail = null;
        }

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        return children;
    }

    public void setAttribute(String name, String value) {

        // Check the arguments.
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        // Search through the attributes.
        //
        // If an attribute which matches is found then modify it and return.
        //
        // Otherwise if no match is found then get a new attribute object,
        // initialise it, insert it at the right point and return.
        AttributeImpl insertPoint = null;
        AttributeImpl attribute;
        for (attribute = attributes; attribute != null;
             attribute = attribute.next) {

            int result = name.compareTo(attribute.name);
            if (result == 0) {
                // The attribute matches an existing attribute so modify the attribute
                // and return.
                attribute.value = value;
                return;
            } else if (result < 0) {
                // The attribute being added comes before the current attribute and
                // after the insert point so break out and add it.
                break;
            }

            // The attribute comes sometime after the current attribute so remember
            // this position in case it should come immediately after the current
            // attribute and move on to the next one.
            insertPoint = attribute;
        }

        // Obtain and initialise the new attribute.
        attribute = allocateAttribute();
        attribute.name = name;
        attribute.value = value;

        if (insertPoint == null) {
            // The attribute should be added at the start of the list.
            attribute.next = attributes;
            attributes = attribute;
        } else {
            // The attribute should be added after the insert point.
            attribute.next = insertPoint.next;
            insertPoint.next = attribute;
        }
    }

    // Javadoc inherited
    public Styles getStyles() {
        return styles;
    }

    // Javadoc inherited
    public void setStyles(Styles styles) {
//        if (styles == null) {
//            throw new IllegalArgumentException("styles cannot be null");
//        }
        this.styles = styles;
    }

    // javadoc inherited.
    public String getAttributeValue(String name) {

        // Search through the attributes.
        //
        // If an attribute which matches is found then return its value.
        //
        // Otherwise if no match is found then keep searching until the end of the
        // list is reached, or the point at which the attribute should be is
        // passed.
        AttributeImpl attribute;
        for (attribute = attributes; attribute != null;
             attribute = attribute.next) {

            int result = name.compareTo(attribute.name);
            if (result == 0) {
                // The attribute matches an existing attribute so return the value.
                return attribute.value;
            } else if (result < 0) {
                // The attribute should have come before the current attribute so this
                // element does not have a setting for the attribute so return null.
                return null;
            }
        }

        // The attribute should have come after the end of the list so this element
        // does not have a setting for the attribute so return null.
        return null;
    }

    public void removeAttribute(String name) {

        // Search through the attributes.
        //
        // If an attribute which matches is found then remove it.
        AttributeImpl removePoint = null;
        AttributeImpl attribute;
        for (attribute = attributes; attribute != null;
             attribute = attribute.next) {

            int result = name.compareTo(attribute.name);
            if (result == 0) {
                // The attribute matches an existing attribute so remove the attribute
                // and return.
                if (removePoint == null) {
                    attributes = attribute.next;
                } else {
                    removePoint.next = attribute.next;
                }
                return;
            } else if (result < 0) {
                // The attribute should have come before the current attribute so this
                // element does not have a setting for the attribute so return.
                return;
            }

            // The attribute comes sometime after the current attribute so remember
            // this position in case it should come immediately after the current
            // attribute and move on to the next one.
            removePoint = attribute;
        }
    }

    public Attribute getAttributes() {
        return attributes;
    }

    public void copyAttributes(Element element) {

        ElementImpl elementImpl = (ElementImpl) element;

        if (elementImpl == null) {
            throw new IllegalArgumentException();
        }
        // Clear the current list of attributes (we want the current list to be
        // overwritten with whatever is in the other element object's attribute list
        clearAttributes();

        AttributeImpl last = null;
        for (AttributeImpl attribute = elementImpl.attributes;
             attribute != null;
             attribute = attribute.next) {
            AttributeImpl copy = allocateAttribute();

            copy.copy(attribute);
            if (last != null) {
                last.next = copy;
            } else {
                // Update my own list with 'copy' becoming the first item in the list
                attributes = copy;
            }
            last = copy;
        }

    }

    public void mergeAttributes(
            Element element, boolean override) {

        ElementImpl elementImpl = (ElementImpl) element;

        AttributeImpl a1 = attributes;
        AttributeImpl a2 = elementImpl.attributes;

        AttributeImpl insertPoint = null;
        String n1;
        String n2;
        while (a2 != null) {

            // Get the name of the attribute to insert.
            n2 = a2.name;

            int result;
            if (a1 == null) {
                // We have reached the end of the existing attributes but still
                // have more to add so fake the result so we will add another
                // attribute.
                result = -1;
            } else {
                n1 = a1.name;
                // Compare the name of the attribute being added to the existing
                // attribute.
                result = n2.compareTo(n1);
            }

            if (result == 0) {
                // The attribute exists in both elements so we should only update
                // the value if the override flag is true.
                if (override) {
                    a1.value = a2.value;
                }

                insertPoint = a1;

                // Move on to the next attributes.
                a1 = a1.next;
                a2 = a2.next;

            } else if (result < 0) {
                // The attribute being added comes before the current attribute and
                // after the insert point so add it and update the insert point.
                insertPoint = insertAttribute(insertPoint, n2, a2.value);

                // Move on to the next attribute to add.
                a2 = a2.next;

            } else {

                insertPoint = a1;

                // The attribute being added comes after the current attribute so
                // move on to the next attribute.
                a1 = a1.next;
            }
        }

        if (a1 == null) {
            if (a2 == null) {
                // Everything has been done.
            } else {
                // Extra attributes to add at the end.

            }
        }
    }

    public void clearAttributes() {

        AttributeImpl next;
        AttributeImpl current = attributes;

        while (current != null) {
            // Preserve the next attribute in the list before releasing.
            next = (AttributeImpl) current.getNext();
            current = next;
        }
        attributes = null;
    }

    private AttributeImpl insertAttribute(
            AttributeImpl insertPoint,
            String name, String value) {

        // Obtain and initialise the new attribute.
        AttributeImpl attribute = allocateAttribute();
        attribute.name = name;
        attribute.value = value;

        if (insertPoint == null) {
            // The attribute should be added at the start of the list.
            attribute.next = attributes;
            attributes = attribute;
        } else {
            // The attribute should be added after the insert point.
            attribute.next = insertPoint.next;
            insertPoint.next = attribute;
        }

        return attribute;
    }

    /**
     * Allocate an Attribute.
     * <p>
     * If this element has an AttributePool then allocate the Attribute from
     * it, otherwise instantiate a new one.
     * </p>
     *
     * @return The Attribute which was allocated.
     */
    private AttributeImpl allocateAttribute() {
        return new AttributeImpl();
    }

    // Javadoc inherited.
    public void accept(DOMVisitor visitor) {
        visitor.visit(this);
    }

    // Javadoc inherited.
    public void forEachChild(NodeIteratee nodeIteratee) {
        NodeImpl nextNode;
        for (NodeImpl node = head; node != null; node = nextNode) {
            nextNode = node.next;
            if (node != null) {
                IterationAction action = nodeIteratee.next(node);
                if (action == IterationAction.BREAK) {
                    break;
                } else if (action != IterationAction.CONTINUE) {
                    throw new IllegalStateException(
                            "Unknown iteration action: " + action);
                }
            }
        }
    }

    public void addText(String string) {
        Text text = factory.createText();
        text.append(string);
        addTail(text);
    }

    /**
     * Insert the sequence of nodes represented by the first and last nodes
     * in the sequence after the specified child.
     *
     * @param first The first node in the sequence.
     * @param last  The last node in the sequence.
     * @param node  The child node to insert after.
     */
    private void insertAfter(NodeImpl first, NodeImpl last, NodeImpl node) {

        if (node == null) {
            addHead(first, last);
        } else {

            updateParentReferences(first, last, this);

            last.next = node.next;
            if (node.next == null) {
                tail = last;
            } else {
                node.next.previous = last;
            }

            first.previous = node;
            node.next = first;
        }
    }

    private void addHead(NodeImpl first, NodeImpl last) {

        updateParentReferences(first, last, this);

        // Update the node's links.
        first.previous = null;
        last.next = head;

        if (head == null) {
            // This element has no children.
            tail = last;
        } else {
            // Insert the node before the first node in the list.
            head.previous = last;
        }

        head = first;
    }

    private void addTail(NodeImpl first, NodeImpl last) {

        updateParentReferences(first, last, this);

        // Update the node's links.
        first.previous = tail;
        last.next = null;

        if (tail == null) {
            // This element has no children.
            head = first;
        } else {
            // Insert the node after the last node in the list.
            tail.next = first;
        }

        tail = last;
    }

    
    // Javadoc inherited.
    public Node insertAfter(NodeSequence nodes, Node child) {

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        PrivateNodeSequence s = (PrivateNodeSequence) nodes;

        NodeImpl node = (NodeImpl) child;

        // If the sequence is empty then return immediately.
        NodeImpl first = (NodeImpl) s.getFirst();
        if (first == null) {
            return child;
        }

        NodeImpl last = (NodeImpl) s.getLast();
        insertAfter(first, last, node);

        if (CHECK_INVARIANTS) {
            checkInvariants();
        }

        return last;
    }

    // Javadoc inherited.
    public void clearStyles() {
        styles = null;
    }

    // Javadoc inherited.
    void checkInvariantsImpl() {
        super.checkInvariantsImpl();

        // Either head and tail must both be null, or neither must be null.
        if ((head == null) != (tail == null)) {
            throw new IllegalStateException(
                    "Either head and tail must both be null, or neither " +
                            "must be null but head is '" + head +
                            "' and tail is '" +
                            tail + "'");
        }

        for (NodeImpl node = head; node != null; node = node.next) {
            if (node.parent != this) {
                throw new IllegalStateException
                        (this + ": child " + node + " has parent of " +
                                node.parent);
            }
            node.checkInvariantsImpl();
        }
    }

    void debug(String indent) {
        if (logger.isDebugEnabled()) {
            logger.debug(indent + "<" + name + ">  " + this);
            logger.debug(indent + "head " + head);
            logger.debug(indent + "tail " + tail);
        }
        super.debugLinks(indent);
        String childIndent = indent + "  ";
        for (NodeImpl node = head; node != null; node = node.next) {
            node.debug(childIndent);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(indent + "</" + name + ">");
        }
    }

    // Javadoc inherited from super class.
    public String toString() {
        return getClass().getName()
                + "@" + Integer.toHexString(System.identityHashCode(this))
                + " [" + paramString() + "]";
    }

    /**
     * Return a String representation of the state of the object.
     *
     * @return The String representation of the state of the object.
     */
    protected String paramString() {
        StringBuffer buf = new StringBuffer();
        buf.append(name);

        for (Attribute att = attributes; att != null; att = att.getNext()) {
            buf.append(" ").append(att.getName()).append("='")
                    .append(att.getValue()).append("'");
        }
        return buf.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 23-Jun-05	8483/7	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 22-Jun-05	8856/3	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 10-Nov-04	6094/2	tom	VBM:2004101510 Created CSSEmulator based StyleEngine

 15-Oct-04	5798/1	adrianj	VBM:2004082515 Device Theme Cascade: Find matching rules

 12-Oct-04	5778/3	adrianj	VBM:2004083106 Provide styling engine API (rework)

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
