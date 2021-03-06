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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.PublicIdFactory;
import com.volantis.mcs.wbsax.wml.WMLVersion1_3TokenTable;

/**
 * We have to create this object that extends WMLVersion1_4Configuration in
 * order to add the hr element to the 'always empty' list of elements.
 * <p/>
 * A better approach (using delegation rather than inheritance) would involve
 * providing an add method for the alwaysEmptyElements Set in
 * ProtocolConfiguration. However, given the fact that configuration objects
 * have previously been created as singletons, this will not work (adding an
 * element to the WML1.3 static object's configuration will persist for the
 * lifetime of that session).
 */
public class WMLOpenWave1_3Configuration
        extends WMLVersion1_3Configuration {

    public WMLOpenWave1_3Configuration() {
        super();
        useTokenTable(new WMLVersion1_3TokenTable() {
            public PublicIdCode getPublicId() {
                return PublicIdFactory.OPENWAVE_1_3;
            }
        });
        alwaysEmptyElements.add("hr");
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

 16-Sep-03	1420/1	byron	VBM:2003091605 Support Openwave GUI Browser extensions - multiple add accurev problem

 16-Sep-03	1301/3	byron	VBM:2003082107 Support Openwave GUI Browser extensions - single select controls handled as type=radio

 10-Sep-03	1301/1	byron	VBM:2003082107 Support Openwave GUI Browser extensions

 ===========================================================================
*/
