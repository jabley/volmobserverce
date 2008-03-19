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

package com.volantis.mcs.xdime.cp.xslt.list;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the list-module.xsl stylesheet.
 */
public class ListModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that non-navigational list elements are transformed according to
     * AN062 section 3.5.1.
     * @throws Exception
     */
    public void testNonNavigationalLists() throws Exception {
        doTransform("Non-navigational list transforms failed",
                getInputSourceForClassResource("list-module-test.xsl"),
                getInputSourceForString(
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS + ">" +
                "<dl class=\"my-dl\">" +
                "<dt>Term 1</dt>" +
                "<dt>Term 2</dt>" +
                "<dd>The first term</dd>" +
                "<dd>The second term</dd>" +
                "</dl>" +
                "<ol class=\"my-ol\">" +
                "<li>First item</li>" +
                "<li>Second item</li>" +
                "<li>Third item</li>" +
                "</ol>" +
                "<ul class=\"my-ul\">" +
                "<li>An item</li>" +
                "<li>Another item</li>" +
                "<li>Yet another item</li>" +
                "</ul>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<dl pane=\"dl-pane\" class=\"my-dl\">" +
                "<dt>Term 1</dt>" +
                "<dt>Term 2</dt>" +
                "<dd>The first term</dd>" +
                "<dd>The second term</dd>" +
                "</dl>" +
                "<ol pane=\"ol-pane\" class=\"my-ol\">" +
                "<li>First item</li>" +
                "<li>Second item</li>" +
                "<li>Third item</li>" +
                "</ol>" +
                "<ul pane=\"ul-pane\" class=\"my-ul\">" +
                "<li>An item</li>" +
                "<li>Another item</li>" +
                "<li>Yet another item</li>" +
                "</ul>" +
                "</unit>"));
    }

    /**
     * Tests that navigational list elements are transformed according to
     * AN062 section 3.5.2.
     * @throws Exception
     */
    public void testNavigationalList() throws Exception {
        doTransform("Navigational list transforms failed",
                getInputSourceForClassResource("list-module-test.xsl"),
                getInputSourceForString(
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS + ">" +
                "<nl class=\"my-menu\">" +
                "<label" +
                "><span class=\"rootmenu\">Root Menu</span></label>" +
                "<li href=\"item1.xml\">" +
                "A <span class=\"stylized\">stylized</span> link</li>" +
                "<li href=\"item2.xml\">A plain link</li>" +
                "<nl>" +
                "<label class=\"menu-label\">Nested Menu</label>" +
                "<li class=\"menu-item\" href=\"item3.xml\">Sub link</li>" +
                "</nl>" +
                "</nl>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<menu class=\"my-menu\" pane=\"menu-pane\">" +
                "<label><span class=\"rootmenu\">Root Menu</span></label>" +
                "<menuitem href=\"item1.xml\" text=\"A stylized link\"/>" +
                "<menuitem href=\"item2.xml\" text=\"A plain link\"/>" +
                "<menu>" +
                "<label  class=\"menu-label\" pane=\"label-pane\">Nested Menu</label>" +
                "<menuitem class=\"menu-item\" pane=\"item-pane\" href=\"item3.xml\" text=\"Sub link\"/>" +
                "</menu>" +
                "</menu>" +
                "</unit>"));
    }

    /**
     * Tests that navigational list elements are transformed according to
     * AN062 section 3.5.2.
     * @throws Exception
     */
    public void testNavigationalItemWithoutHref() throws Exception {
        boolean detected = false;
        try {
            doTransform("Missing href attribute not detected",
                    getInputSourceForClassResource("list-module-test.xsl"),
                    getInputSourceForString(
                            "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                    " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS + ">" +
                    "<nl class=\"my-menu\">" +
                    "<label" +
                    "><span class=\"rootmenu\">Root Menu</span></label>" +
                    "<li>" +
                    "A <span class=\"stylized\">stylized</span> link</li>" +
                    "<li href=\"item2.xml\">A plain link</li>" +
                    "</nl>" +
                    "</mcs:unit>"),
                    getInputSourceForString(
                            "<unit>" +
                    "<menu class=\"my-menu\" pane=\"menu-pane\">" +
                    "<label><span class=\"rootmenu\">Root Menu</span></label>" +
                    "<menuitem text=\"A stylized link\"/>" +
                    "<menu>" +
                    "<label  class=\"menu-label\" pane=\"label-pane\">Nested Menu</label>" +
                    "<menuitem class=\"menu-item\" pane=\"item-pane\" href=\"item3.xml\" text=\"Sub link\"/>" +
                    "</menu>" +
                    "</menu>" +
                    "</unit>"));
        } catch (RuntimeException re) {
            detected = true;
        }

        assertTrue("Should detect absence of href attribute on navigational list item",
                detected);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/15	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/13	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/11	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/8	pcameron	VBM:2004060306 Fixed test cases after integration

 11-Jun-04	4645/6	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/4	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/2	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
*/
