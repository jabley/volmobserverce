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
* (c) Volantis Systems Ltd 2004. 
* ----------------------------------------------------------------------------
*/

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMAssertionUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DoSelectInputTestHelper;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.protocols.trans.OptimizationConstants;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.properties.MCSMenuOrientationKeywords;
import com.volantis.mcs.themes.properties.MCSSelectionListOptionLayoutKeywords;
import com.volantis.mcs.themes.properties.MCSSelectionListStyleKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.values.MutablePropertyValues;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * This class unit tests the HTML_iMode class
 */
public class HTML_iModeTestCase extends HTMLRootTestAbstract {

    /**
     * PageContext for protocol
     */
    private TestMarinerPageContext pageContext;

    /**
     * RequestContext for tests
     */
    private TestMarinerRequestContext requestContext;

    private HTML_iMode protocol;

    private XHTMLBasicTestable testable;

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestHTML_iModeFactory(),
                internalDevice);
        return protocol;
    }

    // javadoc inhertied from superclass
    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);
        this.protocol = (HTML_iMode) protocol;
        this.testable = (XHTMLBasicTestable) testable;
    }

    /**
     * Junit Test Constructor
     * @param name
     */
    public HTML_iModeTestCase(String name) {
        super(name);
    }

    /**
     * Setup the unit test framework by creating some classes ensuring that each
     * test case starts off with a clean slate to work from
     *
     * @todo the fact that some of the tests don't need to call this method
     * at all indicates that we ought to refactor this so each test just
     * sets up what it needs. Otherwise, we can have unintended side effects...
     */
    private void privateSetUp() {
        pageContext = new TestMarinerPageContext();
        requestContext = new TestMarinerRequestContext();
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        pageContext.pushRequestContext(requestContext);
        protocol.setMarinerPageContext(pageContext);
    }

    // Javadoc inherited.
    protected String getExpectedHorizontalRuleMarkup() {
        return "<hr align=\"left\" noshade=\"true\" size=\"5\" width=\"100\"/>";
    }

    /**
     * Test that vertical align is not added to the td element since v align is not
     * an i-mode attribute for td.
     */
    public void testStyleVerticalAlignTD() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        MutablePropertyValues tableCellProperties = createPropertyValues();
        tableCellProperties.setComputedValue(
                StylePropertyDetails.VERTICAL_ALIGN,
                VerticalAlignKeywords.BOTTOM);

        TableCellAttributes tableCellAttributes = new TableCellAttributes();
        tableCellAttributes.setStyles(StylesBuilder.getInitialValueStyles());
        protocol.openTableDataCell(buffer, tableCellAttributes);

        final String actual = bufferToString(buffer);

        verifyStyleVerticalAlignTD(actual);
    }

    /**
     * Helper method for testStyleVerticalAlignTD()
     * Can be overridden in specialisations of HTML-iMode
     * @param actual The actual String
     */
    protected void verifyStyleVerticalAlignTD(final String actual) {
        assertEquals("Markup should match: ",
                "<td/>",
                actual);
    }

    /**
     * This test case should illustrate that img attributes are set.
     * This is an extra test for doImage - tests align
     */
    public void testStyleVerticalAlignIMG() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        MutablePropertyValues properties = createPropertyValues();

        // The middle attribute has to be set in the image attribute and not int the SSP
        // Since images do not go through the emulation renderer
        // - perhaps they will one day
        // ssp.setVerticalAlign(createKeywordStyleValue(
        //       VerticalAlignKeywordMapper.getSingleton(), "middle"));

        // this should be ignored - invalid.
        properties.setComputedValue(StylePropertyDetails.FONT_SIZE,
                FontSizeKeywords.SMALL);

        ImageAttributes imageAttributes = new ImageAttributes();
        imageAttributes.setSrc("images/background.jpg");
        imageAttributes.setAlign("middle");
        protocol.doImage(buffer, imageAttributes);

        verifyStyleVerticalAlignIMG(bufferToString(buffer));
    }

    /**
     * Helper method for testStyleVerticalAlignIMG()
     * Can be overridden in specialisations of HTML-iMode
     * @param actual The actual String
     */
    protected void verifyStyleVerticalAlignIMG(String actual) {
        assertEquals("Markup should match: ",
                "<img align=\"middle\" src=\"images/background.jpg\"/>",
                actual);
    }

    /**
     * Test that body text and background colour are set.
     */
    public void testBodytextColour() throws Exception {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        CanvasAttributes canvas = new CanvasAttributes();
        canvas.setStyles(StylesBuilder.getDeprecatedStyles());

        BodyAttributes attributes = new BodyAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "color: #fff000; " +
                "background-color: #000fff"));
        attributes.setCanvasAttributes(canvas);

        protocol.openBody(buffer, attributes);
        protocol.closeBody(buffer, attributes);

        assertEquals(
                "<body" +
                " bgcolor=\"#000fff\"" +
                " text=\"#fff000\"" +
                "/>",
                bufferToString(buffer));
    }

    /**
     * Test that no background image is set as background image is
     * not supported in i-mode in body.
     */
    public void testNoBackgroundImageInBody() throws Exception {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        CanvasAttributes canvas = new CanvasAttributes();
        canvas.setStyles(StylesBuilder.getDeprecatedStyles());

        BodyAttributes attributes = new BodyAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "background-image: url(background.jpg)"));
        attributes.setCanvasAttributes(canvas);

        protocol.openBody(buffer, attributes);
        protocol.closeBody(buffer, attributes);
        // the colours expected here are the default ones
        assertEquals("<body bgcolor=\"#ffffff\" text=\"#000000\"/>",
                bufferToString(buffer));
    }

    /**
     * Test the open style with many styles set
     * The point of this test is that a lot of unsupported styles are set, along
     * with one supported stlye - font. This should be the only style emulated.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testStyleManySet() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        StyleValue fontSize = createKeywordStyleValue(
                FontSizeKeywordMapper.getSingleton(), "xx-large");
        StyleBitSet textDecoration = styleValueFactory.getBitSet(0,
                TextDecorationEnumeration.__MAX,
                StyleValue.PRIORITY_NORMAL);
        textDecoration.setBit(TextDecorationIndexMapper.getBitSetIndex(
                TextDecorationEnumeration.LINE_THROUGH));
        textDecoration.setBit(TextDecorationIndexMapper.getBitSetIndex(
                TextDecorationEnumeration.UNDERLINE));
        StyleValue fontStyle = new StyleKeyword(FontStyleEnumeration.ITALIC);
        StyleColor color = new StyleColor();
        color.setName(StyleColorNames.RED);

        StyleValue fontFamily = new StyleKeyword(
                FontFamilyEnumeration.SANS_SERIF);
        StyleValue texttAlign = new StyleKeyword(TextAlignEnumeration.RIGHT);
        StyleValue fontWeight = new StyleKeyword(FontWeightEnumeration.NORMAL);

        MutablePropertyValues propertyValues = createPropertyValues();
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.FONT_WEIGHT, fontWeight);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.FONT_SIZE, fontSize);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.TEXT_DECORATION, textDecoration);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.FONT_STYLE, fontStyle);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.COLOR, color);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.FONT_FAMILY, fontFamily);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.TEXT_ALIGN, texttAlign);
        StylesMock styles = new StylesMock(propertyValues);

        protocol.styleEmulationRenderer.open(buffer, styles);
        protocol.styleEmulationRenderer.close(buffer, styles);
        verifyStyleManySet(bufferToString(buffer));
    }
*/


    /**
     * Helper method for testStyleVerticalAlignTD()
     * Can be overridden in specialisations of HTML-iMode
     *
     * @param actual The actual String
     */
    protected void verifyStyleManySet(String actual) {
        assertEquals("Markup should match",
                "<font color=\"red\"/>",
                actual);
    }

    /**
     * Test the style with text alignment.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testStyleTextAlign() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        StyleValue fontSize = createKeywordStyleValue(
                FontSizeKeywordMapper.getSingleton(), "large");
        StyleValue fontStyle = createKeywordStyleValue(
                FontStyleKeywordMapper.getSingleton(), "oblique");
        StyleValue fontFamily = new StyleKeyword(
                FontFamilyEnumeration.SANS_SERIF);
        StyleValue fontWeight = createKeywordStyleValue(
                FontWeightKeywordMapper.getSingleton(), "700");

        StyleBitSet textDecoration = styleValueFactory.getBitSet(0,
                TextDecorationEnumeration.__MAX,
                StyleValue.PRIORITY_NORMAL);
        textDecoration.setBit(TextDecorationIndexMapper.getBitSetIndex(
                TextDecorationEnumeration.UNDERLINE));

        StyleColor color = new StyleColor();
        color.setName(StyleColorNames.BLUE);
        StyleValue backgroundColour = styleValueFactory.getColorByPercentages(
                0, 100, 50, StyleValue.PRIORITY_NORMAL);

        StyleValue texttAlign = createKeywordStyleValue(
                TextAlignKeywordMapper.getSingleton(), "center");
        StyleValue verticalAlign = createKeywordStyleValue(
                VerticalAlignKeywordMapper.getSingleton(), "top");

        StyleValue paddingTop = styleValueFactory.getLength(10,
                LengthUnit.PX,
                StyleValue.PRIORITY_NORMAL);
        StyleValue borderSpacing = styleValueFactory.getLength(15,
                LengthUnit.PC,
                StyleValue.PRIORITY_NORMAL);
        StyleValue borderTopWidth = styleValueFactory.getLength(20,
                LengthUnit.PC,
                StyleValue.PRIORITY_NORMAL);
        StyleValue width = styleValueFactory.getLength(25,
                LengthUnit.PC,
                StyleValue.PRIORITY_NORMAL);
        StyleValue height = styleValueFactory.getLength(30,
                LengthUnit.PC,
                StyleValue.PRIORITY_NORMAL);

        MutablePropertyValues propertyValues = createPropertyValues();
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.FONT_SIZE, fontSize);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.FONT_STYLE, fontStyle);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.FONT_FAMILY, fontFamily);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.FONT_WEIGHT, fontWeight);

        propertyValues.setSynthesizedValue(
                StylePropertyDetails.TEXT_DECORATION, textDecoration);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.COLOR, color);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.BACKGROUND_COLOR,
                backgroundColour);

        propertyValues.setSynthesizedValue(
                StylePropertyDetails.TEXT_ALIGN, texttAlign);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.VERTICAL_ALIGN, verticalAlign);

        propertyValues.setSynthesizedValue(
                StylePropertyDetails.PADDING_TOP, paddingTop);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.BORDER_SPACING, borderSpacing);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.BORDER_TOP_WIDTH, borderTopWidth);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.WIDTH, width);
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.HEIGHT, height);

        StylesMock styles = new StylesMock(propertyValues);

        ParagraphAttributes attributes = new ParagraphAttributes();
        attributes.setStyles(styles);

        protocol.openParagraph(buffer, attributes);
        protocol.closeParagraph(buffer, attributes);

        verifyStyleAlign(bufferToString(buffer));
    }
*/


    /**
     * Helper method for testStyleVerticalAlignTD()
     * Can be overridden in specialisations of HTML-iMode
     * @param actual The actual String
     */
    protected void verifyStyleAlign(String actual) {
        assertEquals("Markup should match: ",
                "<p align=\"center\"><font color=\"blue\"/></p>",
                actual);
    }

    /**
     * Test the style with text alignment.
     */
