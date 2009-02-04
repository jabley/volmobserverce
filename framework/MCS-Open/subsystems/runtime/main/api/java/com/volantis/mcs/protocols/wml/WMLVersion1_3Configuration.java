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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLVersion1_2Configuration.java,v 1.1.2.1 2003/03/13 19:18:22 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-May-03    Mat    		    VBM:2003042907 - Created. New protocol
 *                              configuration to handle tokens for WML1.3
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.wbsax.wml.WMLVersion1_3TokenTable;

import java.util.Set;

/**
 * Handles the differences in configuration for WML1.3
 */
public class WMLVersion1_3Configuration extends WMLVersion1_2Configuration {

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    public WMLVersion1_3Configuration() {
        super();
        
        useTokenTable(new WMLVersion1_3TokenTable());
    }

    // Javadoc inherited.
    public void createStyleEmulationElements() {
        super.createStyleEmulationElements();

        final String[] permittedChildren = {
                "anchor", "do", "u"
            };

        Set allPermittedChildren = mergePermittedChildren(permittedChildren);
        // associating the pre element here augments any previous associations
        // done in any superclass(es). In other words the permitted children
        // defined above aren't necessariliy confined to just 'anchor', 'do'
        // and 'u'.
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

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
