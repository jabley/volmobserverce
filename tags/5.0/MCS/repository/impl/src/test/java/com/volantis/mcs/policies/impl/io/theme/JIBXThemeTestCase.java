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

package com.volantis.mcs.policies.impl.io.theme;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.impl.io.JIBXTestAbstract;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.AttributeSelectorActionEnum;
import com.volantis.mcs.themes.BaseStyleSheet;
import com.volantis.mcs.themes.CSSStyleSheet;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.InvalidSelector;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.mcs.themes.UniversalSelector;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.MCSChartForegroundColorsKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.properties.StyleProperty;
import org.jibx.runtime.JiBXException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JIBXThemeTestCase
        extends JIBXTestAbstract {

    private static final int TYPE_SELECTOR = 0;
    private static final int ATTRIBUTE_SELECTOR = 1;
    private static final int CLASS_SELECTOR = 2;
    private static final int ID_SELECTOR = 3;
    private static final int UNIVERSAL_SELECTOR = 4;
    private static final int PSEUDO_ELEMENT_SELECTOR = 5;

    private static final int COMBINED_SELECTOR = 10;

    private static final int INVALID_SELECTOR = 30;

    private static final int NO_CHILD = 0;
    private static final int FIRST_AND_SECOND_CHILD = 1;
    private static final int LIST_OF_CHILDREN = 2;
    private static final int WHITESPACE_STRING = 3;

    // Factory used to create nodes of the Theme tree
    private StyleSheetFactory factory = StyleSheetFactory.getDefaultInstance();

    /**
     * The object to use to create style values.
     */
    private StyleValueFactory styleValueFactory =
        StyleValueFactory.getDefaultInstance();
    private final CSSParserFactory cssParserFactory = CSSParserFactory.getDefaultInstance();
    private final StyleSheetFactory styleSheetFactory = StyleSheetFactory.getDefaultInstance();
    private final InternalPolicyFactory policyFactory = (InternalPolicyFactory)
                    PolicyFactory.getDefaultInstance();

    public void testWriteRulesThemePolicy() throws Exception {
        String css = "b#as.fred[href~=\"1234\"]:first-child:nth-child(2n+4)::after {color: green}";

        VariablePolicyBuilder themeBuilder = createPolicyFromCSS(css);

        writeAndCompare(themeBuilder, "themeWithLotsOfSelectorsAndNoChildStyle.xml");
    }

    public void testWriteCssThemePolicy() throws Exception {
        String css = "b#as.fred[href~=\"1234\"]:first-child:nth-child(2n+4)::after {color: green}";

        VariablePolicyBuilder themeBuilder = createPolicyContainingCSS(css);

        writeAndCompare(themeBuilder, "themeWithCSS.xml");
    }

    private VariablePolicyBuilder createPolicyFromCSS(String css) {
        StyleSheet styleSheet = createStyleSheetFromCSS(css);

        return createThemePolicy(styleSheet);
    }

    private StyleSheet createStyleSheetFromCSS(String css) {
        CSSParser parser = cssParserFactory.createStrictParser();
        StyleSheet styleSheet =
                parser.parseStyleSheet(new StringReader(css), null);
        return styleSheet;
    }

    private VariablePolicyBuilder createPolicyContainingCSS(String css) {
        CSSStyleSheet styleSheet = createStyleSheetContainingCSS(css);

        return createThemePolicy(styleSheet);
    }

    private CSSStyleSheet createStyleSheetContainingCSS(String css) {
        CSSStyleSheet styleSheet = styleSheetFactory.createCSSStyleSheet();
        styleSheet.setCSS(css);
        return styleSheet;
    }

    private VariablePolicyBuilder createThemePolicy(BaseStyleSheet styleSheet) {

        VariablePolicyBuilder policyBuilder =
                policyFactory.createVariablePolicyBuilder(PolicyType.THEME);

        addThemeVariant(policyBuilder, styleSheet, "Master");

        return policyBuilder;
    }

    private void addThemeVariant(
            VariablePolicyBuilder policyBuilder, BaseStyleSheet styleSheet,
            String deviceName) {
        VariantBuilder variantBuilder =
                policyFactory.createVariantBuilder(VariantType.THEME);
        policyBuilder.addVariantBuilder(variantBuilder);

        TargetedSelectionBuilder targeted =
                policyFactory.createTargetedSelectionBuilder();
        targeted.addDevice(deviceName);
        variantBuilder.setSelectionBuilder(targeted);

        InternalThemeContentBuilder themeContent =
                policyFactory.createThemeContentBuilder();
        themeContent.setStyleSheet(styleSheet);
        variantBuilder.setContentBuilder(themeContent);
    }

    public void testWritingThemeWithTypeAndWhitespaceStyle()
        throws IOException, ParserConfigurationException, SAXException,
            RepositoryException, JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{TYPE_SELECTOR});
        addStyles(r1[0], new int[]{WHITESPACE_STRING});

        String result = writeStructure(policyBuilder);

        String name = "themeWithSubjectSelectorTypeAndWhitespaceStyle.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);
    }

    private StyleSheet addStyleSheet(VariablePolicyBuilder policyBuilder, String deviceName) {
        StyleSheet styleSheet = styleSheetFactory.createStyleSheet();
        addThemeVariant(policyBuilder, styleSheet, deviceName);
        return styleSheet;
    }

    private VariablePolicyBuilder makeThemePolicy() {
        VariablePolicyBuilder policyBuilder =
                policyFactory.createVariablePolicyBuilder(PolicyType.THEME);
        return policyBuilder;
    }

    public void testWritingThemeWithPseudoElementSelectorAndWhitespaceStyle()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{PSEUDO_ELEMENT_SELECTOR});
        addStyles(r1[0], new int[]{WHITESPACE_STRING});

        String result = writeStructure(policyBuilder);

        String name = "themeWithPseudoElementSelectorAndWhitespaceStyle.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);

    }

    public void testWritingThemeWithInvalidSelector()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{INVALID_SELECTOR});
        addStyles(r1[0], new int[0]);

        String result = writeStructure(policyBuilder);

        String name = "themeWithInvalidSelector.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);
    }

    public void testWritingThemeWithAttributeAndNoChildStyle()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{ATTRIBUTE_SELECTOR});
        addStyles(r1[0], new int[]{NO_CHILD});

        String result = writeStructure(policyBuilder);

        String name = "themeWithAttributeSelectorAndNoChildStyle.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);

    }

    public void testWritingThemeWithClassAndNoChildStyle()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{CLASS_SELECTOR});
        addStyles(r1[0], new int[]{NO_CHILD});

        String result = writeStructure(policyBuilder);

        String name = "themeWithSubjectSelectorClassAndNoChildStyle.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);
    }

    public void testWritingThemeWithIDAndNoChildStyle()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{ID_SELECTOR});
        addStyles(r1[0], new int[]{NO_CHILD});

        String result = writeStructure(policyBuilder);

        String name = "themeWithSubjectSelectorIDAndNoChildStyle.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);
    }

    public void testWritingThemeWithUniversalAndNoChildStyle()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{UNIVERSAL_SELECTOR});
        addStyles(r1[0], new int[]{NO_CHILD});

        String result = writeStructure(policyBuilder);

        String name = "themeWithSubjectSelectorUniversalAndNoChildStyle.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);
    }

    public void testWritingThemeWithCombinedSelectorAndNoChildStyle()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{COMBINED_SELECTOR});
        addStyles(r1[0], new int[]{NO_CHILD});

        String result = writeStructure(policyBuilder);

        String name = "themeWithCombinedSelectorAndNoChildStyle.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);

    }

    public void testWritingThemeWithUniversalAndFirstSecondChildStyles()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{UNIVERSAL_SELECTOR});
        addStyles(r1[0], new int[]{FIRST_AND_SECOND_CHILD});

        String result = writeStructure(policyBuilder);

        String name = "themeWithSubjectSelectorUniversalAndFirstSecond.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);
    }

    public void testWritingThemeWithUniversalAndListChildStyles()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet = addStyleSheet(policyBuilder, "deviceName1");

        Rule[] r1 = addRules(styleSheet, 1);
        addSelectors(r1[0], new int[]{UNIVERSAL_SELECTOR});
        addStyles(r1[0], new int[]{LIST_OF_CHILDREN});

        String result = writeStructure(policyBuilder);


        String name = "themeWithSubjectSelectorUniversalAndList.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);
    }

    public void testWritingTwoDeviceThemesWithThreeAndTwoRules()
        throws IOException,
        ParserConfigurationException, SAXException, RepositoryException,
        JiBXException {

        VariablePolicyBuilder policyBuilder = makeThemePolicy();

        StyleSheet styleSheet1 = addStyleSheet(policyBuilder, "deviceName1");
        StyleSheet styleSheet2 = addStyleSheet(policyBuilder, "deviceName2");

        Rule[] r1 = addRules(styleSheet1, 3);
        addSelectors(r1[0], new int[]{UNIVERSAL_SELECTOR});
        addStyles(r1[0], new int[]{NO_CHILD});
        addSelectors((Rule)r1[1], new int[]{TYPE_SELECTOR});
        addStyles((Rule)r1[1], new int[]{NO_CHILD});
        addSelectors((Rule)r1[2], new int[]{UNIVERSAL_SELECTOR});
        addStyles((Rule)r1[2], new int[]{NO_CHILD});

        Rule[] r2 = addRules(styleSheet2, 2);
        addSelectors(r2[0], new int[]{UNIVERSAL_SELECTOR});
        addStyles(r2[0], new int[]{LIST_OF_CHILDREN});
        addSelectors(r2[1], new int[]{UNIVERSAL_SELECTOR});
        addStyles(r2[1], new int[]{LIST_OF_CHILDREN});

        String result = writeStructure(policyBuilder);

        String name = "themeMultipleDeviceThemesAndRules.xml";
        String expectedXML = RESOURCE_LOADER.getResourceAsString(name);

        assertXMLEquals("Generated XML Theme does not match expected XML",
                        expectedXML, result);
    }

    /**
     * Add a number of Rule objects to the given DeviceTheme
     *
     * @param styleSheet
     * @param number of Rules to add
     * @return array of rules added
     */
    private Rule[] addRules(StyleSheet styleSheet, int number) {

        List rules = styleSheet.getRules();

        for (int i = 0; i < number; i++) {
            Rule newRule = ThemeFactory.getDefaultInstance().createRule();

            rules.add(newRule);
        }

        Rule[] ruleArray = new Rule[rules.size()];
        rules.toArray(ruleArray);

        return ruleArray;
    }

    /**
     * Add a single selector to the given Rule.
     *
     * @param parent
     * @param types  arrays of types to add
     * @return
     */
    private Selector[] addSelectors(Rule parent, int[] types) {

        // Add some selectors
        List selectors = new ArrayList();

        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {
                case INVALID_SELECTOR:
                    InvalidSelector iSelector =
                            factory.createInvalidSelector("foobar");
                    selectors.add(iSelector);
                    break;
                case TYPE_SELECTOR:
                case ATTRIBUTE_SELECTOR:
                case CLASS_SELECTOR:
                case ID_SELECTOR:
                case UNIVERSAL_SELECTOR:
                case PSEUDO_ELEMENT_SELECTOR:
                    addSelector(selectors, types[i]);
                    break;
                case COMBINED_SELECTOR:
                    addCombinedSelector(selectors);
            }
        }

        parent.setSelectors(selectors);
        Selector[] selectorArray = new Selector[selectors.size()];
        selectors.toArray(selectorArray);

        return selectorArray;
    }

    /**
     * Add a selector to the given List
     *
     * @param selectors
     * @param type   the type of selector to add to the list
     */
    private void addSelector(List selectors, int type) {

        // Fill in the selector lists
        SelectorSequence simple = factory.createSelectorSequence();

        switch (type) {
            case TYPE_SELECTOR:
                TypeSelector tSelector = factory.createTypeSelector();
                tSelector.setType("code");
                simple.addSelector(tSelector);
                break;

            case ATTRIBUTE_SELECTOR:
//                List attSelectors = new ArrayList();
                AttributeSelector setAtt = factory.createAttributeSelector();
                setAtt.setConstraint(AttributeSelectorActionEnum.SET, null);
                setAtt.setName("attName");
                simple.addSelector(setAtt);
                AttributeSelector containsAtt =
                        factory.createAttributeSelector();
                containsAtt.setConstraint(
                        AttributeSelectorActionEnum.CONTAINS, "aValue");
                containsAtt.setName("contains-attribute");
                simple.addSelector(containsAtt);
                AttributeSelector startsWithAtt = factory.createAttributeSelector();
                startsWithAtt.setConstraint(
                        AttributeSelectorActionEnum.STARTS_WITH, "aStart");
                startsWithAtt.setName("starts-with-attribute");
                simple.addSelector(startsWithAtt);
                break;

            case CLASS_SELECTOR:
                ClassSelector classSelector =
                        factory.createClassSelector("cssClassName");
                simple.addSelector(classSelector);
                break;
            case ID_SELECTOR:
                IdSelector idSelector = factory.createIdSelector("theId");
                simple.addSelector(idSelector);
                break;
            case PSEUDO_ELEMENT_SELECTOR:
                PseudoElementSelector pseudoElementSelector =
                        factory.createPseudoElementSelector("after");
                simple.addSelector(pseudoElementSelector);
                break;
            case UNIVERSAL_SELECTOR:
                UniversalSelector uSelector = factory.createUniversalSelector();
                uSelector.setNamespacePrefix("xhtml2");
                simple.addSelector(uSelector);
        }

        selectors.add(simple);

    }

    /**
     * Add a CombinedSelector to the given list, if nested equals true nest
     * another Combined selector iside it.
     *
     * @param parent
     */
    private void addCombinedSelector(List parent) {

        // Lets build a 2 level deep combined selector
        CombinedSelector combined = factory.createCombinedSelector();

        combined.setCombinator(CombinatorEnum.CHILD);

        // Left object
        SelectorSequence simple2 = factory.createSelectorSequence();

        IdSelector idSelector = factory.createIdSelector("IDSELECTOR");
        simple2.addSelector(idSelector);
        combined.setContext(simple2);

        SelectorSequence simple3 = factory.createSelectorSequence();

        ClassSelector cSelector = factory.createClassSelector("BigOrange");

        simple3.addSelector(cSelector);

        combined.setSubject(simple3);

        parent.add(combined);

    }

    /**
     * Add a number of Styles to a given Rule.
     *
     * @param parent
     * @param types  to add
     */
    private void addStyles(Rule parent, int[] types) {

        // Finally add some styles
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        StyleProperty property = null;
        StyleValue value = null;

        for (int i = 0; i < types.length; i++) {
            Priority priority = Priority.NORMAL;
            switch (types[i]) {
                // A simple style, no child objects
                case NO_CHILD:
                    property = StylePropertyDetails.MARGIN_TOP;
                    value = styleValueFactory.getLength(null, 123, LengthUnit.CM);
                    priority = Priority.IMPORTANT;
                    break;

                    // A Style with a first & second child
                case FIRST_AND_SECOND_CHILD:

                    property = StylePropertyDetails.BORDER_SPACING;

                    StyleValue first =
                            styleValueFactory.getLength(null, 1, LengthUnit.PT);
                    StyleValue second =
                            styleValueFactory.getLength(null, 1, LengthUnit.PX);
                    value = styleValueFactory.getPair(first, second);
                    priority = Priority.IMPORTANT;
                    break;

                    // A Style with a list of children
                case LIST_OF_CHILDREN:

                    property =
                        StylePropertyDetails.MCS_CHART_FOREGROUND_COLORS;

                    List list = new ArrayList();
                    list.add(
                        styleValueFactory.getColorByPercentages(
                                null, 10, 45, 45));
                    list.add(MCSChartForegroundColorsKeywords.TRANSPARENT);
                    list.add(StyleColorNames.BLACK);
                    value = styleValueFactory.getList(list);
                    priority = Priority.IMPORTANT;
                    break;

                case WHITESPACE_STRING:
                    property = StylePropertyDetails.CONTENT;
                    value = styleValueFactory.getString(null, " ");
                    break;
            }
            PropertyValue propertyValue =
                ThemeFactory.getDefaultInstance().createPropertyValue(
                    property, value, priority);
            properties.setPropertyValue(propertyValue);
        }
        parent.setProperties(properties);

    }

}
