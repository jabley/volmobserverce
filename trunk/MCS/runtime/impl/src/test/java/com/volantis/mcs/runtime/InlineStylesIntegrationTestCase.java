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
package com.volantis.mcs.runtime;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.runtime.styling.StylingFunctions;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.styling.compiler.InlineStyleSheetCompilerFactory;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.InputStream;

/**
 * Test the processing of pages with elements which contain style attributes
 */
public class InlineStylesIntegrationTestCase extends TestCaseAbstract {

    private StrictStyledDOMHelper strictStyledDOMHelper;

    public void setUp() {
        strictStyledDOMHelper = new StrictStyledDOMHelper(null) {
            public Document parse(InputStream inputStream) {
                return super.parse(inputStream);
            }

            protected StyleSheetCompilerFactory getStyleSheetCompilerFactory() {
                return new InlineStyleSheetCompilerFactory(
                        StylingFunctions.getResolver());
            }
        };
    }

    /**
     * Test the processing of an element with simple declaration
     */
    public void testSimpleIntegration() {
        String input =
                "<html>" +
                    "<head>" +
                    "</head>" +
                    "<body>" +
                        "<p style=\"color: red\">\n" +
                            "color: red\n" +
                            "<a href=\"http://someurl\">" +
                                "Text Not Formatted by style attribute" +
                            "</a>\n" +
                        "</p>" +
                    "</body>" +
                "</html>";

        String expected =
                "<html><head/><body><p style='color: red'>\n" +
                        "color: red\n" +
                        "<a href=\"http://someurl\">" +
                        "Text Not Formatted by style attribute</a>\n" +
                        "</p></body></html>";

        doTestStyleParsing(input, expected);
    }

    /**
     * Test the processing of elements with pseudo classs
     */
    public void testPseudoClassIntegration() {
        String input =
                "<html>" +
                    "<head>" +
                    "</head>" +
                    "<body>" +
                        "<p >\n" +
                        "<a href=\"http://www.volantis.com\" " +
                        "style=\"{color: yellow}\n" +
                            ":link {background: #ddd}\n" +
                            ":visited {background: red}\">Link To Volantis</a>\n" +
                        "<a href=\"http://localhost:8080/unvisited\" " +
                        "style=\"{color: yellow}\n" +
                        ":link {background: #ddd}\n" +
                        ":visited {background: red}\">Link To Unvisited</a>\n" +
                        "</p>" +
                    "</body>" +
                "</html>";

        String expected =
                "<html>" +
                "<head/>" +
                    "<body>" +
                        "<p>\n" +
                        "<a href=\"http://www.volantis.com\" " +
                            "style='{color: yellow} " +
                            ":link {background-attachment: scroll; " +
                            "background-color: #ddd; background-image: none; " +
                            "background-position: 0% 0%; background-repeat: repeat} " +
                            ":visited {background-attachment: scroll; " +
                            "background-color: red; background-image: none; " +
                            "background-position: 0% 0%; background-repeat: repeat}'>" +
                            "Link To Volantis</a>\n" +
                        "<a href=\"http://localhost:8080/unvisited\" " +
                            "style='{color: yellow} " +
                            ":link {background-attachment: scroll; " +
                            "background-color: #ddd; background-image: none; " +
                            "background-position: 0% 0%; background-repeat: repeat} " +
                            ":visited {background-attachment: scroll; " +
                            "background-color: red; background-image: none; " +
                            "background-position: 0% 0%; " +
                            "background-repeat: repeat}'>Link To Unvisited</a>\n" +
                    "</p>" +
                    "</body>" +
                "</html>";

        doTestStyleParsing(input, expected);
    }

    /**
     * Parse the input and compare the result with the expected value.
     * @param input
     * @param expected
     */
    private void doTestStyleParsing(String input, String expected) {
        Document document = strictStyledDOMHelper.parse(input);

        String result = strictStyledDOMHelper.render(document);

        assertEquals("Unexpected Result", expected,  result);
    }

}
