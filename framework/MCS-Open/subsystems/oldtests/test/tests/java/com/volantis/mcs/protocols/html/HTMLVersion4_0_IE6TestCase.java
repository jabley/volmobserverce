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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.StylesBuilder;

/**
 * Test the HTMLVersion4_0_IE6 protocol.
 */
public class HTMLVersion4_0_IE6TestCase
        extends HTMLVersion4_0TestCase {

    private HTMLVersion4_0_IE6 protocol;
    private HTMLVersion4_0Testable testable;
    private TestMarinerPageContext context;

    final String DEFAULT_VALIGN = "center";
    final String DEFAULT_ALIGN = "left";

    /**
     * Create a new instance of this testcase.
     * @param name The name of the testcase.
     */
    public HTMLVersion4_0_IE6TestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestHTMLVersion4_0_IE6Factory(),
                internalDevice);
        return protocol;
    }

    // javadoc inherited
    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (HTMLVersion4_0_IE6) protocol;
        this.testable = (HTMLVersion4_0Testable) testable;
    }

    private void privateSetup() {
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, context);
        context.pushRequestContext(requestContext);
        context.setDeviceName("PC-Win32-IE6.0");
        protocol.setMarinerPageContext(context);
        testable.setUpCssEmulation(true);
    }

    /**
     * Setup the style attributes for testing panes.
     * @param attributeValueHorizontal
     * @param attributeValueVertical
     * @return the element created.
     */
    private Element setupPaneStyleAttributes(
            String attributeValueHorizontal,
            String attributeValueVertical) {

        privateSetup();

        PaneAttributes attributes = createPaneAttributes(
                attributeValueHorizontal, attributeValueVertical);

        Element element = domFactory.createStyledElement(attributes.getStyles());
        element.setName("td");

        protocol.addPaneCellAttributes(element, attributes.getStyles());
        return element;
    }

    /**
     * Create the pane attribute for the given horizontal and vertical values.
     * @param horizontal the horizontal value.
     * @param vertical the vertical value.
     * @return the newly created PaneAttributes.
     */
    PaneAttributes createPaneAttributes(String horizontal, String vertical) {
        String css = getCssAlign(horizontal, vertical);

        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles(css));
        Pane pane = new Pane(null);
        attributes.setPane(pane);
        return attributes;
    }

    private String getCssAlign(String horizontal, String vertical) {
        StringBuffer css = new StringBuffer();
        if (horizontal != null) {
            css.append("text-align: " + horizontal + ";");
        }
        if (vertical != null) {
            css.append("vertical-align: " + vertical + ";");
        }
        return css.length() == 0 ? null : css.toString();
    }

    /**
     * Test the adding of pane cell attributes.
     * a. With no horizontal and vertical alignment values set for the pane.
     * b. With a style containing horizontal and/or vertical alignment values
     *    and (a).
     * c. (a) and (b) in various combinations.
     */
    public void testAddPaneCellAttributesTC1() throws Exception {
        // (00.00)
        verifyResult(setupPaneStyleAttributes(null, null),
                     DEFAULT_ALIGN,
                     DEFAULT_VALIGN);

    }

    public void testAddPaneCellAttributesTC2() throws Exception {
        // (10.00)
        verifyResult(setupPaneStyleAttributes("right", null),
                     "right",
                     DEFAULT_VALIGN);
    }

    public void testAddPaneCellAttributesTC3() throws Exception {
        // (01.00)
        verifyResult(setupPaneStyleAttributes(null, "bottom"),
                     DEFAULT_ALIGN,
                     "bottom");
    }

    public void testAddPaneCellAttributesTC7() throws Exception {
        // (11.00)
        verifyResult(setupPaneStyleAttributes("right", "bottom"),
                     "right",
                     "bottom");
    }

    /**
     * Utility method for verifying the result of the test.
     * @param element the element.
     * @param hAlignExpected the expected horizontal alignment value.
     * @param vAlignExpected the expected vertical alignment value.
     */
    private void verifyResult(Element element,
                              String hAlignExpected,
                              String vAlignExpected) {
        assertEquals("Align should match",
                     hAlignExpected,
                     element.getAttributeValue("align"));

        assertEquals("Vertical align should match",
                     vAlignExpected,
                     element.getAttributeValue("valign"));
    }

    private void verifyResult(DOMOutputBuffer buffer,
                              String expected)
            throws Exception {

        assertEquals("Result should match",
                     DOMUtilities.provideDOMNormalizedString(
                             expected, protocol.getCharacterEncoder()),
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));
    }

    private DOMOutputBuffer setupGridStyleAttributes(String horizontal,
                                                     String vertical) {

        privateSetup();

        GridChildAttributes attributes = createGridChildAttributes(
                horizontal, vertical);
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.initialise();
        context.setCurrentOutputBuffer(buffer);
        protocol.openGridChild(buffer, attributes);
        return buffer;
    }

    private GridChildAttributes
            createGridChildAttributes2(String horizontal,
                                      String vertical) {

        GridChildAttributes attributes = new GridChildAttributes();

//        StylingFactory factory = StylingFactory.getDefaultInstance();
//        Styles styles = factory.createStyles(null);
//        MutablePropertyValues propertyValues = styles.getPropertyValues();
//        attributes.setStyles(styles);
//        if (horizontal != null) {
//            propertyValues.setSynthesizedValue(new StyleKeyword(TextAlignEnumeration.));
//            attributes.setAlign(horizontal);
//        }
//        if (vertical != null) {
//            attributes.setVAlign(vertical);
//        }
        return attributes;
    }

    private GridChildAttributes
            createGridChildAttributes(String horizontal,
                                      String vertical) {

        GridChildAttributes attributes = new GridChildAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                getCssAlign(horizontal, vertical)));

        return attributes;
    }

    public void testOpenGridChildTC1() throws Exception {
        // (00.00)
        verifyResult(setupGridStyleAttributes(null, null),
                     "<td align=\"" + DEFAULT_ALIGN +
                     "\" valign=\"" + DEFAULT_VALIGN + "\"/>");
    }

    public void testOpenGridChildTC2() throws Exception {
        // (10.00)
        verifyResult(setupGridStyleAttributes("right", null),
                     "<td align=\"right\" valign=\"" + DEFAULT_VALIGN +
                     "\"/>");
    }

    public void testOpenGridChildTC3() throws Exception {
        // (01.00)
        verifyResult(setupGridStyleAttributes(null, "bottom"),
                     "<td align=\"" + DEFAULT_ALIGN +
                     "\" valign=\"bottom\"/>");
    }

    public void testOpenGridChildTC4() throws Exception {
        // (00.10)
        verifyResult(setupGridStyleAttributes("right", "bottom"),
                     "<td align=\"right\" valign=\"bottom\"></td>");
    }

    private Element setupTableCellAttributes(String attributeValueHorizontal,
                                             String attributeValueVertical)
            throws Exception {

        return setupTableCellAttributes(attributeValueHorizontal,
                                        attributeValueVertical,
                                        null, null);
    }

    private Element setupTableCellAttributes(String attributeValueHorizontal,
                                             String attributeValueVertical,
                                             String styleValueHorizontal,
                                             String styleValueVertical)
            throws Exception {

        privateSetup();

        Element element = domFactory.createElement();
        element.setName("td");

        TableCellAttributes attributes = createTableCellAttributes(
                attributeValueHorizontal, attributeValueVertical);
        protocol.addTableCellAttributes(element, attributes);
        return element;
    }

    private TableCellAttributes
            createTableCellAttributes(String horizontal,
                                      String vertical) {

        TableCellAttributes attributes = new TableCellAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                getCssAlign(horizontal, vertical)));

        return attributes;
    }

    public void testOpenTableCellTC1() throws Exception {
        // (00.00)
        verifyResult(setupTableCellAttributes(null, null),
                     DEFAULT_ALIGN,
                     DEFAULT_VALIGN);
    }

    public void testOpenTableCellTC2() throws Exception {
        // (10.00)
        verifyResult(setupTableCellAttributes("right", null),
                     "right",
                     DEFAULT_VALIGN);
    }

    public void testOpenTableCellTC3() throws Exception {
        // (01.00)
        verifyResult(setupTableCellAttributes(null, "bottom"),
                     DEFAULT_ALIGN,
                     "bottom");
    }

    public void testOpenTableCellTC4() throws Exception {
        // (00.10)
        verifyResult(setupTableCellAttributes("right", "bottom"),
                     "right",
                     "bottom");
    }

    /**
     * Test the method for setting the alignment attributes.
     */
    public void testUpdateAlignAttributes() throws Exception {

        doTestUpdateAlignAttributes(null, DEFAULT_ALIGN);
        doTestUpdateAlignAttributes("right", "right");
    }

    private void doTestUpdateAlignAttributes(
            String actualValue,
            String expected) {
        Element element = domFactory.createElement();
        element.setName("td");
        final String ALIGN = "align";

        protocol.updateAlignment(element, ALIGN, DEFAULT_ALIGN, actualValue);
        assertEquals("Value should match", element.getAttributeValue(ALIGN), expected);
    }

    // javadoc inherited
    public void testUseEnclosingTableCell() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        doTestUseEnclosingTableCell(attributes, "<td align=\"left\" valign=\"center\"/>");
    }

    // javadoc inherited
    protected String getExpectedCreateEnclosingElementTable() {
        return  "<td>" +
                    "<table cellpadding=\"0\" cellspacing=\"0\">" +
                        "<tr>" +
                            "<td align=\"left\" valign=\"middle\">" +
                                "<div/>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</td>";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/4	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 19-Aug-05	9245/4	gkoch	VBM:2005081006 vbm2005081006 storing property values in styles

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/3	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 03-Nov-04	5871/3	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 15-Mar-04	3422/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 17-Dec-03	2242/1	andy	VBM:2003121702 vbm2003121702

 30-Sep-03	1475/1	byron	VBM:2003092606 Move contents of accessors.xml package to jdom package

 16-Sep-03	1416/1	byron	VBM:2003090306 Default valign and align lost on IE6 protocol

 16-Sep-03	1408/1	byron	VBM:2003090306 Default valign and align lost on IE6 protocol

 ===========================================================================
*/
