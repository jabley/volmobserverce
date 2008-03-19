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
public class DefaultMarkerTestCase extends MarkerDOMTransformerTestAbstract {

    /**
     * The template for input XML.
     */
    private static final String INPUT_XML =
            "<html>" +
              "<head>" +
              "</head>" +
              "<body>" +
                "<LIST_ELEMENT_NAME style=\"color:blue\">" +
                  "<li style=\"{color:black}" +
                              "::marker {color:red}\">a</li>" +
                  "<li style=\"{} ::marker {color:red}\">b</li>" +
                  "<li style=\"{} ::marker {color:red}\">" +
                    "<ol>" +
                      "<li style=\"{} ::marker {color:red}\">oranges</li>" +
                      "<li style=\"{} ::marker {color:red}\">apples</li>" +
                    "</ol>" +
                  "</li>" +
                  "<li style=\"{} ::marker {color:red}\">c</li>" +
                "</LIST_ELEMENT_NAME>" +
                "<div>this is not in a list</div>" +
                "<LIST_ELEMENT_NAME style=\"color:yellow\">" +
                  "<li style=\"{color:blue}" +
                              "::marker {color:green}\">a</li>" +
                  "<li style=\"{} ::marker {color:green}\">b</li>" +
                  "<li style=\"{} ::marker {color:green}\">c</li>" +
                  "<li style=\"{} ::marker {color:green}\">d</li>" +
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
                "<LIST_ELEMENT_NAME style=\"color: blue\">" +
                  "<li style=\"color: red\">" +
                    "<span style=\"color: black\">a</span>" +
                  "</li>" +
                  "<li style=\"color: red\">" +
                    "<span style=\"color: blue\">b</span>" +
                  "</li>" +
                  "<li style=\"color: red\">" +
                    "<span style=\"color: blue\">" +
                      "<ol>" +
                        "<li style='color: red'>" +
                          "<span style=\"color: blue\">oranges</span>" +
                        "</li>" +
                        "<li style='color: red'>" +
                          "<span style=\"color: blue\">apples</span>" +
                        "</li>" +
                      "</ol>" +
                    "</span>" +
                  "</li>" +
                  "<li style=\"color: red\">" +
                    "<span style=\"color: blue\">c</span>" +
                  "</li>" +
                "</LIST_ELEMENT_NAME>" +
                "<div>this is not in a list</div>" +
                "<LIST_ELEMENT_NAME style=\"color: yellow\">" +
                  "<li style=\"color: green\">" +
                    "<span style=\"color: blue\">a</span>" +
                  "</li>" +
                  "<li style=\"color: green\">" +
                    "<span style=\"color:yellow\">b</span>" +
                  "</li>" +
                  "<li style=\"color: green\">" +
                    "<span style=\"color:yellow\">c</span>" +
                  "</li>" +
                  "<li style=\"color: green\">" +
                    "<span style=\"color:yellow\">d</span>" +
                  "</li>" +
                "</LIST_ELEMENT_NAME>" +
              "</body>" +
            "</html>";

    /**
     * Tests the transformer against a document containing an unordered list.
     *
     * @throws java.io.IOException      if an IO exception occurs.
     * @throws org.xml.sax.SAXException if a SAX exception occurs.
     * @throws javax.xml.parsers.ParserConfigurationException
     *
     */
    public void testDefaultContentMarkerWithUL()
            throws IOException,
                   SAXException,
                   ParserConfigurationException {

        doTransform(getInputXML("ul"),
                    getExpectedXML("ul"));
    }

    /**
     * Tests the transformer against a document containing an ordered list.
     *
     * @throws java.io.IOException      if an IO exception occurs.
     * @throws org.xml.sax.SAXException if a SAX exception occurs.
     * @throws javax.xml.parsers.ParserConfigurationException
     *
     */
    public void testDefaultContentMarkerWithOL()
            throws IOException,
                   SAXException,
                   ParserConfigurationException {

        doTransform(getInputXML("ol"),
                    getExpectedXML("ol"));
    }

    /**
     * Tests the transformer against a document containing a navigation list.
     *
     * @throws java.io.IOException      if an IO exception occurs.
     * @throws org.xml.sax.SAXException if a SAX exception occurs.
     * @throws javax.xml.parsers.ParserConfigurationException
     *
     */
    public void testDefaultContentMarkerWithNL()
            throws IOException,
                   SAXException,
                   ParserConfigurationException {

        doTransform(getInputXML("nl"),
                    getExpectedXML("nl"));
    }

    /**
     * Tests the transformer against a document an upper-case list element name.
     * Despite the upper case of the element, it should still be recognised as a
     * supported list.
     *
     * @throws java.io.IOException      if an IO exception occurs.
     * @throws org.xml.sax.SAXException if a SAX exception occurs.
     * @throws javax.xml.parsers.ParserConfigurationException
     *
     */
    public void testDefaultContentMarkerCaseConversion()
            throws IOException,
                   SAXException,
                   ParserConfigurationException {
        doTransform(getInputXML("NL"),
                    getExpectedXML("NL"));
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

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/
