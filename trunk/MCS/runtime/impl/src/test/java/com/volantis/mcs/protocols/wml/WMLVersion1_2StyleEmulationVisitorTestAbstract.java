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

/**
 * Test the WMLVersion 1.2 style emulation visitor.
 */
public class WMLVersion1_2StyleEmulationVisitorTestAbstract extends
        WMLStyleEmulationVisitorTestCase {

    /**
     * Test that a PRE element is handled correctly.
     */
    public void testPre() throws Exception {
        String p = getParagraphElement();
        String actual =
                "<pre>" +
                    "<" + p + ">" +
                        "<select>" +
                            "<option/>" +
                        "</select>" +
                        "text" +
                    "</" + p + ">" +
                    "<big>text</big>" +
                "</pre>";

        // The pre element should be pushed into the p element but not into
        // the big element (big doesn't allow the pre tag as a child). The
        // 'text' should be in a pre paragraph.
        doTest(actual, getExpectedPre());
    }

    protected String getExpectedPre() {
        String p = getParagraphElement();
        String expected =
                "<pre>" +
                    "<" + p + ">" +
                        "<select>" +
                            "<option/>" +
                        "</select>" +
                        "text" +
                    "</" + p + ">" +
                    "<big>text</big>" +
                "</pre>";
        return expected;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Oct-04	5877/1	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 ===========================================================================
*/
