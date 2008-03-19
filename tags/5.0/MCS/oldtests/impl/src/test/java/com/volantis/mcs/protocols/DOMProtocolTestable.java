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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/DOMProtocolTestable.java,v 1.2 2003/02/06 11:38:48 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Jan-03    Geoff           VBM:2003012101 - Created.
 * 27-May-03    Byron           VBM:2003051904 - Added set style method.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;


import com.volantis.mcs.protocols.layouts.ContainerInstance;

/**
 * This interface defines the extra methods that we need to add to 
 * {@link DOMProtocol} in order to make it testable.
 * <p>
 * Once this code is generally understood we should probably attempt to 
 * refactor the design of the Protocols so that these methods migrate30 slowly
 * into the real Protocol interface, as the need to have these methods here
 * is a design smell, and means that we need to cut & paste code into each
 * TestProtocol subclass.
 * 
 * @see DOMProtocol
 */ 
public interface DOMProtocolTestable extends VolantisProtocolTestable {

    /**
     * The copyright statement.
     */
    public static String mark = "(c) Volantis Systems Ltd 2002.";
    
    /**
     * Sets the private instance variable 
     * {@link DOMProtocol#pageBuffer}.
     * <P>
     * FOR TESTING ONLY.
     *  
     * @param pageBuffer value to set it to.
     */ 
    void setPageBuffer(DOMOutputBuffer pageBuffer);

    /**
     * Saves the buffer into a Map keyed by the container, so that subsequent
     * calls to {@link DOMProtocol#getCurrentBuffer(ContainerInstance)} return
     * the value.
     * <p>
     * This requires that the testable class has a Map instance variable, and
     * also that it overrides
     * {@link DOMProtocol#getCurrentBuffer(ContainerInstance)}.
     * <P>
     * FOR TESTING ONLY.
     *
     * @param containerInstance
     * @param buffer
     */
    void setCurrentBuffer(ContainerInstance containerInstance,
            DOMOutputBuffer buffer);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 25-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 ===========================================================================
*/
