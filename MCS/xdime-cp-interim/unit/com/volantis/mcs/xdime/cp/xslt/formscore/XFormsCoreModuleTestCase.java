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

package com.volantis.mcs.xdime.cp.xslt.formscore;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the core attribute of the forms-module.xsl stylesheet.
 */
public class XFormsCoreModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that the core attributes for the Forms are transformed correctly.
     * See AN062 section 3.1.2.
     * @throws Exception
     */
    public void testCoreAttributes() throws Exception {
        doTransform("Form's Core attribute transformations failed",
                getInputSourceForClassResource("xforms-core-module-test.xsl"),
                getInputSourceForString(
                 "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " " + XDIMECPConstants.XFORMS_XMLNS + ">" +
                "<html><head>" +
                "<xfform class=\"1\" id=\"2\" title=\"3\"/>" +
                "<xfmuselect class=\"1\" id=\"2\" title=\"3\"/>" +
                "<xfsiselect class=\"1\" id=\"2\" title=\"3\"/>" +
                "<xfboolean class=\"1\" id=\"2\" title=\"3\"/>" +
                "<xfaction class=\"1\" id=\"2\" title=\"3\"/>" +
                "<xfoptgroup class=\"1\" id=\"2\" title=\"3\"/>" +
                "<xfoption class=\"1\" id=\"2\" title=\"3\"/>" +
                "<xftextinput class=\"1\" id=\"2\" title=\"3\"/>" +
                "<xfimplicit class=\"1\" id=\"2\" title=\"3\"/>" +
                "</head></html>" +
                "</mcs:unit>"),
                getInputSourceForString(
                "<unit>" +
                "<html><head>" +
                "<xfform class=\"1\" id=\"2\" prompt=\"3\"/>" +
                "<xfmuselect class=\"1\" id=\"2\" prompt=\"3\"/>" +
                "<xfsiselect class=\"1\" id=\"2\" prompt=\"3\"/>" +
                "<xfboolean class=\"1\" id=\"2\" prompt=\"3\"/>" +
                "<xfaction class=\"1\" id=\"2\" prompt=\"3\"/>" +
                "<xfoptgroup class=\"1\" id=\"2\" prompt=\"3\"/>" +
                "<xfoption class=\"1\" id=\"2\" prompt=\"3\"/>" +
                "<xftextinput class=\"1\" id=\"2\" prompt=\"3\"/>" +
                "<xfimplicit class=\"1\" id=\"2\"/>" +
                "</head></html>" +
                "</unit>"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/6	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/3	pcameron	VBM:2004060306 Committed for integration

 11-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
*/
