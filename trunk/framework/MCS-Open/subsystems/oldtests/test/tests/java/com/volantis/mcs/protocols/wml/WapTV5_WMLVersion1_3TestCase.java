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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/WapTV5_WMLVersion1_3TestCase.java,v 1.22 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Oct-02    Allan           VBM:2002100206 - TestCase for WapTV5 protocol
 * 14-Oct-02    Geoff           VBM:2002100905 - Modified to use new DOMPool
 *                              constructor.
 * 03-Dec-02    Sumit           VBM:2002101505 - Removed checks for font element
 *                              in all tests
 * 16-Dec-02    Adrian          VBM:2002100203 - Updated references to
 *                              supportsMultipleStyleClasses to
 *                              supportsMultipleSelectorClasses.
 * 17-Dec-02    Adrian          VBM:2002100313 - Added testGetTextInputFormat
 * 13-Jan-03    Steve           VBM:2002112101 - Added tests for handling missing
 *                              panes on writeImage() method.
 * 13-Jan-03    Allan           VBM:2002120209 - testDoSelectInputAttributes()
 *                              added. Added pageHead member and new set
 *                              methods in My..Protocol inner class to for
 *                              pageHead and supportsAcessKeyAttribute. Added
 *                              getContentBuffer() to same inner class. Added
 *                              ShortCut inner class and override of
 *                              protocol.getTextFromReference() to handle shotcut.
 *                              Reformmated conde to 4 character indents.
 * 14-Jan-03    Steve           VBM:2002112101 - Extended image tests to check
 *                              the tag, src and alt text attributes after
 *                              writing.
 * 27-Jan-03    Doug            VBM:2003012408 - Modified
 *                              testDoSelectInputAccessKey() as it was
 *                              incorrectly assuming the ivalue attribute is
 *                              always set.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes. Also
 *                              removed unnecessary test cases apparently cut
 *                              & pasted from the parent.
 * 17-Feb-02    Sumit           VBM:2003021301 - The results of the changes by
 *                              the above now mean I have to implement the
 *                              testOpenGridRow method here though this VBM
 *                              is not testing this protocol
 * 19-Feb-03    Adrian          VBM:2003010605 - Updated usages of
 *                              ResourceAction to override actionPerformedImpl
 *                              instead of actionPerformed.
 * 21-Feb-03    Byron           VBM:2003021703 - Added
 *                              testAddTableCellAttributesRowColSpan,
 *                              doTestAddTableCellAttributesRowColSpan and
 *                              testAddTableCellAttributesStyles. Added
 *                              WapTV5_WMLVersion1_3StyleMock private inner
 *                              class (only used here).
 * 25-Feb-03    Ian             VBM:2002072605 - Added test for convertColour.
 * 25-Feb-03    Byron           VBM:2003022105 - Added tests for addEnterEvents
 * 07-Mar-03    Sumit           VBM:2003030711 - Added test methods and classes
 *                              to test the connect now meta tag addition
 * 28-Mar-03    Geoff           VBM:2003031711 - Add test for renderAltText,
 *                              and fix ^M problems.
 * 02-Apr-03    Geoff           VBM:2003032609 - Stub out tests added to
 *                              WMLRoot which are not applicable for WapTV.
 * 02-Apr-03    Geoff           VBM:2003032609 - Remove old test stub I forgot.
 * 09-Apr-03    Sumit           VBM:2003032713 - Added test stub for menu item
 *                              separator rendering
 * 16-Apr-03    Geoff           VBM:2003041603 - Add ProtocolException
 *                              declarations where necessary.
 * 24-Apr-03    Chris W         VBM:2003030404 - Added getOutputBufferfactory
 *                              to TestWapTV5_WMLVersion1_3 inner class.
 *                              getExpectedTestOpenLayoutResult added as the
 *                              output of testOpenLayout is different for this
 *                              protocol, than for other wml protocols.
 * 25-Apr-03    Chris W         VBM:2003031905 - getExpectedTestOpenLayoutResult
 *                              added as the output of testOpenLayout is
 *                              different for this protocol, than for other wml
 *                              protocols.
 * 07-May-03    Byron           VBM:2003042208 - Added testOpenCard(),
 *                              testWriteInitialFocus() and
 *                              doWriteInitialFocus.
 * 27-May-03    Byron           VBM:2003051904 - Added createMenuAttributes()
 *                              and templateTestDoMenu(). Updated
 *                              TestWapTV5_WMLVersion1_3 inner class to be able
 *                              to get/set the style.
 * 23-May-03    Mat             VBM:2003042907 - Removed unused DeviceLayoutContext
 *                              import.
 * 30-May-03    Chris W         VBM:2003052702 - getExpectedDissectingPaneMarkup
 *                              overridden so to make tests pass.
 * 21-May-03    Byron           VBM:2003051903 - Added
 *                              testAddTableCellAttributes. Cleaned imports
 *                              unused variables.
 * 22-May-03    Byron           VBM:2003051903 - Fixed comments above.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.DOMAssertionUtilities;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.css.emulator.EmulatorRendererContext;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.wml.css.emulator.styles.WapTV5_WMLVersion1_3Style;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.xml.schema.W3CSchemata;

