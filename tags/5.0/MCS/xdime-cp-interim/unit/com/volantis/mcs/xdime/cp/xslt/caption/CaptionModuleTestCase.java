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

package com.volantis.mcs.xdime.cp.xslt.caption;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the caption-module.xsl stylesheet.
 */
public class CaptionModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that the caption element of a table is transformed correctly.
     * @throws Exception
     */
    public void testCaptionElementOnTable() throws Exception {
        doTransform("table transformations failed",
                getInputSourceForClassResource("caption-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("caption-module-test.xsl"),
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE +
                " ../../../../../../../../../architecture/build/xml-schema/xdime-cp/build/xdime-cp-mcs.xsd\"" +
                ">" +
                "<html><head><title>No title</title></head><body>" +
                "<table>" +
                "<caption class=\"pane-test\">Really Useful Table</caption>" +
                "<tr>" +
                "<th/>" +
                "<th>Column 1</th>" +
                "<th>Column 2</th>" +
                "</tr>" +
                "<tr>" +
                "<th>Row 1</th>" +
                "<td>Row 1, Column 1</td>" +
                "<td>Row 1, Column 2</td>" +
                "</tr>" +
                "<tr>" +
                "<th>Row 2</th>" +
                "<td href=\"row2col1.html\">Row 2, Column 1</td>" +
                "<td>Row 2, Column 2</td>" +
                "</tr>" +
                "</table>" +
                "</body></html>" +
                "</mcs:unit>"),
                getInputSourceForString(
                "<unit>" +
                "<table>" +
                "<tr>" +
                "<th></th>" +
                "<th>Column 1</th>" +
                "<th>Column 2</th>" +
                "</tr>" +
                "<tr>" +
                "<th>Row 1</th>" +
                "<td>Row 1, Column 1</td>" +
                "<td>Row 1, Column 2</td>" +
                "</tr>" +
                "<tr>" +
                "<th>Row 2</th>" +
                "<td><a href=\"row2col1.html\">Row 2, Column 1</a></td>" +
                "<td>Row 2, Column 2</td>" +
                "</tr>" +
                "</table>" +
                "<div class=\"pane-test\" pane=\"CaptionPane\">Really Useful Table</div>" +
                "</unit>"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/10	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/7	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/5	pcameron	VBM:2004060306 Fixed test cases after integration

 11-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 11-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
*/
