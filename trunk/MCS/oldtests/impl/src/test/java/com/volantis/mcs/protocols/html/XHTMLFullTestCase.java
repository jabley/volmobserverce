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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLFullTestCase.java,v 1.11 2003/04/23 09:44:20 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Sep-02    Steve           VBM:2002040809 - Unit test for XHTMLFull.
 *                              Added testOpenPane() to open a pane complete
 *                              with table output into a dom then test the
 *                              generated elements and attributes.
 * 14-Oct-02    Geoff           VBM:2002100905 - Modified to use new DOMPool
 *                              constructor.
 * 17-Oct-02    Adrian          VBM:2002100404 - Fixed testOpenPane to account
 *                              for protocol change where styleclass name is
 *                              now retrieved from pageHead.
 * 17-Oct-02    Sumit           VBM:2002070803 - Added MyMarinerRequestContext
 *                              class and overrode getRequestContext() and
 *                              allocateRSB()
 * 11-Nov-02    Phil W-S        VBM:2002102306 - Updated to work with the new
 *                              mariner element style optimizations.
 * 16-Dec-02    Phil W-S        VBM:2002110516 - Updated after refactoring.
 * 16-Dec-02    Adrian          VBM:2002100203 - Updated references to
 *                              supportsMultipleStyleClasses to
 *                              supportsMultipleSelectorClasses
 * 21-Mar-03    Sumit           VBM:2003022828 - Added getTextInputFormat test
 * 06-Feb-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes.
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Add augmented helpers for
 *                              testAddPhoneNumberAttributes.
 * 23-Apr-03    Geoff           VBM:2003040305 - Add
 *                              testAddEventAttributeUserScript.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Comment;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneRendering;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.MutablePropertyValues;

import java.io.IOException;

import junitx.util.PrivateAccessor;


/**
 * Testcase for XHTMLFull.
 */
public class XHTMLFullTestCase extends XHTMLBasicTestCase {

    private Element element;
    private MCSAttributes attribute;
    private String elementName = "myElementName";

    /**
     * Device name for openPane test
     * todo - we should test Netscape4 separately, not in general tests
     */
    protected static String NETSCAPE4_DEVICE_NAME = "Netscape4";

    /**
     * Layout for openPane test.
     */
    static private CanvasLayout CANVAS_LAYOUT = new CanvasLayout();

    /**
     * The pane for pre-recorded style class.
     */
    private static final Pane PANE = new Pane(CANVAS_LAYOUT);

    static {
        PANE.setName(PANE_NAME);
        PANE.setInstance(0);
    }

    private XHTMLFull protocol;
    private XHTMLBasicTestable testable;

