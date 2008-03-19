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

package com.volantis.mcs.xdime.cp.xslt.table;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the table-module.xsl stylesheet.
 */
public class TableModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that table ements and their contents are transformed according to
     * AN062 section 3.4, and in particular that the caption element is
     * renamed to div and moved to after the table.
     * @throws Exception
     */
    public void testTableElement() throws Exception {
        doTransform("table transformations failed",
                getInputSourceForClassResource("table-module-test.xsl"),
                getInputSourceForString(
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS + ">" +
                "<table class=\"my-table\">" +
                "<caption class=\"my-caption\">Really Useful Table</caption>" +
                "<tr>" +
                "<th></th>" +
                "<th>Column 1</th>" +
                "<th href=\"blah.xml\">Column 2</th>" +
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
                "</mcs:unit>"),
                getInputSourceForString(
                "<unit>" +
                "<table class=\"my-table\" pane=\"table-pane\">" +
                "<tr>" +
                "<th></th>" +
                "<th>Column 1</th>" +
                "<th><a href=\"blah.xml\">Column 2</a></th>" +
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
                "<div class=\"my-caption\" pane=\"caption-pane\">Really Useful Table</div>" +
                "</unit>"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/13	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/11	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/9	pcameron	VBM:2004060306 Fixed test cases after integration

 11-Jun-04	4645/7	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/4	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/2	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
*/
