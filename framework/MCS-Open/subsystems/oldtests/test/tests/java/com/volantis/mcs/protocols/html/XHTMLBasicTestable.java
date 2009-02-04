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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasicTestable.java,v 1.2 2003/02/06 11:38:48 geoff Exp $
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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.DOMProtocolTestable;

/**
 * This interface defines the extra methods that we need to add to 
 * {@link XHTMLBasic} in order to make it testable.
 * <p>
 * Once this code is generally understood we should probably attempt to 
 * refactor the design of the Protocols so that these methods migrate30 slowly
 * into the real Protocol interface, as the need to have these methods here
 * is a design smell, and means that we need to cut & paste code into each
 * TestProtocol subclass.
 * 
 * @see XHTMLBasic
 */ 
public interface XHTMLBasicTestable extends DOMProtocolTestable {

    /**
     * Returns the private instance variable 
     * {@link XHTMLBasic#maxOptgroupDepth}.
     * <P>
     * FOR TESTING ONLY.
     * 
     * @return the maximum nesting depth for opt groups.
     */ 
    int getMaxOptGroupNestingDepth();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/1	claire	VBM:2004092801 Encoding of style classes names for inclusions

 ===========================================================================
*/
