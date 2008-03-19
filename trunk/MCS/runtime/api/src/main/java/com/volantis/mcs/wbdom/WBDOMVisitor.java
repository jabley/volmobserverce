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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom;

/**
 * Visitor for visiting the node (super)structure of the WBDOM. 
 * <p>
 * This visits instances {@link WBDOMElement} and {@link WBDOMText} classes,
 * similar to a "normal" DOM visitor. However, unlike a "normal" DOM, the
 * WBDOM is composed of another level of detail (the WBSAX values which make
 * up attributes and text). These are visited separately using instances of 
 * {@link com.volantis.mcs.wbsax.WBSAXValueVisitor}.
 * <p>
 * Note that because the WBDOM is usually serialised by the accurate dissector,
 * we don't use this class quite as often as you might at first think.
 */ 
public interface WBDOMVisitor {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * An "accept" interface for WBDOM nodes (see GOF book).
     * <p>
     * WBDOM nodes (and proxies for them) must implement this interface
     * so that they may be visited by a {@link WBDOMVisitor}.
     */ 
    public interface Acceptor {
        void accept(WBDOMVisitor visitor) throws WBDOMException;
    }
    
    /**
     * Visit an element node.
     * 
     * @param element the element to visit.
     * @throws WBDOMException if there was an error visiting the element.
     */ 
    void visitElement(WBDOMElement element) throws WBDOMException;
    
    /**
     * Visit a text node.
     * 
     * @param text the text node to visit.
     * @throws WBDOMException if there was an error visiting the text.
     */ 
    void visitText(WBDOMText text) throws WBDOMException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