    /** Creates new TestDOMElement */
    public XHTMLFullTestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLFullFactory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (XHTMLFull) protocol;
        this.testable = (XHTMLBasicTestable) testable;
    }

    /**
     * Get the name of the menu element for the XHTMLFull protocol. Sub-classes
     * should override this method if their menu element is different from
     * their parent class.
     * @return "span".
     */
    protected String getMenuElementName() {
        return "span";
    }

    /**
     * Setup the unit test framework by creating some classes ensuring that each
     * test case starts off with a clean slate to work from
     */
    private void privateSetUp() {
        context = new TestMarinerPageContext();
        TestMarinerRequestContext requestContext =
                new TestMarinerRequestContext();
        ContextInternals.setMarinerPageContext(requestContext, context);
        context.pushRequestContext(requestContext);

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout1 =
                RuntimeDeviceLayoutTestHelper.activate(CANVAS_LAYOUT);
        RuntimeDeviceLayout runtimeDeviceLayout =
                runtimeDeviceLayout1;

        DeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        deviceContext.setDeviceLayout(runtimeDeviceLayout);

        resetElement();

        attribute = new StyleAttributes();
        attribute.setStyles(StylesBuilder.getDeprecatedStyles());
        attribute.setTitle("My title");

        context.pushDeviceLayoutContext(deviceContext);
        protocol.setMarinerPageContext(context);

        PaneInstance paneInstance = new TestPaneInstance();
        paneInstance.setStyleClass("fred");
        context.setFormatInstance(paneInstance);
        context.setDeviceName(NETSCAPE4_DEVICE_NAME);
        PageHead head = new PageHead();
//        head.addFormatStyleClass(LAYOUT_NAME, PANE, CLASS_NAME);
        testable.setPageHead(head);
    }

    private void resetElement() {
        element = domFactory.createElement();
        element.setName(elementName);
    }

    /**
     * Test usesDivForMenus property.
     */
    public void testUsesDivForMenus() {
        VolantisProtocol protocol = createTestableProtocol(internalDevice);
        assertFalse("XHTMLFull does not uses div for menus. " +
                   " usesDivForMenus should be false.",
                   protocol.usesDivForMenus());
    }

    /**
     * Tests that the openPaneTable method outputs a &lt;div&gt; tag if there
     * is a style class specified and the protocol supports stylesheets. If
     * there is no style class specified, or the protocol does not support
     * stylesheets, there should be no &lt;div&gt; tag output.
     */
    public void testOpenPaneTableAndDiv() throws Exception {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        Pane pane = new Pane(canvasLayout);
        pane.setName(PANE_NAME);

        PaneAttributes paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        paneAttributes.setPane(pane);

        protocol.openPaneTable(buffer, paneAttributes);
        Element elem = buffer.getCurrentElement();

        System.out.println("Open Pane Table: " + DOMUtilities.toString(elem));
        if (protocol.supportsStyleSheets()) {
            assertTrue("There should be a div element",
                    "div".equals(elem.getName()));
        } else {
            assertFalse("There should be no div element",
                    "div".equals(elem.getName()));
         }
    }

    /**
     * Tests that the openPaneTable method outputs a table with cell padding
     * and correctly assigns the padding style to the table cell, not the table.
     */
    public void testOpenPaneTableWithPadding() throws Exception {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        Pane pane = new Pane(canvasLayout);
        pane.setName(PANE_NAME);
        PaneAttributes paneAttributes = new PaneAttributes();
        Styles paneStyles = StylesBuilder.getSparseStyles("padding: 10px");
        paneAttributes.setStyles(paneStyles);
        paneAttributes.setPane(pane);

        protocol.openPaneTable(buffer, paneAttributes);
        Element tableElement = (Element) buffer.getRoot().getHead();
        assertTrue("Root element should be a table", "table".equals(tableElement.getName()));
        String cellPadding = tableElement.getAttributeValue("cellpadding");
        assertNotNull("Table should have cell padding attribute set", cellPadding);
        assertEquals("Cell padding should be 10 pixels", "10", cellPadding);

        Styles tableStyles = tableElement.getStyles();
        assertNull("Table should have no padding (bottom)",
                tableStyles.getPropertyValues().
                getSpecifiedValue(StylePropertyDetails.PADDING_BOTTOM));
        assertNull("Table should have no padding (top)",
                tableStyles.getPropertyValues().
                getSpecifiedValue(StylePropertyDetails.PADDING_TOP));
        assertNull("Table should have no padding (left)",
                tableStyles.getPropertyValues().
                getSpecifiedValue(StylePropertyDetails.PADDING_LEFT));
        assertNull("Table should have no padding (right)",
                tableStyles.getPropertyValues().
                getSpecifiedValue(StylePropertyDetails.PADDING_RIGHT));

        // We test for the computed values being null - the current
        // implementation sets them to null. In theory having the default value
        // for the computed value here should be acceptable.
        assertNull("Table should have no padding (bottom)",
                tableStyles.getPropertyValues().
                getComputedValue(StylePropertyDetails.PADDING_BOTTOM));
        assertNull("Table should have no padding (top)",
                tableStyles.getPropertyValues().
                getComputedValue(StylePropertyDetails.PADDING_TOP));
        assertNull("Table should have no padding (left)",
                tableStyles.getPropertyValues().
                getComputedValue(StylePropertyDetails.PADDING_LEFT));
        assertNull("Table should have no padding (right)",
                tableStyles.getPropertyValues().
                getComputedValue(StylePropertyDetails.PADDING_RIGHT));

        Element rowElement = (Element) tableElement.getHead();
        Element cellElement = (Element) rowElement.getHead();

        Styles cellStyles = cellElement.getStyles();
        StyleValue expectedPadding = StyleValueFactory.getDefaultInstance().
            getLength(null, 10, LengthUnit.PX);
        // Check the computed values for the table cell (since these are the
        // values that will be displayed
        assertEquals("Cell should have 10px padding (bottom)", expectedPadding,
                cellStyles.getPropertyValues().
                getComputedValue(StylePropertyDetails.PADDING_BOTTOM));
        assertEquals("Cell should have 10px padding (top)", expectedPadding,
                cellStyles.getPropertyValues().
                getComputedValue(StylePropertyDetails.PADDING_TOP));
        assertEquals("Cell should have 10px padding (left)", expectedPadding,
                cellStyles.getPropertyValues().
                getComputedValue(StylePropertyDetails.PADDING_LEFT));
        assertEquals("Cell should have 10px padding (right)", expectedPadding,
                cellStyles.getPropertyValues().
                getComputedValue(StylePropertyDetails.PADDING_RIGHT));
    }

    /**
     * Helper method which checks the value of the class attribute with an
     * equality assertion against the expected value.
     * @param element the element whose value to check
     * @param expectedClass the expected value
     */
    protected void checkClassAttribute(Element element,
                                        String expectedClass) {
        assertEquals("Unexpected class attribute value",
                expectedClass,
                element.getAttributeValue("class"));
    }

    /**
     * This exercises the checkPaneTableAttributes method when the pane has no
     * style class.
     */
    public void notestCheckPaneTableAttributesWithNoStyleClass()
            throws Throwable {
        PaneAttributes paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        // A border width requires a table.
//        paneAttributes.setBorderWidth("50");
        assertTrue("Table should be required for border width and " +
                "no style class",
                isTableRequired(null, paneAttributes));

        paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        // Cell padding requires a table.
//        paneAttributes.setCellPadding("50");
        assertTrue("Table should be required for cell padding and " +
                "no style class",
                isTableRequired(null, paneAttributes));

        paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        // Cell spacing requires a table.
//        paneAttributes.setCellSpacing("50");
        assertTrue("Table should be required for cell spacing and " +
                "no style class",
                isTableRequired(null, paneAttributes));
    }

    /**
     * This exercises the checkPaneTableAttributes method when the pane has a
     * style class.
     */
    public void notestCheckPaneTableAttributesWithStyleClass() throws Throwable {
        PaneAttributes paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        // A border width requires a table if there is a style class and the
        // protocol supports style sheets.
//        paneAttributes.setBorderWidth("50");

        assertTrue("Table should be required for border width",
                   isTableRequired("fred", paneAttributes));

        // A cell padding requires a table if there is a style class and the
        // protocol supports style sheets.
        paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
//        paneAttributes.setCellPadding("50");

        assertTrue("Table should be required for cell padding",
                   isTableRequired("fred", paneAttributes));

        // A cell spacing requires a table if there is a style class and the
        // protocol supports style sheets.
        paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
//        paneAttributes.setCellSpacing("50");

        assertTrue("Table should be required for cell spacing",
                   isTableRequired("fred", paneAttributes));
    }

    /**
     * This exercises the checkPaneTableAttributes method when the pane has no
     * style class, and a width value is set.
     */
    public void notestCheckPaneTableAttributeWidth() throws Throwable {
        PaneAttributes paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        // A table is not required if none of the appropriate attributes are
        // set.
        assertFalse("Table should not be required when there are no " +
                "attributes set",
                isTableRequired(null, paneAttributes));

        paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        // A 100% width requires no table.
//        paneAttributes.setWidth("100");
//        paneAttributes.setWidthUnits(PaneAttributes.WIDTH_UNITS_VALUE_PERCENT);
        assertFalse("Table should not be required for 100% width",
                isTableRequired(null, paneAttributes));

        paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        // A 0 width requires no table.
//        paneAttributes.setWidth("0");
//        paneAttributes.setWidthUnits(PaneAttributes.WIDTH_UNITS_VALUE_PERCENT);
        assertFalse("Table should not be required for 0% width",
                isTableRequired(null, paneAttributes));

        paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        // A null width requires no table.
//        paneAttributes.setWidth(null);
        assertFalse("Table should not be required for null width",
                isTableRequired(null, paneAttributes));

        paneAttributes = new PaneAttributes();
        paneAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        // A width requires a table if there is no stylesheet.
//        paneAttributes.setWidth("55");
//        paneAttributes.setWidthUnits(PaneAttributes.WIDTH_UNITS_VALUE_PIXELS);
        assertTrue("Table should be required for 55 pixels width",
                isTableRequired(null, paneAttributes));
    }

    /**
     * Helper method which invokes the checkPaneTableAttributes method,
     * returning the result.
     * @param attributes the pane attributes to check
     * @return true if a table is needed; false otherwise
     */
    private boolean isTableRequired(String styleClassName,
                                      PaneAttributes attributes) throws Throwable {
        final Class[] types = {PaneAttributes.class};
        final Object[] params = {attributes};

        Object result = PrivateAccessor.invoke(protocol,
                "checkPaneTableAttributes",
                types, params);

        return ((Boolean) result).booleanValue();
    }

    /**
     * Test open pane
     *
     * todo XDIME-CP: Fix when done layout transformation.
     */
    public void notestOpenPane() throws RepositoryException {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        Element el = null;

        Pane pane = null;
        PaneAttributes attributes = null;

        //=============================================================
        // Styles defined = No
        //=============================================================
        pane = new Pane(null);
        pane.setName(PANE_NAME);
        attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        attributes.setPane(pane);

        protocol.openPane(buffer, attributes);

        // We expect absolutely no markup to be rendered for a pane that has
        // no attributes and no styling
        assertSame("The buffer current element should be the root element " +
                   "but was" + buffer.getCurrentElement(),
                   buffer.getRoot(),
                   buffer.getCurrentElement());

        //=============================================================
        // Styles defined = Yes
        // @todo XDIME-CP pass in Styles and check they're correctly propagated through
        //=============================================================
        pane = new Pane(null);
        pane.setName(PANE_NAME);

        attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
//        attributes.setStyleClass("fred");
//        attributes.setBackgroundColour("#ff0000");
//        attributes.setBorderWidth("1");
//        attributes.setCellPadding("2");
//        attributes.setCellSpacing("3");
        attributes.setPane(pane);

        protocol.openPane(buffer, attributes);

        el = buffer.closeElement("div");
//        assertEquals("Invalid class attribute on div",
//                     "VE-pane-fred", el.getAttributeValue("class"));

        el = buffer.closeElement("td");
//        assertNull("Invalid class attribute on td",
//                   el.getAttributeValue("class"));
        assertNull("bgcolor on td", el.getAttributeValue("bgcolor"));
        assertNull("border on td", el.getAttributeValue("border"));
        assertNull("cellspacing on td", el.getAttributeValue("cellspacing"));
        assertNull("cellpadding on td", el.getAttributeValue("cellpadding"));
        el = buffer.closeElement("tr");
        assertNull("Class attribute on tr", el.getAttributeValue("class"));
        assertNull("bgcolor on tr", el.getAttributeValue("bgcolor"));
        assertNull("border on tr", el.getAttributeValue("border"));
        assertNull("cellspacing on tr", el.getAttributeValue("cellspacing"));
        assertNull("cellpadding on tr", el.getAttributeValue("cellpadding"));
        el = buffer.closeElement("table");
        assertNull("Class attribute on table", el.getAttributeValue("class"));
        assertNull("bgcolor on table", el.getAttributeValue("bgcolor"));
        assertEquals("Invalid border attribute", "1",
                     el.getAttributeValue("border"));
        assertEquals("Invalid cellspacing attribute", "3",
                     el.getAttributeValue("cellspacing"));
        assertEquals("Invalid cellpadding attribute", "2",
                     el.getAttributeValue("cellpadding"));
    }

    /**
     * Test the method: closePane
     */
    public void testClosePane() throws Exception {
        context = new TestMarinerPageContext();
        protocol.setMarinerPageContext(context);
        TestDeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        context.pushDeviceLayoutContext(deviceContext);

        DOMOutputBuffer dom = new DOMOutputBuffer();
        dom.initialise();
        PaneAttributes attributes = new PaneAttributes();
        Pane pane = new Pane(null);
        attributes.setPane(pane);

        Element body;
        body = dom.openStyledElement("body", attributes);

        dom.appendEncoded("Example");

        protocol.closePane(dom, attributes);

        assertSame("The DOM's current element isn't the body element",
                   dom.getCurrentElement(),
                   body);
    }

    /**
     * Ensure that the user script portion of the event attribute rendering
     * is working properly.
     *
     * @throws com.volantis.mcs.protocols.ProtocolException
     */
    public void testAddEventAttributeUserScript() throws ProtocolException {
        Element element = domFactory.createElement();
        String attributeName = "event";
        ScriptAssetReference userScript =
                new LiteralScriptAssetReference("userscript");
        String internalScript = null;
        protocol.addEventAttribute(element, attributeName, userScript,
                internalScript);
        String attributeValue = element.getAttributeValue(attributeName);
        assertEquals("script attribute", userScript.getScript(), attributeValue);
    }

    /**
     * Augmented to ensure that all events are registered in the attributes,
     * even those not needed by this protocol. This covers the initialization
     * of events for specializations too.
     */
    protected PhoneNumberAttributes createPhoneNumberAttributes(
        TextAssetReference fullNumber) {
        PhoneNumberAttributes phoneNumberAttributes =
            super.createPhoneNumberAttributes(fullNumber);
        EventAttributes eventAttributes =
            phoneNumberAttributes.getEventAttributes(true);

        // Add all the events
        for (int i = 0;
             i < EventConstants.MAX_EVENTS;
             i++) {
            eventAttributes.setEvent(i, "set");
        }

        return phoneNumberAttributes;
    }

    /**
     * This checking must match the setting of attributes
     * @param element
     * @throws Exception
     */
    protected void checkAddPhoneNumberAttributes(Element element)
        throws Exception {

        if (protocol.supportsEvents()) {
            // Check and remove only those events that this protocol generates
            final String[] eventAttributeNames =
                    {"onclick",
                     "ondblclick",
                     "onkeydown",
                     "onkeypress",
                     "onkeyup",
                     "onmousedown",
                     "onmousemove",
                     "onmouseout",
                     "onmouseover",
                     "onmouseup",
                     "onfocus",
                     "onblur"};

            for (int i = 0;
                 i < eventAttributeNames.length;
                 i++) {
                assertEquals("event " + eventAttributeNames[i] + " should be set",
                        "set",
                        element.getAttributeValue(eventAttributeNames[i]));
                element.removeAttribute(eventAttributeNames[i]);
            }
        }
        // Call the superclass last to ensure that the processing is performed
        // correctly
        super.checkAddPhoneNumberAttributes(element);
    }

    public void testOpenPaneWithContainerCell() throws Exception {
        // stub out XHTMLBasic version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-impl if that makes sense
    }

    /**
     * Test for doProtocolString
     */
    public void testDoProtocolString() throws IOException, RepositoryException {
        String expected = "<!DOCTYPE html" +
                " PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"" +
                " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";

        checkDoProtocolString(protocol, expected);
    }

    // Javadoc inherited.
    protected String getExpectedHorizontalRuleMarkup() {
        return "<hr/>";
    }

    /**
     * Test the method addPaneTableAttributes(Element, PaneAttributes)
     */
    public void testAddPaneTableAttributes() throws Exception {
        doAddPaneTableAttributesTest(
                " cellpadding=\"0\" cellspacing=\"0\"",
                " border=\"2\" cellpadding=\"3\" cellspacing=\"1\"");
    }

    protected void doAddPaneTableAttributesTest(
            final String expectedUnstyledAttributes,
            final String expectedStyledAttributes) throws Exception {
        doAddPaneAttributesTest(new PaneAttributes(), new InvokeElementAttributesMethod() {
            public void invoke(Element element, MCSAttributes attributes) {
                protocol.addPaneTableAttributes(
                        element, (PaneAttributes) attributes);
            }
        }, "PANE", "", false,
           expectedUnstyledAttributes,
           expectedStyledAttributes, expectedStyledAttributes);
    }

    public static interface InvokeElementAttributesMethod {
        public void invoke(Element element, MCSAttributes attributes);
    }

    private void doAddPaneAttributesTest(
            final PaneAttributes attrs,
            final InvokeElementAttributesMethod invokeMethod,
            final String elementName,
            String additionalAttributes,
            final boolean expectOptimize,
            final String expectedUnstyledAttributes,
            final String expectedStyledAttributes,
            final String expectedStylesheetAttributes)
            throws Exception {

        context = new TestMarinerPageContext();
        TestMarinerRequestContext requestContext1 =
                new TestMarinerRequestContext();
        ContextInternals.setMarinerPageContext(requestContext1, context);
        context.pushRequestContext(requestContext1);

        context.setDeviceName("Master");
        protocol.setMarinerPageContext(context);

        Pane pane = new Pane(CANVAS_LAYOUT);
        attrs.setPane(pane);

        attrs.setStyles(StylesBuilder.getDeprecatedStyles());
        Element element = domFactory.createStyledElement(attrs.getStyles());
        element.setName(elementName);
        invokeMethod.invoke(element, attrs);

        String actual;
        actual = DOMUtilities.toString(element);
        String optionalOptimizeAttributes =
                (expectOptimize ? " OPTIMIZE=\"little impact\"" : "");
        assertXMLEquals("No styles",
                        "<" + elementName +
                        optionalOptimizeAttributes +
                        expectedUnstyledAttributes +
                        additionalAttributes + "/>",
                        actual);

        attrs.setStyles(createTestPaneStyles());
        element = domFactory.createStyledElement(attrs.getStyles());
        element.setName(elementName);
        invokeMethod.invoke(element, attrs);

        actual = DOMUtilities.toString(element);

        if (protocol.supportsStyleSheets()) {
            assertXMLEquals(
                    "Stylesheet supported",
                    "<" + elementName +
                    optionalOptimizeAttributes +
                    expectedStylesheetAttributes +
                    additionalAttributes + "/>",
                    actual);
        } else {
            assertXMLEquals(
                    "Stylesheet not supported",
                    "<" + elementName +
                    optionalOptimizeAttributes +
                    expectedStyledAttributes +
                    additionalAttributes + "/>",
                    actual);
        }
    }

    /**
     * Test the method
     * addRowIteratorPaneAttributes(Element, RowIteratorPaneAttributes)
     */
    public void testAddRowIteratorPaneAttributes() throws Exception {
        doAddRowIteratorPaneAttributesTest(
                " cellpadding=\"0\" cellspacing=\"0\"",
                " border=\"2\" cellpadding=\"3\" cellspacing=\"1\"");
//        commonSetupForAddXXXPaneAttributesTests();
//
////        String cellpadding;
//        String cellspacing;
//        String border;
//
//        Element element = domFactory.createElement();
//        RowIteratorPaneAttributes attrs = new RowIteratorPaneAttributes();
//        attrs.setStyles(StylesBuilder.getStyles());
//        Pane pane = new Pane(DEVICE_LAYOUT);
//
//        // Mimic an element created by openRowIteratorPane and assign
//        // an optimization level
//        element.setName("table");
//        pane.setOptimizationLevel(
//                FormatConstants.OPTIMIZATION_LEVEL_VALUE_LITTLE_IMPACT);
//        attrs.setPane(pane);
//
//        protocol.addRowIteratorPaneAttributes(element, attrs);
//
//        attrs.setStyles(createTestPaneStyles());
//        if (protocol.supportsStyleSheets()) {
////            cellpadding = element.getAttributeValue("cellpadding");
////            assertNull("cellpadding attribute should not have been set as " +
////                    "stylesheets are supported.", cellpadding);
//
//            cellspacing = element.getAttributeValue("cellspacing");
//            assertNull("cellspacing attribute should not have been set as " +
//                    "stylesheets are supported.", cellspacing);
//
//            border = element.getAttributeValue("border");
//            assertNull("border attribute should not have been set as " +
//                    "stylesheets are supported.", border);
//
//            String optimizationLevel = element.getAttributeValue(
//                    OptimizationConstants.OPTIMIZATION_ATTRIBUTE);
//            if (protocol.supportsFormatOptimization) {
//                assertEquals("Optimization level attribute should have been set",
//                        OptimizationConstants.OPTIMIZE_LITTLE_IMPACT,
//                        optimizationLevel);
//            } else {
//                assertNull("Optimization level attribute should not have " +
//                        "been set - format optimization not supported.",
//                        optimizationLevel);
//            }
//        } else {
////            cellpadding = element.getAttributeValue("cellpadding");
////            assertEquals("cellpadding attribute should have been set as " +
////                    "stylesheets are not supported.", "1", cellpadding);
//
//            cellspacing = element.getAttributeValue("cellspacing");
//            assertEquals("cellspacing attribute should have been set as " +
//                    "stylesheets are not supported.", "1", cellspacing);
//
//            border = element.getAttributeValue("border");
//            assertEquals("border attribute should have been set as " +
//                    "stylesheets are not supported.", "1", border);
//
//            element = domFactory.createElement();
//            attrs = new RowIteratorPaneAttributes();
//            attrs.setStyles(StylesBuilder.getStyles(
//                    "border-width: 2px; " +
//                    "border-spacing: 2px"));
////            attrs.setBorderWidth("2");
////            attrs.setCellPadding("2");
////            attrs.setCellSpacing("2");
//            protocol.addRowIteratorPaneAttributes(element, attrs);
//
////            cellpadding = element.getAttributeValue("cellpadding");
////            assertEquals("cellpadding attribute should have been set as " +
////                    "stylesheets are not supported.", "2", cellpadding);
//
//            cellspacing = element.getAttributeValue("cellspacing");
//            assertEquals("cellspacing attribute should have been set as " +
//                    "stylesheets are not supported.", "2", cellspacing);
//
//            border = element.getAttributeValue("border");
//            assertEquals("border attribute should have been set as " +
//                    "stylesheets are not supported.", "2", border);
//        }
    }

    protected void doAddRowIteratorPaneAttributesTest(
            final String expectedUnstyledAttributes,
            final String expectedStyledAttributes)
            throws Exception {

        doAddPaneAttributesTest(
                new RowIteratorPaneAttributes(),
                new InvokeElementAttributesMethod() {
                    public void invoke(Element element,
                                       MCSAttributes attributes) {

                        RowIteratorPaneAttributes iteratorPaneAttributes =
                                (RowIteratorPaneAttributes) attributes;

                        // Assign an optimization level
                        iteratorPaneAttributes.getPane().setOptimizationLevel(
                                FormatConstants.OPTIMIZATION_LEVEL_VALUE_LITTLE_IMPACT);

                        protocol.addRowIteratorPaneAttributes(
                                element, iteratorPaneAttributes);
                    }
                }, "table", "", protocol.supportsFormatOptimization,
                expectedUnstyledAttributes,
                expectedStyledAttributes, expectedStyledAttributes);
    }

    /**
     * Test the method
     * addColumnIteratorPaneAttributes(Element, ColumnIteratorPaneAttributes
     */
    public void testAddColumnIteratorPaneAttributes() throws Exception {

        doAddColumnIteratorPaneAttributesTest(
                " cellpadding=\"0\" cellspacing=\"0\"",
                " border=\"2\" cellpadding=\"3\" cellspacing=\"1\"");
//
////        String cellpadding;
//        String cellspacing;
//        String border;
//
//        Element element = domFactory.createElement();
//        ColumnIteratorPaneAttributes attrs =
//                new ColumnIteratorPaneAttributes();
//        attrs.setStyles(StylesBuilder.getStyles());
//        Pane pane = new Pane(DEVICE_LAYOUT);
//
//        // Mimic an element created by openColumnIteratorPane and assign
//        // an optimization level
//        element.setName("table");
//        pane.setOptimizationLevel(FormatConstants.OPTIMIZATION_LEVEL_VALUE_LITTLE_IMPACT);
//        attrs.setPane(pane);
//
//        protocol.addColumnIteratorPaneAttributes(element, attrs);
//
//        attrs.setStyles(createTestPaneStyles());
//        if (protocol.supportsStyleSheets()) {
////            cellpadding = element.getAttributeValue("cellpadding");
////            assertNull("cellpadding attribute should not have been set as " +
////                    "stylesheets are supported.", cellpadding);
//
//            cellspacing = element.getAttributeValue("cellspacing");
//            assertNull("cellspacing attribute should not have been set as " +
//                    "stylesheets are supported.", cellspacing);
//
//            border = element.getAttributeValue("border");
//            assertNull("border attribute should not have been set as " +
//                    "stylesheets are supported.", border);
//
//            // Test for optimization - attributes should have been applied if
//            // format optimization is supported, and should not have been
//            // applied otherwise.
//            String optimizationLevel = element.getAttributeValue(
//                    OptimizationConstants.OPTIMIZATION_ATTRIBUTE);
//            if (protocol.supportsFormatOptimization) {
//                assertEquals("Optimization level attribute should have been set",
//                        OptimizationConstants.OPTIMIZE_LITTLE_IMPACT,
//                        optimizationLevel);
//            } else {
//                assertNull("Optimization level attribute should not have " +
//                        "been set - format optimization not supported.",
//                        optimizationLevel);
//            }
//        } else {
////            cellpadding = element.getAttributeValue("cellpadding");
////            assertEquals("cellpadding attribute should have been set as " +
////                    "stylesheets are not supported.", "1", cellpadding);
//
//            cellspacing = element.getAttributeValue("cellspacing");
//            assertEquals("cellspacing attribute should have been set as " +
//                    "stylesheets are not supported.", "1", cellspacing);
//
//            border = element.getAttributeValue("border");
//            assertEquals("border attribute should have been set as " +
//                    "stylesheets are not supported.", "1", border);
//
//            element = domFactory.createElement();
//            attrs = new ColumnIteratorPaneAttributes();
//            attrs.setStyles(StylesBuilder.getStyles(
//                    "border-width: 2px; " +
//                    "border-spacing: 2px"));
////            attrs.setBorderWidth("2");
////            attrs.setCellPadding("2");
////            attrs.setCellSpacing("2");
//            protocol.addColumnIteratorPaneAttributes(element, attrs);
//
////            cellpadding = element.getAttributeValue("cellpadding");
////            assertEquals("cellpadding attribute should have been set as " +
////                    "stylesheets are not supported.", "2", cellpadding);
//
//            cellspacing = element.getAttributeValue("cellspacing");
//            assertEquals("cellspacing attribute should have been set as " +
//                    "stylesheets are not supported.", "2", cellspacing);
//
//            border = element.getAttributeValue("border");
//            assertEquals("border attribute should have been set as " +
//                    "stylesheets are not supported.", "2", border);
//        }
    }

    private void doAddColumnIteratorPaneAttributesTest(
            final String expectedUnstyledAttributes,
            final String expectedStyledAttributes)
            throws Exception {

        doAddPaneAttributesTest(
                new ColumnIteratorPaneAttributes(),
                new InvokeElementAttributesMethod() {
                    public void invoke(Element element, MCSAttributes attributes) {

                        ColumnIteratorPaneAttributes iteratorPaneAttributes =
                                (ColumnIteratorPaneAttributes) attributes;

                        // Assign an optimization level
                        iteratorPaneAttributes.getPane().setOptimizationLevel(
                                FormatConstants.OPTIMIZATION_LEVEL_VALUE_LITTLE_IMPACT);

                        protocol.addColumnIteratorPaneAttributes(
                                element, iteratorPaneAttributes);
                    }
                }, "table", " width=\"100%\"", protocol.supportsFormatOptimization,
                expectedUnstyledAttributes,
                expectedStyledAttributes, expectedStyledAttributes);
    }

    /**
     * Test the method supportsStyleSheets()
     */
    public void testSupportsStyleSheets() throws Exception {
        assertTrue("This protocol should support stylesheets.",
                protocol.supportsStyleSheets());
    }

    // javadoc inherited
    protected String expectedDefaultMimeType() {
        return "text/html";
    }

    /**
     * Test the method: createEnclosingElement.
     */
    public void testCreateEnclosingElement() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "vertical-align: middle"));

