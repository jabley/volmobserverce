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
package com.volantis.mcs.dom2theme.integration.rewriter;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.rewriter.DOMStyleAttributeRewriter;
import com.volantis.mcs.dom2theme.integration.generator.OutputStyledElementsFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DOMStyleAttributeRewriterTestCase
        extends TestCaseAbstract {

    protected void setUp() throws Exception {

        super.setUp();

        // Turn up log4j *Threshold* to debug.
        // Each test still needs to set a *Level* for it's Category
        enableLog4jDebug();
    }

    /**
     * Check if rewriting styles to attribute 'style' works properly
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    protected void checkStylesRewriter(String testName, 
            String inputXml, String expectedXml)
            throws IOException, SAXException, ParserConfigurationException {

        Document styledDom = StyledDOMTester.createStyledDom(inputXml);
        OutputStyledElementsFactory factory = new OutputStyledElementsFactory();
        OutputStyledElementList outputElementList =
                factory.createOutputStyledElements(styledDom);
        OutputStyledElementIteratee iteratee = new
            DOMStyleAttributeRewriter();
        outputElementList.iterate(iteratee);
        checkXmlEquals(testName, expectedXml, styledDom);
    }


    /**
     * Compare if documents are equal
     */    
    private void checkXmlEquals(String testName, String expectedXml,
            Document actualDocument) throws IOException, SAXException,
            ParserConfigurationException {

        String actualXml = DOMUtilities.toString(actualDocument);
        System.out.println("expected XML: " + expectedXml);
        System.out.println("actual   XML: " + actualXml);
        assertXMLEquals(testName, expectedXml, actualXml);
    }
    
    /**
     * The standard input XML from AN004 5.9.3 Device Theme Generation
     */
    private String inputXml =
        "<body style='text-align: left'>" +
            "<p style='{font-family: sans-serif}" +
                ":first-letter {font-size: x-large; font-weight: bold; color: red}'>" +
                "<a style='{mcs-text-underline-style: solid}" +
                    ":link {color: blue}" +
                    ":visited {color: green}" +
                    ":hover:link {background-color: gray; color:purple}" +
                    ":hover:visited {background-color: gray; color:yellow}'/>" +
                "<a style='mcs-text-underline-style: solid; font-size: large'/>" +
                "<a style='{mcs-text-underline-style: solid; font-size: medium}" +
                    ":link {color: blue}'/>" +
                "<a style='{mcs-text-underline-style: solid; font-size: medium}" +
                    ":link {color: blue}'/>" +
            "</p>" +
            "<p style='{font-family:fantasy}" +
                ":first-letter {font-size: x-large; color: green}'>" +
                "<span style='color: red'/>" +
                "<span style='color: red; font-size: large'/>" +
            "</p>" +
            "<p style='{font-family:fantasy}" +
                ":first-letter {font-size: x-large}'>" +
            "</p>" +
        "</body>";

    /**
     * Expected output - XHTML file with styles as attributes
     */
    private String expectedXml =
        "<body style='text-align:left'>" +
            "<p style=\"font-family:sans-serif\">" +
                "<a style=\"text-decoration:underline\"/>"+
                "<a style=\"font-size:large;text-decoration:underline\"/>" +
                "<a style=\"font-size:medium;text-decoration:underline\"/>" +
                "<a style=\"font-size:medium;text-decoration:underline\"/>" +
            "</p>" +
            "<p style=\"font-family:fantasy\">" +
                "<span style=\"color:red\"/>" +
                "<span style=\"color:red;font-size:large\"/>" +
            "</p>" +
            "<p style=\"font-family:fantasy\"/>"+
        "</body>";
    

    /**
     * Test of styles rewriter
     */
    public void testStylesRewriter() throws IOException,
            ParserConfigurationException, SAXException {

        checkStylesRewriter("Styles rewriter test",
                inputXml, expectedXml);
    }
}
