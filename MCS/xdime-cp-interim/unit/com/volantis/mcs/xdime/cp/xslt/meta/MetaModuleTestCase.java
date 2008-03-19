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

package com.volantis.mcs.xdime.cp.xslt.meta;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the meta-module.xsl stylesheet.
 */
public class MetaModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that meta elements are transformed according to
     * AN062 section 3.9.
     * @throws Exception
     */
    public void testMetaElements() throws Exception {
        doTransform("meta element transforms failed",
                getInputSourceForClassResource("meta-module-test.xsl"),
                getInputSourceForString(
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS + ">" +
                "<head>" +
                "<meta class=\"myclass-ignored\" name=\"author\" title=\"mytitle-ignored\">Peter Cameron</meta>" +
                "<meta id=\"myid-ignored\" name=\"description\">" +
                "Some potentially <span class=\"stylized\">stylized</span>" +
                " meta information.</meta>" +
                "</head>" +
                "</mcs:unit>"),
                getInputSourceForString(
                "<unit>" +
                "<head>" +
                "<meta name=\"author\" content=\"Peter Cameron\"/>" +
                "<meta name=\"description\" content=\"Some potentially " +
                "stylized meta information.\"/>" +
                "</head>" +
                "</unit>"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/9	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/7	pcameron	VBM:2004060306 Fixed test cases after integration

 11-Jun-04	4645/5	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
*/
