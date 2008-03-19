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

package com.volantis.mcs.xdime.cp.xslt.miscellaneous;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the miscellaneous-module.xsl stylesheet.
 */
public class MiscellaneousModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that the unit element is transformed according to
     * AN062 section 3.13.1.
     * @throws Exception
     */
    public void testUnitElement() throws Exception {
        doTransform("unit element transformations failed",
                getInputSourceForClassResource("miscellaneous-module-test.xsl"),
                getInputSourceForString(
                "<mcs:unit " +
                XDIMECPConstants.XDIMECP_MCS_XMLNS + ">" +
                "<html>" +
                "<mcs:unit " +
                XDIMECPConstants.XDIMECP_MCS_XMLNS + ">" +
                "<h1>Some text</h1>" +
                "</mcs:unit>" +
                "</html>" +
                "</mcs:unit>"),
                getInputSourceForString(
                "<unit>" +
                "<html>" +
                "<h1>Some text</h1>" +
                "</html>" +
                "</unit>"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/6	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/4	pcameron	VBM:2004060306 Fixed test cases after integration

 11-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
*/
