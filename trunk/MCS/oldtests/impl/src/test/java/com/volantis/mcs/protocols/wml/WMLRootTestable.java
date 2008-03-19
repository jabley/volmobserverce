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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/WMLRootTestable.java,v 1.2 2003/02/06 11:38:48 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Jan-03    Geoff           VBM:2003012101 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.DOMProtocolTestable;

/**
 * This interface defines the extra methods that we need to add to 
 * {@link WMLRoot} in order to make it testable.
 * <p>
 * Once this code is generally understood we should probably attempt to 
 * refactor the design of the Protocols so that these methods migrate30 slowly
 * into the real Protocol interface, as the need to have these methods here
 * is a design smell, and means that we need to cut & paste code into each
 * TestProtocol subclass.
 * 
 * @see WMLRoot
 */ 
public interface WMLRootTestable extends DOMProtocolTestable {

    /**
     * The copyright statement.
     */
    public static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Sets the private instance variable 
     * {@link WMLRoot#supportsAccessKeyAttribute}.
     * <P>
     * FOR TESTING ONLY.
     *  
     * @param value value to set it to.
     */ 
    void setSupportsAccessKeyAttribute(boolean value);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