/*  XDIME-CP Needs fixing, now that emulation is done later
    public void testStyleBlink() throws Throwable {
        privateSetUp();
        protocol.setMarinerPageContext(pageContext);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();

        StyleBitSet textDecoration = styleValueFactory.getBitSet(0,
                TextDecorationEnumeration.__MAX,
                StyleValue.PRIORITY_NORMAL);
        textDecoration.setBit(TextDecorationIndexMapper.getBitSetIndex(
                TextDecorationEnumeration.BLINK));

        MutablePropertyValues propertyValues = createPropertyValues();
        propertyValues.setSynthesizedValue(
                StylePropertyDetails.TEXT_DECORATION, textDecoration);

        StylesMock styles = new StylesMock(propertyValues);

        ParagraphAttributes attributes = new ParagraphAttributes();
        attributes.setStyles(styles);

        protocol.openParagraph(buffer, attributes);
        buffer.writeText("Hello");
        protocol.closeParagraph(buffer, attributes);

        String result = bufferToString(buffer);

        assertEquals("Markup should match: ",
                "<p><blink>Hello</blink></p>",
                result);
    }*/

    // Javadoc inherited
    public void testNoOptGroupsWithControlSelect() throws Exception {
        privateSetUp();

        boolean[] selected = {false, false, false};

        // no options selected
        // single select - ie radio
        // vertical options
        // align caption to the left
        // do not create any optgroups
        boolean multiple = false;
        boolean vertical = true;
        boolean alignright = false;
        int optGroupCount = 1;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);

        optGroupCount = 4;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);
    }

    // Javadoc inherited
    public void doTestControlSelect(boolean selectedOtions[],
                                    boolean multiSelect,
                                    final boolean vertical,
                                    final boolean rightAlignCaption,
                                    int optGroupCount)
            throws Exception {


        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();

        XFSelectAttributes atts = helper.buildSelectAttributes();
        Styles styles = atts.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();

        if (!multiSelect) {
            for (int i = 0; i < selectedOtions.length; i++) {
                if (selectedOtions[i]) {
                    atts.setInitial("Value" + (i + 1));
                    break;
                }
            }
        }

        atts.setMultiple(multiSelect);
        // add some options
        helper.addOption(atts, "Caption1", "Prompt1", "Value1",
                selectedOtions[0]);
        helper.addOption(atts, "Caption2", "Prompt2", "Value2",
                selectedOtions[1]);


        SelectOptionGroup group = null;
        String caption, prompt;
        int count = 0;
        do {
            caption = "Group" + (count + 1);
            prompt = "Prompt" + (count + 1);
            if (0 == count) {
                group = helper.addOptionGroup(atts, caption, prompt);
            } else {
                group = helper.addOptionGroup(group, caption, prompt);
            }
        } while (++count < optGroupCount);


        helper.addOption(group, "Caption3", "Prompt3", "Value3",
                selectedOtions[2]);

        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        propertyValues.setComputedValue(
                StylePropertyDetails.MCS_SELECTION_LIST_STYLE,
                MCSSelectionListStyleKeywords.CONTROLS);
        propertyValues.setComputedValue(
                StylePropertyDetails.MCS_SELECTION_LIST_OPTION_LAYOUT,
                rightAlignCaption ? MCSSelectionListOptionLayoutKeywords.CONTROL_FIRST
                : MCSSelectionListOptionLayoutKeywords.CAPTION_FIRST);
        propertyValues.setComputedValue(
                StylePropertyDetails.MCS_MENU_ORIENTATION,
                vertical ? MCSMenuOrientationKeywords.VERTICAL
                : MCSMenuOrientationKeywords.HORIZONTAL);

        TestMarinerPageContext testPageContext =
                (TestMarinerPageContext) protocol.getMarinerPageContext();
        testPageContext.setFormFragmentResetState(true);

        testable.setCurrentBuffer(atts.getEntryContainerInstance(), buffer);

        String type = (multiSelect) ? "checkbox" : "radio";
        String seperator = (vertical) ? "<br/>" : "\u00a0";
        StringBuffer sb = new StringBuffer();

        sb.append("<div>");

        for (int i = 0; i < 3; i++) {
            if (i > 0) {
                sb.append(seperator);
            }
            if (!rightAlignCaption) {
                sb.append("Caption").append(i + 1);
            }
            sb.append("<input ");
            if (selectedOtions[i]) {
                sb.append("checked=\"checked\" ");
            }
            sb.append("name=\"").append(atts.getName()).append("\" ");
            sb.append("type=\"").append(type).append("\" ");
            sb.append("value=\"").append("Value").append(i + 1).append("\"/>");
            if (rightAlignCaption) {
                sb.append("Caption").append(i + 1);
            }
        }
        sb.append("</div>");


        helper.runTest(protocol, buffer, atts, sb.toString());
    }

    // Javadoc inherited
    public void testImageNoSrc() throws ProtocolException {

/* XDIME-CP Needs fixing, now that emulation is done later
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

        protocol.doImage(buffer, attributes);
        assertEquals("i-Mode should have alt text here",
                expected, bufferToString(buffer));
*/
    }

    // Javadoc inherited
    public void testControlSingleSelect() throws Exception {
        privateSetUp();

        boolean[] selected = {false, false, false};

        // no options selected
        // single select - ie radio
        // vertical options
        // align caption to the left
        // do not create any optgroups
        boolean multiple = false;
        boolean vertical = true;
        boolean alignright = false;
        int optGroupCount = 0;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);
    }


    // Javadoc inherited
    public void testControlSingleSelectOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selected = {true, false, false};

        boolean multiple = false;
        boolean vertical = false;
        boolean alignright = true;
        int optGroupCount = 0;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);
    }

    // Javadoc inherited
    public void testControlMultiSelect() throws Exception {
        privateSetUp();

        boolean[] selected = {false, false, false};

        // no options selected
        // single select - ie radio
        // horizontal options
        // align caption to the left
        boolean multiple = true;
        boolean vertical = false;
        boolean alignright = false;
        int optGroupCount = 0;

        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);
    }


    // Javadoc inherited
    public void testControlMultiSelectOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selected = {true, true, false};

        // no options selected
        // multi select - ie chackbox
        // vertical options
        // align caption to the right
        boolean multiple = true;
        boolean vertical = true;
        boolean alignright = true;
        int optGroupCount = 0;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);
    }

    // Javadoc inherited
    public void testOpenPane() throws RepositoryException, IOException, ParserConfigurationException, SAXException {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        Element el = null;

        Pane pane = null;
        PaneAttributes attributes = null;

        pane = new Pane(null);
        pane.setName("test");
        attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles("background-color: red"));
        attributes.setPane(pane);

        // Set up the required contexts
        testable.setStyleSheetRenderer(CSSStyleSheetRenderer.getSingleton());
        pageContext.setDeviceName("PC-Win32-IE5.5");
        protocol.setMarinerPageContext(pageContext);
        PaneInstance paneInstance = new TestPaneInstance();
        paneInstance.setStyleClass("testStyle");
        pageContext.setFormatInstance(paneInstance);
        protocol.setMarinerPageContext(pageContext);

        TestDeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        deviceContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS,
                paneInstance);
        pageContext.pushDeviceLayoutContext(deviceContext);

        MutablePropertyValues properties = createPropertyValues();
        StyleColor value =
            styleValueFactory.getColorByPercentages(null, 50, 50, 50);
        properties.setComputedValue(StylePropertyDetails.BACKGROUND_COLOR,
                value);
        properties.setComputedValue(StylePropertyDetails.TEXT_ALIGN,
                TextAlignKeywords.CENTER);

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

        // System.out.println(bufferToString(buffer));

        assertEquals("td", el.getName());

        // todo test that bg colours get added here
    }

    // Javadoc inherited
    public void testDoImage() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Needed to allow the call to getTextFromReference within doImage to work
        context = new TestMarinerPageContext();

        protocol.setMarinerPageContext(context);

        ImageAttributes attrs = new ImageAttributes();
        attrs.setSrc("http://www.volantis.com/my_image.jpg");
        attrs.setLocalSrc(true);
        attrs.setAltText("my_alt_text");
        attrs.setWidth("10");
        attrs.setHeight("20");
        attrs.setBorder("5");

        protocol.doImage(buffer, attrs);

        String expected = "<img alt=\"my_alt_text\" " +
                "height=\"20\" src=\"http://www.volantis.com/my_image.jpg\" " +
                "width=\"10\"/>";

        assertEquals("Unexpected img markup generated.",
                expected,
                bufferToString(buffer));
    }

    // Javadoc inherited
    public void testAddRowIteratorPaneAttributes() throws Exception {
        // todo add test here - row iterator panes are depreacted and therefore not
        // tested here
    }

    /**
     * Test the method addPaneTableAttributes(Element, PaneAttributes)
     */
    public void testAddPaneTableAttributes() throws Exception {
        // todo add test here - addPaneTableAttributes was not altered by
        // VBM 2004091403 - however it requires a specialised set up to work
        // this should be done soon
    }


    // Javadoc inherrited
    protected void doTestDoMenu(MenuAttributes menuAttrs,
                                DOMOutputBuffer buffer,
                                String expected)
            throws Exception {
        // todo add test here - doMenu was not altered by
        // VBM 2004091403 - however it requires a specialised set up to work
        // this should be done soon
    }

    // Javadoc inherited
    public void testOptGroupsNestingWithDefaultSelect() throws Exception {
        privateSetUp();

        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();


        XFSelectAttributes atts = helper.buildSelectAttributes();

        // build the following nested option optgroup structure
        // <group 1>
        //   <group 2>
        //      <group 3>
        //         <option 1>
        //      </group>
        //   <group>
        // </group>
        // <group 4>
        //   <option 2>
        // </group>
        SelectOptionGroup group = helper.addOptionGroup(atts, "Group1",
                "Prompt1");
        group = helper.addOptionGroup(group, "Group2", "Prompt2");
        group = helper.addOptionGroup(group, "Group3", "Prompt3");
        helper.addOption(group, "Caption1", "Prompt1", "Value1", false);

        group = helper.addOptionGroup(atts, "Group4", "Prompt4");
        helper.addOption(group, "Caption2", "Prompt2", "Value2", false);


        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        testable.setCurrentBuffer(atts.getEntryContainerInstance(), buffer);

        StringBuffer sb = new StringBuffer();

        sb.append("<select ");
        sb.append("name=\"").append(atts.getName()).append("\">");
        sb.append("<option value=\"Value1\">Caption1</option>");

        sb.append("<option value=\"Value2\">Caption2</option>");

        sb.append("</select>");
        helper.runTest(protocol, buffer, atts, sb.toString());
    }

    // Javadoc inherited
    public void testDefaultMultiSelectOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selected = {false, true, true};

        doTestDefaultSelectInput(selected, true);
    }

    // Javadoc inherited
    public void doTestDefaultSelectInput(boolean selectedOptions[],
                                         boolean multiSelect)
            throws Exception {

        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();

        final String rowCount = "3";
        final String tabIndex = "1";

        XFSelectAttributes atts = helper.buildSelectAttributes();
        atts.setStyles(StylesBuilder.getStyles("mcs-rows: " + rowCount));
        atts.setMultiple(multiSelect);

        if (!multiSelect) {
            for (int i = 0; i < selectedOptions.length; i++) {
                if (selectedOptions[i]) {
                    atts.setInitial("Value" + (i + 1));
                    break;
                }
            }
        }
        atts.setTabindex(tabIndex);
        // add some options

        for (int i = 0; i < selectedOptions.length; i++) {
            helper.addOption(atts,
                    "Caption" + (i + 1),
                    "Prompt" + (i + 1),
                    "Value" + (i + 1),
                    selectedOptions[i]);
        }


        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        TestMarinerPageContext testPageContext =
                (TestMarinerPageContext) protocol.getMarinerPageContext();
        testPageContext.setFormFragmentResetState(true);

        testable.setCurrentBuffer(atts.getEntryContainerInstance(), buffer);

        StringBuffer sb = new StringBuffer();

        sb.append("<select ");
        if (multiSelect) {
            sb.append("multiple=\"multiple\" ");
        }
        sb.append("name=\"").append(atts.getName()).append("\" ");
        if (multiSelect) {
            sb.append("size=\"").append(rowCount).append("\" ");
        }
        sb.append("tabindex=\"").append(tabIndex).append("\">");

        for (int i = 0; i < selectedOptions.length; i++) {
            sb.append("<option ");
            if (selectedOptions[i]) {
                sb.append("selected=\"selected\" ");
            }
            sb.append("value=\"Value").append(i + 1)
                    .append("\">Caption").append(i + 1).append("</option>");
        }

        sb.append("</select>");


        helper.runTest(protocol, buffer, atts, sb.toString());

    }

    // Javadoc inherited
    protected void checkActionInputType() throws ProtocolException {

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        // Setup test for normal behaviour for input tag generation (submit)
        String name = "My Name";
        String actionType = "submit";
        String caption = "This is my caption";
        String value = "This is my value";

        attributes.setName(name);
        attributes.setType(actionType);
        attributes.setCaption(new LiteralTextAssetReference(caption));
        attributes.setValue(value);

        protocol.doActionInput(buffer, attributes);
        String actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" " +
                "type=\"" + actionType + "\" value=\"" + caption + "\"/>",
                actualResult);

        // Setup test for normal behaviour for input tag generation (perform)
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        actionType = "perform";
        attributes.setType(actionType);
        protocol.doActionInput(buffer, attributes);
        actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" " +
                "type=\"" + "submit" + "\" value=\"" + caption + "\"/>",
                actualResult);

        // Setup test for normal behaviour for input tag generation (perform)
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        actionType = "reset";
        attributes.setType(actionType);
        protocol.doActionInput(buffer, attributes);
        actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" " +
                "type=\"" + actionType + "\" value=\"" + caption + "\"/>",
                actualResult);

        // Setup test for abnormal behaviour - garbage actionType
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        actionType = "GARBAGE";
        attributes.setType(actionType);
        try {
            protocol.doActionInput(buffer, attributes);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        // Setup test for abnormal behaviour - garbage actionType
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        actionType = "submit";
        attributes.setType(actionType);
        attributes.setTabindex("1");
        protocol.doActionInput(buffer, attributes);
        assertTrue(protocol.supportsTabindex);
        actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" " +
                "tabindex=\"1\" " +
                "type=\"" + actionType + "\" value=\"" + caption + "\"/>",
                actualResult);
    }

    // javadoc inherited
    public void doTest2002120611() throws Exception {

        privateSetUp();

        String name = "My Name";
        String actionType = "submit";
        String caption = "The caption";
        String value = "This is my value";

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        attributes.setName(name);
        attributes.setType(actionType);
        attributes.setCaption(new LiteralTextAssetReference(caption));
        attributes.setValue(value);

        // Setup test to ensure that link style form submision fallsback to the
        // default form submision
        protocol.doActionInput(buffer, attributes);
        String actualResult = bufferToString(buffer);

        // check that the correct output has been generated.
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" " +
                "type=\"" + "submit" + "\" value=\"" + caption + "\"/>",
                actualResult);
    }

    // javadoc inherited
    protected void checkActionInputShortcut(boolean generated,
                                            boolean styled) throws ProtocolException {

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        // Setup test for normal behaviour for input tag generation (submit)
        String shortcut = "1";
        String name = "My Name";
        String actionType = "submit";
        String caption = "This is my caption";

        attributes.setShortcut(new LiteralTextAssetReference(shortcut));
        attributes.setName(name);
        attributes.setType(actionType);
        attributes.setCaption(new LiteralTextAssetReference(caption));

        protocol.doActionInput(buffer, attributes);
        String actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input " +
                (generated ? "accesskey=\"" + shortcut + "\" " : "") +
                "name=\"" + name + "\" " +
                "type=\"" + actionType + "\" " +
                "value=\"" + caption + "\"/>",
                actualResult);
    }

    // Javadoc inherited
    public void testAddClassAttribute() {
        // todo this tests that we are adding class attributes - i-Mode does not have them
        // but really we should be testing that no class attributes are added
        // this has not been done in VBM:2004091403 because a specialised set up is
        // required for this protocol and we do not currently have time for this
    }

    // javadoc inherited
    public void testNoneTransformation() {
        // todo this tests style classes
        // this has not been done in VBM:2004091403 because a specialised set up is
        // required for this protocol and we do not currently have time for this
    }

    // Javadoc inherited
    public void testAttributeTransformation() {
        // todo - tests device theme - we should write tests that we get inline styles
        // this has not been done in VBM:2004091403 because
        // we do not currently have time for this
    }

    // javadoc inherited
    public void doTestSelectorTransformation(String testElement) {
        // todo - the functionality of this should not have been affected by
        // VBM:2004091403 but this test requires additional setup to work in this protocol
        // which we do not currently have time for
    }

    // Javadoc inherited
    public void testNoDeviceThemeAndNoAltClassName() {
        // todo - the functionality of this should not have been affected by
        // VBM:2004091403 but this test requires additional setup to work in this protocol
        // which we do not currently have time for
    }


    public void testRenderAltText() throws Exception {
        // todo - the functionality of this should not have been affected by
        // VBM:2004091403 but this test requires additional setup to work in this protocol
        // which we do not currently have time for
    }


    /**
     * test for the openDissectingPane method
     */
    public void testOpenDissectingPane() throws RepositoryException {
        // todo - the functionality of this should not have been affected by
        // VBM:2004091403 but this test requires additional setup to work in this protocol
        // which we do not currently have time for
    }

    public void testShardLinkStyleClass() throws Exception {
        // todo - the functionality of this should not have been affected by
        // VBM:2004091403 but this test requires additional setup to work in this protocol
        // which we do not currently have time for
    }

    // Javadoc inherited
    public void checkActionInput(boolean generateAccessKey,
                                 boolean generateStyles) throws Exception {
        privateSetUp();

        if (generateStyles) {
            checkActionInputType();
            doTest2002120611();
        }
        // @todo refactor so that that these test methods work without styles

        checkActionInputShortcut(generateAccessKey, generateStyles);
    }

    // Javadoc inherited
    public void testActionInput() throws Exception {
        checkActionInput(true, true);
    }

    // Javadoc inherited
    public void testAddColumnIteratorPaneAttributes() throws Exception {
        // nb: column iterator pane attributes are now deprecated
        context = new TestMarinerPageContext();
        TestMarinerRequestContext requestContext =
                new TestMarinerRequestContext();
        ContextInternals.setMarinerPageContext(requestContext, context);
        context.pushRequestContext(requestContext);

        context.setDeviceName("Master");
        protocol.setMarinerPageContext(context);
        MutablePropertyValues properties = createPropertyValues();

        Element element = domFactory.createElement();
        ColumnIteratorPaneAttributes attrs =
                new ColumnIteratorPaneAttributes();
        Pane pane = new Pane(new CanvasLayout());
        // Mimic an element created by openColumnIteratorPane and assign
        // an optimization level
        element.setName("table");
        pane.setOptimizationLevel(
                FormatConstants.OPTIMIZATION_LEVEL_VALUE_LITTLE_IMPACT);
        attrs.setPane(pane);

        protocol.addColumnIteratorPaneAttributes(element, attrs);

//        String cellpadding = element.getAttributeValue("cellpadding");
//        assertNull("cellpadding attribute should not have been set as " +
//                "stylesheets are supported.", cellpadding);

        String cellspacing = element.getAttributeValue("cellspacing");
        assertNull("cellspacing attribute should not have been set as " +
                "stylesheets are supported.", cellspacing);

        String border = element.getAttributeValue("border");
        assertNull("border attribute should not have been set as " +
                "stylesheets are supported.", border);

        // Test for optimization - attributes should have been applied if
        // format optimization is supported, and should not have been
        // applied otherwise.
        String optimizationLevel = element.getAttributeValue(
                OptimizationConstants.OPTIMIZATION_ATTRIBUTE);

        Field inlineSupport = VolantisProtocol.
                class.getDeclaredField("supportsInlineStyles");
        boolean inlineAccessible = inlineSupport.isAccessible();
        inlineSupport.setAccessible(true);

        boolean inlineOrigValue = inlineSupport.getBoolean(protocol);
        inlineSupport.setBoolean(protocol, false);

        Field externalSupport = VolantisProtocol.
                class.getDeclaredField("supportsExternalStyleSheets");
        boolean externalAccessible = externalSupport.isAccessible();
        externalSupport.setAccessible(true);

        boolean externalOrigValue = externalSupport.getBoolean(protocol);
        externalSupport.setBoolean(protocol, false);


        element = domFactory.createElement();
        protocol.addColumnIteratorPaneAttributes(element, attrs);

//        cellpadding = element.getAttributeValue("cellpadding");
//        assertNull("cellpadding attribute should have not have been set as " +
//                "cell padding is not supported in i-mode", cellpadding);

        cellspacing = element.getAttributeValue("cellspacing");
        assertNull("cellspacing attribute should have not have been set as " +
                "cellspacing is not supported in i-mode.", cellspacing);

        border = element.getAttributeValue("border");
        assertNull("border attribute should have not have been set as " +
                "border is not supported in i-mode.", border);

        element = domFactory.createElement();
        attrs = new ColumnIteratorPaneAttributes();
        attrs.setStyles(StylesBuilder.getStyles(
                "border-width: 2px; " +
                "border-spacing: 2px"));
//        attrs.setBorderWidth("2");
//        attrs.setCellPadding("2");
//        attrs.setCellSpacing("2");
        protocol.addColumnIteratorPaneAttributes(element, attrs);

//        cellpadding = element.getAttributeValue("cellpadding");
//        assertNull("cellpadding attribute should not have been set as " +
//                "cellpadding is not supported in i-mode", cellpadding);

        cellspacing = element.getAttributeValue("cellspacing");
        assertNull("cellspacing attribute should not have been set as " +
                "cellspacing is not supported in i-mode.", cellspacing);

        border = element.getAttributeValue("border");
        assertNull("border attribute should have not have  been set as " +
                "cellspacing is not supported in imode.", border);

        // reset the value and accessibility of supportsInlineStyles
        inlineSupport.setBoolean(protocol, inlineOrigValue);
        inlineSupport.setAccessible(inlineAccessible);

        externalSupport.setBoolean(protocol, externalOrigValue);
        externalSupport.setAccessible(externalAccessible);
    }

    // Javadoc inherited
    public void testSupportsStyleSheets() throws Exception {
        assertFalse("This protocol should not support stylesheets.",
                protocol.supportsStyleSheets());

        // Check that external stylesheets are supported.
        Field inlineSupport = VolantisProtocol.
                class.getDeclaredField("supportsInlineStyles");
        boolean inlineAccessible = inlineSupport.isAccessible();
        inlineSupport.setAccessible(true);

        boolean inlineOrigValue = inlineSupport.getBoolean(protocol);
        inlineSupport.setBoolean(protocol, false);

        assertFalse("This protocol should not support external stylesheets.",
                protocol.supportsStyleSheets());

        inlineSupport.setBoolean(protocol, inlineOrigValue);

        // Check that inline stylesheets are supported.
        Field externalSupport = VolantisProtocol.
                class.getDeclaredField("supportsExternalStyleSheets");
        boolean externalAccessible = externalSupport.isAccessible();
        externalSupport.setAccessible(true);

        boolean externalOrigValue = externalSupport.getBoolean(protocol);
        externalSupport.setBoolean(protocol, false);

        assertFalse("This protocol should not support internal stylesheets.",
                protocol.supportsStyleSheets());


        // Check that the method returns false if we support neither external
        // or inline stylesheets.
        inlineSupport.setAccessible(true);
        inlineOrigValue = inlineSupport.getBoolean(protocol);
        inlineSupport.setBoolean(protocol, false);

        assertTrue("The method should return false as we have disabled " +
                "support for inline and external stylesheets.",
                !protocol.supportsStyleSheets());

        // reset the value and accessibility of supportsInlineStyles
        inlineSupport.setBoolean(protocol, inlineOrigValue);
        inlineSupport.setAccessible(inlineAccessible);

        externalSupport.setBoolean(protocol, externalOrigValue);
        externalSupport.setAccessible(externalAccessible);
    }


    // Javadoc inherited
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testSmall() throws Exception {
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        protocol.openSmall(buffer, new SmallAttributes());

        buffer.appendEncoded("Test");
        protocol.closeSmall(buffer, new SmallAttributes());
        String expected = "Test";
        assertEquals("Protocol string should match", expected,
                bufferToString(buffer));
    }