public class WapTV5_WMLVersion1_3TestCase
    extends WMLVersion1_3TestCase {

    private WapTV5_WMLVersion1_3 protocol;
    private WMLRootTestable testable;

    private TestDOMOutputBuffer buffer;
    private HeadingAttributes attributes;
    private PageHead pageHead;

    /**
     * PageContext for protocol
     */
    private TestMarinerPageContext pageContext;

    /**
     * RequestContext for tests
     */
    private MarinerRequestContext requestContext;

    /** Creates new TestDOMElement */
    public WapTV5_WMLVersion1_3TestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestWapTV5_WMLVersion1_3Factory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (WapTV5_WMLVersion1_3) protocol;
        this.testable = (WMLRootTestable) testable;
    }

    /**
     * Setup the unit test framework by creating some classes ensuring that each
     * test case starts off with a clean slate to work from
     */
    private void privateSetUp() {
        attributes = new HeadingAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        document = domFactory.createDocument();
        pageContext = new TestMarinerPageContext();
        requestContext = new TestMarinerRequestContext();
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        // Strangely, this line is only required in Mimas and not Metis. Weird.
        // I bet Paul knows why. Might be something to do with the
        // ApplicationContext stuff not being in Mimas yet?
        pageContext.pushRequestContext(requestContext);

        protocol.setMarinerPageContext(pageContext);
        pageHead = new PageHead();
        testable.setPageHead(pageHead);
        buffer = new TestDOMOutputBuffer();
        pageContext.setCurrentOutputBuffer(buffer);
        testable.setCurrentBuffer(null, buffer);

        // Add in a protocols configuration so that action input fields do
        // not have exceptions for dollar encoding.
        Volantis v = getVolantis();
        ProtocolsConfiguration pc = v.getProtocolsConfiguration();
        pc.setWmlPreferredOutputFormat("wml");
        pageContext.setVolantis(v);
    }

    /**
     * @todo this DTD is currently incorrect. There is a correct version
     * see VBM:2003090305 and 2002122012
     */
    public void testProtocolHasCorrectDTD() throws Exception {
        super.testProtocolHasCorrectDTD();
    }

    protected String[] expectedDefaultHeading1Elements() {
        return addFontToExpected(super.expectedDefaultHeading1Elements());
    }

    protected String[] expectedDefaultHeading2Elements() {
        return addFontToExpected(super.expectedDefaultHeading2Elements());
    }

    protected String[] expectedDefaultHeading3Elements() {
        return addFontToExpected(super.expectedDefaultHeading3Elements());
    }

    protected String[] expectedDefaultHeading4Elements() {
        return addFontToExpected(super.expectedDefaultHeading4Elements());
    }

    protected String[] expectedDefaultHeading5Elements() {
        return addFontToExpected(super.expectedDefaultHeading5Elements());
    }

    protected String[] expectedDefaultHeading6Elements() {
        return addFontToExpected(super.expectedDefaultHeading6Elements());
    }

    protected String getSelectStyle() {
        return "style=\"normal\" ";
    }

    protected String getSelectPrefix() {
        return "<font id=\"helvetica\" size=\"12\">";
    }

    protected String getSelectSuffix() {
        return "</font>";
    }

    private String[] addFontToExpected(final String[] superExpected) {
        final String[] expected = new String[superExpected.length + 1];
        expected[0] = "font";
        System.arraycopy(superExpected, 0, expected,  1, superExpected.length);
        return expected;
    }

    public void testDoProtocolString() throws Exception {
        privateSetUp();

        StringBuffer expected =
              new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        expected.append( "<!DOCTYPE WML [\n" )
               .append( "<!ENTITY nbsp \"&#160;\">\n" )
               .append( "<!ENTITY quot \"&#34;\">\n" )
               .append( "<!ENTITY amp \"&#38;#38;\">\n" )
               .append( "<!ENTITY apos \"&#39;\">\n" )
               .append( "<!ENTITY lt \"&#38;#60;\">\n" )
               .append( "<!ENTITY gt \"&#62;\">\n" )
               .append( "<!ENTITY shy \"&#173;\">\n" )
               .append( "<!ENTITY eur \"&#xa4;\">\n" )
               .append( "]>" );

        checkDoProtocolString(protocol, expected.toString());
    }

    public void testOpenCanvas() throws Exception {
        privateSetUp();
        CanvasAttributes attributes = new CanvasAttributes();
        protocol.openCanvas( buffer, attributes );
        Element e = buffer.popElement();
        assertEquals( "Incorrect Element", "wml", e.getName() );
        assertEquals( "Incorrect namespace",  "http://waptv.com/xsd/wtvml",
                      e.getAttributeValue( "xmlns") );
        assertEquals( "Incorrect schema instance",
                      W3CSchemata.XSI_NAMESPACE,
                      e.getAttributeValue( "xmlns:xsi") );
        assertEquals( "Incorrect schema location",
                      "http://waptv.com/xsd/wtvml_5.0.xsd",
                      e.getAttributeValue( "xsi:schemaLocation") );
    }

    public void testOpenAction() {
        // stub out parent for now, since actions are different in waptv
        // need to implement this really.
    }

    /**
     * Test openFont() with an empty set of attributes
     * @todo later what is this testing???
     */
    public void testOpenFontEmptyAttributes() {
        privateSetUp();

        protocol.openFont(buffer, attributes, false);
    }

    /**
     * Ensure that if the input has rows set to greater than 1 that the numeric
     * value of the rows is used as the format string.
     */
    public void testAddTextInputValidation() {
        privateSetUp();

        StrictStyledDOMHelper helper = new StrictStyledDOMHelper();

        XFTextInputAttributes attributes = new XFTextInputAttributes();
        attributes.setStyles(StylesBuilder.getCompleteStyles("mcs-rows: 3"));

        Element element = domFactory.createElement("input");
        protocol.addTextInputValidation(element, attributes);

        String output = helper.render(element);
        assertEquals("<input format=\"3\"/>", output);

        super.testAddTextInputValidation();
    }

    /**
     * Test writeImage with no pane defined
     */
    public void testWriteImageNoPane() {
        privateSetUp();

        ImageAttributes ia = new ImageAttributes();
        ia.setPane(null);
        ia.setAlign("left");
        ia.setAltText("AltText");
        ia.setBorder("1");
        ia.setSrc("http://www.images.com/test/test.jpg");
        try {
            protocol.writeImage(ia);
            fail("Should have thrown illegal argument exception.");
        } catch (IllegalArgumentException iae) {
            // Expected as neither the attributes or page context
            // contain a valid pane.
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test writeImage with pane defined in the attributes
     */
    public void testWriteImageAttributePane() throws ProtocolException {
        privateSetUp();

        ImageAttributes ia = new ImageAttributes();
        ia.setStyles(StylesBuilder.getDeprecatedStyles());
        Pane outPane = new Pane(null);
        outPane.setName("MyPane");
        outPane.setDestinationArea(null);
        ia.setPane(outPane);
        ia.setAltText("AltText");
        ia.setSrc("http://www.images.com/test/test.jpg");
        ia.setLocalSrc(false);
        protocol.writeImage(ia);

        // Check the image tag and attributes
        Element nullElement = buffer.popElement();
        Element element = (Element) nullElement.getHead();
        assertEquals("img", element.getName());
        assertNull(element.getAttributeValue("localsrc"));
        assertEquals("http://www.images.com/test/test.jpg",
                element.getAttributeValue("src"));
        assertEquals("AltText", element.getAttributeValue("alt"));
    }

    /**
     * Test writeImage with pane defined in the page context
     */
    public void testWriteImageContextPane() throws ProtocolException {
        privateSetUp();

        ImageAttributes ia = new ImageAttributes();
        ia.setStyles(StylesBuilder.getDeprecatedStyles());
        Pane outPane = new Pane(null);
        outPane.setName("MyPane");

        outPane.setDestinationArea(null);
        pageContext.setCurrentPane(outPane);
        ia.setPane(null);
        ia.setAltText("AltText");
        ia.setSrc("http://www.images.com/test/test.jpg");
        ia.setLocalSrc(false);
        protocol.writeImage(ia);

        // Check the image tag and attributes
        Element nullElement = buffer.popElement();
        Element element = (Element) nullElement.getHead();
        assertEquals("img", element.getName());
        assertNull(element.getAttributeValue("localsrc"));
        assertEquals("http://www.images.com/test/test.jpg",
                element.getAttributeValue("src"));
        assertEquals("AltText", element.getAttributeValue("alt"));
    }

    /**
     * Ensure that valid image tags are generated
     * @throws ProtocolException
     */
    public void testImageValid() throws ProtocolException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Initialise our protocol attributes with an id attribute.
        ImageAttributes attributes = new ImageAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        attributes.setLocalSrc(true);
        attributes.setSrc("myImage.jpg");
        attributes.setAltText("Alternate Text");

        Pane outPane = new Pane(null);
        outPane.setName("MyPane");
        outPane.setDestinationArea(null);
        attributes.setPane(outPane);

        protocol.doImage(buffer, attributes);

        // valid src and alt generates <img src="url" alt="text" />
        Element root = buffer.getCurrentElement();
        Element element = (Element) root.getHead();

        DOMAssertionUtilities.assertElement("img", element);
        DOMAssertionUtilities.assertAttributeEquals("localsrc","myImage.jpg",element);
        DOMAssertionUtilities.assertAttributeEquals("alt","Alternate Text",element);
    }

    /**
     * Ensure that valid image tags are generated when there is no source
     * @throws ProtocolException
     */
    public void testImageNoSrc() throws ProtocolException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        String expected = "Alternate Text";

        // Initialise our protocol attributes with an id attribute.
        ImageAttributes attributes = new ImageAttributes();
        attributes.setLocalSrc(true);
        attributes.setSrc(null);
        attributes.setAltText(expected);

        Pane outPane = new Pane(null);
        outPane.setName("MyPane");
        outPane.setDestinationArea(null);
        attributes.setPane(outPane);

        protocol.doImage(buffer, attributes);

        // No src with alt text generates <span>text</span>
        Element root = buffer.getCurrentElement();
        Text altText = (Text)root.getHead();
        String txtAltText = new String(altText.getContents(),
                0, expected.length());
        assertEquals("Incorrect Text", expected, txtAltText);
    }

    /**
     * Ensure that no output is  generated when there is no source or alt text
     * @throws ProtocolException
     */
    public void testImageNoSrcNoAlt() throws ProtocolException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Initialise our protocol attributes with an id attribute.
        ImageAttributes attributes = new ImageAttributes();
        attributes.setLocalSrc(true);
        attributes.setSrc(null);
        attributes.setAltText("    ");

        Pane outPane = new Pane(null);
        outPane.setName("MyPane");
        outPane.setDestinationArea(null);
        attributes.setPane(outPane);

        protocol.doImage(buffer, attributes);

        // No src and whitespace text generates no output
        Element root = buffer.getCurrentElement();
        Element empty = (Element) root.getHead();
        assertNull("No output should be generated", empty);
    }

    /**
     * Test the image element. If there is an AssetURLSuffix set then it should
     * be appended to the image URL.
     */
    public void testDoImageNoSuffix() throws ProtocolException {
        privateSetUp();
        ImageAttributes ia = new ImageAttributes();
        ia.setStyles(StylesBuilder.getDeprecatedStyles());
        Pane outPane = new Pane(null);
        outPane.setName("MyPane");
        outPane.setDestinationArea(null);
        ia.setPane(outPane);
        ia.setAltText("AltText");
        ia.setSrc("http://www.images.com/test/test.jpg");
        ia.setLocalSrc(false);
        protocol.writeImage(ia);

        // Check the image tag and attributes
        Element nullElement = buffer.popElement();
        Element element = (Element) nullElement.getHead();
        assertEquals("img", element.getName());
        assertNull(element.getAttributeValue("localsrc"));
        assertEquals("http://www.images.com/test/test.jpg",
                element.getAttributeValue("src"));
        assertEquals("AltText", element.getAttributeValue("alt"));
    }

    public void testDoImageSuffix() throws ProtocolException {
        privateSetUp();
        ImageAttributes ia = new ImageAttributes();
        ia.setStyles(StylesBuilder.getDeprecatedStyles());
        Pane outPane = new Pane(null);
        outPane.setName("MyPane");
        outPane.setDestinationArea(null);
        ia.setPane(outPane);
        ia.setAltText("AltText");
        ia.setSrc("http://www.images.com/test/test.jpg");
        ia.setAssetURLSuffix("?name=fred");
        ia.setLocalSrc(false);
        protocol.writeImage(ia);

        // Check the image tag and attributes
        Element nullElement = buffer.popElement();
        Element element = (Element) nullElement.getHead();
        assertEquals("img", element.getName());
        assertNull(element.getAttributeValue("localsrc"));
        assertEquals("http://www.images.com/test/test.jpg?name=fred",
                element.getAttributeValue("src"));
        assertEquals("AltText", element.getAttributeValue("alt"));
    }


    /**
     * Test the adding of cell attributes (specifically the colspan and rowspan
     * values).
     *
     * @throws Exception if any exception are thrown.
     */
    public void testAddTableCellAttributesRowColSpan() throws Exception {

        doTestAddTableCellAttributesRowColSpan(
                "No Value expected for Rowspan", null, null,
                "No Value expected for Colspan", null, null);

        doTestAddTableCellAttributesRowColSpan(
                "No Value expected for Rowspan", null, null,
                "Value should be as", "1", "1");

        doTestAddTableCellAttributesRowColSpan(
                "Value should be as", "2", "2",
                "No Value expected for Colspan", null, null);

        doTestAddTableCellAttributesRowColSpan(
                "Value should be as", "3", "3",
                "Value should be as", "4", "4");

    }

    /**
     * Helper method for testing the cell attributes generation for adding
     * table cells.
     *
     * @param msgRow          the msg to display for the rowspan test
     * @param rowspan         the value to be used to set the rowspan
     * @param expectedRowspan the expected result for the rowspan
     * @param msgCol          the msg to display for the colspan test
     * @param colspan         the value to be used to set the colspan
     * @param expectedColspan the expected result for the colspan
     */
    protected void
            doTestAddTableCellAttributesRowColSpan(String msgRow,
                                                   String rowspan,
                                                   String expectedRowspan,
                                                   String msgCol,
                                                   String colspan,
                                                   String expectedColspan) {
        Element element = domFactory.createElement();
        TableCellAttributes tableCellAttributes = new TableCellAttributes();
        tableCellAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        tableCellAttributes.setColSpan(colspan);
        tableCellAttributes.setRowSpan(rowspan);
        protocol.addTableCellAttributes(element, tableCellAttributes);
        assertEquals(msgCol, expectedColspan, element.getAttributeValue("colspan"));
        assertEquals(msgRow, expectedRowspan, element.getAttributeValue("rowspan"));
    }

    /**
     * Test the adding of cell attributes (specifically the style attributes).
     *
     * We can test that style is applied to most properties by setting values
     * on various attributes on a {@link StyleProperties}.
     *
     * @throws Exception if any exception are thrown.
     */
    public void testAddTableCellAttributesStyles() throws Exception {
        privateSetUp();
        TestWapTV5_WMLVersion1_3 wapProtocol =
                (TestWapTV5_WMLVersion1_3)protocol;

        wapProtocol.setEmulatorRendererContext(
                new EmulatorRendererContext(requestContext,
                        CSSStyleSheetRenderer.getSingleton(), protocol,
                        protocol.getProtocolConfiguration().getCssVersion()));

        TableCellAttributes tableCellAttributes = new TableCellAttributes();
        tableCellAttributes.setStyles(StylesBuilder.getStyles(
                "{" +
                "mcs-line-gap: 1px; " +
                "background-color: #0000ff; " +
                "background-image: url(background.jpg); " +
                "mcs-corner-radius: 1px; " +
                "text-align: right; " +
                "white-space: pre; " +
                "width: 1px; " +
                "height: 1px; " +
                "}" +
                "*:active {" +
                "background-color: #0000ff; " +
                "}"));

        Styles styles = tableCellAttributes.getStyles();

        // we need to override the style with this mock style only for testing
        // the bgimage as the actual addBackgroundImage in the style attempts
        // to perform methods on a null Volantis object.
        WapTV5_WMLVersion1_3Style style =
                new WapTV5_WMLVersion1_3Style(
                        styles.getPropertyValues(), wapProtocol);
        wapProtocol.setStyle(style);
        wapProtocol.setActiveStyle(new WapTV5_WMLVersion1_3Style(
                styles.findNestedStyles(StatefulPseudoClasses.ACTIVE)
                .getPropertyValues(), wapProtocol));
        wapProtocol.setFormatStyle(new WapTV5_WMLVersion1_3Style(
                styles.findNestedStyles(StatefulPseudoClasses.ACTIVE)
                .getPropertyValues(), wapProtocol));

        Element element = domFactory.createElement();
        protocol.addTableCellAttributes(element, tableCellAttributes);
        assertEquals("1", element.getAttributeValue("linegap"));
        assertEquals("#0000ff", element.getAttributeValue("bgcolor"));
        assertEquals("background.jpg", element.getAttributeValue("bgimage"));
        assertEquals("1", element.getAttributeValue("bgradius"));
        assertEquals("r", element.getAttributeValue("align"));
        assertEquals("nowrap", element.getAttributeValue("mode"));
        assertEquals("1", element.getAttributeValue("width"));
        assertEquals("1", element.getAttributeValue("height"));
/*

        // change the property values while keeping the same pseudo style entity styles
        StyleLength styleLength =
            StyleValueFactory.getDefaultInstance().getLength(
                null, 1, LengthUnit.PX);
        normalProperties = createPropertyValues();
        normalProperties.setSynthesizedValue(
                StylePropertyDetails.MCS_LINE_GAP, styleLength);
        styles.setPropertyValues(normalProperties);

        wapProtocol.setStyle(null);

        tableCellAttributes.setStyles(styles);

        element = domFactory.createElement();
        protocol.addTableCellAttributes(element, tableCellAttributes);
        assertEquals("#0000ff", element.getAttributeValue("bgactivated"));
        assertEquals(null, element.getAttributeValue("height"));

        // now recreate the styles so that the red bg properties are associated
        // with the FOCUS pseudoClass
        styles = new StylesMock(normalProperties);
        styles.setNestedStyles(StatefulPseudoClasses.ACTIVE, emptyPropsStyles);
        styles.setNestedStyles(StatefulPseudoClasses.FOCUS, redBGPropsStyles);

        wapProtocol.setActiveStyle(emptyStyle);
        wapProtocol.setFormatStyle(redBGStyle);

        tableCellAttributes.setStyles(styles);

        element = domFactory.createElement();
        protocol.addTableCellAttributes(element, tableCellAttributes);
        assertEquals("#0000ff", element.getAttributeValue("bgfocused"));
        assertEquals(null, element.getAttributeValue("width"));
*/
    }

    /**
     * Test the adding of enter events for WapTV5 (specifically the
     * onenterforward and onenterbackward events)
     */
    public void testAddEnterEvents() throws Exception {
        privateSetUp();

        CanvasAttributes canvasAttributes = new CanvasAttributes();
        protocol.addEnterEvents(buffer, canvasAttributes);
        assertEquals("", DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder()));

        EventAttributes events = canvasAttributes.getEventAttributes (false);
        events.reset();
        events.setEvent(EventConstants.ON_ENTER_BACKWARD, "eventBackward");
        events.setEvent(EventConstants.ON_ENTER_FORWARD, "eventForward");
        protocol.addEnterEvents(buffer, canvasAttributes);
        assertEquals("<onevent type=\"onenterforward\">eventForward</onevent>" +
                     "<onevent type=\"onenterbackward\">eventBackward</onevent>",
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));
    }

    /**
     * Test the adding of enter events for WapTV5 (specifically the
     * onenterforward and onenterbackward events)
     */
    public void testAddEnterEventsOnlyBackward() throws Exception {
        privateSetUp();

        CanvasAttributes canvasAttributes = new CanvasAttributes();
        protocol.addEnterEvents(buffer, canvasAttributes);
        assertEquals("", DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder()));

        EventAttributes events = canvasAttributes.getEventAttributes (false);
        events.reset();
        events.setEvent(EventConstants.ON_ENTER_BACKWARD, "eventBackward");
        protocol.addEnterEvents(buffer, canvasAttributes);
        assertEquals("<onevent type=\"onenterbackward\">eventBackward</onevent>",
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));

    }

    /**
     * Test the adding of enter events for WapTV5 (specifically the
     * onenterforward and onenterbackward events when their script component
     * matches). In this case the onenter must be used only.
     */
    public void testAddEnterEventsUseOnEnterOnly() throws Exception {
        privateSetUp();

        CanvasAttributes canvasAttributes = new CanvasAttributes();
        EventAttributes events = canvasAttributes.getEventAttributes (false);
        events.reset();
        events.setEvent(EventConstants.ON_ENTER_BACKWARD, "Script");
        events.setEvent(EventConstants.ON_ENTER_FORWARD, "Script");
        protocol.addEnterEvents(buffer, canvasAttributes);
        assertEquals("<onevent type=\"onenter\">Script</onevent>",
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));
    }


    public void testDoActionInputWithoutForm() throws ProtocolException {
        privateSetUp();
        testable.setPageHead(new TestPageHead(protocol));
        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());
        attributes.setFormAttributes(null);
        attributes.setName("TestAction");
        protocol.doActionInput(buffer, attributes);
        assertTrue(buffer.getCurrentElement().getName()==null);
        assertNull(protocol.getPageHead().getAttribute("wtv_connect_now"));
    }

    protected Element getSelectElement(Element element) {
        assertEquals("Root element is font", "font", element.getName());
        assertTrue("Should only be one child",
                element.getHead().getNext() == null);

        assertTrue("Child should be element",
                element.getHead() instanceof Element);

        return (Element) element.getHead();
    }

    public void testDoActionInputWithForm() throws ProtocolException {
        privateSetUp();
        testable.setPageHead(new TestPageHead(protocol));
        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        XFFormAttributes formAttributes = new XFFormAttributes();
        Form form = new Form(new CanvasLayout());
        FormInstance formInstance = new FormInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(form);
        formAttributes.setAction(new LiteralLinkAssetReference("testaction"));
        formAttributes.setFormData(formInstance);
        formAttributes.setMethod("post");
        formAttributes.setFormSpecifier("test");
        attributes.setFormAttributes(formAttributes);
        attributes.setName("TestAction");
        attributes.setType("submit");
        protocol.doActionInput(buffer, attributes);
        Element element = buffer.getCurrentElement();
        assertEquals(null, element.getName());
        document.addNode(element);
        try {
          String br = DOMUtilities.toString(
                  document, protocol.getCharacterEncoder());
          assertEquals("<do name=\"TestAction\" type=\"accept\">" +
                       "<go href=\"MarinerFFP\" method=\"post\">" +
                       "<postfield name=\"vform\" value=\"test\"/>" +
                       "<postfield name=\"TestAction\" value=\" \"/>" +
                       "</go></do>", br);
        }
        catch(java.lang.Exception e) {
            fail(e.toString());
        }
        assertNotNull(protocol.getPageHead().getAttribute("wtv_connect_now"));
     }

    /**
     * Test addPostField puts the correct element in the output buffer when
     * we pass it just a clientVariableName XFImplicit attributes.
     */
    public void testAddPostFieldClientVariableName() throws Exception {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        buffer = new TestDOMOutputBuffer();
        buffer.initialise();
        document = domFactory.createDocument();

        attributes.setName("name");
        attributes.setClientVariableName("clientVariableName");
        protocol.addPostField(buffer, attributes);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        // The same like in WMLRootTestCase.java
        // Here I replaced $ with WMLVariable.WML_NOBRACKETS -because for variable encoding
        // $ character is not expected. Modification was possible because main role
        // of test is to examine if XFImplicitAttributes is build correctly 
        assertEquals("Wrong output in postfield element",
            "<postfield name=\"name\" " +
            "value=\""+WMLVariable.WMLV_NOBRACKETS+"clientVariableName"
            +WMLVariable.WMLV_NOBRACKETS+"\"/>", output);
    }

    /**
     * Test addPostField puts the correct element in the output buffer when
     * we pass it clientVariableName and value XFImplicit attributes.
     */
    public void testAddPostFieldClientVariableNameAndValueAttributes()
             throws Exception {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        // Test that clientVariableName takes precedence over value
        buffer = new TestDOMOutputBuffer();
        buffer.initialise();
        document = domFactory.createDocument();

        attributes.setName("name");
        attributes.setValue("value");
        attributes.setClientVariableName("clientVariableName");
        protocol.addPostField(buffer, attributes);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        // Here I replaced $ with WMLVariable.WML_NOBRACKETS -because for variable encoding
        // $ character is not expected. Modification was possible because main role
        // of test is to examine if XFImplicitAttributes is build correctly 
        // in this case if clientVariableName takes precedence over value 
        // as it was written above
        assertEquals("Wrong output in postfield element",
            "<postfield name=\"name\" " +
            "value=\""+WMLVariable.WMLV_NOBRACKETS+"clientVariableName"
            +WMLVariable.WMLV_NOBRACKETS+"\"/>", output);

    }

    // javadoc inherited
    protected MenuAttributes createMenuAttributes() {
        MenuAttributes menuAttributes = super.createMenuAttributes();
        menuAttributes.setPane(new Pane(new CanvasLayout()));
        return menuAttributes;
    }

    /**
     * Get the name of the menu element for the protocol under test.
     * @return The name of the menu element for the protocol under test.
     */
    protected String getMenuElementName() {
        return "font";
    }

    // javadoc inherited
    protected void templateTestDoMenuHorizontal(StringBuffer buf,
                                                MenuItem menuItem,
                                                boolean hasNext) {
        String shortcut = "accept";
        if (menuItem.getShortcut() != null) {
            shortcut = menuItem.getShortcut().getText(TextEncoding.PLAIN);
        }
        buf.append("<do label =\"" + menuItem.getText() + "\" localsrc=\"image\"");
        buf.append(" name=\"123\" type=\"" + shortcut + "\">");
        buf.append("<go href=\"" + menuItem.getHref() + "\"/>");
        buf.append("</do>");
    }
    /**
     * Return the expected result when testOpenLayout() is run. We get
     * different results in WMLRoot and WapTV5_WMLVersion1_3
     * @return String
     */
    protected String getExpectedTestOpenLayoutResult() {
        return "<card title=\"title\">" +
                 "<onevent type=\"ontimer\">" +
                   "<go href=\"http://www.my.com\"/>" +
                 "</onevent>" +
                 "<onevent/>" +
                 "<timer/>" +
// TODO: re-enable this when we fix emulate emphasis
//                 "<p align=\"center\">" +
//                   "<devicePolicyValue>" +
//                     "devicePolicyValuetitledevicePolicyValue" +
//                   "</devicePolicyValue>" +
//                 "</p>" +
                   "<beforebody/>" +
                 "<p/>" +
               "</card>";
    }

    public void testSingleColumnGridRowAlign() throws Exception {
        // WapTV renders single column grids as normal, so we can stub this.
    }

    public void testSingleColumnGridPaneAlign() throws Exception {
        // WapTV renders single column grids as normal, so we can stub this.
    }

    /**
     * Test the opening of the card element. Note that this test calls the
     * WMLRoot's method.
     */
    public void testOpenCard() throws ProtocolException {

        privateSetUp();
        pageContext.pushDeviceLayoutContext(new TestDeviceLayoutContext() {
            public OutputBuffer getOutputBuffer(String name, boolean create) {
                return buffer;
            }
        });
        assertNull(protocol.initialFocusElement);
        CanvasAttributes canvasAttributes = new CanvasAttributes();
        canvasAttributes.setStyles(StylesBuilder.getInitialValueStyles());
        protocol.openCard(buffer, canvasAttributes);
        assertNotNull(protocol.initialFocusElement);

        Element element = buffer.popElement();
        assertEquals("card", element.getName());
        assertNull(element.getAttributeValue("id"));

        canvasAttributes.setId("idValue");
        protocol.openCard(buffer, canvasAttributes);
        element = buffer.popElement();
        assertEquals("card", element.getName());
        assertEquals("idValue", element.getAttributeValue("id"));
    }

    protected String getExpectedCardAttributes() {
        return " scroll=\"false\"";
    }

    /**
     * Test the writing of the writeIntialFocus method.
     */
    public void testWriteInitialFocus() throws Exception {
        doWriteInitialFocusTest(null, null, null);
        doWriteInitialFocusTest(domFactory.createElement(), null, null);
        doWriteInitialFocusTest(null, "Index for tab", null);
        doWriteInitialFocusTest(domFactory.createElement(), "Index for tab", "Index for tab");
    }

    /**
     * Suporting method for testing the writeInitialFocus method.
     *
     * @param element  the element that has been 'stored' in the protocol.
     * @param tabindex the tab index value.
     * @param expected the expected output
     */
    private void doWriteInitialFocusTest(Element element,
                                         String tabindex,
                                         String expected) {
        protocol.initialFocusElement = element;
        protocol.writeInitialFocus(tabindex);
        assertEquals(protocol.initialFocusElement, element);
        if (element != null) {
            assertEquals(element.getAttributeValue("tabindex"), expected);
        }
    }

    /**
     * Test the adding of table cell attributes.
     */
    public void testAddTableCellAttributes() throws Exception {
        Element element = domFactory.createElement();

        TableCellAttributes attributes = new TableCellAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        protocol.addTableCellAttributes(element, attributes);

        assertEquals(protocol.supportsTabindex, true);
        String value = "test_value";
        attributes.setTabindex(value);
        protocol.addTableCellAttributes(element, attributes);
        assertEquals(element.getAttributeValue("tabindex"), value);
        assertNull(element.getAttributeValue("colspan"));
        assertNull(element.getAttributeValue("rowspan"));

        attributes.setColSpan(value);
        protocol.addTableCellAttributes(element, attributes);
        assertEquals(element.getAttributeValue("tabindex"), value);
        assertEquals(element.getAttributeValue("colspan"), value);
        assertNull(element.getAttributeValue("rowspan"));

        attributes.setRowSpan(value);
        protocol.addTableCellAttributes(element, attributes);
        assertEquals(element.getAttributeValue("tabindex"), value);
        assertEquals(element.getAttributeValue("colspan"), value);
        assertEquals(element.getAttributeValue("rowspan"), value);
    }

    public void testCloseDissectingPane() throws Exception {
        // stubbed out for now since new menu code doesn't support WapTV yet.
        // NOTE: this implies the method below is not used but it is left in
        // as example of how it used to work.
        // todo: re-enable when WapTV is supported by new menu code.
    }

    /**
     * Tests that a row of a spatial format iterator is contained within
     * &lt;tr&gt; tags for this protocol.
     */
    public void testSpatialFormatIteratorRowContainingTag() throws Exception {
        privateSetUp();

        MarinerRequestContext requestContext
                = new TestMarinerRequestContext();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        protocol.setMarinerPageContext(context);

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        context.setCurrentOutputBuffer(buffer);

        SpatialFormatIteratorAttributes attributes =
                new SpatialFormatIteratorAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        Pane pane = new Pane(canvasLayout);
        attributes.setFormat(pane);

        // Open and then close the row.
        protocol.writeOpenSpatialFormatIteratorRow(attributes);
        protocol.writeCloseSpatialFormatIteratorRow(attributes);

        // Retrieve and test the row's containing tag.
        String containingTag = DOMUtilities.toString(buffer.getRoot());
        assertEquals("Spatial iterator row should be contained within <tr> " +
                "tags", containingTag, "<tr/>");
    }

    // javadoc inherited
    protected String getExpectedDissectingPaneMarkup(DissectingPane pane, DissectingPaneAttributes attr) {
        String expected =
          "<" + DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT + "/>" +
          "<" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">" +
              "<p mode=\"wrap\">" +
                "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
                  "<do label=\"" + attr.getLinkText() + "\" " +
                      "name=\"123\" type=\"" + pane.getNextShardShortcut() + "\">" +
                      "<go href=\"" + DissectionConstants.URL_MAGIC_CHAR + "\"/>" +
                  "</do>" +
                "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
                "<" + DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT + "/>" +
                "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
                  "<do label=\"" + attr.getBackLinkText() + "\" " +
                      "name=\"123\" type=\"" + pane.getPreviousShardShortcut() + "\">" +
                      "<go href=\"" + DissectionConstants.URL_MAGIC_CHAR + "\"/>" +
                  "</do>" +
                "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
              "</p>" +
          "</" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">";
        return expected;
    }

    private static class TestPageHead extends PageHead {
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();
        public TestPageHead(WapTV5_WMLVersion1_3 protocol) {
        }

        public OutputBuffer getHead() {
            // TODO Auto-generated method stub
            return buffer;
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

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/5	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/6	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/3	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/9	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 18-Aug-05	9007/4	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Aug-05	9187/1	tom	VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file] - second attempt

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 23-Jun-05	8483/7	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/2	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 07-Jun-05	8637/1	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 16-Mar-05	7372/4	emma	VBM:2005031008 Modifications after review

 16-Mar-05	7372/2	emma	VBM:2005031008 Make cols attribute optional on the XDIME table element

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-Nov-04	6076/7	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 20-Sep-04	5575/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 20-Sep-04	5527/3	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 20-Sep-04	5527/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 06-Aug-04	5132/1	pcameron	VBM:2004080313 Spatial format iterator row renders correctly for WML protocols

 06-Aug-04	5100/3	pcameron	VBM:2004080313 Spatial format iterator row renders correctly for WML protocols

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 12-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 29-Jun-04	4733/4	allan	VBM:2004062105 Rework issues.

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 28-Jun-04	4685/36	steve	VBM:2004050406 Remove empty span around alt text

 28-Jun-04	4685/34	steve	VBM:2004050406 Remove empty span around alt text

 11-Jun-04	4676/1	steve	VBM:2004050406 Remove span from whitespace alt text

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 14-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 05-May-04	4167/3	steve	VBM:2004042901 Fix up javadoc

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 28-Jun-04	4676/5	steve	VBM:2004050406 supermerged

 11-Jun-04	4676/1	steve	VBM:2004050406 Remove span from whitespace alt text

 04-May-04	4117/2	steve	VBM:2004042901 Style class rendering fix

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 25-Mar-04	3386/1	steve	VBM:2004030901 Supermerged and merged back with Proteus

 12-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config
 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 04-May-04	4117/2	steve	VBM:2004042901 Style class rendering fix

 30-Apr-04	4096/1	mat	VBM:2004042809 Make DOMProtocol object pools configurable

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 30-Apr-04	4096/1	mat	VBM:2004042809 Make DOMProtocol object pools configurable

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 05-Apr-04	3559/5	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA
 23-Mar-04	3559/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 23-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 09-Mar-04	3366/1	geoff	VBM:2004022712 Image component fallback to text is wrapped in a paragraph element on WML

 12-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 09-Mar-04	3364/1	geoff	VBM:2004022712 Image component fallback to text is wrapped in a paragraph element on WML

 19-Dec-03	2275/1	steve	VBM:2003121601 Dollar encoding in WAP TV - Merged from Proteus2

 19-Dec-03	2263/1	steve	VBM:2003121601 Dollar encoding in WAP TV

 31-Oct-03	1184/1	geoff	VBM:2003081901 spaces appearing in rendered page (supermerge)

 26-Oct-03	1648/1	steve	VBM:2003090305 WapTV Doctype header

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 04-Sep-03	1331/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 04-Sep-03	1355/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 04-Sep-03	1331/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 05-Apr-04	3559/5	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 23-Mar-04	3559/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 23-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 12-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 09-Mar-04	3364/1	geoff	VBM:2004022712 Image component fallback to text is wrapped in a paragraph element on WML

 19-Dec-03	2263/1	steve	VBM:2003121601 Dollar encoding in WAP TV

 29-Oct-03	1683/3	steve	VBM:2003090305 Fixed merge problems

 26-Oct-03	1648/1	steve	VBM:2003090305 WapTV Doctype header

 04-Sep-03	1355/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 04-Sep-03	1331/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 17-Aug-03	1052/2	allan	VBM:2003073101 Support styles on menu and menuitems

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 13-Jun-03	399/1	mat	VBM:2003060503 Handle new special dissection tags

 06-Jun-03	208/9	byron	VBM:2003051903 Commit after conflict resolution - part 2

 06-Jun-03	208/6	byron	VBM:2003051903 Commit after conflict resolution

 ===========================================================================
*/
