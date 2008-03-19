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
package com.volantis.mcs.dom;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Test case which verifies behaviour of the StyledDOMTester. This is a
 * good idea because it is used by other subsystem's tests.
 */
public class StyledDOMTesterTestCase extends TestCaseAbstract {

    /**
     * This test verifies that the {@link StyledDOMTester#createStyledDom}
     * and {@link StyledDOMTester#renderStyledDOM} are symmetrical.
     *
     * @throws IOException if there was a problem reading the XML into a DOM
     * @throws SAXException if there was a problem reading the XML into a DOM
     * @throws ParserConfigurationException if there was a problem comparing
     * the XML.
     */
    public void testCircular() throws IOException, SAXException,
            ParserConfigurationException {

        String inputXML =
"<body>" +
    "<p style='{font-family: sans-serif}" +
        " :first-letter {color: green}" +
        " :link {color: purple}" +
        " :link:hover {mcs-text-blink: blink; mcs-text-underline-style: solid}'>" +
        "<div style = '{font-family: sans-serif} :first-letter {color: red}'/>" +
    "</p>" +
    "<p style='font-family: sans-serif'/>" +
"</body>";

        Document doc = StyledDOMTester.createStyledDom(inputXML);
        String resultXML = StyledDOMTester.renderStyledDOM(doc);
        assertXMLEquals("", inputXML, resultXML);
        Document resultDoc = StyledDOMTester.createStyledDom(resultXML);

        // compare Documents
        Element originalBody = (Element)doc.getRootElement();
        Element resultBody = (Element)resultDoc.getRootElement();
        assertStylesEqual(originalBody, resultBody);

        Element originalParagraphOne = (Element) originalBody.getHead();
        Element resultParagraphOne = (Element) resultBody.getHead();
        assertStylesEqual(originalParagraphOne, resultParagraphOne);

        Element originalDiv = (Element) originalParagraphOne.getHead();
        Element resultDiv = (Element) resultParagraphOne.getHead();
        assertStylesEqual(originalDiv, resultDiv);

        Element originalParagraphTwo = (Element) originalParagraphOne.getNext();
        Element resultParagraphTwo = (Element) resultParagraphOne.getNext();
        assertStylesEqual(originalParagraphTwo, resultParagraphTwo);
    }

    /**
     * Compares the original and result elements by verifying that their
     * names are the same, and that the Styles have the same values.
     *
     * @param original Element whose name and Styles to compare
     * @param result Element whose name and Styles to compare
     */
    private void assertStylesEqual(Element original, Element result) {

        assertEquals(original.getName(), result.getName());
        Styles originalStyles = original.getStyles();
        Styles resultStyles = result.getStyles();

        if (originalStyles == null) {
            assertNull(resultStyles);
        } else {
            PropertyValues originalValues = originalStyles.getPropertyValues();
            PropertyValues resultValues = resultStyles.getPropertyValues();
            StylePropertyDefinitions definitions =
                    StylePropertyDetails.getDefinitions();
            for(Iterator i = definitions.stylePropertyIterator(); i.hasNext();) {
                StyleProperty property = (StyleProperty)i.next();
                assertEquals(originalValues.getComputedValue(property),
                        resultValues.getComputedValue(property));
                assertEquals(originalValues.getSpecifiedValue(property),
                        resultValues.getSpecifiedValue(property));
            }
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9195/1	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 ===========================================================================
*/
