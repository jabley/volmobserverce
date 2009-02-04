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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.ProtocolConfigurationImpl;

/**
 * Test the WML version 1.3 style emulation. Note that WML version 1.2 is
 * abstract so this test case extends WMLStyleEmulationVisitorTestCase.
 */
public class WMLVersion1_3StyleEmulationVisitorTestCase extends
        WMLVersion1_2StyleEmulationVisitorTestAbstract {
   
    // Javadoc inherited.
    protected ProtocolConfigurationImpl getProtocolConfiguration() {
        return new WMLVersion1_3Configuration();
    }

    // javadoc inherited.
    public void testPre() throws Exception {
        super.testPre();
    }

    // javadoc inherited.
    protected String getExpectedPre() {
        String p = getParagraphElement();
        return "<" + p + ">" +
                  "<pre>" +
                    "<select>" +
                        "<option/>" +
                    "</select>" +
                    "text" +
                  "</pre>" +
                "</" + p + ">" +
                "<big>text</big>";

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9522/2	ibush	VBM:2005091502 no_save on images

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 09-Aug-05	9211/2	pabbott	VBM:2005080902 End to End CSS emulation test

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Oct-04	5877/3	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 27-Oct-04	5877/1	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 ===========================================================================
*/