*/

    // javadoc inherited.
    public void testAddGridAttributes() throws Exception {
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        Element element = domFactory.createElement();
        element.setName("testElement");

        context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        context.setDeviceName("PC");
        protocol.setMarinerPageContext(context);

        // INPUT: no style class, a device name that doesn't end with 'Netscape4'.
        // EXPECTED: Should result no borderwidth and style class.
        GridAttributes attributes = new GridAttributes();
        protocol.addGridAttributes(element, attributes);

        // INPUT: a device name that ends with 'Netscape4'.
        // EXPECTED: Should result in a '0' borderwidth and no style class.
        attributes = new GridAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        element = domFactory.createElement();
        element.setName("testElement");
        context.setDeviceName("xxxxxxNetscape4");
        protocol.addGridAttributes(element, attributes);
    }

    // javadoc inherited.
    public void testCreateEnclosingElement() throws Exception {
        // NOTE: This protocol doesn't support style classes and we should never
        // have a class attribute in real life situations. However, this is
        // explicitly set in the super class' method during this test.

        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        // Set a valid style class on the pane attributes.
        // Also set a style class on the element itself.
        doTestCreateEnclosingElement(attributes,
                "<td>" +
                "<div/>" +
                "</td>");
    }

    // javadoc inherited.
    public void testUseEnclosingTableCell() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        doTestUseEnclosingTableCell(attributes, "<td/>");
    }

    /**
     * iMode doesn't support css so the close pane should behave differently.
     */
    public void testClosePane() throws Exception {
        privateSetUp();
        context = new TestMarinerPageContext();
        protocol.setMarinerPageContext(context);
        TestDeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        context.pushDeviceLayoutContext(deviceContext);

        DOMOutputBuffer dom = new DOMOutputBuffer();
        dom.initialise();
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        Pane pane = new Pane(new CanvasLayout());
        attributes.setPane(pane);

        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        dom.openStyledElement("body", attributes);
        dom.openElement("table");
        dom.openElement("tr");
        Element td = dom.openElement("td");

        // Requires non-null pane attributes with a valid pane.
        protocol.closePane(dom, attributes);

        assertSame("The DOM's current element isn't the td element",
                dom.getCurrentElement(),
                td);
    }

    /**
     * Ensure that the protocol implements
     * {@link XHTMLBasic#enableNameIdentification} correctly for anchors.
     *
     * @throws ProtocolException
     */
    public void testAnchorNameIdentification() throws ProtocolException, RepositoryException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Initialise our protocol attributes with an id attribute.
        String idValue = "the identifier value";
        AnchorAttributes attributes = new AnchorAttributes();
        attributes.setId(idValue);

        // Create the expected value for the name attribute output.
        String expectedNameValue = null;
        if (protocol.enableNameIdentification) {
            expectedNameValue = idValue;
        }

        // Open and close the anchor to generate the markup.
        protocol.openAnchor(buffer, attributes);
        protocol.closeAnchor(buffer, attributes);

        // Check that the element generated has appropriate id and name
        // attributes.
        Element root = buffer.getCurrentElement();
        Element element = (Element) root.getHead();
        DOMAssertionUtilities.assertElement("a", element);
        DOMAssertionUtilities.assertAttributeEquals("name", expectedNameValue, element);
    }

    /**
     * Tests that the iMode protocol does not support nested tables.
     *
     * todo XDIME-CP
     */
    public void notestSupportsNestedTables() throws Exception {
        privateSetUp();

        ProtocolsConfiguration config = new ProtocolsConfiguration();
        config.setWmlPreferredOutputFormat("wmlc");
        Volantis bean = new Volantis();
        bean.setProtocolsConfiguration(config);
        pageContext.setVolantis(bean);

        DefaultDevice device = new DefaultDevice("TestDevice", null, null);
        device.setPolicyValue("ssversion", null);
        pageContext.setDevice(
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));

        protocol.initialise();
        assertFalse("iMode protocol should not support nested tables",
                protocol.supportsNestedTables());
    }

    // javadoc inherited
    protected String getExpectedXFSelectString() {
        // STYLE EMULATION ELEMENT is expected since this protocol needs to
        // emulate the bold styling
        return  "<testRoot>" +
                    "<div>" +
                        "<input name=\"TestSelect\" type=\"radio\" " +
                                "value=\"Value1\"/>" +
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

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 07-Nov-05	10096/1	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 07-Nov-05	10166/1	geoff	VBM:2005102408 Backwards port: Pane style class renders layout rather than theme bgcolor

 07-Nov-05	10046/1	geoff	VBM:2005102408 Post-review modifications

 07-Nov-05	10126/1	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 07-Nov-05	10096/1	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 07-Nov-05	10046/1	geoff	VBM:2005102408 Post-review modifications

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9600/2	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 07-Sep-05	9413/5	schaloner	VBM:2005070406 Changed style property iteration to direct access

 06-Sep-05	9413/2	schaloner	VBM:2005070406 Implemented before and after pseudo-element support

 02-Sep-05	9407/3	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 25-Aug-05	9370/1	gkoch	VBM:2005070507 xform select option to store caption styles instead of caption (style) class

 22-Aug-05	9363/6	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/3	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/6	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9151/7	pduffin	VBM:2005080205 Recommitted after super merge

 05-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 05-Aug-05	8859/4	emma	VBM:2005062006 Fixing merge conflicts

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 01-Aug-05	8990/9	pcameron	VBM:2005052606 Fixed merge conflicts

 12-Jul-05	8990/2	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 19-Jul-05	9039/4	emma	VBM:2005071401 supermerge required

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 22-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/9	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 09-Jun-05	8665/6	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 26-May-05	7995/5	pduffin	VBM:2005050323 Committing results of super merge

 25-May-05	7995/2	pduffin	VBM:2005050323 Committing changes as a result of super merge

 24-May-05	8123/5	ianw	VBM:2005050906 Fix merge conflicts

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 25-May-05	8517/2	pduffin	VBM:2005052404 Commiting changes from supermerge

 24-May-05	8123/5	ianw	VBM:2005050906 Fix merge conflicts

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 08-Jun-05	7997/2	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 26-May-05	7995/5	pduffin	VBM:2005050323 Committing results of super merge

 25-May-05	7995/2	pduffin	VBM:2005050323 Committing changes as a result of super merge

 24-May-05	8123/5	ianw	VBM:2005050906 Fix merge conflicts

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 25-May-05	8517/2	pduffin	VBM:2005052404 Commiting changes from supermerge

 24-May-05	8123/5	ianw	VBM:2005050906 Fix merge conflicts

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 07-Jun-05	8637/1	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 24-May-05	8491/1	tom	VBM:2005052311 Supermerge to MCS Mainline

 24-May-05	8489/1	tom	VBM:2005052311 Added width to tables and font colour to <a>

 18-May-05	8273/5	tom	VBM:2004091703 Added Stylistic Blink Support to iMode

 18-May-05	8273/3	tom	VBM:2004091703 Added Stylistic Blink Support to iMode

 18-May-05	8273/1	tom	VBM:2004091703 Added Stylistic Blink Support to iMode

 06-May-05	8090/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 06-May-05	8048/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/11	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	5733/9	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	5733/6	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	6183/5	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 18-Nov-04	6183/2	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/3	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 04-Nov-04	5871/5	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - rework issues

 03-Nov-04	5871/3	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 28-Sep-04	5661/5	tom	VBM:2004091403 Added style emulation to i-mode and fixed bgcol in cells

 ===========================================================================
*/
