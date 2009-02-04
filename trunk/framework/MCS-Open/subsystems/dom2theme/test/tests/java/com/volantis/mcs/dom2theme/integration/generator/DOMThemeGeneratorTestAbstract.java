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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.integration.generator;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom2theme.impl.generator.DOMThemeGenerator;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetTester;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public abstract class DOMThemeGeneratorTestAbstract extends TestCaseAbstract {

    /**
     * Check that the input XML can be processed by the theme generator into
     * the expected XML and the expected theme.
     *
     * @param generator the theme generator under test
     * @param testName The test we are checking on behalf of.
     * @param inputXml An input XML document, with style property sets for
     *      elements defined using the style attribute.
     * @param expectedXml The expected XML document, which should have been
     *      modified to reference the rules in the expected theme.
     * @param expectedTheme The expected theme which is generated from the
     *      input.
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    protected void checkThemeGeneration(DOMThemeGenerator generator,
            String testName, String inputXml, String expectedXml,
            String expectedTheme)
            throws IOException, SAXException, ParserConfigurationException {

        Document actualXml = StyledDOMTester.createStyledDom(inputXml);

        OutputStyledElementsFactory factory = new OutputStyledElementsFactory();

        OutputStyledElementList outputElementList =
                factory.createOutputStyledElements(actualXml);

        StyleSheet actualStyleSheet = generator.generateStyleSheetFor(outputElementList);

        StyleSheetTester.assertStyleSheetEquals(testName, expectedTheme, actualStyleSheet);

        checkXmlEquals(testName, expectedXml, actualXml);
    }

    private void checkXmlEquals(String testName, String expectedXml,
            Document actualDocument) throws IOException, SAXException,
            ParserConfigurationException {

        String actualXml = DOMUtilities.toString(actualDocument);
        System.out.println("expected XML: " + expectedXml);
        System.out.println("actual   XML: " + actualXml);
        assertXMLEquals(testName, expectedXml, actualXml);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9195/1	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 19-Jul-05	8668/10	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/8	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