//        doTestCreateEnclosingElement(attributes,
//                                     "<td>" +
//                                        "<table cellspacing=\"0\">" +
//                                     "<tr>" +
//                                     "<td valign=\"middle\">" +
//                                     "<div/>" +
//                                     "</td>" +
//                                     "</tr>" +
//                                     "</table>" +
//                                     "</td>");

        Pane pane = new Pane(new CanvasLayout());
        attributes.setPane(pane);

        // todo add in a test to make sure that the protocol is using the correct border handler for a netscape device.
        // Intialize the context.
        //context = new TestMarinerPageContext();
        //context.pushDeviceLayoutContext(new TestDeviceLayoutContext());
        //context.setDeviceName(NETSCAPE4_DEVICE_NAME);
//        TestMarinerPageContext context = (TestMarinerPageContext)
//                protocol.getMarinerPageContext();
//        context.setDeviceName(NETSCAPE4_DEVICE_NAME);

        // Intialize the protocol
//        protocol.setMarinerPageContext(context);

        // Reinitialise the style handlers to make sure that they take into
        // account the fact that the device is actually a netscape 4 one.
//        protocol.initialiseStyleHandlers();

        doTestCreateEnclosingElement(attributes,
                                     getExpectedCreateEnclosingElementTable());
    }

    /**
     * IE6 protocol has a different expected result
     */ 
    protected String getExpectedCreateEnclosingElementTable() {
        return  "<td>" +
                    "<table cellpadding=\"0\" cellspacing=\"0\">" +
//                "<table border=\"0\" cellspacing=\"0\">" +
                        "<tr>" +
                            "<td valign=\"middle\">" +
                                "<div/>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</td>";
    }

    /**
     * Helper method.
     */
    protected void doTestCreateEnclosingElement(
            PaneAttributes attributes,
            String expected)
            throws Exception {
        TestDOMOutputBuffer dom = new TestDOMOutputBuffer();
        Element element = dom.openStyledElement("td", attributes);

//        if (elementClassAttributes != null) {
//            element.setAttribute("class", elementClassAttributes);
//        }

        // Pane attributes can't be null, must contain a valid pane.
        if (attributes.getPane() == null) {
            attributes.setPane(new Pane(new CanvasLayout()));
        }

        protocol.createEnclosingElement(dom, attributes);

        String actual = DOMUtilities.toString(dom.getRoot());
        assertEquals("Result shouild match\n" +
                     "EXPECTED: " + expected + "\n" +
                     "ACTUAL  : " + actual + "\n", expected, actual);
    }

    /**
     * Test the method: createEnclosingElement
     */
    public void testUseEnclosingTableCell() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        doTestUseEnclosingTableCell(attributes, "<td/>");

        // Set some styles on the element.
        attributes.setStyles(StylesBuilder.getStyles("background-color: red"));
        doTestUseEnclosingTableCell(attributes,
                                    getExpectedEnclosingTableCellMarkup());
    }

    protected String getExpectedEnclosingTableCellMarkup() {
        return "<td/>";
    }

    /**
     * Helper method.
     */
    protected void doTestUseEnclosingTableCell(
            PaneAttributes attributes,
            String expected)
            throws Exception {
        TestDOMOutputBuffer dom = new TestDOMOutputBuffer();
        Element element = dom.openStyledElement("td", attributes);

//        if (elementClassAttributes != null) {
//            element.setAttribute("class", elementClassAttributes);
//        }

        // Pane attributes can't be null, must contain a valid pane.
        attributes.setPane(new Pane(new CanvasLayout()));

        protocol.useEnclosingTableCell(dom, attributes);

        String actual = DOMUtilities.toString(dom.getRoot());
        assertEquals("Result shouild match\n" +
                     "EXPECTED: " + expected + "\n" +
                     "ACTUAL  : " + actual + "\n", expected, actual);
        final MutablePropertyValues attributePropertyValues =
            attributes.getStyles().getPropertyValues();
        final MutablePropertyValues elementPropertyValues =
            element.getStyles().getPropertyValues();
        final StylePropertyDefinitions stylePropertyDefinitions =
            attributePropertyValues.getStylePropertyDefinitions();
        final int count = stylePropertyDefinitions.count();
        for (int i = 0; i < count; i++) {
            final StyleProperty styleProperty =
                stylePropertyDefinitions.getStyleProperty(i);
            if (styleProperty.equals(StylePropertyDetails.DISPLAY)) {
                assertEquals(DisplayKeywords.TABLE_CELL,
                    elementPropertyValues.getComputedValue(styleProperty));
                assertEquals(DisplayKeywords.TABLE_CELL,
                    elementPropertyValues.getSpecifiedValue(styleProperty));
            } else {
                assertEquals(attributePropertyValues.getComputedValue(styleProperty),
                    elementPropertyValues.getComputedValue(styleProperty));
                assertEquals(attributePropertyValues.getSpecifiedValue(styleProperty),
                    elementPropertyValues.getSpecifiedValue(styleProperty));
            }
        }
    }

    /**
     * Test the method: checkPaneRendering.
     */
    public void testCheckPaneRendering() throws Exception {
        Pane pane = new Pane(null);
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        attributes.setPane(pane);

        doTestCheckPaneRendering("not-td", attributes,
                                 PaneRendering.DO_NOTHING);

        doTestCheckPaneRendering("td", attributes,
                                 PaneRendering.DO_NOTHING);

//        attributes.setCellSpacing("10");
        attributes.setStyles(StylesBuilder.getStyles("border-width: 2px"));
        doTestCheckPaneRendering("td", attributes,
                                 PaneRendering.USE_TABLE);

        // Other permutations tested by testCheckPaneCellAttributes and
        // existing test cases (testCheckPaneTableAttributesXXXX)
    }

    /**
     * Helper method.
     */
    protected void doTestCheckPaneRendering(String elementName,
                                          PaneAttributes attributes,
                                          PaneRendering expected)
            throws Exception {

        TestDOMOutputBuffer dom = new TestDOMOutputBuffer();
        dom.openStyledElement(elementName, attributes);

        PaneRendering actual = protocol.checkPaneRendering(dom, attributes);
        assertEquals("Result shouild match\n" +
                     "EXPECTED: " + expected + "\n" +
                     "ACTUAL  : " + actual + "\n", expected, actual);

    }

    /**
     * Test the method: checkpaneCellAttributes.
     */
    public void notestCheckPaneCellAttributes() throws Exception {
        Pane pane = new Pane(null);
        PaneAttributes attributes = new PaneAttributes();
        TestDOMOutputBuffer dom = new TestDOMOutputBuffer();

        Element element = dom.openStyledElement("td", attributes);

        attributes.setPane(pane);
        
        // No width set, return DO_NOTHING.
        PaneRendering result;
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.DO_NOTHING,
                     result);

        // No width set to zero, return DO_NOTHING.
