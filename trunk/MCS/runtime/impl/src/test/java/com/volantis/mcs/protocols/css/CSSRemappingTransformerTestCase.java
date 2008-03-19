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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.css.version.CSSVersionMock;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.DOMProtocolMock;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.ProtocolSupportFactoryMock;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CSSRemappingTransformerTestCase extends TestCaseAbstract {

    public void testImageHeightAndWidthRemapping()  throws Exception {
        String input =
                "<html>" +
                  "<head>" +
                    "<title>Samsung issue</title>" +
                  "</head>" +
                  "<body>" +
                    "<table>" +
                      "<tr>" +
                        "<td align=\"left\">" +
                          "<img src=\"/mcs/images/left.gif\" " +
                            "style=\"height: 41px; width: 53px\" />" +
                        "</td>" +
                        "<td align=\"right\">" +
                          "<img src=\"/mcs/images/cg_icon.png\" " +
                            "style=\"height: 40px; width: 53px\"/>" +
                        "</td>" +
                      "</tr>" +
                    "</table>" +
                  "</body>" +
                "</html>";
        String expected =
                "<html>" +
                  "<head>" +
                    "<title>Samsung issue</title>" +
                  "</head>" +
                  "<body>" +
                    "<table>" +
                      "<tr>" +
                        "<td align=\"left\">" +
                          "<img height=\"41px\" src=\"/mcs/images/left.gif\" " +
                            "width=\"53px\" style='height: 41px; width: 53px'/>" +
                        "</td>" +
                        "<td align=\"right\">" +
                          "<img height=\"40px\" src=\"/mcs/images/cg_icon.png\" " +
                            "width=\"53px\" style='height: 40px; width: 53px'/>" +
                        "</td>" +
                      "</tr>" +
                    "</table>" +
                  "</body>" +
                "</html>";

        final CSSVersionMock cssVersionMock =
                new CSSVersionMock("cssVersion", expectations);

        final Map expressions = new HashMap();
        expressions.put("width", createExpression("width"));
        expressions.put("height", createExpression("height"));
        final Map remappableElements = new HashMap();
        remappableElements.put("img", expressions);

        cssVersionMock.expects.getRemappableElements().returns(
                remappableElements).any();

        final ProtocolSupportFactoryMock protocolSupportFactoryMock =
                new ProtocolSupportFactoryMock("protocolSupportFactory",
                        expectations);

        protocolSupportFactoryMock.expects.getDOMFactory().returns(
                DOMFactory.getDefaultInstance()).any();

        final ProtocolConfigurationMock protocolConfigurationMock =
                new ProtocolConfigurationMock("protocolConfiguration",
                        expectations);

        protocolConfigurationMock.expects.getCssVersion().returns(
                cssVersionMock).any();

        final DOMProtocolMock domProtocolMock = new DOMProtocolMock(
                "domProtocol", expectations,
                protocolSupportFactoryMock, protocolConfigurationMock);

        domProtocolMock.expects.getProtocolConfiguration().returns(
                protocolConfigurationMock).any();

        final StrictStyledDOMHelper domHelper = new StrictStyledDOMHelper();

        Document dom = domHelper.parse(input);
        final DOMTransformer transformer = new CSSRemappingTransformer();

        dom = transformer.transform(domProtocolMock, dom);

        assertXMLEquals("", expected, domHelper.render(dom));
    }

    /**
     * Verify that if one of the supplied expressions is null then no rule is
     * generated for that attribute (but that processing of other rules
     * continues).
     */
    public void testBuildRulesForElementWithANullExpression() {
        // Create a map of expressions and populate it with valid and
        // invalid expressions.
        Map expressions = new HashMap();
        expressions.put("width", createExpression("width"));
        expressions.put("height", null);

        // Run the test.
        final String elementName = "img";
        CSSRemappingTransformer transformer =  new CSSRemappingTransformer();
        Collection rules = transformer.buildRulesForElement(expressions,
                elementName);
        assertEquals(1, rules.size());
    }

    /**
     * Verify that if one of the supplied expressions is empty then no rule is
     * generated for that attribute (but that processing of other rules
     * continues).
     */
    public void testBuildRulesForElementWithAnEmptyExpression() {
        // Create a map of expressions and populate it with valid and
        // invalid expressions.
        Map expressions = new HashMap();
        expressions.put("width", createExpression("width"));
        expressions.put("height", "");

        // Run the test.
        final String elementName = "img";
        CSSRemappingTransformer transformer =  new CSSRemappingTransformer();
        Collection rules = transformer.buildRulesForElement(expressions,
                elementName);
        assertEquals(1, rules.size());
    }

    public void testBuildRulesForElementWithInvalidExpression() {
        // Create a map of expressions and populate it with valid and
        // invalid expressions.
        Map expressions = new HashMap();
        expressions.put("width", createExpression("width"));
        expressions.put("height", "'"+ createExpression("width"));

        // Run the test.
        final String elementName = "img";
        CSSRemappingTransformer transformer =  new CSSRemappingTransformer();
        Collection rules = transformer.buildRulesForElement(expressions,
                elementName);
        assertEquals(1, rules.size());
    }

    private String createExpression(String cssProperty) {
        return "length(css('"+ cssProperty + "'),'px')";
    }

    public void testTransformerName()  throws Exception {
        assertEquals("CSSRemappingTransformer",
                new CSSRemappingTransformer().getTransformerName());
    }
}


