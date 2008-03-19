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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/MHTMLTestCase.java,v 1.5 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Jan-03    Steve           VBM:2003010710  MHTML test case copied from
 *                              HTML 3.2
 * 30-Jan-03    Steve           VBM:2003010710  Test case does not test the
 *                              bgcolor attribute on tables which the VBM was
 *                              raised for in the first place.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add ProtocolException
 *                              declarations where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;

/**
 * This class unit test the MHTML class.
 */
public class MHTMLTestCase extends HTMLVersion3_2TestCase {

    // this will be used when we add some of our test here rather than
    // just inherit parent tests
    private MHTML protocol;
    private XHTMLBasicTestable testable;

    public MHTMLTestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestMHTMLFactory(), internalDevice);
        return protocol;
    }

    // this will be used when we add some of our test here rather than
    // just inherit parent tests
    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (MHTML) protocol;
        this.testable = (XHTMLBasicTestable) testable;
    }


    // JavaDoc inherited
    public void testTableInclusionMarkup() throws Exception {
        // PalmWCA doesn't do style emulation hence this test as written
        // is not applicable.
    }

    // JavaDoc inherited
    public void testStandardInclusionMarkup() throws Exception {
        // PalmWCA doesn't do style emulation hence this test as written
        // is not applicable.
    }

   /**
    * Overridden from XTHMLBasicTestCase because it tests for the setting of
    * a class core attribute and MHTML does not have such an attribute. - Not
    * a good design but a side effect of the hierarchical nature of protocols
    * and poor design in XHTMLBasicTestCase. Prior to this change the
    * MHTMLTestCase (this class) was never tested.
    * @todo later provide a better solution for this test here and in parent
    */
    protected void doTest2002082101() {
    }

    /**
     * Overridden from XTHMLBasicTestCase because it tests for the setting of
     * a class core attribute and MHTML does not have such an attribute. - Not
     * a good design but a side effect of the hierarchical nature of protocols
     * and poor design in XHTMLBasicTestCase. Prior to this change the
    * MHTMLTestCase (this class) was never tested.
     * @todo later provide a better solution for this test here and in parent
     */
     protected void doTest2002082102() {
     }

    /**
     * Overridden from XTHMLBasicTestCase because it tests for the setting of
     * a class core attribute and MHTML does not have such an attribute. - Not
     * a good design but a side effect of the hierarchical nature of protocols
     * and poor design in XHTMLBasicTestCase. Prior to this change the
    * MHTMLTestCase (this class) was never tested.
     * @todo later provide a better solution for this test here and in parent
     */
     protected void doTest2002082202() {
     }

    /**
     * Overridden from XTHMLBasicTestCase because it tests for the setting of
     * a class core attribute and MHTML does not have such an attribute. - Not
     * a good design but a side effect of the hierarchical nature of protocols
     * and poor design in XHTMLBasicTestCase. Prior to this change the
    * MHTMLTestCase (this class) was never tested.
     * @todo later provide a better solution for this test here and in parent
     */
        public void testNoneTransformation() {
    }

    /**
     * Overridden from XTHMLBasicTestCase because it tests for the setting of
     * a class core attribute and MHTML does not have such an attribute. - Not
     * a good design but a side effect of the hierarchical nature of protocols
     * and poor design in XHTMLBasicTestCase. Prior to this change the
    * MHTMLTestCase (this class) was never tested.
     * @todo later provide a better solution for this test here and in parent
     */
    public void testAttributeTransformation() {
    }

    /**
     * Overridden from XTHMLBasicTestCase because it tests for the setting of
     * a class core attribute and MHTML does not have such an attribute. - Not
     * a good design but a side effect of the hierarchical nature of protocols
     * and poor design in XHTMLBasicTestCase. Prior to this change the
    * MHTMLTestCase (this class) was never tested.
     * @todo later provide a better solution for this test here and in parent
     */
    public void doTestSelectorTransformation(String testElement) {
    }

    /**
     * Overridden from XTHMLBasicTestCase because it tests for the setting of
     * a class core attribute and MHTML does not have such an attribute. - Not
     * a good design but a side effect of the hierarchical nature of protocols
     * and poor design in XHTMLBasicTestCase. Prior to this change the
    * MHTMLTestCase (this class) was never tested.
     * @todo later provide a better solution for this test here and in parent
     */
    public void testNoDeviceThemeWithAltClassName() {
    }
    
   /**
    * Test that bgcolor and other table attributes are written to the table
    * element when present in a style.
    */
