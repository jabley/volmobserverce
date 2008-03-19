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
package com.volantis.mcs.dom2theme.integration.generator.rule.type;

import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.dom2theme.impl.generator.rule.builder.SimpleSelectorSequenceBuilder;
import com.volantis.mcs.dom2theme.impl.generator.rule.type.TypeSelectorSequence;
import com.volantis.mcs.dom2theme.impl.generator.rule.type.TypeSelectorSequenceExtractor;
import com.volantis.mcs.dom2theme.impl.generator.rule.type.TypeSelectorSequenceIteratee;
import com.volantis.mcs.dom2theme.impl.generator.rule.type.TypeSelectorSequenceList;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.integration.generator.OutputStyledElementsFactory;
import com.volantis.mcs.themes.StyleSheetTester;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Assert;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeSelectorExtractorTestCase extends TestCaseAbstract {

    private static final CSSParserFactory CSS_PARSER_FACTORY =
            CSSParserFactory.getDefaultInstance();
    
    public void testSomething() throws IOException,
            ParserConfigurationException, SAXException {

        String inputXml =
                "<body style='text-align: left'>" +
                    "<p style='{font-family: sans-serif}" +
                        ":first-letter {font-size: x-large; font-weight: bold; color: red}\n'>" +
                        "<a style='{mcs-text-underline-style: solid}" +
                            ":link {color: blue}" +
                            ":visited {color: green}" +
                            ":hover:link {background-color: gray; color:purple}" +
                            ":hover:visited {background-color: gray; color:yellow}'>" +
                        "</a>" +
                        "<a style='mcs-text-underline-style: solid; font-size: large'/>" +
                        "<a style='{mcs-text-underline-style: solid; font-size: medium}" +
                            ":link {color: blue}'>" +
                        "</a>" +
                        "<a style='{mcs-text-underline-style: solid; font-size: medium}" +
                            ":link {color: blue}'>" +
                        "</a>" +
                    "</p>" +
                    "<p style='{font-family:fantasy}" +
                        ":first-letter {font-size: x-large; color: green}\n'>" +
                        "<span style='color: red'/>" +
                        "<span style='color: red; font-size: large'/>" +
                    "</p>" +
                    "<p style='font-family:fantasy'/>" +
                "</body>";

        String expectedSelectors =
            // type selectors (specificity: 1)
            "a," +
            "body," +
            "p," +
            "span," +
            // pseudo element selectors (specificity: 1)
            ":first-letter," +
            // combination of type and pseudo element selectors (specificity: 2)
            "p:first-letter," +
            // pseudo class selectors (specificity: 10)
            ":link," +
            ":visited," +
            // combination of type and pseudo class selectors (specificity: 11)
            "a:link," +
            "a:visited," +
            // combination of multiple pseudo class selectors (specificity: 20)
            ":link:hover," +
            ":hover:visited," +
            // combination of type and multiple pseudo class selectors (specificity: 21)
            "a:link:hover," +
            "a:hover:visited";

        checkSelectorExtraction(new TypeSelectorSequenceExtractor(), "something",
                inputXml, expectedSelectors);
    }

    protected void checkSelectorExtraction(TypeSelectorSequenceExtractor sequenceExtractor,
            String testName, String inputXml, String expectedSelectors)
            throws IOException, SAXException, ParserConfigurationException {

        // Create a styled dom from the input XML.
        Document actualXml = StyledDOMTester.createStyledDom(inputXml);

        // Extract the elements from the styled dom.
        final OutputStyledElementList outputElementList =
                new OutputStyledElementsFactory().createOutputStyledElements(
                        actualXml);

        // Test the extraction of selectors from those elements.
        // todo: deal with null return
        TypeSelectorSequenceList actualOutputSelectorList =
                sequenceExtractor.extractSequences(outputElementList);

        // translate output selectors to theme selectors
        final List actualSelectorList = new ArrayList();
        actualOutputSelectorList.iterate(new TypeSelectorSequenceIteratee() {
            public void next(TypeSelectorSequence sequence) {
                final SimpleSelectorSequenceBuilder builder =
                        new SimpleSelectorSequenceBuilder(
                                StyleSheetFactory.getDefaultInstance());
                if (sequence.getType() != null) {
                    builder.setTypeSelector(sequence.getType());
                }
                builder.addPseudoSelectors(sequence.getPath());
                actualSelectorList.add(builder.getSequence());
            }
        });


        // Parse the expected selectors (to canonicalise them).
        List expectedSelectorList = null;
        try {
            CSSParser parser = CSS_PARSER_FACTORY.createStrictParser();
            expectedSelectorList = parser.parseSelectorGroup(
                            expectedSelectors);
        } catch (IllegalArgumentException e) {
            Assert.fail("Error occurred during parsing: " + expectedSelectors);
        }

        // Render back to string for comparison purposes.
        String renderedExpectedSelectors = StyleSheetTester.renderSelectors(
                expectedSelectorList);
        System.out.println("expected: " + renderedExpectedSelectors);
        String renderedActualSelectors = StyleSheetTester.renderSelectors(
                actualSelectorList);
        System.out.println("actual  : " + renderedActualSelectors);

        // do the comparison.
        Assert.assertEquals(testName, renderedExpectedSelectors,
                renderedActualSelectors);
    }

    // todo: extract into a StyledElementExtractor?


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 05-Sep-05	9407/5	pduffin	VBM:2005083007 Removed old themes model

 02-Sep-05	9407/3	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05	9287/2	gkoch	VBM:2005080509 vbm2005080509 applied review comments

 09-Aug-05	9195/2	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 19-Jul-05	8668/15	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/13	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
