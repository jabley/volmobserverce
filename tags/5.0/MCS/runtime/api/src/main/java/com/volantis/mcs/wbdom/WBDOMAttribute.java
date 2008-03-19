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
 * $Header: /src/voyager/com/volantis/mcs/dom/Attribute.java,v 1.2 2002/03/22 18:24:28 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-May-03    Geoff           VBM:2003042905 - Created; represents an 
 *                              attribute of an element in the WBDOM.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.mcs.wbsax.WBSAXException;

/**
 * Represents an attribute of an element in the WBDOM.
 * <p>
 * This uses a very simple representation which is optimised for read only
 * behaviour. 
 */
public abstract class WBDOMAttribute implements NameVisitor.Acceptor, 
        WBSAXValueVisitor.Acceptor  {

    /**
     * The next attribute, is null if this is the last attribute in the list.
     * <p>
     * This is currently package local because it is accessed from element
     * directly. This is a design cut and paste from the original MCS DOM.
     * I certainly wouldn't complain if we made this use an accessor...
     */
    WBDOMAttribute next;

    /**
     * The value associated with the attribute.
     */
    private WBSAXAttributeValueBuffer value;

    /**
     */ 
    protected WBDOMAttribute() {
    }

    /**
     * Get the next attribute.
     * @return The next attribute.
     */
    public WBDOMAttribute getNext() {
        return next;
    }

    /**
     * Get the name.
     * @return The name.
     */
    public abstract String getName() throws WBDOMException;
    
    /**
     * Get the optional value prefix; may be null.
     * 
     * @return the value prefix
     */ 
    public abstract String getValuePrefix();
    
    /**
     * Get the value.
     * 
     * @return The value.
     */
    public WBSAXAttributeValueBuffer getValueBuffer() {
        if (value == null) {
            // Lazily create value buffers to avoid creating them if this
            // attribute is composed of just a start code with the entire
            // value in the value prefix.
            value = new WBSAXAttributeValueBuffer();
        }
        return value;
    }

    //
    // Visitor methods.
    //
    
    /**
     * NOTE: call this version rather than 
     * {@link WBSAXAttributeValueBuffer#accept} to avoid creating unnecesary
     * garbage.
     */ 
    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        if (value != null) {
            value.accept(visitor);
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

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