//        attributes.setWidth("0");
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.DO_NOTHING,
                     result);

        // If the width isn't 100% (or zero) and the element has no width
        // attribute set already, we can use the enclosing table cell.
//        attributes.setWidth("10");
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.USE_ENCLOSING_TABLE_CELL,
                     result);

        // If the enclosing cell is null we need to use a table.
//        attributes.setWidth("10");
        result = protocol.checkPaneCellAttributes(null, attributes);
        assertEquals("Expected ",
                     PaneRendering.USE_TABLE,
                     result);

        // If the width isn't 100% (or zero) and the element has a width
        // attribute set already, we need to use a table.
//        attributes.setWidth("10");
        element.setAttribute("width", "40");
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.USE_TABLE,
                     result);

        // If the width is 100% we should return DO_NOTHING.
//        attributes.setWidth("100");
//        attributes.setWidthUnits(PaneAttributes.WIDTH_UNITS_VALUE_PERCENT);
        element.setAttribute("width", "40");
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.DO_NOTHING,
                     result);

        // If the style class has been set on the element and the attributes
        // and we do not support multiple style classes we should return
        // CREATE_ENCLOSING_ELEMENT.
//        attributes.setWidth(null);
        element.setAttribute("class", "elementStyleClass");
//        attributes.setStyleClass("attributeStyleClass");
//        testable.setSupportsMultipleAttributeClasses(false);
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.CREATE_ENCLOSING_ELEMENT,
                     result);

        // Same as above except the support of multiple style classes is
        // set to true.
        // Expect USE_ENCLOSING_TABLE_CELL (classes may be merged).
