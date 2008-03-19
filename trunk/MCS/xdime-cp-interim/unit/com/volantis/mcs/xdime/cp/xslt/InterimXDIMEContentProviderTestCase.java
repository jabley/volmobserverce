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

import org.xml.sax.InputSource;

import java.net.URL;

public class InterimXDIMEContentProviderTestCase
        extends XSLTTestAbstract {

    public void testSimple()
            throws Exception {

        doTransform("Simple test failed",
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<xsl:stylesheet version=\"1.0\" " +
                    XDIMECPConstants.XSLT_XMLNS + ">" +
                    "    <xsl:template match=\"foo\">" +
                    "        <bar>" +
                    "            <xsl:apply-templates/>" +
                    "        </bar>" +
                    "    </xsl:template>" +
                    "</xsl:stylesheet>",
                    "<foo/>",
                    "<bar/>");
    }

    /**
     * Test that the POC transform does something.
     */
    public void testPOC()
            throws Exception {

        doTransform("POC failed",
                    getInputSourceForClassResource("poc-transform.xsl"),
                    getInputSourceForClassResource("poc-test-input.xml"),
                    getInputSourceForClassResource("poc-test-expected-output.xml"));
    }

    /**
     * Test that the XDIME CP Interim transform does the right things.
     */
    public void testXDIMECPInterim()
            throws Exception {

        doTransform("XDIME CP Interim failed",
                    getInputSourceForClassResource("xdime-cp-test.xsl"),
                    getInputSourceForClassResource("xdime-cp-test-input.xml"),
                    getInputSourceForClassResource("xdime-cp-expected-output.xml"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jun-04	4630/5	pduffin	VBM:2004060306 Added some more xdime cp stuff

 08-Jun-04	4630/3	pduffin	VBM:2004060306 Added some constants

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
*/
