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
package com.volantis.mcs.css.impl.parser;

import com.volantis.mcs.css.parser.ExtensionHandlerMock;
import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.impl.DefaultFirstLetterSelector;
import com.volantis.mcs.themes.impl.DefaultHoverSelector;
import com.volantis.mcs.themes.impl.DefaultInlineStyleSelector;
import com.volantis.mcs.themes.impl.DefaultLinkSelector;
import com.volantis.mcs.themes.impl.DefaultVisitedSelector;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.List;

/**
 * Test the parsing of inline style attribute values.
 */
public class StyleAttributeParsingTestCase
        extends TestCaseAbstract {

    /**********************
     * mocks needed for testing
     **********************/
    protected ExtensionHandlerMock extensionHandlerMock;

    //selector added to the rules generated from style attributes.
    private final InlineStyleSelector selector =
            new DefaultInlineStyleSelector(0);

    //setup
    protected void setUp() throws Exception {
        super.setUp();

        extensionHandlerMock =
                new ExtensionHandlerMock("extensionHandlerMock", expectations);
    }

    /**
     * Test the processing of a simple style declaration
     */
    public void testSimpleDeclaration() {
        String styleAttribute =
                "color: red; background-color: #000";

        CSSParserImpl strictCSSParser = createStrictCSSParser();
        StyleSheet styleSheet =
                strictCSSParser.parseInlineStyleAttribute(styleAttribute,
                        null, selector);

        List rules = styleSheet.getRules();

        assertEquals("Style Sheet should have 1 rule", rules.size(), 1);

        Rule rule = (Rule) rules.get(0);

        List selectors = rule.getSelectors();

        assertEquals("Rule should have 1 selector", selectors.size(), 1);

        SelectorSequence sequence = (SelectorSequence) selectors.get(0);
        assertEquals("Selector invalid", this.selector,
                sequence.getSelectors().get(0));

        StyleProperties properties = rule.getProperties();

        assertEquals("Background color incorrect",
            properties.getPropertyValue(
                    StylePropertyDetails.BACKGROUND_COLOR).toString(),
            "background-color:#000");

        assertEquals("color incorrect",
            properties.getPropertyValue(
                    StylePropertyDetails.COLOR).toString(),
            "color:red");
    }

    /**
     * Test the processing of a simple style wrapped in braces
     */
    public void testSimpleDeclarationWithBraces() {
        String styleAttribute =
                "{color: red; background-color: #000}";

        CSSParserImpl strictCSSParser = createStrictCSSParser();
        StyleSheet styleSheet =
                strictCSSParser.parseInlineStyleAttribute(styleAttribute,
                        null, selector);

        List rules = styleSheet.getRules();

        assertEquals("Style Sheet should have 1 rule", rules.size(), 1);

        Rule rule = (Rule) rules.get(0);

        List selectors = rule.getSelectors();

        assertEquals("Rule should have 1 selector", selectors.size(), 1);

        SelectorSequence sequence = (SelectorSequence) selectors.get(0);
        assertEquals("Selector invalid", this.selector,
                sequence.getSelectors().get(0));

        StyleProperties properties = rule.getProperties();

        assertEquals("Background color incorrect",
            properties.getPropertyValue(
                    StylePropertyDetails.BACKGROUND_COLOR).toString(),
            "background-color:#000");

        assertEquals("color incorrect",
            properties.getPropertyValue(
                    StylePropertyDetails.COLOR).toString(),
            "color:red");
    }

    /**
     * Test the processing of a style Block
     */
    public void testDeclarationBlock() {
        String styleAttribute =
                "{color: red; background-color: #000}" +
                        "::first-letter {color: yellow}";

        CSSParserImpl strictCSSParser = createStrictCSSParser();
        StyleSheet styleSheet =
                strictCSSParser.parseInlineStyleAttribute(styleAttribute,
                        null, selector);

        List rules = styleSheet.getRules();

        assertEquals("Style Sheet should have 2 rules", rules.size(), 2);

        Rule rule = (Rule) rules.get(0);

        List selectors = rule.getSelectors();

        assertEquals("Rule should have 1 selector", selectors.size(), 1);

        SelectorSequence sequence = (SelectorSequence) selectors.get(0);
        assertEquals("Selector invalid", this.selector,
                sequence.getSelectors().get(0));

        StyleProperties properties = rule.getProperties();

        assertEquals("Background color incorrect",
            properties.getPropertyValue(
                    StylePropertyDetails.BACKGROUND_COLOR).toString(),
            "background-color:#000");

        assertEquals("color incorrect",
            properties.getPropertyValue(
                    StylePropertyDetails.COLOR).toString(),
            "color:red");

        rule = (Rule) rules.get(1);
        properties = rule.getProperties();

        assertEquals("color incorrect",
            properties.getPropertyValue(
                    StylePropertyDetails.COLOR).toString(),
            "color:yellow");

        selectors = rule.getSelectors();

        assertEquals("Rule should have a one selector", selectors.size(), 1);

        SelectorSequence selectorSequence = (SelectorSequence) selectors.get(0);

        List innerSelectors = selectorSequence.getSelectors();

        assertTrue("Unexpected Selector", innerSelectors.get(0)
                instanceof DefaultFirstLetterSelector);

        assertTrue("Unexpected Selector", innerSelectors.get(1)
                instanceof DefaultInlineStyleSelector);

        return;
    }

    /**
     * Test the processing of Pseudo classes
     */
    public void testPseudoClasses() {
        String styleAttribute =
                "{color: red}" +
                        ":link:hover {background: blue}" +
                        ":visited {background: green}";

        CSSParserImpl strictCSSParser = createStrictCSSParser();
        StyleSheet styleSheet =
                strictCSSParser.parseInlineStyleAttribute(styleAttribute,
                        null, selector);

        List rules = styleSheet.getRules();

        assertEquals("Style Sheet should have 3 rules", rules.size(), 3);

        Rule rule = (Rule) rules.get(0);

        List selectors = rule.getSelectors();

        assertEquals("Rule should have 1 selector", selectors.size(), 1);

        SelectorSequence sequence = (SelectorSequence) selectors.get(0);
        assertEquals("Selector invalid", this.selector,
                sequence.getSelectors().get(0));

        StyleProperties properties = rule.getProperties();

        assertEquals("Color incorrect",
            properties.getPropertyValue(
                    StylePropertyDetails.COLOR).toString(), "color:red");

        rule = (Rule) rules.get(1);

        selectors = rule.getSelectors();

        assertEquals("Rule should have one selector", selectors.size(), 1);

        SelectorSequence selectorSequence = (SelectorSequence) selectors.get(0);

        List innerSelectors = selectorSequence.getSelectors();

        assertTrue("Unexpected Selector", innerSelectors.get(0)
                instanceof DefaultLinkSelector);

        assertTrue("Unexpected Selector", innerSelectors.get(1)
                instanceof DefaultHoverSelector);

        assertTrue("Unexpected Selector", innerSelectors.get(2)
                instanceof DefaultInlineStyleSelector);

        assertEquals("color incorrect",
            rule.getProperties().getPropertyValue(
                    StylePropertyDetails.BACKGROUND_COLOR).toString(),
            "background-color:blue");

        rule = (Rule) rules.get(2);

        selectors = rule.getSelectors();

        assertEquals("Rule should have a one selector", selectors.size(), 1);

        selectorSequence = (SelectorSequence) selectors.get(0);

        innerSelectors = selectorSequence.getSelectors();

        assertTrue("Unexpected Selector", innerSelectors.get(0)
                instanceof DefaultVisitedSelector);

        assertTrue("Unexpected Selector", innerSelectors.get(1)
                instanceof DefaultInlineStyleSelector);

        assertEquals("color incorrect",
            rule.getProperties().getPropertyValue(
                    StylePropertyDetails.BACKGROUND_COLOR).toString(),
            "background-color:green");

    }

    /**
     * Test the processing of invalid Pseudo classes
     */
    public void testInvalidPseudoClasses() {
        String styleAttribute =
                "{color: red}" +
                        ":link :hover {background: blue}" +
                        ":visited {background: green}";

        CSSParserImpl strictCSSParser = createStrictCSSParser();

        boolean errored = false;

        try {
            strictCSSParser.parseInlineStyleAttribute(styleAttribute,
                null, selector);
        } catch (IllegalStateException ex) {
            errored = true;
        }

        assertTrue("Illegal State Exception expected", errored);

    }


    /**
     * Create a parser
     * @return CSSParserImpl instance.
     */
    private CSSParserImpl createStrictCSSParser() {
        return new CSSParserImpl(
                StyleSheetFactory.getDefaultInstance(),
                new StrictDiagnosticListener(),
                extensionHandlerMock);
    }
}
