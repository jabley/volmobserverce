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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLVersion1_2Configuration.java,v 1.2 2003/03/17 11:21:44 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Mar-03    Phil W-S        VBM:2003031110 - Created. New protocol
 *                              configuration to handle the differences between
 *                              WMLRoot/WML 1.1 and WML 1.2 protocols.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import java.util.Set;

/**
 * NOTE: WML1.2 protocol configuration is abstract because we don't support
 * WML 1.2 externally. It is purely here to hang features that were added in
 * 1.2 onto.
 */
public abstract class WMLVersion1_2Configuration
        extends WMLVersion1_1Configuration {

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    public WMLVersion1_2Configuration() {
        super ();
        
        // This protocol also allows the pre element to be a direct child of a
        // card
        cardElements.add("pre");
    }

    // Javadoc inherited.
    public void createStyleEmulationElements() {
        super.createStyleEmulationElements();

        final String[] permittedChildren = {
            "a", "br", "i", "b", "em", "strong", "input", "select"
        };

        final Set allPermittedChildren =
                mergePermittedChildren(permittedChildren);

        styleEmulationElements.associateStylisticAndAntiElements("pre", null,
                allPermittedChildren);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Oct-04	5877/1	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 14-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 14-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
