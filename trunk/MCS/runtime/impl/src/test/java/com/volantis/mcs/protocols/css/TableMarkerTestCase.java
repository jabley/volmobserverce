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
package com.volantis.mcs.protocols.css;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Tests DOM transformations for documents
 */
public class TableMarkerTestCase extends MarkerDOMTransformerTestAbstract {

    /**
     * The template for input XML.
     */
    private static final String INPUT_XML =
            "<html>" +
              "<head>" +
              "</head>" +
              "<body>" +
                "<LIST_ELEMENT_NAME style=\"color:blue; list-style-position: outside\">" +
                  "<li style=\"{color:black}" +
                              "::marker {color:red; content:\'&gt;\'}\">a</li>" +
                  "<li style=\"{} ::marker {color:red; content:'&gt;'}\">b</li>" +
                  "<li style=\"{} ::marker {color:red; content:'&gt;'}\">" +
                    "<ol style=\"list-style-position: outside\">" +
                      "<li style=\"{color:black}" +
                      "::marker {color:#3399ff; content:'#'}\">apples</li>" +
                      "<li>oranges</li>" +
                      "<li>bananas</li>" +
                      "<li>" +
                        "<nl style=\"color:blue; list-style-position: outside\">" +
                            "<li style=\"{color:green}" +
                            "::marker {color:black; content:'+'}\">grapes</li>" +
                            "<li style=\"{} ::marker {color:black; content:'-'}\">melons</li>" +
                            "<li>kiwi fruit</li>" +
                        "</nl>" +
                      "</li>" +
                    "</ol>" +
                  "</li>" +
                  "<li style=\"{} ::marker {color:red; content:'&gt;'}\">d</li>" +
                "</LIST_ELEMENT_NAME>" +
                "<div>this is not in a list</div>" +
                "<LIST_ELEMENT_NAME style=\"color:yellow; list-style-position: outside\">" +
                  "<li style=\"{color:blue}" +
                              "::marker {color:green; content:'&gt;'}\">a</li>" +
                  "<li style=\"{} ::marker {color:green; content:'&gt;'}\">b</li>" +
                  "<li style=\"{} ::marker {color:green; content:'&gt;'}\">c</li>" +
                  "<li style=\"{} ::marker {color:green; content:'&gt;'}\">d</li>" +
                "</LIST_ELEMENT_NAME>" +
              "</body>" +
            "</html>";

    /**
     * The template for expected XML.
     */
    private static final String EXPECTED_XML =
            "<html>" +
              "<head/>" +
                "<body>" +
                  "<table style='color: blue'>" +
                    "<tr style='color: black'>" +
                      "<td style='color: red; text-align: right; vertical-align: top'>></td>" +
                      "<td style='vertical-align: top'>a</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td style='color: red; text-align: right; vertical-align: top'>></td>" +
                      "<td style='vertical-align: top'>b</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td style='color: red; text-align: right; vertical-align: top'>></td>" +
                      "<td style='vertical-align: top'>" +
                        "<table>" +
                          "<tr style='color: black'>" +
                            "<td style='color: #39f; text-align: right; vertical-align: top'>#</td>" +
                            "<td style='vertical-align: top'>apples</td>" +
                          "</tr>" +
                          "<tr>" +
                            "<td/>" +
                            "<td style='vertical-align: top'>oranges</td>" +
                          "</tr>" +
                          "<tr>" +
                            "<td/>" +
                            "<td style='vertical-align: top'>bananas</td>" +
                          "</tr>" +
                          "<tr>" +
                            "<td/>" +
                            "<td style='vertical-align: top'>" +
                              "<table style='color: blue'>" +
                                "<tr style='color: green'>" +
                                  "<td style='color: black; text-align: right; vertical-align: top'>+</td>" +
                                  "<td style='vertical-align: top'>grapes</td>" +
                                "</tr>" +
                                "<tr>" +
                                  "<td style='color: black; text-align: right; vertical-align: top'>-</td>" +
                                  "<td style='vertical-align: top'>melons</td>" +
                                "</tr>" +
                                "<tr>" +
                                  "<td/>" +
                                  "<td style='vertical-align: top'>kiwi fruit</td>" +
                                "</tr>" +
                              "</table>" +
                            "</td>" +
                          "</tr>" +
                        "</table>" +
                      "</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td style='color: red; text-align: right; vertical-align: top'>></td>" +
                      "<td style='vertical-align: top'>d</td>" +
                    "</tr>" +
                  "</table>" +
                  "<div>this is not in a list</div>" +
                  "<table style='color: yellow'>" +
                    "<tr style='color: blue'>" +
                      "<td style='color: green; text-align: right; vertical-align: top'>></td>" +
                      "<td style='vertical-align: top'>a</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td style='color: green; text-align: right; vertical-align: top'>></td>" +
                      "<td style='vertical-align: top'>b</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td style='color: green; text-align: right; vertical-align: top'>></td>" +
                      "<td style='vertical-align: top'>c</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td style='color: green; text-align: right; vertical-align: top'>></td>" +
                      "<td style='vertical-align: top'>d</td>" +
                    "</tr>" +
                  "</table>" +
                "</body>" +
              "</html>";

    /**
     * Tests the transformer against a document containing an unordered list
     * at the highest level of lists.
     *
     * @throws java.io.IOException      if an IO exception occurs.
     * @throws org.xml.sax.SAXException if a SAX exception occurs.
     * @throws javax.xml.parsers.ParserConfigurationException
     *
     */
    public void testTableContentMarkerWithUL()
            throws IOException,
                   SAXException,
                   ParserConfigurationException {

        doTransform(getInputXML("ul"),
                    getExpectedXML("ul"));
    }

    /**
     * Tests the transformer against a document containing an ordered list
     * at the highest level of lists.
     *
     * @throws java.io.IOException      if an IO exception occurs.
     * @throws org.xml.sax.SAXException if a SAX exception occurs.
     * @throws javax.xml.parsers.ParserConfigurationException
     *
     */
    public void testTableContentMarkerWithOL()
            throws IOException,
                   SAXException,
                   ParserConfigurationException {

        doTransform(getInputXML("ol"),
                    getExpectedXML("ol"));
    }

    /**
     * Tests the transformer against a document containing a navigation list
     * at the highest level of lists.
     *
     * @throws java.io.IOException      if an IO exception occurs.
     * @throws org.xml.sax.SAXException if a SAX exception occurs.
     * @throws javax.xml.parsers.ParserConfigurationException
     *
     */
    public void testTableContentMarkerWithNL()
            throws IOException,
                   SAXException,
                   ParserConfigurationException {

        doTransform(getInputXML("nl"),
                    getExpectedXML("nl"));
    }

    /**
     * Gets the input XML with the correct list name.
     * @param listElementName the name of the list element.
     * @return the input XML.
     */
    private String getInputXML(String listElementName) {
        return INPUT_XML.replaceAll("LIST_ELEMENT_NAME",
                                    listElementName);
    }

    /**
     * Gets the expected XML with the correct list name.
     * @param listElementName the name of the list element.
     * @return the expected XML.
     */
    private String getExpectedXML(String listElementName) {
        return EXPECTED_XML.replaceAll("LIST_ELEMENT_NAME",
                                       listElementName);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/
