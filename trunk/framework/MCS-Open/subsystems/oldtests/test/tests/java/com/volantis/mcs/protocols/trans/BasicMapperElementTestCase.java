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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/trans/BasicMapperElementTestCase.java,v 1.3 2002/11/18 13:10:42 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 18-Nov-02    Phil W-S        VBM:2002100307 - Updated to test valid
 *                              attribute retention.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.protocols.html.XHTMLBasicElementHelper;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This is the unit test for the BasicMapperElement class.
 */
public class BasicMapperElementTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * {@link TransformationConfiguration} which considers no style properties
     * to be important, and therefore will always return false from the
     * hasImportantStyleProperties method.
     */
    private static final TransformationConfiguration NO_IMPORTANT_PROPERTIES =
            new ProtocolConfigurationImpl(){
        public boolean optimisationWouldLoseImportantStyles(Element element) {
            return false;
        }
    };

    /**
     * Test TransformationConfiguration which considers only the
     * {@link StylePropertyDetails#TEXT_ALIGN} to be important and therefore
     * its value should not be lost when optimizing.
     */
    private static final TestTransConfiguration TEXT_ALIGN_IMPORTANT =
            new TestTransConfiguration();
    static {
        TEXT_ALIGN_IMPORTANT.addImportantStyleProperty(
                StylePropertyDetails.TEXT_ALIGN);
    }

    /**
     * StyleValueFactory to use to create StyleValue objects/
     */
    private static StyleValueFactory styleValueFactory =
            StyleValueFactory.getDefaultInstance();

    /**
     * Constant StyleValues which are used to populate the Styles while testing.
     */
    private static final StyleKeyword textAlign = TextAlignKeywords.CENTER;

    private static final StyleLength parentPadding =
            styleValueFactory.getLength(null, 10, LengthUnit.PX);

    private static final StyleValue topAlign = VerticalAlignKeywords.TOP;

    private static final StyleLength childPadding =
            styleValueFactory.getLength(null, 20, LengthUnit.PX);

    /**
     * Used when testing BasicMapperElement - same as would be returned by
     * {@link com.volantis.mcs.protocols.html.XHTMLBasicTransMapper#getValidDivAttributes}
     */
    private final String[] validDivAttributes = {"id", "class", "title",
                                                 "dir", "lang", "xml:lang"};
    private TransformationConfigurationMock configurationMock;

    protected void setUp() throws Exception {
        super.setUp();

        configurationMock = new TransformationConfigurationMock(
                "configurationMock", expectations);
    }

    /**
     * Simple method test.
     */
    public void testGetName() {
        BasicMapperElement me = new BasicMapperElement("table", null, false);

        assertEquals("mapper element name not as expected",
                     me.getName(),
                     "table");
    }

    /**
     * Simple method test.
     */
    public void testSetName() {
        BasicMapperElement me = new BasicMapperElement("table", null, false);

        me.setName("tr");

        assertEquals("mapper element name not as expected",
                     me.getName(),
                     "tr");
    }

    /**
     * Simple method test.
     */
    public void testGetSibling() {
        Element element = domFactory.createElement();
        BasicMapperElement me = new BasicMapperElement("table",
                                                       element,
                                                       false);

        assertSame("sibling returned should be the one passed in",
                   me.getSibling(),
                   element);
    }

    /**
     * Complex method test.
     */
    public void testGetSiblingInstance() {
        Element element = domFactory.createElement();
        Element elementResult;
        Text text = domFactory.createText();
        Text textResult;
        String textMessage = "atishoo, atishoo, we all fall down";
        BasicMapperElement me = new BasicMapperElement("tr", element, false);
        BasicMapperElement mt = new BasicMapperElement("td", text, true);
        Node result;
        char[] textResultMessage;

        element.setName("br");
        text.append(textMessage);

        result = me.getSiblingInstance();

        assertTrue("result should be an element",
                   (result instanceof Element));

        elementResult = (Element)result;

        assertTrue("sibling returned should not be the one passed in",
                   (element != elementResult));

        assertEquals("sibling instance should have same name",
                     elementResult.getName(),
                     element.getName());

        result = mt.getSiblingInstance();

        assertTrue("result should be a text node",
                   (result instanceof Text));

        textResult = (Text)result;

        assertTrue("sibling returned should not be the one passed in",
                   (text != textResult));

        assertEquals("sibling instance should have same content",
                     textResult.getLength(),
                     text.getLength());

        textResultMessage = textResult.getContents();

        for (int i = 0;
             i < textMessage.length();
             i++) {
          assertEquals("content different at index " + i,
                       textResultMessage[i],
                       textMessage.charAt(i));
        }
    }

    /**
     * Simple method test.
     */
    public void testSetSibling() {
        Element element = domFactory.createElement();
        Element other = domFactory.createElement();

        BasicMapperElement me = new BasicMapperElement("table",
                                                       element,
                                                       false);

        me.setSibling(other);

        assertTrue("sibling should have been replaced",
                   (me.getSibling() == other));
    }

    /**
     * Simple method test.
     */
    public void testIsOptimizeIfNoStyles() {
        Element element = domFactory.createElement();

        BasicMapperElement me = new BasicMapperElement("table", element, false);
        BasicMapperElement meToo = new BasicMapperElement("tr", element, true);

        assertTrue("constructed with false returned true",
                   !me.isOptimizeIfNoStyles());

        assertTrue("constructed with true returned false",
                   meToo.isOptimizeIfNoStyles());
    }

    /**
     * Simple method test.
     */
    public void testSetOptimizeIfNoStyles() {
        Element element = domFactory.createElement();

        BasicMapperElement me = new BasicMapperElement("table", element, false);

        me.setOptimizeIfNoStyles(true);

        assertTrue("setting failed", me.isOptimizeIfNoStyles());
    }

    /**
     * Verifies that if the element has no 'important' style properties set and
     * {@link BasicMapperElement#isOptimizeIfNoStyles()} returns:
     * <ol>
     * <li>true the element is optimized away</li>
     * <li>false the element is remapped</li>
     * </ol>
     * <p/>
     * NB: When the element is optimized away its Styles should be merged with
     * those of its childrens.
     */
    public void testRemapWithNoImportantStyles() {

        Element element = createTestElement("td", "div", false);

        configurationMock.expects.optimisationWouldLoseImportantStyles(element)
                .returns(false).any();

        // verify the element is optimized away if optimizeIfNoStyles is true
        BasicMapperElement me = new BasicMapperElement("div",
                validDivAttributes, null, true, configurationMock,
                DisplayKeywords.BLOCK);

        // check the retention of style information
        // styles should be propagated down to children.
        checkRemapOptimizesElementAway(element, me);

        element = createTestElement("td", "div", false);

        configurationMock.expects.optimisationWouldLoseImportantStyles(element)
                .returns(false).any();

        // verify the element is remapped if optimizeIfNoStyles is false
        me = new BasicMapperElement("div", validDivAttributes, null, false,
                NO_IMPORTANT_PROPERTIES,
                DisplayKeywords.BLOCK);
        // check the retention of style information
        // styles should be propagated down to children.
        checkRemapRemapsElementName(element, me);

    }

    /**
     * Verifies that if the element has 'important' style properties set (and
     * does not contain a single table child, or single child of the same name)
     * the element is remapped to a div regardless of what is returned by
     * {@link BasicMapperElement#isOptimizeIfNoStyles()}.
     */
    public void testRemapWithImportantStyles() {

        // verify the element is optimized if optimizeIfNoStyles is true and
        // all its important style properties are set to the same value on
        // its children.
        Element element = createTestElement("td", "div", false);

        configurationMock.expects.optimisationWouldLoseImportantStyles(element)
                .returns(true);

        BasicMapperElement me = new BasicMapperElement("div",
                validDivAttributes, null, true, configurationMock,
                DisplayKeywords.BLOCK);
        checkRemapRemapsElementName(element, me);

        // verify the element is remapped if optimizeIfNoStyles is true
        me = new BasicMapperElement("div", validDivAttributes, null, false,
                TEXT_ALIGN_IMPORTANT, DisplayKeywords.BLOCK);
        element = createTestElement("td", "div", false);
        checkRemapRemapsElementName(element, me);
    }

    /**
     * Verifies that if the element has 'important' style properties set (and
     * does not contain a single table child, or single child of the same name)
     * but that all of the important style properties are already set to the
     * same value on its children, then the element is optimized away if
     * {@link BasicMapperElement#isOptimizeIfNoStyles()} is true.
     */
    public void testRemapWithImportantStylesAlreadyOnChildren() {

        // verify the element is optimized away if optimizeIfNoStyles is true
        BasicMapperElement me = new BasicMapperElement("div",
                validDivAttributes, null, true, TEXT_ALIGN_IMPORTANT,
                DisplayKeywords.BLOCK);
        Element element = createTestElement("td", "div", true);
        checkRemapOptimizesElementAway(element, me);

        // verify the element is remapped if optimizeIfNoStyles is false
        me = new BasicMapperElement("div", validDivAttributes, null, false,
                TEXT_ALIGN_IMPORTANT, DisplayKeywords.BLOCK);
        element = createTestElement("td", "div", true);
        checkRemapRemapsElementName(element, me);
    }

    /**
     * Verifies that if the element has no 'important' style properties set and
     * has only a single table child it is optimized away regardless of the
     * value of {@link BasicMapperElement#isOptimizeIfNoStyles()}
     * <p/>
     * NB: When the element is optimized away its Styles should be merged with
     * those of its childrens.
     */
    public void testRemapWithNoImportantStylesAndSingleTableChild() {

        // verify the element is optimized away if optimizeIfNoStyles is true
        BasicMapperElement me = new BasicMapperElement("div", null, null,
                true, NO_IMPORTANT_PROPERTIES, DisplayKeywords.BLOCK);
        Element element = createTestElement("td", "table", false);
        checkRemapOptimizesElementAway(element, me);

        // verify the element is optimized away if optimizeIfNoStyles is false
        me = new BasicMapperElement("div", null, null, false,
                NO_IMPORTANT_PROPERTIES, DisplayKeywords.BLOCK);
        element = createTestElement("td", "table", false);
        checkRemapOptimizesElementAway(element, me);
    }

    /**
     * Verifies that if the element has only a single table child it is
     * optimized away regardless of whether it has 'important' property values
     * set or what {@link BasicMapperElement#isOptimizeIfNoStyles()} returns
     * <p/>
     * NB: When the element is optimized away its Styles should be merged with
     * those of its childrens.
     */
    public void testRemapWithImportantStylesAndSingleTableChild() {

        // verify the element is optimized away if optimizeIfNoStyles is true
        BasicMapperElement me = new BasicMapperElement("div", null, null,
                true, TEXT_ALIGN_IMPORTANT, DisplayKeywords.BLOCK);
        Element element = createTestElement("td", "table", false);
        checkRemapOptimizesElementAway(element, me);

        // verify the element is optimized away if optimizeIfNoStyles is false
        me = new BasicMapperElement("div", null, null, false,
                TEXT_ALIGN_IMPORTANT, DisplayKeywords.BLOCK);
        element = createTestElement("td", "table", false);
        checkRemapOptimizesElementAway(element, me);
    }

    /**
     * Verifies that if the element has only a single child of the same type
     * and has no 'important' style properties set, then it is optimized away
     * regardless of the value of {@link BasicMapperElement#isOptimizeIfNoStyles()}
     * <p/>
     * NB: When the element is optimized away its Styles should be merged with
     * those of its childrens.
     */
    public void testRemapWithNoImportantStylesAndSingleChildOfSameType() {

        // verify the element is optimized away if optimizeIfNoStyles is true
        BasicMapperElement me = new BasicMapperElement("div", null, null,
                true, NO_IMPORTANT_PROPERTIES, DisplayKeywords.BLOCK);
        Element element = createTestElement("div", "div", false);
        checkRemapOptimizesElementAway(element, me);

        // verify the element is optimized away if optimizeIfNoStyles is false
        me = new BasicMapperElement("div", null, null, false,
                NO_IMPORTANT_PROPERTIES, DisplayKeywords.BLOCK);
        element = createTestElement("div", "div", false);
        checkRemapOptimizesElementAway(element, me);
    }

    /**
     * Verifies that if the element has only a single child of the same type it
     * is optimized away regardless of whether it has 'important' property
     * values set or what {@link BasicMapperElement#isOptimizeIfNoStyles()}
     * returns.
     * <p/>
     * NB: When the element is optimized away its Styles should be merged with
     * those of its childrens.
     */
    public void testRemapWithImportantStylesAndSingleChildOfSameType() {

        // verify the element is optimized away if optimizeIfNoStyles is true
        BasicMapperElement me = new BasicMapperElement("div", null, null,
                true, TEXT_ALIGN_IMPORTANT, DisplayKeywords.BLOCK);
        Element element = createTestElement("div", "div", false);
        checkRemapOptimizesElementAway(element, me);

        // verify the element is optimized away if optimizeIfNoStyles is false
        me = new BasicMapperElement("div", null, null, false,
                TEXT_ALIGN_IMPORTANT, DisplayKeywords.BLOCK);
        element = createTestElement("div", "div", false);
        checkRemapOptimizesElementAway(element, me);
    }

    /**
     * Verifies that an element with no Styles and the optimizeIfNoClass
     * variable set to:
     * <ol>
     * <li>true is optimized away</li>
     * <li>false is remapped to div</li>.
     * </ol>
     */
    public void testRemapWithNullStyles() {

        // verify the element is optimized away if optimizeIfNoStyles is true
        BasicMapperElement me = new BasicMapperElement("div", null, null,
                true, TEXT_ALIGN_IMPORTANT, DisplayKeywords.BLOCK);
        Element element = createTestElement("td", "div", false);
        element.setStyles(null);
        checkRemapOptimizesElementAway(element, me);

        // verify the element is remapped if optimizeIfNoStyles is false
        me = new BasicMapperElement("div", null, null, false,
                TEXT_ALIGN_IMPORTANT, DisplayKeywords.BLOCK);
        element = createTestElement("td", "div", false);
        element.setStyles(null);
        checkRemapRemapsElementName(element, me);
    }

    /**
     * Verify that when the element should be remapped (and not optimized away)
     * any attributes that are not appropriate to the remapped element are
     * removed.
     */
    public void testRemapWithAttributes() {
        Element element = domFactory.createElement();

        BasicMapperElement me = new BasicMapperElement("div",
                validDivAttributes, null, false, NO_IMPORTANT_PROPERTIES,
                DisplayKeywords.BLOCK);

        element.setName("td");
        element.setAttribute("valign", "top");
        element.setAttribute("class", "myClass");
        element.setAttribute("id", "myID");
        element.setAttribute("colspan", "2");

        me.remap(element, XHTMLBasicElementHelper.getInstance());

        assertEquals("Element not mapped as", "div", element.getName());

        // Check the retention of attributes
        assertEquals("Element class not retained as", "myClass",
                element.getAttributeValue("class"));
        assertEquals("Element ID not retained as", "myID",
                element.getAttributeValue("id"));
        assertNull("Element valign not removed as expected",
                element.getAttributeValue("valign"));
        assertNull("Element colspan not removed as expected",
                element.getAttributeValue("colspan"));
    }

    /**
     * Check that the element is optimized away (i.e. name and {@link Styles}
     * are null) and that the style information on the element is merged with
     * that of its children.
     *
     * @param element the element to remap
     * @param me      the BasicMapperElement to use to remap the element.
     */
    private void checkRemapOptimizesElementAway(Element element,
            BasicMapperElement me) {

        // determine if the element had any Styles before remapping
        boolean nullStyles = element.getStyles() == null;

        me.remap(element, XHTMLBasicElementHelper.getInstance());

        assertNull("Element not optimized away", element.getName());

        assertNull("Element should have been optimised away",
                element.getName());
        assertNull("Styles should be null if element is optimized away",
                element.getStyles());

        assertTrue(element.getHead() instanceof Element);
        PropertyValues childPropertyValues =
                ((Element) element.getHead()).getStyles().getPropertyValues();
        assertEquals("Child styles should be preserved after the parent " +
                "optimized away", topAlign, childPropertyValues.
                getComputedValue(StylePropertyDetails.VERTICAL_ALIGN));

        assertEquals("Child style should 'win' if both parent and child have" +
                "a value set for a particular property", childPadding,
                childPropertyValues.getComputedValue(
                        StylePropertyDetails.PADDING_BOTTOM));
    }

    /**
     * Check that the element name is remapped to that specified in the
     * {@link BasicMapperElement} and that the style information is retained
     * unchanged.
     *
     * @param element the element to remap
     * @param me      the BasicMapperElement to use to remap the element.
     */
    private void checkRemapRemapsElementName(Element element,
            BasicMapperElement me) {

        // determine if the element had any Styles before remapping
        boolean nullStyles = (element.getStyles() == null);

        me.remap(element, XHTMLBasicElementHelper.getInstance());

        assertEquals("Element not mapped as", "div", element.getName());

        // verify that the Styles on the element are unchanged after remapping
        if (nullStyles) {
            assertNull("Styles should not be changed after remapping",
                    element.getStyles());
        } else {
            // check the retention of style information
            Styles stylesAfterRemap = element.getStyles();
            assertNotNull("Styles should not be null after remapping to div",
                    stylesAfterRemap);

            assertEquals("Styles should be preserved after remapping",
                    textAlign, stylesAfterRemap.getPropertyValues().
                    getComputedValue(StylePropertyDetails.TEXT_ALIGN));

            assertEquals("Styles should be preserved after remapping",
                    parentPadding, stylesAfterRemap.getPropertyValues().
                    getComputedValue(StylePropertyDetails.PADDING_BOTTOM));
        }
    }

    private Element createTestElement(String elementName, String childName,
                                      boolean importantMatches) {
        return createTestElement(elementName, childName,
                importantMatches ? textAlign : TextAlignKeywords.LEFT);
    }

    /**
     * Convenience method which creates an element with a single child, both of
     * which specify values for two style properties, one of which overlaps.
     *
     * @param elementName       name of the element to create
     * @param childName         name of the single child element
     * @return an element with a single child, both of which specify values for
     *         style properties, one of which overlaps.
     */
    private Element createTestElement(String elementName, String childName,
                                      StyleValue childTextAlign) {

        Element element = domFactory.createElement();
        element.setName(elementName);
        element.setAttribute("valign", "top");
        element.setAttribute("id", "myID");
        element.setAttribute("colspan", "2");

        String elementStyle = "text-align: " + textAlign + "; " +
            "padding-bottom: " + parentPadding;
        if ("td".equals(elementName)) {
            elementStyle += "; display: table-cell";
        }
        element.setStyles(StylesBuilder.getCompleteStyles(elementStyle, true));

        Element child = domFactory.createElement();
        child.setName(childName);

        final StringBuffer styles = new StringBuffer();
        styles.append("vertical-align: ").append(topAlign).append("; ");
        styles.append("padding-bottom: ").append(childPadding);

        // If both the parent and child should have the same value for the
        // important element, then add it.
        styles.append("; ").append("text-align: ").append(childTextAlign);

        child.setStyles(
                StylesBuilder.getCompleteStyles(styles.toString(), true));

        element.addHead(child);

        return element;
    }

    /**
     * Test class which allows us to configure the important style properties
     * on a per test basis.
     */
    static class TestTransConfiguration extends ProtocolConfigurationImpl {
        /**
         * Convenience method which allows the important style properties
         * to be configured on a per test basis.
          */
        public void addImportantStyleProperty(StyleProperty property) {
            importantProperties.add(property);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 14-Sep-05	9472/2	ibush	VBM:2005090808 Add default styling for sub/sup elements

 22-Aug-05	9223/4	emma	VBM:2005080403 Remove style class from within protocols and transformers

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 22-Jul-05	8859/2	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/
