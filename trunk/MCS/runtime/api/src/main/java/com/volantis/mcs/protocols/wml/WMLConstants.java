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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLConstants.java,v 1.3 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Feb-02    Paul            VBM:2002021203 -  Created to make sure that
 *                              the WMLRoot protocol and WMLContentTree use the
 *                              same names for elements.
 * 01-Mar-02    Mat             VBM:2002021203 - Added SSI_INCLUDE_ELEMENT and
 *                              SSI_CONFIG_ELEMENT
 * 22-Feb-02    Paul            VBM:2002021802 - Moved from protocols package
 *                              and removed some general constants.
 * 23-May-02    Paul            VBM:2002042202 - Moved ssi constants into
 *                              parent interface.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.ProtocolConstants;

/**
 * This interface contains WML related constants.
 */
public interface WMLConstants
        extends ProtocolConstants {

    public static final String CARD_ELEMENT = "card";

    public static final String LINK_ELEMENT = "a";

    public static final String ANCHOR_ELEMENT = "anchor";

    /**
     * The element used to represent a logical paragraph/block element in the
     * device dependent markup created by the protocol.
     * <p>
     * This logical paragraph element will be replaced with another element or
     * combination of elements during the transformation stage in order to best
     * emulate the whitespace requirements specified in the CSS margin
     * properties associated with the element. This may be <p>, <br>, etc.
     * <p>
     * Note that a paragraph in general is a special kind of block element
     * which cannot contain other blocks, but that WML does not contain the
     * more general kind of block (i.e. div).
     */
    String BLOCH_ELEMENT = "BLOCK";


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9600/3	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-May-05	7982/1	emma	VBM:2005041321 Merge from 3.3.0 - Re-enabling wml tag emulation support

 04-May-05	7980/1	emma	VBM:2005041321 Merge from 3.2.3 - Re-enabling wml tag emulation support

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
