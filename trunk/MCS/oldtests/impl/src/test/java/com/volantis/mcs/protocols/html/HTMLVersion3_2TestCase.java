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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/HTMLVersion3_2TestCase.java,v 1.6 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Nov-02    Adrian          VBM:2002111109 - Created this class as a
 *                              testcase for HTMLVersion3_2
 * 19-Nov-02    Adrian          VBM:2002111109 - updated tests to reflect
 *                              change to openFont and closeFont.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and so it
 *                              uses the new TestMariner...Context classes
 *                              rather than a "cut & paste" inner classes
 *                              which extend Mariner...Context. Then, add the
 *                              test for xfaction not generating accesskey.
 * 28-Mar-03    Geoff           VBM:2003031711 - Add test for renderAltText.
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Changed inheritance to
 *                              accurately reflect the shadowed protocol
 *                              hierarchy.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.styling.StylesBuilder;

import java.util.HashMap;

/**
 * This class unit test the HTMLVersion3_2class.
 */
public class HTMLVersion3_2TestCase
        extends HTMLRootTestAbstract {

    private HTMLVersion3_2 protocol;

    public HTMLVersion3_2TestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();

        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestHTMLVersion3_2Factory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (HTMLVersion3_2) protocol;
    }

    /**
     * Get the name of the menu element for the protocol under test.
     * @return The name of the menu element for the protocol under test.
     */
    protected String getMenuElementName() {
        return "font";
    }

    // javadoc inherited
    public void testSupportsStyleSheets() throws Exception {
        assertFalse("This protocol should not support stylesheets.",
                    protocol.supportsStyleSheets());
    }

    public void testAddClassAttribute() {
        // Nothing to test: no style support
    }

    public void testNoneTransformation() {
        // Nothing to test: no style support
    }

    public void testSelectorTransformation() {
        // Nothing to test: no style support
    }

    public void testAttributeTransformation() {
        // Nothing to test: no style support
    }

    /**
     * This tests that when the styles associated with an inclusion include
     * a background color, the inclusion is rendered as a table.
     * <p>
     * This was added as a consequence of the problem reported in vbm
     * 2004102801.
     * <p>
     * A test for standard inclusion markup can be found in
     * {@link #testStandardInclusionMarkup}.
     */
/* todo XDIME-CP Fix now that emulation is done later
    public void testTableInclusionMarkup() throws Exception {
        // Test data used for this test
        final String domText = "Some text in the DOM...";

        // Setup the test
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        protocol.setInclusion(true);

        // Create a buffer for the protocol to use
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        // Setup theme and style information via the attributes
        CanvasAttributes attributes = new CanvasAttributes();

        // Create a styles with populated properties and set it on the attributes
        attributes.setStyles(StylesBuilder.getStyles(
                "font-size: xx-large; background-color: #0000ff"));

        // Open the inclusion
        protocol.openInclusion(buffer, attributes);
        // Add some content to the DOM so the open/close elements are not
        // transformed away because of empty content
        buffer.writeText(domText);
        // Close the inclusion
        protocol.closeInclusion(buffer, attributes);

        // Ensure that the output is as expected
        String output = DOMUtilities.toString(buffer.getRoot());
        String expected =
                "<table>" +
                    "<tr>" +
                        "<td bgcolor=\"" + "#0000ff" + "\">" +
                            "<font size=\"7\">" +
                            domText +
                            "</font>" +
                        "</td>" +
                    "</tr>" +
                "</table>";
        assertEquals("The output should match that expected", expected, output);
    }
*/

    /**
     * This tests that when the styles associated with an inclusion does not
     * include a background color, the inclusion is rendered as normal (as a
     * div).
     * <p>
     * This was added as a consequence of the problem reported in vbm
     * 2004102801.
     * <p>
     * A test for inclusion markup where there is a background colour can
     * be found in {@link #testTableInclusionMarkup}.
     */
