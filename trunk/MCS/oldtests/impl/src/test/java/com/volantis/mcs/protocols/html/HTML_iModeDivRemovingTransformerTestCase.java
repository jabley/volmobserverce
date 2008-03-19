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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the transformation of the DOM tree for HTML_iMode protocol.
 */
public class HTML_iModeDivRemovingTransformerTestCase
        extends TestCaseAbstract {

    private StrictStyledDOMHelper helper;
    private InternalDevice internalDevice;

    protected void setUp() throws Exception {
        super.setUp();

        internalDevice = InternalDeviceTestHelper.createTestDevice();

        helper = new StrictStyledDOMHelper(null);
    }

    /**
     * Test the transformation of the DOM tree for XHTMLBasic
     */
    public void testTransform() throws Exception {
        // Test page from 2006060835
        String input =
        "<html>" +
            "<head>" +
                "<title>eBay Mobile</title>" +
            "</head>" +
            "<body bgcolor=\"#ffffff\" text=\"#000000\">" +
                "<div align=\"left\">" +
                    "<div align=\"left\">" +
                        "<p>Hello!</p>" +
                        "<hr/>" +
                    "</div>" +
                    "<div align=\"left\">Welcome to eBay Mobile</div>" +
                    "<div align=\"left\">" +
                        "<div align=\"left\">" +
                            "<div align=\"left\">" +
                                "<form action=\"form.jsp;jsessionid=SESSIONID\" method=\"GET\">" +
                                    "<div>" +
                                        "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                                        "<input name=\"id\" type=\"hidden\" value=\"999999\"/>" +
                                    "</div>" +
                                    "<table align=\"left\" cellpadding=\"0\" cellspacing=\"0\">" +
                                        "<tr align=\"left\">" +
                                            "<td align=\"left\" valign=\"middle\">" +
                                                "<input maxlength=\"50\" name=\"query\" size=\"20\" type=\"text\"/>" +
                                            "</td>" +
                                        "</tr>" +
                                        "<tr align=\"left\">" +
                                            "<td align=\"left\" valign=\"middle\">" +
                                                "<input type=\"submit\" value=\"Search\"/>" +
                                            "</td>" +
                                        "</tr>" +
                                    "</table>" +
                                "</form>" +
                            "</div>" +
                        "</div>" +
                    "</div>" +
                    "<div align=\"left\">To bid, buy or ....</div>" +
                "</div>" +
            "</body>" +
        "</html>";

        String expectedString =
        "<html>" +
            "<head>" +
                "<title>eBay Mobile</title>" +
            "</head>" +
            "<body bgcolor=\"#ffffff\" text=\"#000000\">" +
                "<div align=\"left\">" +
                    "<p>Hello!</p>" +
                    "<hr/>" +
                    "Welcome to eBay Mobile" +
                    "<div align=\"left\">" +
                        "<form action=\"form.jsp;jsessionid=SESSIONID\" method=\"GET\">" +
                            "<div>" +
                                "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                                "<input name=\"id\" type=\"hidden\" value=\"999999\"/>" +
                            "</div>" +
                            "<table align=\"left\" cellpadding=\"0\" cellspacing=\"0\">" +
                                "<tr align=\"left\">" +
                                    "<td align=\"left\" valign=\"middle\">" +
                                        "<input maxlength=\"50\" name=\"query\" size=\"20\" type=\"text\"/>" +
                                    "</td>" +
                                "</tr>" +
                                "<tr align=\"left\">" +
                                    "<td align=\"left\" valign=\"middle\">" +
                                        "<input type=\"submit\" value=\"Search\"/>" +
                                    "</td>" +
                                "</tr>" +
                            "</table>" +
                        "</form>" +
                    "</div>" +
                    "To bid, buy or ...." +
                "</div>" +
            "</body>" +
        "</html>";
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestHTML_iModeFactory(),
                internalDevice);

        Document dom = helper.parse(input);
        Document expected = helper.parse(expectedString);
        String domAsString;
        String expectedAsString;
        DOMTransformer transformer = new HTML_iModeDivRemovingTransformer();

        // Useful debug output
//        System.out.println("Original DOM:");
//        System.out.println(DOMUtilities.toString(dom, protocol.getCharacterEncoder()));

        dom = transformer.transform(protocol, dom);

        // Useful debug output
//        System.out.println("Transformed DOM:");
//        System.out.println(DOMUtilities.toString(dom, protocol.getCharacterEncoder()));

        domAsString = helper.render(dom);
        expectedAsString = helper.render(expected);

        assertEquals("DOM comparison failed - Test ", expectedAsString,
                domAsString);
    }
}
