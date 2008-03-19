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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.DOMFactoryMock;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.ElementMock;
import com.volantis.mcs.dom.TextMock;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMProtocolMock;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.ProtocolSupportFactoryMock;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueArray;
import com.volantis.mcs.themes.SparsePropertyValueArray;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.StylesMergerMock;
import com.volantis.styling.StylesMock;
import com.volantis.styling.StylingFactoryMock;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class TransTableTestAbstract extends TestCaseAbstract {

    /**
     * Mock elements which are used to create a mock nested table for testing.
     * Will be initialised after createTestableNestedTransTable has been
     * called.
     */
    protected ElementMock outerTable;
    protected ElementMock outerRow;
    protected ElementMock outerCell;
    protected ElementMock innerTable;
    protected ElementMock innerRow;
    protected ElementMock innerCell;
    protected TextMock text;

    protected DOMProtocolMock protocol;

    protected TransTable innerTransTable;
    protected TransCell outerTransCell;

    /**
     * Constants which aid testing.
     */
    protected final HashMap emptyExpectedValues = new HashMap();

    protected static final int INNER = 1;
    protected static final int CELL = 2;
    protected static final int OUTER = 4;

    StyleLength nonNullStyleValue =
        STYLE_VALUE_FACTORY.getLength(null, 5, LengthUnit.PX);
    String nonNullStringValue = "5px";

    final String innerCellClass = "ic";
    final String innerRowClass = "ir";
    final String innerTableClass = "it";
    final String outerCellClass = "oc";
    final String outerRowClass = "or";
    final String outerTableClass = "ot";
    final String hspace = "10";
    final char[] contents = {'C', 'e', 'l', 'l'};
    final String tabIndex = "2";

    /**
     * The constant DOM element attribute names whose values are checked by
     * the various TransTable implementations of canOptimizeStyle.
     */
    public static final String ROWSPAN = "rowspan";
    public static final String COLSPAN = "colspan";
    public static final String STYLES = "styles";
    public static final String HEIGHT = "height";
    public static final String WIDTH = "width";
    public static final String BORDER = "border";
    public static final String CELLSPACING = "cellspacing";
    public static final String CELLPADDING = "cellpadding";
    public static final String SUMMARY = "summary";
    public static final String ID = "id";
    public static final String DIR = "dir";
    public static final String FRAME = "frame";
    public static final String LANG = "lang";
    public static final String RULES = "rules";
    public static final String STYLE = "style";
    public static final String TITLE = "title";
    public static final String COLWIDTHS = "colwidths";
    public static final String BGIMAGE = "bgimage";
    public static final String TABINDEX = "tabindex";
    public static final String ROWGAP = "rowgap";
    public static final String COLGAP = "colgap";
    public static final String UPDATE = "update";
    public static final String ONCLICK = "onclick";
    public static final String ONDBLCLICK = "ondblclick";
    public static final String ONKEYDOWN = "onkeydown";
    public static final String ONKEYPRESS = "onkeypress";
    public static final String ONKEYUP = "onkeyup";
    public static final String ONMOUSEDOWN = "onmousedown";
    public static final String ONMOUSEMOVE = "onmousemove";
    public static final String ONMOUSEOUT = "onmouseout";
    public static final String ONMOUSEOVER = "onmouseover";
    public static final String ONMOUSEUP = "onmouseup";

    public static final String[] EVENT_ATTS =
            {ONCLICK, ONDBLCLICK, ONKEYDOWN, ONKEYPRESS, ONKEYUP, ONMOUSEDOWN,
             ONMOUSEMOVE, ONMOUSEOUT, ONMOUSEOVER, ONMOUSEUP};

    private static final String[] XHTML_TABLE_ATTS = {
        BORDER, CELLPADDING, CELLSPACING, DIR, FRAME, LANG, RULES, STYLE};

    public static final String[] layoutAttributes = {

    };
    private static final StyleValueFactory STYLE_VALUE_FACTORY = StyleValueFactory.getDefaultInstance();

    /**
     * Returns the list of style attributes which should be preserved when
     * merging tables.
     *
     * @return string array of attribute names which should be preserved when
     *         merging tables.
     */
    protected abstract String[] getExpectedPreserveStyleAttributes();

    /**
     * This should be implemented by sub classes to provide a specific
     * TransTable implementation.
     * <p>
     * NOTE: for now these are always fixed to
     *
     * @param table             to be represented by the returned TransTable
     * @param protocol          protocol to use. If null, a default will be
     *                          provided.
     * @return a new TransTable instance
     */
    public abstract TransTable createTransTable(Element table,
            DOMProtocol protocol);

    /**
     * Convenience method which sets the expectations for the various
     * attributes and styles that are checked to see whether the table can be
     * optimized away.
     *
     * @param expectedValues    the expected values to use to set the
     *                          expectations
     * @param element           element on which the expectations should be
     *                          set. This should be one of the inner table,
     *                          it's containing cell or the outer table class
     */
    protected void setCanOptimizeStyleExpectations(HashMap expectedValues,
                                                   int element) {
        if (element == INNER) {
            setInnerTableCanOptimizeExpectations(expectedValues);
            setOuterCellCanOptimizeExpectations(emptyExpectedValues);
            setOuterTableCanOptimizeExpectations(emptyExpectedValues);
        } else if (element == CELL) {
            setInnerTableCanOptimizeExpectations(emptyExpectedValues);
            setOuterCellCanOptimizeExpectations(expectedValues);
            setOuterTableCanOptimizeExpectations(emptyExpectedValues);
        } else if (element == OUTER) {
            setInnerTableCanOptimizeExpectations(emptyExpectedValues);
            setOuterCellCanOptimizeExpectations(emptyExpectedValues);
            setOuterTableCanOptimizeExpectations(expectedValues);
        } else {
            throw new UnsupportedOperationException("Expectations for the " +
                    "method canOptimizeStyle cannot be set on this element: " +
                    element);
        }
    }

    /**
     * Set expectations which apply to both XHTMLFull and XHTMLTransitional
     * protocols, and which can be applied to both the inner and outer tables.
     *
     * @param expectedValues    values to use to set the expectations
     * @param table             the table on which to set the expectations
     */
    protected void setXHTMLTableExpectations(HashMap expectedValues,
                                             ElementMock table) {

        for (int i = 0; i < XHTML_TABLE_ATTS.length; i++) {
            table.expects.getAttributeValue(XHTML_TABLE_ATTS[i]).
                    returns((String) expectedValues.get(XHTML_TABLE_ATTS[i]))
                    .optional();
        }
        for (int i = 0; i < EVENT_ATTS.length; i++) {
            table.expects.getAttributeValue(EVENT_ATTS[i]).
                    returns((String) expectedValues.get(EVENT_ATTS[i]))
                    .optional();
        }
    }

    /**
     * Convenience method to set the expectations on the inner of the nested
     * tables.
     * @param expectedValues    values to use to set the expectations
     */
    protected void setInnerTableCanOptimizeExpectations(
            HashMap expectedValues) {
        // this expectation is optional because WapTV5TransTable checks it at a
        // different point to the others, so it may not always be called.
        innerTable.expects.getAttributeValue(WIDTH).
                returns((String) expectedValues.get(WIDTH)).optional();
        innerTable.expects.getStyles().
                returns(expectedValues.get(STYLES)).any();
    }
    /**
     * Convenience method to set the expectations on the cell which contains
     * the inner of the nested tables.
     *
     * @param expectedValues values to use to set the expectations
     */
    protected void setOuterCellCanOptimizeExpectations(HashMap expectedValues) {
        // set up outer cell expectations
        outerCell.expects.getAttributeValue(ROWSPAN).
                returns((String) expectedValues.get(ROWSPAN)).atLeast(1);
        outerCell.expects.getAttributeValue(COLSPAN).
                returns((String) expectedValues.get(COLSPAN)).atLeast(1);
        outerCell.expects.getStyles().returns(expectedValues.get(STYLES)).any();
    }

    /**
     * Convenience method to set the expectations on the outer of the nested
     * tables.
     *
     * @param expectedValues values to use to set the expectations
     */
    protected void setOuterTableCanOptimizeExpectations(
            HashMap expectedValues) {
        outerTable.expects.getStyles().
                returns(expectedValues.get(STYLES)).any();
    }

    /**
     * Convenience method which recreates the expectations and table mock
     * elements which are used for testing. This allows multiple tests to be
     * run in one test method.
     */
    protected void resetExpectations() {
        expectations = mockFactory.createUnorderedBuilder();
        protocol = createTestableProtocol();
        innerTransTable = createTestableNestedTransTable();
        outerTransCell = (TransCell)innerTransTable.getParent();
    }

    /**
     * Convenience method which returns an initialised DOMProtocolMock.
     *
     * @return a testable DOM protocol mock
     */
    protected DOMProtocolMock createTestableProtocol() {
        // ===================================================================
        //   Create Mocks
        // ===================================================================
        ProtocolSupportFactoryMock psfMock =
                new ProtocolSupportFactoryMock("psfMock", expectations);
        ProtocolConfigurationMock protocolConfigMock =
                new ProtocolConfigurationMock("protocolConfigMock",
                        expectations);
        DOMFactoryMock domFactoryMock =
                new DOMFactoryMock("domFactoryMock", expectations);
        ProtocolConfigurationMock configuration =
                new ProtocolConfigurationMock("configuration",
                        expectations);

        // ===================================================================
        //   Set Expectations
        // ===================================================================
        psfMock.expects.getDOMFactory().returns(domFactoryMock);
        protocolConfigMock.expects.getValidationHelper().returns(null).any();

        DOMProtocolMock protocol = new DOMProtocolMock(
                "protocol", expectations, psfMock, protocolConfigMock);

        protocol.expects.getProtocolConfiguration().returns(configuration).any();
        configuration.fuzzy.optimisationWouldLoseImportantStyles(
                mockFactory.expectsInstanceOf(Element.class))
                .returns(true).any();

        return protocol;
    }

    /**
     * Convenience method which creates a two level nested table structure (as
     * shown below) and the TransTable structure which corresponds to it.

     * The table is:
     *  <table>
     *      <tr>
     *          <td>
     *              <table>
     *                  <tr>
     *                      <td>Cell</td>
     *                  </tr>
     *              </table>
     *          </td>
     *      </tr>
     *  </table>
     *
     * @return a testable nested TransTable
     */
    protected TransTable createTestableNestedTransTable() {

        createTestableNestedTable();

        // Set up expectations for TransTable.determineOptimizationLevel()
        // Using LITTLE_IMPACT forces canOptimize to invoke canOptimizeStyle
        protocol.expects.supportsNestedTables().returns(true).any();
        outerTable.expects.getAttributeValue(
                OptimizationConstants.OPTIMIZATION_ATTRIBUTE).returns(
                        OptimizationConstants.OPTIMIZE_LITTLE_IMPACT);
        innerTable.expects.getAttributeValue(
                OptimizationConstants.OPTIMIZATION_ATTRIBUTE).returns(
                        OptimizationConstants.OPTIMIZE_LITTLE_IMPACT);
        outerTable.expects.removeAttribute(
                OptimizationConstants.OPTIMIZATION_ATTRIBUTE);
        innerTable.expects.removeAttribute(
                OptimizationConstants.OPTIMIZATION_ATTRIBUTE);

        TransTable outerTransTable = createTransTable(outerTable, protocol);
        outerTransCell = outerTransTable.getFactory().getCell(outerRow,
                outerCell, 0, 0, protocol);
        outerTransCell.setParent(outerTransTable);
        TransTable innerTransTable = createTransTable(innerTable, protocol);
        innerTransTable.setParent(outerTransCell);

        return innerTransTable;
    }

    private ElementMock createTestableNestedTable() {

        // ===================================================================
        //   Create Mocks
        // ===================================================================
        outerTable = new ElementMock("outerTable", expectations);
        outerRow = new ElementMock("outerRow", expectations);
        outerCell = new ElementMock("outerCell", expectations);
        innerTable = new ElementMock("innerTable", expectations);
        innerRow = new ElementMock("innerRow", expectations);
        innerCell = new ElementMock("innerCell", expectations);
        text = new TextMock("text", expectations);

        // ===================================================================
        //   Set Expectations
        // ===================================================================
        outerTable.expects.getName().returns("table").optional();
        outerRow.expects.getName().returns("tr").optional();
        outerCell.expects.getName().returns("td").optional();
        innerTable.expects.getName().returns("table").optional();

        innerRow.expects.getName().returns("tr").optional();

        innerCell.expects.getName().returns("td").optional();
        text.expects.getContents().returns(contents).optional();

        innerCell.expects.getParent().returns(innerRow).any();
        innerRow.expects.getParent().returns(innerTable).any();
        innerTable.expects.getParent().returns(outerCell).any();
        outerCell.expects.getParent().returns(outerRow).any();
        outerRow.expects.getParent().returns(outerTable).any();

        outerTable.expects.getHead().returns(outerRow).any();
        outerRow.expects.getHead().returns(outerCell).any();
        outerCell.expects.getHead().returns(innerTable).any();
        innerTable.expects.getHead().returns(innerRow).any();
        innerRow.expects.getHead().returns(innerCell).any();

        return innerCell;
    }

    protected MutablePropertyValuesMock createTestablePropertyValues(
            StyleProperty property, StyleValue value) {

        MutablePropertyValuesMock values =
                new MutablePropertyValuesMock("values", expectations);

        StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();
        for (Iterator i = definitions.stylePropertyIterator(); i.hasNext();) {
            StyleProperty prop = (StyleProperty)i.next();
            if (!property.equals(prop) ){
                values.expects.getComputedValue(prop).
                        returns(null).any();
            } else {
                values.expects.getComputedValue(property).returns(value).any();
            }
        }
        return values;
    }

    protected MutablePropertyValuesMock createTestablePropertyValues(
            PropertyValueArray array) {

        MutablePropertyValuesMock values =
                new MutablePropertyValuesMock("values", expectations);

        StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();
        for (Iterator i = definitions.stylePropertyIterator(); i.hasNext();) {
            StyleProperty prop = (StyleProperty)i.next();
            PropertyValue propertyValue = null;
            if (array != null) {
                propertyValue = array.getPropertyValue(prop);
            }
            if (propertyValue == null) {
                values.expects.getComputedValue(prop).
                        returns(null).any();
            } else {
                StyleValue value = propertyValue.getValue();
                values.expects.getComputedValue(prop).returns(value).any();
            }
        }
        return values;
    }

    /**
     * Verify that the list of style attributes that should be preserved
     * matches the one specified.
     */
    public void testGetPreserveStyleAttributes() throws Exception {

        // ===================================================================
        //   Create Mocks
        // ===================================================================
        resetExpectations();
        ElementMock table = new ElementMock("table", expectations);

        // ===================================================================
        //   Set Expectations
        // ===================================================================
        table.expects.getAttributeValue(
                OptimizationConstants.OPTIMIZATION_ATTRIBUTE).
                returns(null).optional();

        // ===================================================================
        //   Test Expectations
        // ===================================================================

        // Don't expect to find class as this is handled separately and don't
        // expect to find id, colspan, rowspan, width, height and tabindex
        // as these should not be handled at all
        String result = findMismatches(getExpectedPreserveStyleAttributes(),
                innerTransTable.getPreserveStyleAttributes());

        assertTrue("Preserve style attribute mismatches: " + result,
                (result == null));
    }

    /**
     * Verifies that tables can be optimized away (when the optimization level
     * is LITTLE_IMPACT) if the row and colspans of the inner and outer tables
     * are null or the default of one, and not for any other value.
     */
    public void testCanOptimizeStyleWhenRowAndColSpanAreZeroOrOne() {
        checkCanOptimizeWithParticularAttributeValues(ROWSPAN, "1", "10", CELL);
        checkCanOptimizeWithParticularAttributeValues(COLSPAN, "1", "10", CELL);
    }

    /**
     * Verifies that tables can only be optimized away (when the optimization
     * level is LITTLE_IMPACT) if the borders of both tables are either null
     * or zero.
     */
    public void doTestCanOptimizeStyleWhenBorderIsZeroOrNull() {

        // ===================================================================
        //   Test when setting attribute values
        // ===================================================================
        checkCanOptimizeWithParticularAttributeValues(BORDER, "0", "10", INNER);
        checkCanOptimizeWithParticularAttributeValues(BORDER, "0", "10", INNER);

        StyleLength length0 =
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX);
        StyleLength length10 =
            STYLE_VALUE_FACTORY.getLength(null, 10, LengthUnit.PX);

        // ===================================================================
        //   Test when setting Styles values
        // ===================================================================

        for (int i = 0; i < PropertyGroups.EDGE_COUNT; i += 1) {
            StyleProperty styleProperty = PropertyGroups.BORDER_STYLE_PROPERTIES[i];
            StyleProperty widthProperty = PropertyGroups.BORDER_WIDTH_PROPERTIES[i];

            PropertyValueArray optimiseValues;

            optimiseValues = new SparsePropertyValueArray();
            optimiseValues.setStyleValue(styleProperty, BorderStyleKeywords.NONE);
            optimiseValues.setStyleValue(widthProperty, length10);

            PropertyValueArray noOptValues = new SparsePropertyValueArray();
            noOptValues.setStyleValue(styleProperty, BorderStyleKeywords.SOLID);
            noOptValues.setStyleValue(widthProperty, length10);

            checkCanOptimizeWithParticularPropertyValues(
                    optimiseValues,
                    noOptValues,
                    STYLES, INNER);

            optimiseValues = new SparsePropertyValueArray();
            optimiseValues.setStyleValue(styleProperty, BorderStyleKeywords.SOLID);
            optimiseValues.setStyleValue(widthProperty, length0);

            checkCanOptimizeWithParticularPropertyValues(
                    optimiseValues,
                    noOptValues,
                    STYLES, OUTER);
        }
    }

    /**
     * Verifies that the attributes which should be preserved during table
     * flattening are correctly preserved. The table used during testing is
     * that returned by {@link #createTestableNestedTable()}.
     *
     * @throws Throwable if there was a problem running the test
     */
    public void testPreserveStyleAttributes() throws Throwable {
        // ===================================================================
        //   Create test objects
        // ===================================================================
        resetExpectations();

        // ===================================================================
        //   Initialise test objects and set expectations
        // ===================================================================
        ElementMock cell = new ElementMock("cell", expectations);
        ElementMock ancestor = new ElementMock("ancestor", expectations);

        String[] attributes = getExpectedPreserveStyleAttributes();
        final String cellValue = "cell";
        final String ancestorValue = "ancestor";

        // ===================================================================
        //   Set expectations
        // ===================================================================
        assertTrue(attributes.length >= 3);

        cell.expects.getAttributeValue(attributes[0]).returns(null);
        cell.expects.getAttributeValue(attributes[1]).returns(null);
        cell.expects.getAttributeValue(attributes[2]).returns(cellValue);

        ancestor.expects.getAttributeValue(attributes[0]).returns(null);
        ancestor.expects.getAttributeValue(attributes[1]).returns(ancestorValue);

        for (int i = 3; i < attributes.length; i++) {
            cell.expects.getAttributeValue(attributes[i]).returns(null);
            ancestor.expects.getAttributeValue(attributes[i]).returns(null);
        }

        cell.expects.setAttribute(attributes[1], ancestorValue);

        // ===================================================================
        //   Test behaviour.
        // ===================================================================

        Class[] paramTypes = {Element.class, Element.class, String[].class};
        Object[] params = {cell, ancestor, attributes};
        PrivateAccessor.invoke(innerTransTable, "preserveStyleAttributes",
                paramTypes, params);
    }

    private void setAttributeExpectations(String attribute) {
        outerCell.expects.getAttributeValue(attribute).
                returns(outerCellClass).any();
        innerTable.expects.getAttributeValue(attribute).
                returns(innerTableClass).any();
        innerRow.expects.getAttributeValue(attribute).
                returns(innerRowClass).any();
        innerCell.expects.getAttributeValue(attribute).
                returns(innerCellClass).any();

        outerCell.fuzzy.setAttribute(attribute,
                mockFactory.expectsInstanceOf(String.class)).returns().any();
        innerTable.fuzzy.setAttribute(attribute,
                mockFactory.expectsInstanceOf(String.class)).returns().any();
        innerRow.fuzzy.setAttribute(attribute,
                mockFactory.expectsInstanceOf(String.class)).returns().any();
        innerCell.fuzzy.setAttribute(attribute,
                mockFactory.expectsInstanceOf(String.class)).returns().any();
    }

    /**
     * Verifies that Styles are correctly preserved when flattening tables. The
     * table used during testing is that returned by
     * {@link #createTestableNestedTable()}.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testPreserveStyle() throws Exception {
        // ===================================================================
        //   Create test objects
        // ===================================================================
        resetExpectations();

        StylingFactoryMock stylingFactory =
                new StylingFactoryMock("stylingFactory", expectations);
        StylesMergerMock mergerMock =
                new StylesMergerMock("mergerMock2", expectations);
        StylesMock cellStyles =
                new StylesMock("cellStyles", expectations);
        StylesMock rowStyles =
                new StylesMock("rowStyles", expectations);
        StylesMock tableStyles =
                new StylesMock("tableStyles", expectations);
        StylesMock originalCellStyles =
                new StylesMock("originalCellStyles", expectations);
        StylesMock mergedStyles =
                new StylesMock("mergedStyles", expectations);

        MutablePropertyValuesMock propertyValues =
                new MutablePropertyValuesMock(
                        "mergedPropertyValues", expectations);
        // ===================================================================
        //   Set expectations.
        // ===================================================================
        String[] attributes = getExpectedPreserveStyleAttributes();
        for (int i = 0; i < attributes.length; i++) {
            setAttributeExpectations(attributes[i]);
        }
        setAttributeExpectations("class");
        mergedStyles.expects.getPropertyValues().returns(propertyValues).any();

        propertyValues.fuzzy.getComputedValue(
                mockFactory.expectsAny()).returns(null).any();

        // Make sure that it sets the display.
        propertyValues.fuzzy
                .setComputedValue(StylePropertyDetails.DISPLAY,
                        mockFactory.expectsAny()).returns().optional();

        innerCell.expects.getStyles().returns(cellStyles).max(2);
        innerRow.expects.getStyles().returns(rowStyles);
        innerTable.expects.getStyles().returns(tableStyles);
        outerCell.expects.getStyles().returns(originalCellStyles);

        stylingFactory.expects.getStylesMerger().returns(mergerMock);
        mergerMock.expects.merge(cellStyles, rowStyles).returns(mergedStyles);
        mergerMock.expects.merge(mergedStyles, tableStyles).returns(mergedStyles);
        mergerMock.expects.merge(mergedStyles, originalCellStyles).
                returns(mergedStyles);


        innerCell.expects.setStyles(mergedStyles);
        innerCell.fuzzy.setAttribute("class",
                mockFactory.expectsInstanceOf(String.class)).returns().optional();

        // ===================================================================
        //   Test behaviour.
        // ===================================================================
        innerTransTable.preserveStyle(innerCell, outerRow, outerCell,
                stylingFactory);
    }

    /**
     * Verifies that tables cannot be optimized away (when the optimization
     * level is LITTLE_IMPACT) if certain key attributes are set at all.
     *
     * @param attribute   the name of the attribute which should cause
     *                  optimization to fail if non null.
     * @param element   index which specifies which of the elements in the
     *                  nested table test hierarchy to test with a non null
     *                  value.
     */
    protected void checkCanOptimizeStyleIfKeyAttributesNonNull(
            String attribute, int element) {

        resetExpectations();
        assertFalse("Unexpectedly could optimize style with " + attribute +
                "set to a non null value",
                doTestCanOptimizeStyleWithParticularValue(
                        attribute, nonNullStringValue, element));
    }

    /**
     * Verifies that a table can be optimized away if the specified attribute
     * is null or set to a particular value, and that it can't for another
     * value.
     *
     * @param keyName           the name of the attribute that will stop the
     *                          table being optimized if not set to a
     *                          particular value
     * @param optimizeValue     the value which should allow oXHTMLptimization
     * @param noOptValue        a value which should stop optimization
     * @param element           the element on which to set the expectation
     */
    protected void checkCanOptimizeWithParticularAttributeValues(
            String keyName, String optimizeValue, String noOptValue,
            int element) {

        // ===================================================================
        //   Set and Test Expectations
        // ===================================================================

        resetExpectations();
        assertFalse("Unexpectedly could optimize style with " + keyName +
                " = " + noOptValue,
                doTestCanOptimizeStyleWithParticularValue(
                        keyName, noOptValue, element));

        resetExpectations();
        assertTrue("Unexpectedly couldn't optimize style with " + keyName +
                " = " + optimizeValue,
                doTestCanOptimizeStyleWithParticularValue(
                        keyName, optimizeValue, element));

        resetExpectations();
        assertTrue("Unexpectedly couldn't optimize style with " + keyName +
                " = null",
                doTestCanOptimizeStyleWithParticularValue(
                        keyName, null, element));

        resetExpectations();
    }

    /**
     * Verifies that a table can be optimized away if the specified
     * StyleProperty is null or set to a particular value, and that it can't
     * be optimized away if set to any other value.
     *
     * @param property      the StyleProperty that will stop the table
     *                      being optimized if not set to a particular value
     * @param optimizeValue the value which should allow optimization
     * @param noOptValue    a value which should stop optimization
     * @param keyName       the key used to identify the property in the Map
     *                      of expectations
     * @param element       the element on which to set the expectations
     */
    public void checkCanOptimizeWithParticularPropertyValues(
            StyleProperty property, StyleValue optimizeValue,
            StyleValue noOptValue, String keyName, int element) {

        // ===================================================================
        //   Create Mocks
        // ===================================================================
        StylesMock styles = new StylesMock("styles", expectations);
        MutablePropertyValuesMock values;

        // ===================================================================
        //   Set and Test Expectations
        // ===================================================================

        resetExpectations();
        values = createTestablePropertyValues(property, noOptValue);
        styles.expects.getPropertyValues().returns(values);
        assertFalse("Unexpectedly could optimize style with " + property +
                " = " + noOptValue,
                doTestCanOptimizeStyleWithParticularValue(
                        keyName, styles, element));

        resetExpectations();
        values = createTestablePropertyValues(property, optimizeValue);
        styles.expects.getPropertyValues().returns(values);
        assertTrue("Unexpectedly couldn't optimize style with " + property +
                " = " + optimizeValue,
                doTestCanOptimizeStyleWithParticularValue(
                        keyName, styles, element));

        resetExpectations();
        values = createTestablePropertyValues(property, null);
        styles.expects.getPropertyValues().returns(values);
        assertTrue("Unexpectedly couldn't optimize style with " + property +
                " = null",
                doTestCanOptimizeStyleWithParticularValue(
                        keyName, styles, element));
        resetExpectations();
    }

    /**
     * Verifies that a table can be optimized away if the specified
     * StyleProperty is null or set to a particular value, and that it can't
     * be optimized away if set to any other value.
     *
     * @param optimizeValues the values which should allow optimization
     * @param noOptValues    the values which should stop optimization
     * @param keyName       the key used to identify the property in the Map
     *                      of expectations
     * @param element       the element on which to set the expectations
     */
    public void checkCanOptimizeWithParticularPropertyValues(
            PropertyValueArray optimizeValues, PropertyValueArray noOptValues,
            String keyName, int element) {

        // ===================================================================
        //   Create Mocks
        // ===================================================================
        StylesMock styles = new StylesMock("styles", expectations);
        MutablePropertyValuesMock values;

        // ===================================================================
        //   Set and Test Expectations
        // ===================================================================

        resetExpectations();
        values = createTestablePropertyValues(noOptValues);
        styles.expects.getPropertyValues().returns(values);
        assertFalse("Unexpectedly could optimize style with " + noOptValues,
                doTestCanOptimizeStyleWithParticularValue(
                        keyName, styles, element));

        resetExpectations();
        values = createTestablePropertyValues(optimizeValues);
        styles.expects.getPropertyValues().returns(values);
        assertTrue("Unexpectedly couldn't optimize style with " + optimizeValues,
                doTestCanOptimizeStyleWithParticularValue(
                        keyName, styles, element));

        resetExpectations();
        values = createTestablePropertyValues(null);
        styles.expects.getPropertyValues().returns(values);
        assertTrue("Unexpectedly couldn't optimize style with null",
                doTestCanOptimizeStyleWithParticularValue(
                        keyName, styles, element));
        resetExpectations();
    }

    /**
     * Try to optimize a table with one attribute or property set to a
     * particular value.
     *
     * @param keyName       the key used to identify the property in the Map
     * @param expectedValue the value to be returned when the property is
     *                      requested
     * @param element       the element on which to set the expectations
     * @return true if the optimization was successful, false otherwise
     */
    public boolean doTestCanOptimizeStyleWithParticularValue(
            String keyName, Object expectedValue, int element) {
        // ===================================================================
        //   Set Expectations
        // ===================================================================
        HashMap expectedValues = new HashMap();
        expectedValues.put(keyName, expectedValue);
        setCanOptimizeStyleExpectations(expectedValues, element);

        // ===================================================================
        //   Test Expectations
        // ===================================================================
        return innerTransTable.canOptimize(outerTransCell);
    }

    /**
     * Verify that optimizing a table with non matching values for the same
     * property in both the inner and outer tables is not successful.
     *
     * @param keyName   the key used to identify the property in the Map
     */
    public void checkCannotOptimizeStyleWithNonMatchingValues(String keyName) {

        // ===================================================================
        //   Set Expectations
        // ===================================================================
        HashMap expectedValues = new HashMap();
        resetExpectations();
        setOuterCellCanOptimizeExpectations(expectedValues);
        expectedValues.put(keyName, "1");
        setOuterTableCanOptimizeExpectations(expectedValues);
        expectedValues.put(keyName, "4");
        setInnerTableCanOptimizeExpectations(expectedValues);

        // ===================================================================
        //   Test Expectations
        // ===================================================================
        assertFalse("Unexpectedly could optimize styles with non matching " +
                "values for" + keyName,
                innerTransTable.canOptimize(outerTransCell));
    }

    /**
     * Verify that optimizing a table with matching values for the same
     * property in both the inner and outer tables is successful.
     *
     * @param keyName   the key used to identify the property in the Map
     */
    public void checkCanOptimizeStyleWithMatchingValues(String keyName) {

        // ===================================================================
        //   Set Expectations
        // ===================================================================
        HashMap expectedValues = new HashMap();
        resetExpectations();
        setOuterCellCanOptimizeExpectations(expectedValues);
        expectedValues.put(keyName, "1");
        setInnerTableCanOptimizeExpectations(expectedValues);
        setOuterTableCanOptimizeExpectations(expectedValues);

        // ===================================================================
        //   Test Expectations
        // ===================================================================
        assertTrue("Unexpectedly couldn't optimize styles with matching " +
                "values for " + keyName,
                innerTransTable.canOptimize(outerTransCell));
    }

    /**
     * Verifies that a table cannot be optimized away if the specified
     * StyleProperty is set to different values in the inner and outer nested
     * tables (and all other property values are null).
     *
     * @param property      the StyleProperty to test
     */
    public void checkCannotOptimizeStyleWithNonMatchingPropertyValues(
            StyleProperty property) {

        // ===================================================================
        //   Create Mocks
        // ===================================================================
        StylesMock styles = new StylesMock("styles", expectations);
        HashMap expectedValues = new HashMap();

        // ===================================================================
        //   Set Expectations
        // ===================================================================
        resetExpectations();
        setOuterCellCanOptimizeExpectations(expectedValues);
        setInnerTableCanOptimizeExpectations(expectedValues);
        MutablePropertyValues values =
                createTestablePropertyValues(property, nonNullStyleValue);
        styles.expects.getPropertyValues().returns(values);
        expectedValues.put("styles", styles);
        setOuterTableCanOptimizeExpectations(expectedValues);

        // ===================================================================
        //   Test Expectations
        // ===================================================================
        assertFalse("Unexpectedly could optimize styles with non matching " +
                "values for " + property,
                innerTransTable.canOptimize(outerTransCell));
    }

    /**
     * Verifies that a table can be optimized away if the specified
     * StyleProperty is set to matching values in the inner and outer nested
     * tables (and all other property values are null).
     *
     * @param property the StyleProperty to test
     */
    public void checkCanOptimizeStyleWithMatchingPropertyValues(
            StyleProperty property) {

        // ===================================================================
        //   Create Mocks
        // ===================================================================
        StylesMock styles = new StylesMock("styles", expectations);
        HashMap expectedValues = new HashMap();

        // ===================================================================
        //   Set Expectations
        // ===================================================================
        resetExpectations();
        setOuterCellCanOptimizeExpectations(expectedValues);
        setInnerTableCanOptimizeExpectations(expectedValues);
        MutablePropertyValues values =
                createTestablePropertyValues(property, nonNullStyleValue);
        styles.expects.getPropertyValues().returns(values);
        expectedValues.put("styles", styles);
        setOuterTableCanOptimizeExpectations(expectedValues);

        // ===================================================================
        //   Test Expectations
        // ===================================================================
        assertFalse("Unexpectedly could optimize styles with non matching " +
                "values for " + property,
                innerTransTable.canOptimize(outerTransCell));
    }

    /**
     * Checks that the two arrays contain the same attribute names. If they
     * do then null is returned. If they do not, a message is generated and
     * returned detailing the mismatches.
     *
     * @param expected
     * @param actual
     * @return a message detailing mismatches or null if there are none
     * @todo This method is a direct copy of one in
     * {@link  com.volantis.mcs.protocols.Utils}. This was necessary because
     * there is currently no common area for test code, and it is not visible
     * from this class. When there is a common area, Utils should be moved
     * there and this code should be removed.
     */
    static public String findMismatches(String[] expected,
                                        String[] actual) {
        String result = null;
        Set expectedSet = new HashSet(expected.length);
        Set actualSet = new HashSet(actual.length);

        populate(expectedSet, expected);
        populate(actualSet, actual);

        if (!expectedSet.equals(actualSet)) {
            String expectedsMissing = null;
            String actualsMissing = null;

            for (int i = 0;
                 i < expected.length;
                 i++) {
                if (!actualSet.contains(expected[i])) {
                    if (expectedsMissing == null) {
                        expectedsMissing = expected[i];
                    } else {
                        expectedsMissing += ", " + expected[i];
                    }
                }
            }

            for (int i = 0;
                 i < actual.length;
                 i++) {
                if (!expectedSet.contains(actual[i])) {
                    if (actualsMissing == null) {
                        actualsMissing = actual[i];
                    } else {
                        actualsMissing += ", " + actual[i];
                    }
                }
            }

            if (expectedsMissing != null) {
                result = "expected to find \"" + expectedsMissing + "\" but " +
                        "did not";
            }

            if (actualsMissing != null) {
                if (result != null) {
                    result += "; ";
                } else {
                    result = "";
                }

                result += "found unexpected \"" + actualsMissing + "\"";
            }
        }

        return result;
    }

    /**
     * Populates the given set from the given array of values.
     *
     * @param set
     * @param values
     * @todo This method is a direct copy of one in
     * {@link  com.volantis.mcs.protocols.Utils}. This was necessary because
     * there is currently no common area for test code, and it is not visible
     * from this class. When there is a common area, Utils should be moved
     * there and this code should be removed.
     */
    static public void populate(Set set,
                                String[] values) {
        for (int i = 0;
             i < values.length;
             i++) {
            set.add(values[i]);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10743/1	ianw	VBM:2005113021 Fixed up promotion of table's width 100% to set value to auto

 09-Dec-05	10719/11	ianw	VBM:2005113021 Fixed up promotion of table's width 100% to set value to auto

 09-Dec-05	10719/9	ianw	VBM:2005113021 Fixed up promotion of table's width 100% to set value to auto

 09-Dec-05	10719/7	ianw	VBM:2005113021 Fixed up promotion of table's width 100% to set value to auto

 09-Dec-05	10719/5	ianw	VBM:2005113021 Fixed up promotion of table's width 100% to set value to auto

 09-Dec-05	10719/3	ianw	VBM:2005113021 Fixed up promotion of table's width 100% to set value to auto

 09-Dec-05	10719/1	ianw	VBM:2005113021 Fixed up promotion of table's width 100% to set value to auto

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 14-Sep-05	9472/3	ibush	VBM:2005090808 Add default styling for sub/sup elements

 09-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9223/3	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/2	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 ===========================================================================
*/
