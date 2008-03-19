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

package com.volantis.mcs.xdime.cp.xslt.formscontrols;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the core attribute of the xforms-controls-module.xsl stylesheet.
 */
public class XFormsControlsModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that the core attributes for the Forms are transformed correctly.
     * See AN062 section 3.11.
     * @throws Exception
     */
    public void testFormControls() throws Exception {
        doTransform("Form's control element transformations failed",
                getInputSourceForClassResource("xforms-controls-module-test.xsl"),
                getInputSourceForString(
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " " + XDIMECPConstants.XFORMS_XMLNS + ">" +
                "<html><head>" +
                "<xf:model><xf:submission id=\"mySubID99\"></xf:submission></xf:model>" +
                "</head><body>" +
                "<xf:input class=\"xyz\">" +
                "<xf:label>my label is this</xf:label>" +
                "</xf:input>" +
                "<xf:select class=\"qrs\">" +
                "<xf:label>the qrs label is this</xf:label>" +
                "<xf:item>" +
                "<xf:label>another label here</xf:label>" +
                "<xf:value>and a value</xf:value>" +
                "</xf:item>" +
                "<xf:item>" +
                "<xf:label>one more label</xf:label>" +
                "<xf:value>one more value</xf:value>" +
                "</xf:item>" +
                "</xf:select>" +
                "<xf:submit class=\"abc\">" +
                "<xf:label>my abc label</xf:label>" +
                "</xf:submit>" +
                "<xf:input class=\"xyz\" ref=\"Field1\">" +
                "<xf:label>Label for <span>Field 1</span></xf:label>" +
                "</xf:input>" +
                "<xf:textarea class=\"tuv\" ref=\"Field2\">" +
                "<xf:label class=\"ignored\">Label for <span>Field 2</span></xf:label>" +
                "</xf:textarea>" +
                "<xf:select class=\"qrs\" ref=\"Field3\">" +
                "<xf:label>Label for Field 3</xf:label>" +
                "<xf:item class=\"item1\">" +
                "<xf:label class=\"item1label\">Item 1</xf:label>" +
                "<xf:value>1</xf:value>" +
                "</xf:item>" +
                "<xf:item>" +
                "<xf:label>Item 2</xf:label>" +
                "<xf:value>2</xf:value>" +
                "</xf:item>" +
                "</xf:select>" +
                "<xf:secret ref=\"MyPassword\">" +
                "<xf:label>Enter your password</xf:label>" +
                "</xf:secret>" +
                "<xf:submit class=\"abc\" submission=\"mySubID99\">" +
                "<xf:label>Press Me</xf:label>" +
                "</xf:submit>" +
                "</body></html>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<xftextinput entryPane=\"XYZEntryPane\" class=\"xyz\" captionPane=\"XYZCaptionPane\" caption=\"my label is this\" />" +
                "<xfmuselect entryPane=\"QRSEntryPane\" class=\"qrs\" captionPane=\"QRSCaptionPane\" caption=\"the qrs label is this\">" +
                "<xfoption entryPane=\"QRSItemEntryPane1\" captionPane=\"QRSItemCaptionPane1\" caption=\"another label here\" value=\"and a value\"/>" +
                "<xfoption entryPane=\"QRSItemEntryPane1\" captionPane=\"QRSItemCaptionPane1\" caption=\"one more label\" value=\"one more value\"/>" +
                "</xfmuselect>" +
                "<xfaction type=\"submit\" class=\"abc\" caption=\"my abc label\"/>" +
                "<xftextinput entryPane=\"XYZEntryPane\" class=\"xyz\" name=\"Field1\" captionPane=\"XYZCaptionPane\" caption=\"Label for Field 1\"/>" +
                "<xftextinput class=\"tuv\" name=\"Field2\" caption=\"Label for Field 2\" captionClass=\"ignored\"/>" +
                "<xfmuselect entryPane=\"QRSEntryPane\" class=\"qrs\" name=\"Field3\" captionPane=\"QRSCaptionPane\" caption=\"Label for Field 3\">" +
                "<xfoption entryPane=\"QRSItemEntryPane1\" class=\"item1\" captionPane=\"QRSItemCaptionPane1\" caption=\"Item 1\" captionClass=\"item1label\" value=\"1\"/>" +
                "<xfoption entryPane=\"QRSItemEntryPane1\" captionPane=\"QRSItemCaptionPane1\" caption=\"Item 2\" value=\"2\"/>" +
                "</xfmuselect>" +
                "<xftextinput type=\"password\" name=\"MyPassword\" caption=\"Enter your password\"/>" +
                "<xfaction type=\"submit\" class=\"abc\" caption=\"Press Me\"/>" +
                "</unit>"));
    }

    public void testSubmissionID() throws Exception {
        boolean detected = false;
        try {
            doTransform("Submission id checking failed",
                    getInputSourceForClassResource("xforms-controls-module-test.xsl"),
                    getInputSourceForString(
                            "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                    " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                    " " + XDIMECPConstants.XFORMS_XMLNS + ">" +
                    "<html><head>" +
                    "<xf:model><xf:submission id=\"mySubID8888888\"></xf:submission></xf:model>" +
                    "</head><body>" +
                    "<xf:submit class=\"abc\" submission=\"mySubID99\">" +
                    "<xf:label>Press Me</xf:label>" +
                    "</xf:submit>" +
                    "</body>" +
                    "</html></mcs:unit>"),
                    getInputSourceForString(
                            "<unit>" +
                    "<xfaction class=\"abc\" caption=\"Press Me\"/>" +
                    "</unit>"));
        } catch (RuntimeException re) {
            detected = true;
        }
        assertTrue("Did not detect submission id attribute mismatch",
                detected);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/13	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/11	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/9	pcameron	VBM:2004060306 Committed for integration

 ===========================================================================
*/