//        attributes.setWidth(null);
        element.setAttribute("class", "elementStyleClass");
//        attributes.setStyleClass("attributeStyleClass");
//        testable.setSupportsMultipleAttributeClasses(true);
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.USE_ENCLOSING_TABLE_CELL,
                     result);

        // Same as above except the support of multiple style classes is
        // set to true and a valid width has been set. Expect USE_ENCLOSING_TABLE_CELL.
//        attributes.setWidth("10");
        element.setAttribute("class", "elementStyleClass");
        element.removeAttribute("width");
//        attributes.setStyleClass("attributeStyleClass");
//        testable.setSupportsMultipleAttributeClasses(true);
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.USE_ENCLOSING_TABLE_CELL,
                     result);

        // If the style class has been set on the element and the attributes
        // and we do not support multiple style classes and the style classes
        // are the same we should return DO_NOTHING.
//        attributes.setWidth(null);
        element.setAttribute("class", "bg-blue");
//        attributes.setStyleClass("bg-blue");
//        testable.setSupportsMultipleAttributeClasses(false);
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.DO_NOTHING,
                     result);

        // Same as above except style class is contained by element style class
        // already. Expected DO_NOTHING
//        attributes.setWidth(null);
        element.setAttribute("class", "bg-blue");
//        attributes.setStyleClass("VE-test bg-blue");
//        testable.setSupportsMultipleAttributeClasses(false);
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.DO_NOTHING,
                     result);

        // If the style class has been set on the element and the attributes
        // and we do not support multiple style classes we should return
        // USE_TABLE.
