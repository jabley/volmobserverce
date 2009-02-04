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

import java.io.IOException;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Tests DOM transformations for documents
 */
public class BlockInlineMarkerTestCase extends MarkerDOMTransformerTestAbstract {

    /**min
     * The template for input XML.
     */
    private static final String INPUT_XML =
            "<html>" +
              "<head>" +
              "</head>" +
              "<body>" +
                "<LIST_ELEMENT_NAME style=\"list-style-position: inside; color:blue\">" +
                  "<li style=\"{color:black}" +
                              "::marker {color:red; content:\'&gt;\'}\">a</li>" +
                  "<li style=\"{} ::marker {color:red; content:'&gt;'}\">b</li>" +
                  "<li style=\"{} ::marker {color:red; content:'&gt;'}\">" +
                    "<ol style=\"list-style-position: inside\">" +
                      "<li style=\"{color:black}" +
                      "::marker {color:#3399ff; content:'#'}\">apples</li>" +
                      "<li>oranges</li>" +
                      "<li>bananas</li>" +
                      "<li>" +
                        "<nl style=\"list-style-position: inside; color:blue\">" +
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
                "<LIST_ELEMENT_NAME style=\"color:yellow; list-style-position: inside\">" +
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
            // The list-style-position in the following needs to be discarded
            // but unfortunately it is inherited so just clearing it is not
            // sufficient. What we need to do is somehow, possibly using
            // additional Priority values mark it as unimportant so that the
            // styles normalizer can discard it and any inherited values.
                "<div style='color: blue; list-style-position: inside'>" +
                  "<div style='color: black'>" +
                    "<span style='color: red; padding-right: 3px'>></span>" +
                    "a" +
                  "</div>" +
                  "<div>" +
                    "<span style='color: red; padding-right: 3px'>></span>" +
                    "b" +
                  "</div>" +
                  "<div>" +
                    "<span style='color: red; padding-right: 3px'>></span>" +
                    "<div>" +
                      "<div style='color: black'>" +
                        "<span style='color: #39f; padding-right: 3px'>#</span>" +
                        "apples" +
                      "</div>" +
                      "<div>" +
                        "oranges" +
                      "</div>" +
                      "<div>" +
                        "bananas" +
                      "</div>" +
                      "<div>" +
                        "<div style='color: blue'>" +
                          "<div style='color: green'>" +
                            "<span style='color: black; padding-right: 3px'>+</span>" +
                            "grapes" +
                          "</div>" +
                          "<div>" +
                            "<span style='color: black; padding-right: 3px'>-</span>" +
                            "melons" +
                          "</div>" +
                          "<div>" +
                            "kiwi fruit" +
                          "</div>" +
                        "</div>" +
                      "</div>" +
                    "</div>" +
                  "</div>" +
                  "<div>" +
                    "<span style='color: red; padding-right: 3px'>></span>" +
                    "d" +
                  "</div>" +
                "</div>" +
              "<div>this is not in a list</div>" +
              "<div style='color: yellow; list-style-position: inside'>" +
                "<div style='color: blue'>" +
                  "<span style='color: green; padding-right: 3px'>></span>" +
                  "a" +
                "</div>" +
                "<div>" +
                  "<span style='color: green; padding-right: 3px'>></span>" +
                  "b" +
                "</div>" +
                "<div>" +
                  "<span style='color: green; padding-right: 3px'>></span>" +
                  "c" +
                "</div>" +
                "<div>" +
                  "<span style='color: green; padding-right: 3px'>></span>" +
                  "d" +
                "</div>" +
              "</div>" +
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

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/
