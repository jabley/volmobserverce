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
 * $Header: /src/voyager/com/volantis/mcs/dom/Element.java,v 1.6.2.1.2.5 2003/01/28 15:07:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-May-03    Geoff           VBM:2003042905 - Created; represents an 
 *                              element in the WBDOM.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.wbdom;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Represents an element node in the WBDOM.
 */
public abstract class WBDOMElement extends WBDOMNode 
        implements NameVisitor.Acceptor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(WBDOMElement.class);

    /**
     * The head of the list of child nodes.
     */
    WBDOMNode children;

    /**
     * The tail of the list of child nodes.
     */
    private WBDOMNode lastChild;

    /**
     * A head of the list of attributes.
     */
    WBDOMAttribute attributes;

    /**
     * The tail of the list of attributes.
     */
    private WBDOMAttribute lastAttribute;
    
    /**
     * Get the name, <b>FOR DEBUGGING ONLY</b>.
     * <p>
     * In general, instances of {@link NameVisitor} should be used to access
     * the name information of a WBDOM element, since it may occur in two 
     * different forms, either coded or literal. Converting back to a string
     * is possible but may be high cost, and any operations we might do on
     * a string would be quicker if implemented as codes anyway (eg int 
     * comparison is quicker than string comparison). So, this is marked as
     * for debugging only.
     *  
     * @return The name, for debugging only.
     */
    public abstract String getName() throws WBDOMException;

    
    //
    // Child Management
    //
    
    /**
     * Return true if this element has children and false otherwise.
     */
    public boolean hasChildren() {
        return (children != null);
    }

    /**
     * Get the first node in the child list.
     * @return The head node.
     */
    public WBDOMNode getChildren() {
        return children;
    }

    /**
     * Internal iterator for child nodes.
     * <p>
     * Using this is preferable to the external iterator methods 
     * {@link #hasChildren}, {@link #getChildren} and 
     * {@link #getLastChild} since the element handles the iteration
     * for you.
     *  
     * @param iterator
     * @throws WBDOMException
     */ 
    public void forEachChild(ChildrenInternalIterator iterator) 
            throws WBDOMException {
        iterator.before();
        WBDOMNode child = getChildren();
        if (child != null) {
            while (child != null) {
                iterator.next(child);
                child = child.getNext();
            }
        } 
        iterator.after();
    }
    
    /**
     * Get the last node in the child list.
     * @return The tail node.
     */
    public WBDOMNode getLastChild() {
        return lastChild;
    }

    /**
     * Add the specified node at the tail of the list of children.
     * 
     * @param node The node to add.
     */
    public void addChild(WBDOMNode node) {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding " + node + " to tail of " + this);
        }

        if (node.next != null || node.parent != null) {
            throw new IllegalStateException("Node must be standalone");
        }

        // Reject child addition if element is declared to be empty.
        if (emptyType != null) {
            throw new IllegalStateException("Attempt to add child to an " +
                "element which has empty type " + emptyType);
        }
        
        // Update the node's links.
        node.parent = this;
        if (lastChild == null) {
            // This element has no children.
            children = node;
        } else {
            // Insert the node after the last node in the list.
            lastChild.next = node;
        }
        lastChild = node;
    }

    /**
     * The type of element to size and write if this element is empty (i.e.
     * has no children).
     * <p>
     * This may only be set if there are no children, and must be set before 
     * {@link #getEmptyType} is called. Once it is set, no children may be
     * added to this element. 
     */ 
    private EmptyElementType emptyType;

    /**
     * Sets the type of element to size and write if this element is empty 
     * (i.e. has no children).
     * <p>
     * If you intend to call {@link #getEmptyType} you need to call this 
     * otherwise it will fail. In this sense, this can be considered to be
     * an extension of the constructor.
     * <p>
     * This will fail with an exception if this element has children or if
     * the type provided is null. 
     * 
     * @param type
     */ 
    public void setEmptyType(EmptyElementType type) {
        // Disallow null empty type and ensure we have no children.
        checkEmpty(type);
        emptyType = type;
    }

    /**
     * Returns the type of empty element this is, if it is empty (i.e. has no
     * children).
     * <p>
     * This will fail with an exception if this element has children or if
     * the matching {@link #setEmptyType} has never been called. 
     * 
     * @return an EmptyElementType
     */ 
    public EmptyElementType getEmptyType() {
        // Disallow null empty type and ensure we have no children.
        checkEmpty(emptyType);
        return emptyType;
    }

    /**
     * Convenience method to check for prerequisites for the empty type
     * accessor and mutator, which happen to be shared.
     * 
     * @param type empty element type proposed to be used.
     */ 
    private void checkEmpty(EmptyElementType type) {
        if (type == null) {
            throw new IllegalStateException(
                    "Empty element may not have a null empty element type");
        }
        if (hasChildren()) {
            throw new IllegalArgumentException("Unable to access empty " +
                    "element type for element " + this + ", it has children");
        }
    }
    
    //
    // Attribute Management.
    //
    
    /**
     * Return true if this element has at least one attribute.
     */
    public boolean hasAttributes() {
        return (attributes != null);
    }
    
    /**
     * Get the first attribute in a singly linked list of attributes.
     * @return The first attribute in the linked list of attributes.
     */
    public WBDOMAttribute getAttributes() {
        return attributes;
    }

    /**
     * Get the last node in the attribute list.
     * @return The last attribute.
     */
    public WBDOMAttribute getLastAttribute() {
        return lastAttribute;
    }

    /**
     * Internal iterator for attributes.
     * <p>
     * Using this is preferable to the external iterator methods 
     * {@link #hasAttributes}, {@link #getAttributes} and 
     * {@link #getLastAttribute} since the element handles the iteration
     * for you.
     *  
     * @param iterator
     * @throws WBDOMException
     */ 
    public void forEachAttribute(AttributesInternalIterator iterator) 
            throws WBDOMException {
        iterator.before();
        WBDOMAttribute attribute = getAttributes();
        if (attribute != null) {
            while (attribute != null) {
                iterator.next(attribute);
                attribute = attribute.getNext();
            }
        } 
        iterator.after();
    }

    /**
     * Add the specified node at the tail of the list of children.
     * 
     * @param attribute The node to add.
     */
    public void addAttribute(WBDOMAttribute attribute) {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding " + attribute + " to " + this);
        }

        if (attribute.next != null) {
            throw new IllegalStateException("Attribute must be standalone");
        }

        // Update the node's links.
        if (lastAttribute == null) {
            // This element has no children.
            attributes = attribute;
        } else {
            // Insert the node after the last node in the list.
            lastAttribute.next = attribute;
        }
        lastAttribute = attribute;
    }

    
    //
    // Visitors
    //

    public void accept(WBDOMVisitor visitor) throws WBDOMException {
        visitor.visitElement(this);
    }

    // Javadoc inherited from super class.
    public String toString() {
        return getClass().getName()
                + "@" + Integer.toHexString(System.identityHashCode(this))
                + " [" + toStringName() + "]";
    }

    private String toStringName() {
        try {
            return getName();
        } catch (WBDOMException e) {
            // famous last words: should never really happen.
            return "unavailable"; 
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jul-03	751/2	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 ===========================================================================
*/
