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

package com.volantis.mcs.xdime.cp.xslt.diselect;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the content-selection-module.xsl stylesheet.
 */
public class DISelectModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that the sel:selid attribute is transformed into the id attribute.
     * See section 3.11.1 of AN062.
     * @throws Exception
     */
    public void testSelIDAttr() throws Exception {
        doTransform("sel:selid attribute transform failed",
                getInputSourceForClassResource("diselect-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("diselect-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " " + XDIMECPConstants.DISELECT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE +
                " ../../../../../../../../../architecture/build/xml-schema/xdime-cp/build/xdime-cp-mcs.xsd\"" +
                ">" +

                "<html><head><title>No title</title></head><body>" +
                "<div>" +
                "<h1 sel:selid=\"some-id\">Some Heading</h1>" +
                "</div>" +
                "</body></html>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<region name=\"Region0\">" +
                "<canvas type=\"inclusion\" layoutName=\"/xdimecp/pane.mlyt\">" +
                "<div pane=\"CaptionPane\">" +
                "<h1 id=\"some-id\">Some Heading</h1>" +
                "</div></canvas></region></unit>"));
    }

    /**
     * Tests that if an element has both the sel:selid and id attributes then
     * the transformation is aborted with an error message.
     * See section 3.11.1 of AN062.
     * @throws Exception
     */
    public void testSelIDAttrAndIDAttr() throws Exception {
        boolean detected = false;
        try {
            doTransform("sel:selid and id attribute transform failed",
                    getInputSourceForClassResource("diselect-module-test.xsl"),
                    getInputSourceForString(
                            getSystemIdForClassResource("diselect-module-test.xsl"),
                            "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                    " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                    " " + XDIMECPConstants.DISELECT_XMLNS +
                    " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                    " xsi:schemaLocation=\"" +
                    XDIMECPConstants.XDIMECP_MCS_NAMESPACE +
                    " ../../../../../../../../../architecture/build/xml-schema/xdime-cp/build/xdime-cp-mcs.xsd\"" +
                    ">" +
                    "<html><head><title>No title</title></head><body>" +
                    "<div>" +
                    "<h1 sel:selid=\"some-id\" id=\"illegal-here\">Some Heading</h1>" +
                    "</div>" +
                    "</body></html>" +
                    "</mcs:unit>"),
                    getInputSourceForString(
                            "<unit>" +
                    "<region name=\"Region0\">" +
                    "<canvas type=\"inclusion\" layoutName=\"/xdimecp/pane.mlyt\">" +
                    "<div pane=\"CaptionPane\">" +
                    "<h1 id=\"some-id\">Some Heading</h1>" +
                    "</div></canvas></region></unit>"));
        } catch (RuntimeException re) {
            detected = true;
        }

        assertTrue("Should detect presence of both sel:selid and id attributes",
                detected);
    }

    /**
     * Tests that the sel:expr attribute is transformed into the expr attribute.
     * See section 3.11.2 of AN062.
     * @throws Exception
     */
    public void testExprAttr() throws Exception {
        doTransform("sel:expr attribute transform failed",
                getInputSourceForClassResource("diselect-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("diselect-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " " + XDIMECPConstants.DISELECT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE +
                " ../../../../../../../../../architecture/build/xml-schema/xdime-cp/build/xdime-cp-mcs.xsd\"" +
                ">" +
                "<html><head><title>No title</title></head><body>" +
                "<div>" +
                "<h1 sel:expr=\"some expression\"/>" +
                "</div>" +
                "</body></html>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<region name=\"Region0\">" +
                "<canvas type=\"inclusion\" layoutName=\"/xdimecp/pane.mlyt\">" +
                "<div pane=\"CaptionPane\">" +
                "<h1 expr=\"some expression\"/>" +
                "</div></canvas></region></unit>"));
    }

    /**
     * Tests that the sel:if element is transformed into select/when elements.
     * See section 3.11.3 of AN062.
     * @throws Exception
     */
    public void testIfElement() throws Exception {
        doTransform("sel:if element transform failed",
                getInputSourceForClassResource("diselect-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("diselect-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " " + XDIMECPConstants.DISELECT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE +
                " ../../../../../../../../../architecture/build/xml-schema/xdime-cp/build/xdime-cp-mcs.xsd\"" +
                ">" +
                "<html><head><title>No title</title></head><body>" +
                "<div>" +
                "<sel:if expr=\"foo()\"><h1>my header</h1></sel:if>" +
                "</div>" +
                "</body></html>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<region name=\"Region0\">" +
                "<canvas type=\"inclusion\" layoutName=\"/xdimecp/pane.mlyt\">" +
                "<div pane=\"CaptionPane\">" +
                "<select><when expr=\"foo()\">" +
                "<h1>my header</h1></when></select>" +
                "</div></canvas></region></unit>"));
    }

    /**
     * Tests that the sel:select element and its sel:when and sel:otherwise
     * children have their namespace prefixes removed.
     * See section 3.11.4 of AN062.
     * @throws Exception
     */
    public void testSelectWhenOtherwiseElements() throws Exception {
        doTransform("sel:select element transform failed",
                getInputSourceForClassResource("diselect-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("diselect-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " " + XDIMECPConstants.DISELECT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE +
                " ../../../../../../../../../architecture/build/xml-schema/xdime-cp/build/xdime-cp-mcs.xsd\"" +
                ">" +
                "<html><head><title>No title</title></head><body>" +
                "<div>" +
                "<sel:select>" +
                "<sel:when expr=\"foo()\"><h1>my header1</h1></sel:when>" +
                "<sel:when expr=\"bar()\"><h2>my header2</h2></sel:when>" +
                "<sel:otherwise><h3>my header3</h3></sel:otherwise>" + "" +
                "</sel:select>" +
                "</div>" +
                "</body></html>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<region name=\"Region0\">" +
                "<canvas type=\"inclusion\" layoutName=\"/xdimecp/pane.mlyt\">" +
                "<div pane=\"CaptionPane\">" +
                "<select><when expr=\"foo()\">" +
                "<h1>my header1</h1></when><when expr=\"bar()\">" +
                "<h2>my header2</h2></when><otherwise><h3>my header3</h3>" +
                "</otherwise></select>" +
                "</div></canvas></region></unit>"));
    }

    /**
     * Tests that the sel:select element with an expr attribute is transformed
     * into select/when elements.
     * See section 3.11.4 of AN062.
     * @throws Exception
     */
    public void testSelectElementExprAttr() throws Exception {
        doTransform("sel:select element with expr attribute transform failed",
                getInputSourceForClassResource("diselect-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("diselect-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " " + XDIMECPConstants.DISELECT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE +
                " ../../../../../../../../../architecture/build/xml-schema/xdime-cp/build/xdime-cp-mcs.xsd\"" +
                ">" +
                "<html><head><title>No title</title></head><body>" +
                "<div>" +
                "<sel:select expr=\"foo()\">" +
                "<h1>my header1</h1></sel:select>" +
                "</div>" +
                "</body></html>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<region name=\"Region0\">" +
                "<canvas type=\"inclusion\" layoutName=\"/xdimecp/pane.mlyt\">" +
                "<div pane=\"CaptionPane\">" +
                "<select><when expr=\"foo()\">" +
                "<h1>my header1</h1></when></select>" +
                "</div></canvas></region></unit>"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/7	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/3	pcameron	VBM:2004060306 Fixed test cases after integration

 15-Jun-04	4630/2	pduffin	VBM:2004060306 Added some more xdime cp stuff

 08-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 08-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
*/