/* todo XDIME-CP Fix now that emulation is done later
    public void testStandardInclusionMarkup() throws Exception {
        // Test data used for this test
        final String domText = "Some text in the DOM...";

        // Setup the test
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        protocol.setInclusion(true);

        // Create a buffer for the protocol to use
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        // Setup theme and style information via the attributes
        CanvasAttributes attributes = new CanvasAttributes();

        // Create a styles with populated properties and set it on the attributes
        attributes.setStyles(StylesBuilder.getStyles("font-size: xx-large"));

        // Open the inclusion
        protocol.openInclusion(buffer, attributes);
        // Add some content to the DOM so the open/close elements are not
        // transformed away because of empty content
        buffer.writeText(domText);
        // Close the inclusion
        protocol.closeInclusion(buffer, attributes);

        // Ensure that the output is as expected
        String output = DOMUtilities.toString(buffer.getRoot());
        String expected =
                "<div>" +
                    "<font size=\"7\">" +
                    domText +
                    "</font>" +
                "</div>";

        assertEquals("The output should match that expected", expected, output);
    }

    /**
     * Create a pane with a style and open it.  Check that the <td> tag
     * gets the width and align attributes set.
     */
/* todo XDIME-CP Fix now that emulation is done later
public void testOpenPane() {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        Element el = null;

        Pane pane = null;
        PaneAttributes attributes = null;

        pane = new Pane(null);
        pane.setName("test");
        attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "width: 60.0px; " +
                "text-align: center"));
//        attributes.setStyleClass("testStyle");
        attributes.setPane(pane);

        // Set up the required contexts
        testable.setStyleSheetRenderer(CSSStyleSheetRenderer.getSingleton());
        String deviceName = "PC-Win32-IE5.5";
        pageContext.setDeviceName(deviceName);
        protocol.setMarinerPageContext(pageContext);
        PaneInstance paneInstance = new TestPaneInstance();
        paneInstance.setStyleClass("testStyle");
        pageContext.setFormatInstance(paneInstance);

        TestDeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        deviceContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS,
                paneInstance);
        pageContext.pushDeviceLayoutContext(deviceContext);
        canvasLayout = new Layout(LAYOUT_NAME, DEVICE_NAME);
        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        deviceContext.setDeviceLayout(runtimeDeviceLayout);
        testable.setPageHead(new MyPageHead(protocol));
        pageContext.setDeviceName(deviceName);

        // Open the containing <td> element which will receive the width and
        // align attributes.
        buffer.openStyledElement("td", attributes);

        protocol.openPane(buffer, attributes);
        // Check the td element.
        el = buffer.getCurrentElement();
        // First we need to find it!
        while (!el.getName().equals("td")) {
            buffer.closeElement(el.getName());
            el = buffer.getCurrentElement();
        }
        el = buffer.closeElement("td");
        /// todo XDIME-CP fix this
        assertEquals("60", el.getAttributeValue("width"));
        assertEquals("center", el.getAttributeValue("align"));
    }
*/

    public void testShardLinkStyleClass() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testActionInput() throws Exception {
        // ensure that the xfaction generates without accesskey or style.
        checkActionInput(false, false);
    }

    public void testNoDeviceThemeAndNoAltClassName() {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testDoProtocolString() {
        // stub out XMTML version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    // Javadoc inherited.
    protected String getExpectedHorizontalRuleMarkup() {
        return "<hr align=\"left\" noshade=\"true\" size=\"5\" width=\"100\"/>";
    }

    public void testOptGroupsNestingWithDefaultSelect() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testNoOptGroupsWithControlSelect() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testDefaultSingleSelectOptionsSelected() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testDefaultMultiSelectOptionsSelected() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testDefaultMultiSelect() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testDefaultSingleSelect() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testControlSingleSelectOptionsSelected() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testControlSingleSelect() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testControlMultiSelect() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testControlMultiSelectOptionsSelected() throws Exception {
        // stub out XMTML style aware version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    /**
     * Ensure that valid image tags are generated when there is no source
     */
    public void testImageNoSrc() throws ProtocolException {
    }


    /**
     * Test the open/close Style methods.
     */
/* @todo XDIME-CP Fix now that emulation is done later
public void testStyle() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        // Create a styles with populated properties and set it on the attributes
        MutablePropertyValues propertyValues =
                DefaultPropertyValueFactory.create();
        propertyValues.setSynthesizedValue(StylePropertyDetails.FONT_WEIGHT,
                createKeywordStyleValue(
                        FontWeightKeywordMapper.getSingleton(), "bold"));
        Styles styles = new StylesMock(propertyValues);

        protocol.styleEmulationRenderer.open(buffer, styles);
        assertEquals("Should have <b> and </b> tag", "<b/>",
                bufferToString(buffer));

        buffer.clear();
        propertyValues.setSynthesizedValue(StylePropertyDetails.FONT_WEIGHT, null);

        protocol.styleEmulationRenderer.open(buffer, styles);
        assertEquals("Should NOT have <b> and </b> tag", "",
                bufferToString(buffer));
    }*/

    /**
     * Test the open style with many styles set
     */
/* @todo XDIME-CP Fix now that emulation is done later
public void testStyleManySet() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        StyleBitSet textDecoration = styleValueFactory.getBitSet(0,
                TextDecorationEnumeration.__MAX,
                StyleValue.PRIORITY_NORMAL);
        textDecoration.setBit(TextDecorationIndexMapper.getBitSetIndex(
                TextDecorationEnumeration.LINE_THROUGH));
        textDecoration.setBit(TextDecorationIndexMapper.getBitSetIndex(
                TextDecorationEnumeration.UNDERLINE));

        StyleColor color = new StyleColor();
        color.setName(StyleColorNames.RED);

        // Create a styles with populated properties and set it on the attributes
        MutablePropertyValues propertyValues =
                DefaultPropertyValueFactory.create();

        propertyValues.setSynthesizedValue(StylePropertyDetails.FONT_WEIGHT,
                createKeywordStyleValue(
                        FontWeightKeywordMapper.getSingleton(), "bold"));
        propertyValues.setSynthesizedValue(StylePropertyDetails.FONT_SIZE,
                createKeywordStyleValue(
                        FontSizeKeywordMapper.getSingleton(), "xx-large"));
        propertyValues.setSynthesizedValue(StylePropertyDetails.FONT_STYLE,
                new StyleKeyword(FontStyleEnumeration.ITALIC));
        propertyValues.setSynthesizedValue(StylePropertyDetails.FONT_FAMILY,
                new StyleKeyword(FontFamilyEnumeration.SANS_SERIF));

        propertyValues.setSynthesizedValue(StylePropertyDetails.TEXT_DECORATION,
                textDecoration);
        propertyValues.setSynthesizedValue(StylePropertyDetails.COLOR, color);
        propertyValues.setSynthesizedValue(StylePropertyDetails.TEXT_ALIGN,
                new StyleKeyword(TextAlignEnumeration.RIGHT));
        Styles styles = new StylesMock(propertyValues);

        protocol.styleEmulationRenderer.open(buffer, styles);
        protocol.styleEmulationRenderer.close(buffer, styles);
        verifyStyleManySet(bufferToString(buffer));
    }*/

    /**
     * Helper method which may be overridden by specialisations of this protocol.
     */
    protected void verifyStyleManySet(String actual) {
        assertEquals("Markup should match",
                "<font color=\"red\" size=\"7\">" +
                    "<b>" +
                        "<i>" +
                            "<u>" +
                                "<strike/>" +
                            "</u>" +
                        "</i>" +
                    "</b>" +
                "</font>",
                actual);
    }

    /**
     * Test the style with text alignment.
     */
/* @todo XDIME-CP Fix now that emulation is done later
    public void testStyleTextAlign() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        // Create a styles with populated properties and set it on the attributes
        ParagraphAttributes attributes = new ParagraphAttributes();
        Styles styles = StylesBuilder.getStyles(
                        "text-decoration: underline; " +
                        "font-weight: 700; " +
                        "font-style: oblique; " +
                        "color: blue; " +
                        "font-size: large; " +
                        "text-align: center; " +
                        "vertical-align: top; " +
                        "padding-top: 10px; " +
                        "border-spacing: 15pc 15pc; " +
                        "border-top-width: 20pc; " +
                        "width: 25pc; " +
                        "height: 30pc; " +
                        "background-color: yellow;");
        attributes.setStyles(styles);

        PropertyValues propertyValues = styles.getPropertyValues();
        Style style = new HTMLVersion3_2Style(propertyValues, protocol);
        testable.setStyle(attributes, style);

        protocol.openParagraph(buffer, attributes);
        protocol.closeParagraph(buffer, attributes);

        verifyStyleAlign(bufferToString(buffer));
    }
*/


    /**
     * Helper method which may be overridden by specialisations of this protocol.
     */
    protected void verifyStyleAlign(String actual) {
        assertEquals("Markup should match: ",
                "<p align=\"center\">" +
                    "<font color=\"blue\" size=\"5\">" +
                        "<b>" +
                            "<i>" +
                                "<u/>" +
                            "</i>" +
                        "</b>" +
                    "</font>" +
                "</p>",
                actual);
    }

    /**
     * Test that vertical align is added to the td element.
     */
/* @todo XDIME-CP Fix now that emulation is done later
    public void testStyleVerticalAlignTD() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        TableAttributes attributes = new TableAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        protocol.openTable(buffer, attributes);

        TableCellAttributes tableCellAttributes = new TableCellAttributes();
        tableCellAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        protocol.openTableDataCell(buffer, tableCellAttributes);

        ImageAttributes imageAttributes = new ImageAttributes();
        imageAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
        imageAttributes.setSrc("images/background.jpg");

        // Create a styles with populated properties and set it on the attributes
        MutablePropertyValues propertyValues = createPropertyValues();
        Styles styles = new StylesMock(propertyValues);
        // Create standard properties specifically for the image element.
        MutablePropertyValues imagePropertyValues = createPropertyValues();
        imagePropertyValues.setSynthesizedValue(
                StylePropertyDetails.VERTICAL_ALIGN, createKeywordStyleValue(
                        VerticalAlignKeywordMapper.getSingleton(), "bottom"));

        Styles imageStyles = new StylesMock(imagePropertyValues);

        attributes.setStyles(styles);
        imageAttributes.setStyles(imageStyles);

        protocol.doImage(buffer, imageAttributes);
        final String actual = bufferToString(buffer);
        verifyStyleVerticalAlignTD(actual);
    }

*/
    protected void verifyStyleVerticalAlignTD(final String actual) {
        assertEquals("Markup should match: ",
                "<table>" +
                    "<td>" +
                        "<img align=\"bottom\" border=\"0\" " +
                                "src=\"images/background.jpg\"/>" +
                    "</td>" +
                "</table>",
                actual);
    }

    /**
     * This test case should illustrate that img attributes are set.
     * todo a mechansism should exist that does this.
     */
/* @todo XDIME-CP Fix now that emulation is done later
    public void testStyleVerticalAlignIMG() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        ImageAttributes imageAttributes = new ImageAttributes();
        imageAttributes.setSrc("images/background.jpg");

        // Create a styles with populated properties and set it on the attributes
        MutablePropertyValues propertyValues = createPropertyValues();
        // this should render.
        propertyValues.setSynthesizedValue(StylePropertyDetails.VERTICAL_ALIGN,
                createKeywordStyleValue(
                        VerticalAlignKeywordMapper.getSingleton(), "middle"));
        // this should be ignored - invalid.
        propertyValues.setSynthesizedValue(StylePropertyDetails.FONT_SIZE,
                createKeywordStyleValue(FontSizeKeywordMapper.getSingleton(),
                        "small"));
        imageAttributes.setStyles(new StylesMock(propertyValues));

        protocol.doImage(buffer, imageAttributes);

        verifyStyleVerticalAlignIMG(bufferToString(buffer));
    }
*/

    /**
     * Helper method which may be overridden by specialisations of this protocol.
     */
    protected void verifyStyleVerticalAlignIMG(String actual) {
        assertEquals("Markup should match: ",
                "<img align=\"middle\" border=\"0\" " +
                        "src=\"images/background.jpg\"/>",
                actual);
    }

    /**
     * Test the body text colour.
     */
/* @todo XDIME-CP Fix now that emulation is done later
    public void testBodytextColour() throws Exception {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        CanvasAttributes canvas = new CanvasAttributes();
        BodyAttributes attributes = new BodyAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "color: #0000FF; " +
                "background-color: #FFFFFF; " +
                "background-image: url(background.jpg)"));
        attributes.setCanvasAttributes( canvas );

        protocol.openBody(buffer, attributes);
        protocol.closeBody(buffer, attributes);

        assertEquals(
                "<body " +
                    "background=\"background.jpg\"" +
                    " bgcolor=\"#ffffff\"" +
                    " text=\"#0000ff\"" +
                "/>",
                bufferToString(buffer));

    }
*/

    protected void checkRenderAltText(String altText, DOMOutputBuffer buffer) {
        // HTML3 has an emulated span which doesn't render to anything if
        // we don't have styles, which in this case we don't.
        Element root = buffer.getCurrentElement();
        checkTextEquals(altText, root.getHead());
    }

    protected static Text checkTextEquals(String expectedText,
                                          Node actualNode) {

        // Due to the behavior of HTMLVersion3_2 protocol
        // the Text node may be hanging off a style emulation Element node.
        // so recursively move down the tree until we find a non-element node
        Class lastNodeClass = null;
        while (actualNode instanceof Element) {
            lastNodeClass = actualNode.getClass();
            actualNode = ((Element)actualNode).getHead();
        }
        assertNotNull("Node not a Text: " + lastNodeClass, actualNode);
        Text text = (Text)actualNode;
        assertEquals(expectedText,
                     new String(text.getContents(), 0, text.getLength()));
        return text;
    }


    // javadoc inherited
    protected String expectedDefaultMimeType() {
        return "text/html";
    }

    /**
     * Overridden to do nothing since HTML3.2 does not support the class
     * attribute.
     */
    protected void checkClassAttribute(Element element,
                                       String expectedClass) {
    }

    // javadoc inherited.
    public void testUseEnclosingTableCell() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        doTestUseEnclosingTableCell(attributes, "<td/>");

        // Set some styles on the element.
        attributes.setStyles(StylesBuilder.getStyles("background-color: red"));
        doTestUseEnclosingTableCell(attributes, "<td bgcolor=\"red\"/>");
    }

    // javadoc inherited.
    public void testCreateEnclosingElement() throws Exception {
        // NOTE: This protocol doesn't support style classes and we should never
        // have a class attribute in real life situations. However, this is
        // explicitly set in the super class' method during this test.
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles("background-color: green"));

        // Set a valid style class on the pane attributes.
        // Also set a style class on the element itself.
        doTestCreateEnclosingElement(attributes,
                                     "<td>" +
                                        "<div/>" +
                                     "</td>");
    }

    /**
     * test for the openDissectingPane method
     */
    public void testOpenDissectingPane() throws RepositoryException {
        // todo - this test no longer works due to restucturing of
        // style emulation - will need to fix
    }

    // javadoc inherited
    protected String getExpectedXFSelectString() {
        // ##STYLE EMULATION ELEMENT is expected since this protocol needs to
        // emulate the bold styling
        return
                "<testRoot>" +
                    "<div>" +
                        "<input name=\"TestSelect\" type=\"radio\" value=\"Value1\"/>" +
                        "<STYLE-EMULATION-ELEMENT style='font-weight: bold'>" +
                            "Caption1" +
                        "</STYLE-EMULATION-ELEMENT>" +
                        "<br/>" +
                        "<input name=\"TestSelect\" type=\"radio\" value=\"Value2\"/>" +
                        "Caption2" +
                    "</div>" +
                "</testRoot>";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/4	pduffin	VBM:2005111405 Massive changes for performance

 23-Nov-05	10402/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 23-Nov-05	10381/2	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 07-Nov-05	10166/1	geoff	VBM:2005102408 Backwards port: Pane style class renders layout rather than theme bgcolor

 07-Nov-05	10046/1	geoff	VBM:2005102408 Post-review modifications

 23-Nov-05	10381/2	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 07-Nov-05	10046/1	geoff	VBM:2005102408 Post-review modifications

 29-Sep-05	9600/2	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 06-Sep-05	9413/2	schaloner	VBM:2005070406 Implemented before and after pseudo-element support

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/4	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 25-Aug-05	9370/1	gkoch	VBM:2005070507 xform select option to store caption styles instead of caption (style) class

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/7	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9184/4	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 19-Jul-05	9039/4	emma	VBM:2005071401 supermerge required

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 23-Jun-05	8483/7	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/7	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 08-Jun-05	7997/2	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 07-Jun-05	8637/1	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 06-May-05	8090/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 06-May-05	8048/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 22-Apr-05	7791/1	philws	VBM:2005040113 Port openPane changes from 3.3

 22-Apr-05	7746/1	philws	VBM:2005040113 Correct pane rendering where width and/or alignment are specified

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-Nov-04	6076/4	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 22-Nov-04	5733/9	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	5733/6	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	6183/5	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 18-Nov-04	6183/2	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/3	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 04-Nov-04	5871/5	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - rework issues

 03-Nov-04	5871/3	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 02-Nov-04	5882/3	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6064/1	claire	VBM:2004102801 mergevbm: Handling background colour for HTML 3.2 portlets

 01-Nov-04	6014/3	claire	VBM:2004102801 Handling background colour for HTML 3.2 portlets

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 09-Sep-04	4839/10	pcameron	VBM:2004062801 div tag is now used again in a pane's table if stylesheets are in use

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 22-Jul-04	4713/12	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 02-Jul-04	4713/9	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 29-Jun-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Jul-04	4897/2	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 15-Jul-04	4869/2	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 29-Jun-04	4720/9	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 28-Jun-04	4720/6	byron	VBM:2004061604 Core Emulation Facilities - rework issues

 25-Jun-04	4720/4	byron	VBM:2004061604 Core Emulation Facilities

 25-Jun-04	4720/2	byron	VBM:2004061604 Core Emulation Facilities

 28-Jun-04	4685/1	steve	VBM:2004050406 Remove empty span around alt text

 10-Jun-04	4676/1	steve	VBM:2004050406 Remove span from alt text

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 27-May-04	4589/13	steve	VBM:2004051102 Output text colour in body tag

 26-May-04	4589/3	steve	VBM:2004051102 Output text colour in body tag

 26-May-04	4589/1	steve	VBM:2004051102 Output text colour in body tag

 26-May-04	4570/1	steve	VBM:2004051102 Output text colour in body tag

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 05-May-04	4167/3	steve	VBM:2004042901 Fix up javadoc

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/1	steve	VBM:2004042901 Style class rendering fix

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 09-Dec-03	2180/1	mat	VBM:2003120504 Add style attributes to format cells

 09-Dec-03	2162/3	mat	VBM:2003120504 Add style attributes to format cells

 26-May-04	4570/1	steve	VBM:2004051102 Output text colour in body tag

 04-May-04	4117/1	steve	VBM:2004042901 Style class rendering fix

 09-Dec-03	2162/3	mat	VBM:2003120504 Add style attributes to format cells

 09-Dec-03	2162/1	mat	VBM:2003120504 Add style attributes to format cells

 09-Dec-03	2162/1	mat	VBM:2003120504 Add style attributes to format cells

 21-Aug-03	1052/5	allan	VBM:2003073101 Wrap each menu item within a menu if required

 21-Aug-03	1052/3	allan	VBM:2003073101 Wrap each menu item within a menu if required

 17-Aug-03	1052/1	allan	VBM:2003073101 Support styles on menu and menuitems

 07-Jul-03	728/4	adrian	VBM:2003052001 fixed pane attribute generation

 07-Jul-03	728/2	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	706/1	allan	VBM:2003070302 Added TestSuiteGenerator ant task. Run testsuite in a single jvm

 ===========================================================================
*/