//        attributes.setWidth(null);
        element.setAttribute("class", "elementStyleClass");
        element.setAttribute("valign", "center"); // not supported on a div so use a table.
//        attributes.setStyleClass("attributeStyleClass");
//        testable.setSupportsMultipleAttributeClasses(false);
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.CREATE_ENCLOSING_ELEMENT,
                     result);

        // Same as above except the support of multiple style classes is
        // set to true and a valid width has been set.
        // Expect CREATE_ENCLOSING_ELEMENT.
//        attributes.setWidth("10");
        element.setAttribute("class", "elementStyleClass");
        element.removeAttribute("valign");
//        attributes.setStyleClass("attributeStyleClass");
//        testable.setSupportsMultipleAttributeClasses(false);
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.CREATE_ENCLOSING_ELEMENT,
                     result);

        // If the width is 100 pixels we should return USE_TABLE.
//        attributes.setWidth("100");
//        attributes.setWidthUnits(PaneAttributes.WIDTH_UNITS_VALUE_PIXELS);
        element.setAttribute("width", "40");
        result = protocol.checkPaneCellAttributes(element, attributes);
        assertEquals("Expected ",
                     PaneRendering.USE_TABLE,
                     result);

    }

    /**
     * Create a Styles that is populated with values
     * for the border, padding and margin properties.
     */
    Styles createTestPaneStyles() {
        return StylesBuilder.getStyles(
                "border-spacing: 1px; " +
                "border-width: 2px; " +
                "border-style: solid; " +
                "padding: 3px; " +
                "margin: 4px");
    }

    /**
     * Tests if media types are applied correctly.
     */
    public void testOpenStyleHandheldMedia() throws IOException {

        final DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        final ProtocolConfigurationImpl protocolConfiguration =
            (ProtocolConfigurationImpl) protocol.getProtocolConfiguration();
        protocolConfiguration.setCSSMedia("handheld");
        final StyleAttributes styleAttributes = new StyleAttributes();
        protocol.openStyle(buffer, styleAttributes);
        final Element head = (Element) buffer.getRoot().getHead();
        assertEquals("handheld", head.getAttributeValue("media"));
        assertEquals("text/css", head.getAttributeValue("type"));
        buffer.appendEncoded(".c {color:red}");
        protocol.closeStyle(buffer, styleAttributes);
        final Comment comment = (Comment) head.getHead();
        assertEquals(".c {color:red}",
            new String(comment.getContents(), 0, comment.getLength()));
    }

    /**
     * Tests if media type can be omitted.
     */
    public void testOpenStyleNoMedia() {

        final DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        final ProtocolConfigurationImpl protocolConfiguration =
            (ProtocolConfigurationImpl) protocol.getProtocolConfiguration();
        protocolConfiguration.setCSSMedia(null);
        final StyleAttributes styleAttributes = new StyleAttributes();
        protocol.openStyle(buffer, styleAttributes);
        final Element head = (Element) buffer.getRoot().getHead();
        assertNull(head.getAttributeValue("media"));
        assertEquals("text/css", head.getAttributeValue("type"));
        buffer.appendEncoded(".c {color:red}");
        protocol.closeStyle(buffer, styleAttributes);
        final Comment comment = (Comment) head.getHead();
        assertEquals(".c {color:red}",
            new String(comment.getContents(), 0, comment.getLength()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/5	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 19-Jul-05	9039/4	emma	VBM:2005071401 supermerge required

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/9	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 09-Jun-05	8665/6	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 08-Jun-05	7997/2	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 07-Jun-05	8637/1	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 22-Apr-05	7791/1	philws	VBM:2005040113 Port openPane changes from 3.3

 22-Apr-05	7746/1	philws	VBM:2005040113 Correct pane rendering where width and/or alignment are specified

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/3	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 04-Nov-04	5871/8	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - moved PaneRendering

 04-Nov-04	5871/6	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - rework issues

 03-Nov-04	5871/4	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 27-Sep-04	5661/1	tom	VBM:2004091403 Added stylesheet support to iMode and fixed bgcol in cells

 27-Sep-04	5581/3	tom	VBM:2004091403 Introduced stylesheet emulation for font and repaired background

 09-Sep-04	4839/22	pcameron	VBM:2004062801 div tag is now used again in a pane's table if stylesheets are in use

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 22-Jul-04	4713/10	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 20-Jul-04	4713/7	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 29-Jun-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 02-Jul-04	4803/7	adrianj	VBM:2003041504 Improved javadoc for adding column/row iterator pane attributes

 02-Jul-04	4803/5	adrianj	VBM:2003041504 Improved javadoc for adding column/row iterator pane attributes

 02-Jul-04	4803/3	adrianj	VBM:2003041504 Fixed optimization attribute propagation for column iterator panes

 02-Jul-04	4803/1	adrianj	VBM:2003041504 Fixed optimization attribute propagation for column iterator panes

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 05-May-04	4167/3	steve	VBM:2004042901 Fix up javadoc

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/1	steve	VBM:2004042901 Style class rendering fix

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 26-Aug-03	1015/4	geoff	VBM:2003072208 Style Class on spatial iterated pane not set on table cell in generated markup (supermerge fixes)

 08-Aug-03	1015/1	geoff	VBM:2003072208 merge from Mimas

 08-Aug-03	1011/1	geoff	VBM:2003072208 port from metis

 08-Aug-03	1004/1	geoff	VBM:2003072208 fix pane rendering in xhtml from netfront3

 18-Aug-03	1052/4	allan	VBM:2003073101 Update add display inline method.

 17-Aug-03	1052/2	allan	VBM:2003073101 Support styles on menu and menuitems

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 14-Aug-03	1063/1	geoff	VBM:2003081305 Port 2003060909 forward

 24-Jul-03	728/6	adrian	VBM:2003052001 fixedup testcases - removed suite and main methods

 07-Jul-03	728/4	adrian	VBM:2003052001 fixed pane attribute generation

 07-Jul-03	728/2	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	706/1	allan	VBM:2003070302 Added TestSuiteGenerator ant task. Run testsuite in a single jvm

 ===========================================================================
*/

