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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.unit.wml;

import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.wml.WMLVersion1_1;
import com.volantis.mcs.protocols.wml.WMLVersion1_1Configuration;

/**
 * Unit test for WMLVersion1_1.
 */
public class WMLVersion1_1UnitTestCase
        extends WMLRootUnitTestAbstract {

    // Javadoc inherited.
    protected VolantisProtocol createProtocol(
            ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {

        return new WMLVersion1_1(protocolSupportFactory,
                (WMLVersion1_1Configuration) protocolConfiguration);
    }

    public void testDUMMY() {

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
