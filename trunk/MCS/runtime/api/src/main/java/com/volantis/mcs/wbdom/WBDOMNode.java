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
 * $Header: /src/voyager/com/volantis/mcs/dom/Node.java,v 1.3.4.2 2003/01/28 15:07:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.wbdom;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.dom.NodeAnnotation;

/**
 * The base class for nodes which represent the common tree structure of a DOM
 * for WBDOM.
 * <p>
 * This uses a very simple representation which is optimised for read only
 * behaviour. If you want to modify existing elements, this class may need 
 * to be modified to look more like the MCS DOM version.
 * <p>
 * WBDOM nodes act as a tree structured facade over another level of objects 
 * which contain the actual WBSAX value objects which store the data content. 
 */
public abstract class WBDOMNode implements WBDOMVisitor.Acceptor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(WBDOMNode.class);

    /**
     * The parent element, is null if this is the root node.
     * <p>
     * This is package private so it can be accessed by other classes in this
     * package.
     * </p>
     */
    WBDOMElement parent;

    /**
     * The next node, is null if this is the last node in the list.
     * <p>
     * This is package private so it can be accessed by other classes in this
     * package.
     * </p>
     */
    WBDOMNode next;

    /**
     * Some user data associated with the node.
     */
    private NodeAnnotation annotation;

    /**
     * Create a new <code>Node</code>.
     */
    WBDOMNode() {
    }

    //
    // Structural methods.
    // 
    
    /**
     * Get the parent element.
     * @return The parent element.
     */
    public WBDOMElement getParent() {
        return parent;
    }

    /**
     * Get the next node.
     * @return The next node.
     */
    public WBDOMNode getNext() {
        return next;
    }

    //
    // Annotations.
    //
    
    /**
     * Set the object.
     * @param annotation The object.
     */
    public void setAnnotation(NodeAnnotation annotation) {
        this.annotation = annotation;
    }

    /**
     * Get the object.
     * @return The object.
     */
    public NodeAnnotation getAnnotation() {
        return annotation;
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

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/3	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 11-Jun-03	376/1	steve	VBM:2003061004 Removed ; that caused production build to fail

 ===========================================================================
*/
