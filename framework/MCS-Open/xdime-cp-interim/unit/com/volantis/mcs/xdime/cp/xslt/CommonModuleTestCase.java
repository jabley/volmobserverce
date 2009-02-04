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

package com.volantis.mcs.xdime.cp.xslt;

public class CommonModuleTestCase
        extends XSLTTestAbstract {

    /**
     * Test that the common module will detect unknown elements.
     * todo Add tests for unknown attributes and unexpected text.
     */
    public void testUnknownElement()
            throws Exception {

        boolean detected = true;
        try {
            doTransform("Failed to detect unknown element",
                        getInputSourceForClassResource("common.xsl"),
                        getInputSourceForString("<foo/>"),
                        getInputSourceForString("<foo/>"));

            detected = false;
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message.equals("Root element not set")) {
                // Drop through to the finally.
                detected = false;
            } else {
                assertEquals("Incorrect error",
                             "Stylesheet directed termination",
                             e.getMessage());
            }
        } finally {
            if (!detected) {
                fail("Unknown element not detected");
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
*/