/*
   public void testOpenTable() throws ProtocolException {
       TestMarinerPageContext pageContext = new TestMarinerPageContext();
       // Strangely, these lines only required in Mimas and not Metis. Weird.
       // I bet Paul knows why. Might be something to do with the
       // ApplicationContext stuff not being in Mimas yet?
       TestMarinerRequestContext requestContext =
               new TestMarinerRequestContext();
       pageContext.pushRequestContext(requestContext);

       protocol.setMarinerPageContext(pageContext);
       requestContext.setMarinerPageContext(pageContext);

       TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

       MutablePropertyValues properties = createPropertyValues();
       properties.setSynthesizedValue(StylePropertyDetails.TEXT_ALIGN,
               new StyleKeyword(TextAlignEnumeration.LEFT));
       properties.setSynthesizedValue(StylePropertyDetails.BORDER_SPACING,
               new StylePair(styleValueFactory.getLength(30,
               LengthUnit.PX, StyleValue.PRIORITY_NORMAL), null));
       properties.setSynthesizedValue(StylePropertyDetails.BORDER_TOP_WIDTH,
               styleValueFactory.getLength(10, LengthUnit.PX,
                       StyleValue.PRIORITY_NORMAL));
       properties.setSynthesizedValue(StylePropertyDetails.WIDTH,
               styleValueFactory.getLength(40, LengthUnit.PX,
                       StyleValue.PRIORITY_NORMAL));
       properties.setSynthesizedValue(StylePropertyDetails.BACKGROUND_COLOR,
               new StyleColor(0x777777));

       TableAttributes attributes = new TableAttributes();       
       attributes.setStyles(new StylesMock(properties));

       protocol.openTable(buffer, attributes);

       Element element = buffer.popElement();
       assertNotNull(element);
       assertNotNull(element.getName());
       assertEquals("table", element.getName());

       // text-align:left -> align="left"
       assertEquals("left", element.getAttributeValue("align"));
       // border-spacing:30px -> cellspacing="30" (was margin-top previously?)
       assertEquals("30", element.getAttributeValue("cellspacing"));
       // border-width:10px -> border="10"
       assertEquals("10", element.getAttributeValue("border"));
       // width:40px -> width="40"
       assertEquals("40", element.getAttributeValue("width"));
       // background-color:#777777 -> bgcolor="#777777"
       assertEquals("#777777", element.getAttributeValue("bgcolor"));
   }

*/
    /**
     * Test the method addPaneTableAttributes(Element, PaneAttributes)
     */
    public void notestAddPaneTableAttributes() throws Exception {
        doAddPaneTableAttributesTest(
                " cellspacing=\"0\"",
                " cellspacing=\"1\"");
//        context = new TestMarinerPageContext();
//        TestMarinerRequestContext requestContext =
//                new TestMarinerRequestContext();
//        ContextInternals.setMarinerPageContext(requestContext, context);
//        context.pushRequestContext(requestContext);
//
//        context.setDeviceName("Master");
//        protocol.setMarinerPageContext(context);
//
//        Element element = domFactory.createElement();
//        PaneAttributes attrs = new PaneAttributes();
//
//        protocol.addPaneTableAttributes(element, attrs);
//
////        String cellpadding = element.getAttributeValue("cellpadding");
////        assertEquals("cellpadding attribute should have been set to " +
////                "default as no styles were specified.", "0", cellpadding);
//
//        String cellspacing = element.getAttributeValue("cellspacing");
//        assertEquals("cellspacing attribute should have been set to " +
//                "default as no styles were specified.", "0", cellspacing);
//
//        String border = element.getAttributeValue("border");
//        assertNull("border attribute should not have been set as " +
//                "no styles were specified.", border);
//
//        MutablePropertyValues properties = createTestPanePropertyValues();
//        Style style = StyleFactory.getStyle(properties, protocol);
//
//        testable.setStyle(attrs, style);
//
//        element = domFactory.createElement();
//        protocol.addPaneTableAttributes(element, attrs);
//
////        cellpadding = element.getAttributeValue("cellpadding");
////        assertEquals("cellpadding attribute should have been set as " +
////                "stylesheets are not supported.", "1", cellpadding);
//
//        cellspacing = element.getAttributeValue("cellspacing");
//        assertEquals("cellspacing attribute should have been set as " +
//                "stylesheets are not supported.", "1", cellspacing);
//
//        border = element.getAttributeValue("border");
//        assertEquals("border attribute should have been set as " +
//                "stylesheets are not supported.", "1", border);
//
//        element = domFactory.createElement();
//        attrs = new PaneAttributes();
//        attrs.setStyles(StylesBuilder.getStyles(
//                "border-width: 2px; " +
//                "border-spacing: 2px"));
////        attrs.setBorderWidth("2");
////        attrs.setCellPadding("2");
////        attrs.setCellSpacing("2");
//        protocol.addPaneTableAttributes(element, attrs);
//
////        cellpadding = element.getAttributeValue("cellpadding");
////        assertEquals("cellpadding attribute should have been set as " +
////                "stylesheets are not supported.", "2", cellpadding);
//
//        cellspacing = element.getAttributeValue("cellspacing");
//        assertEquals("cellspacing attribute should have been set as " +
//                "stylesheets are not supported.", "2", cellspacing);
//
//        border = element.getAttributeValue("border");
//        assertEquals("border attribute should have been set as " +
//                "stylesheets are not supported.", "2", border);
    }

    /**
     * Test the method
     * addRowIteratorPaneAttributes(Element, RowIteratorPaneAttributes)
     *
     * @todo XDIME-CP
     */
    public void notestAddRowIteratorPaneAttributes() throws Exception {
//        context = new TestMarinerPageContext();
//        TestMarinerRequestContext requestContext =
//                new TestMarinerRequestContext();
//        ContextInternals.setMarinerPageContext(requestContext, context);
//        context.pushRequestContext(requestContext);
//
//        context.setDeviceName("Master");
//        protocol.setMarinerPageContext(context);
//
//        MutablePropertyValues properties = createTestPanePropertyValues();
//        Style style = StyleFactory.getStyle(properties, protocol);
//
//        Element element = domFactory.createElement();
//        RowIteratorPaneAttributes attrs =
//                new RowIteratorPaneAttributes();
//
//        protocol.addRowIteratorPaneAttributes(element, attrs);
//
//        String cellpadding = element.getAttributeValue("cellpadding");
//        assertEquals("cellpadding attribute should have been set as " +
//                "stylesheets are not supported.", "1", cellpadding);
//
//        String cellspacing = element.getAttributeValue("cellspacing");
//        assertEquals("cellspacing attribute should have been set as " +
//                "stylesheets are not supported.", "1", cellspacing);
//
//        String border = element.getAttributeValue("border");
//        assertEquals("border attribute should have been set as " +
//                "stylesheets are not supported.", "1", border);
//
//        element = domFactory.createElement();
//        attrs = new RowIteratorPaneAttributes();
////        attrs.setBorderWidth("2");
////        attrs.setCellPadding("2");
////        attrs.setCellSpacing("2");
//        protocol.addRowIteratorPaneAttributes(element, attrs);
//
//        cellpadding = element.getAttributeValue("cellpadding");
//        assertEquals("cellpadding attribute should have been set as " +
//                "stylesheets are not supported.", "2", cellpadding);
//
//        cellspacing = element.getAttributeValue("cellspacing");
//        assertEquals("cellspacing attribute should have been set as " +
//                "stylesheets are not supported.", "2", cellspacing);
//
//        border = element.getAttributeValue("border");
//        assertEquals("border attribute should have been set as " +
//                "stylesheets are not supported.", "2", border);
    }

    /**
     * Test the method
     * addColumnIteratorPaneAttributes(Element, ColumnIteratorPaneAttributes
     *
     * @todo XDIME-CP
     */
    public void notestAddColumnIteratorPaneAttributes() throws Exception {
//        context = new TestMarinerPageContext();
//        TestMarinerRequestContext requestContext =
//                new TestMarinerRequestContext();
//        ContextInternals.setMarinerPageContext(requestContext, context);
//        context.pushRequestContext(requestContext);
//
//        context.setDeviceName("Master");
//        protocol.setMarinerPageContext(context);
//
//        MutablePropertyValues properties = createTestPanePropertyValues();
//        Style style = StyleFactory.getStyle(properties, protocol);
//
//        Element element = domFactory.createElement();
//        ColumnIteratorPaneAttributes attrs =
//                new ColumnIteratorPaneAttributes();
//
//        protocol.addColumnIteratorPaneAttributes(element, attrs);
//
//        String cellpadding = element.getAttributeValue("cellpadding");
//        assertEquals("cellpadding attribute should have been set as " +
//                "stylesheets are not supported.", "1", cellpadding);
//
//        String cellspacing = element.getAttributeValue("cellspacing");
//        assertEquals("cellspacing attribute should have been set as " +
//                "stylesheets are not supported.", "1", cellspacing);
//
//        String border = element.getAttributeValue("border");
//        assertEquals("border attribute should have been set as " +
//                "stylesheets are not supported.", "1", border);
//
//        element = domFactory.createElement();
//        attrs = new ColumnIteratorPaneAttributes();
////        attrs.setBorderWidth("2");
////        attrs.setCellPadding("2");
////        attrs.setCellSpacing("2");
//        protocol.addColumnIteratorPaneAttributes(element, attrs);
//
//        cellpadding = element.getAttributeValue("cellpadding");
//        assertEquals("cellpadding attribute should have been set as " +
//                "stylesheets are not supported.", "2", cellpadding);
//
//        cellspacing = element.getAttributeValue("cellspacing");
//        assertEquals("cellspacing attribute should have been set as " +
//                "stylesheets are not supported.", "2", cellspacing);
//
//        border = element.getAttributeValue("border");
//        assertEquals("border attribute should have been set as " +
//                "stylesheets are not supported.", "2", border);
    }

    /**
     * Test the method supportsStyleSheets()
     */
    public void testSupportsStyleSheets() throws Exception {
        assertTrue("This protocol should not support stylesheets.",
                !protocol.supportsStyleSheets());
    }

    // This was only applicable to HTML3.2
    public void testBodytextColour() throws Exception {
        // Do nothing
    }

    // javadoc inherited.
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

        final Element expected;
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        dom.openStyledElement("body", attributes);
        dom.openElement("table");
        dom.openElement("tr");
        expected = dom.openElement("td");

        dom.appendEncoded("Example");

        protocol.closePane(dom, attributes);

        assertSame("The DOM's current element isn't as expected",
                   dom.getCurrentElement(),
                   expected);
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 23-Nov-05	10402/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 23-Nov-05	10381/4	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 23-Nov-05	10381/4	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 22-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	6183/5	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 18-Nov-04	6183/2	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 03-Nov-04	5871/2	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 02-Nov-04	6068/4	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 01-Nov-04	6064/1	claire	VBM:2004102801 mergevbm: Handling background colour for HTML 3.2 portlets

 01-Nov-04	6014/1	claire	VBM:2004102801 Handling background colour for HTML 3.2 portlets

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 20-Jul-04	4897/2	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 15-Jul-04	4869/1	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 27-May-04	4589/5	steve	VBM:2004051102 Output text colour in body tag

 26-May-04	4589/1	steve	VBM:2004051102 Output text colour in body tag

 26-May-04	4570/1	steve	VBM:2004051102 Output text colour in body tag

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 26-May-04	4570/1	steve	VBM:2004051102 Output text colour in body tag

 24-Jul-03	728/6	adrian	VBM:2003052001 fixedup testcases - removed suite and main methods

 07-Jul-03	728/4	adrian	VBM:2003052001 fixed pane attribute generation

 07-Jul-03	728/2	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	706/1	allan	VBM:2003070302 Added TestSuiteGenerator ant task. Run testsuite in a single jvm

 ===========================================================================
*/
