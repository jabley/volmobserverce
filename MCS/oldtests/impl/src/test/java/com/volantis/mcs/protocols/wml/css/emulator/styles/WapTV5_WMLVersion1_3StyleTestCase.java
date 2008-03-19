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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/css/emulator/styles/WapTV5_WMLVersion1_3StyleTestCase.java,v 1.7 2003/04/25 23:23:45 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Dec-02    Adrian          VBM:2002110106 - Created to test changes to the
 *                              method addPosition in WapTV5_WMLVersion1_3
 * 16-Jan-03    Adrian          VBM:2002110505 - Updated testAddPosition. Added
 *                              testAddBackgroundPosition.
 * 06-Feb-03    Geoff           VBM:2003012101 - Refactor so that it uses the
 *                              new TestMariner...Context classes rather than
 *                              "cut & paste" inner classes which extend
 *                              Mariner...Context.
 * 24-Feb-03    Ian             VBM:2002072414 - Added test for refactored
 *                              methods.
 * 18-Mar-03    Byron           VBM:2003031105 - Renamed setUp to privateSetup.
 *                              Added testAddPixelLength, testAddVspace (and
 *                              associated helper methods).
 * 23-Apr-03    Adrian          VBM:2003041104 - Added heirarchy methods
 *                              getProtocol and getStyle
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml.css.emulator.styles;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.wml.WapTV5_WMLVersion1_3;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.FontFamilyKeywords;
import com.volantis.mcs.themes.properties.PositionKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ArrayList;


/**
 * This class unit test the WapTV5_WMLVersion1_3Styleclass.
 */
public class WapTV5_WMLVersion1_3StyleTestCase
        extends TestCaseAbstract {

    /**
     * Property values to populate and use in the tests
     */
    private MutablePropertyValues properties;

    /**
     * The protocol style to use for the tests.
     */
    WapTV5_WMLVersion1_3Style style;
    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();
    /**
     * The object to use to create style values.
     */
    protected StyleValueFactory styleValueFactory;

    // Javadoc inherited from super class
    protected VolantisProtocol getProtocol() {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestWapTV5_WMLVersion1_3Factory(), null);
        ((VolantisProtocolTestable) protocol).setStyleSheetRenderer(
                CSSStyleSheetRenderer.getSingleton());
        return protocol;
    }

    protected void privateSetUp() throws Exception {
        MarinerPageContext pageContext = new TestMarinerPageContext();
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        properties = createPropertyValues();

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder builder = new ProtocolBuilder();
        WapTV5_WMLVersion1_3 protocol = (WapTV5_WMLVersion1_3) builder.build(
                new TestProtocolRegistry.TestWapTV5_WMLVersion1_3Factory(),
                internalDevice);
        ((VolantisProtocolTestable) protocol).setStyleSheetRenderer(
                CSSStyleSheetRenderer.getSingleton());
        protocol.setMarinerPageContext(pageContext);
        style = new WapTV5_WMLVersion1_3Style(properties, protocol);
    }


    /**
     * Test public void addHorizontalPadding ( Element,String ) method.
     */
    public void testAddBorderHorizontalSpacing() throws Exception {
        privateSetUp();

        StyleLength borderHorizontalSpacing = styleValueFactory.getLength(null, 10,
                LengthUnit.PX);

        StyleLength borderVerticalSpacing = styleValueFactory.getLength(null, 20,
                LengthUnit.PX);

        StylePair borderSpacing = styleValueFactory.getPair(
            borderHorizontalSpacing, borderVerticalSpacing);

        properties.setComputedValue(
                StylePropertyDetails.BORDER_SPACING, borderSpacing);

        Element element = domFactory.createElement();
        style.addBorderHorizontalSpacing(element, "ardvark");
        String attribute = element.getAttributeValue("ardvark");

        // The value should be BorderHorizontalSpacing
        assertEquals("Method addBorderHorizontalSpacing added incorrect value to the given" +
                "Element.", "10", attribute);
    }

    /**
     * Test public void addVerticalSpacing ( Element,String ) method.
     */
    public void testAddBorderVerticalSpacing() throws Exception {
        privateSetUp();

        StyleLength borderHorizontalSpacing = styleValueFactory.getLength(null, 10,
                LengthUnit.PX);

        StyleLength borderVerticalSpacing = styleValueFactory.getLength(null, 20,
                LengthUnit.PX);

        StylePair borderSpacing = styleValueFactory.getPair(
            borderHorizontalSpacing, borderVerticalSpacing);

        properties.setComputedValue(
                StylePropertyDetails.BORDER_SPACING, borderSpacing);

        Element element = domFactory.createElement();
        style.addBorderVerticalSpacing(element, "ardvark");
        String attribute = element.getAttributeValue("ardvark");

        // The value should be BorderVerticalSpacing
        assertEquals("Method addBorderVerticalSpacing added incorrect value to the given" +
                "Element.", "20", attribute);
    }

    /**
     * Test public void addHorizontalPadding ( Element,String ) method.
     */
    public void testAddHorizontalPadding() throws Exception {
        privateSetUp();

        StyleLength paddingLeft = styleValueFactory.getLength(null, 10,
                LengthUnit.PX);
        properties.setComputedValue(
                StylePropertyDetails.PADDING_LEFT, paddingLeft);

        StyleLength paddingRight = styleValueFactory.getLength(null, 20,
                LengthUnit.PX);
        properties.setComputedValue(
                StylePropertyDetails.PADDING_RIGHT, paddingRight);

        Element element = domFactory.createElement();
        style.addHorizontalPadding(element, "ardvark");
        String attribute = element.getAttributeValue("ardvark");

        // The value should be (left + right)/2
        assertEquals("Method addHorizontalPadding added incorrect value to the given" +
                "Element.", "15", attribute);
    }

    /**
     * Test public void addHorizontalSpace ( Element,String ) method.
     */
    public void testAddHorizontalSpace() throws Exception {
        privateSetUp();

        StyleLength marginLeft = styleValueFactory.getLength(null, 10,
                LengthUnit.PX);
        properties.setComputedValue(
                StylePropertyDetails.MARGIN_LEFT, marginLeft);

        StyleLength marginRight = styleValueFactory.getLength(null, 20,
                LengthUnit.PX);
        properties.setComputedValue(
                StylePropertyDetails.MARGIN_RIGHT, marginRight);

        Element element = domFactory.createElement();
        style.addHorizontalSpace(element, "ardvark");
        String attribute = element.getAttributeValue("ardvark");

        // The value should be (left + right)/2
        assertEquals("Method addHorizontalSpace added incorrect value to the given" +
                "Element.", "15", attribute);
    }

    /**
     * Test public void addVerticalPadding ( Element,String ) method.
     */
    public void testAddVerticalPadding() throws Exception {
        privateSetUp();

        StyleLength paddingTop = styleValueFactory.getLength(null, 10,
                LengthUnit.PX);
        properties.setComputedValue(
                StylePropertyDetails.PADDING_TOP, paddingTop);

        StyleLength paddingBottom = styleValueFactory.getLength(null, 20,
                LengthUnit.PX);
        properties.setComputedValue(
                StylePropertyDetails.PADDING_BOTTOM, paddingBottom);

        Element element = domFactory.createElement();
        style.addVerticalPadding(element, "ardvark");
        String attribute = element.getAttributeValue("ardvark");

        // The value should be (left + right)/2
        assertEquals("Method addVerticalPadding added incorrect value to the given" +
                "Element.", "15", attribute);
    }

    /**
     * Test public void addMarinerLineGap ( Element,String ) method.
     */
    public void testAddMarinerLineGap() throws Exception {
        privateSetUp();

        StyleLength marinerLineGap = styleValueFactory.getLength(null, 10,
                LengthUnit.PX);

        properties.setComputedValue(
                StylePropertyDetails.MCS_LINE_GAP, marinerLineGap);

        Element element = domFactory.createElement();
        style.addMarinerLineGap(element, "ardvark");
        String attribute = element.getAttributeValue("ardvark");

        // The value should be MarinerLineGap
        assertEquals("Method addMarinerLineGap added incorrect value to the given" +
                "Element.", "10", attribute);
    }

    /**
     * Test public void addParagraph ( Element,String ) method.
     */
    public void testAddMarinerParagraphGap() throws Exception {
        privateSetUp();

        StyleLength marinerParagraphGap = styleValueFactory.getLength(null, 10,
                LengthUnit.PX);

        properties.setComputedValue(
                StylePropertyDetails.MCS_PARAGRAPH_GAP, marinerParagraphGap);

        Element element = domFactory.createElement();
        style.addMarinerParagraphGap(element, "ardvark");
        String attribute = element.getAttributeValue("ardvark");

        // The value should be MarinerLineGap
        assertEquals("Method addMarinerParagraphGap added incorrect value to the given" +
                "Element.", "10", attribute);
    }

    /**
     * Test public void addPixelLength() method.
     */
    public void testAddPixelLength() throws Exception {
        privateSetUp();

        style.addPixelLength(null,null,null);

        doAddPixelTest(0.5, LengthUnit.MM, null);
        doAddPixelTest(0.0, LengthUnit.MM, null);
        doAddPixelTest(0.5, LengthUnit.PX, "1");
        doAddPixelTest(10.5, LengthUnit.PX, "11");
    }
    
    public void testAddFontFamilyDefault() throws Exception {
        privateSetUp();
        Element element = domFactory.createElement();
        style.addFontFamily(element, "font", true);
        assertTrue("Default font must be helvetica",
            element.getAttributeValue("font").equals("helvetica"));
    }
    
    public void testAddFontFamily() throws Exception {
        privateSetUp();
        Element element = domFactory.createElement();
        ArrayList fonts = new ArrayList();
        StyleKeyword keyword = FontFamilyKeywords.SANS_SERIF;
        fonts.add(keyword);
        StyleList list = styleValueFactory.getList(fonts);            
        properties.setComputedValue(StylePropertyDetails.FONT_FAMILY, list);
        style.addFontFamily(element, "font", true);
        assertTrue("Font must be sans-serif and not "+
                    element.getAttributeValue("font"),
                    element.getAttributeValue("font").equals("sans-serif"));
    }
    /**
     * Helper method for addPixelLength test.
     */
    private void doAddPixelTest(double value, LengthUnit unitType, String expected) {
        Element element = domFactory.createElement();
        StyleValue styleValue = styleValueFactory.getLength(null, value, unitType);
        style.addPixelLength(element, "attr", styleValue);
        assertEquals(expected, element.getAttributeValue("attr"));
    }

    /**
     * This method tests the method public void addPosition ( Element,String )
     */
   public void testAddPosition() throws Exception {
        privateSetUp();

        StyleKeyword styleValuePosition = PositionKeywords.ABSOLUTE;
        properties.setComputedValue(
                StylePropertyDetails.POSITION, styleValuePosition);

        StyleInteger styleTopInteger =
                styleValueFactory.getInteger(null, 50);
        properties.setComputedValue(StylePropertyDetails.TOP, styleTopInteger);

        StyleInteger styleLeftInteger =
                styleValueFactory.getInteger(null, 25);
        properties.setComputedValue(
                StylePropertyDetails.LEFT, styleLeftInteger);

        Element element = domFactory.createElement();
        style.addPosition(element, "pos");

        String position = element.getAttributeValue("pos");

        assertNull("Method addPosition should not have added a value as " +
                "only Lengths with pixel units are valid", position);

        // Test with StyleLength values.
        StyleLength styleTopLength = styleValueFactory.getLength(null, 50.0, LengthUnit.PX);
        properties.setComputedValue(StylePropertyDetails.TOP, styleTopLength);

        StyleLength styleLeftLength = styleValueFactory.getLength(null, 25.0, LengthUnit.PX);
        properties.setComputedValue(StylePropertyDetails.LEFT, styleLeftLength);

        element = domFactory.createElement();
        style.addPosition(element, "pos");

        position = element.getAttributeValue("pos");

        assertEquals("Method addPosition added incorrect value to the given" +
                "Element.", "25,50", position);
    }

    /**
     * This method tests the method public void addBackgroundPosition ( Element,String )
     * for the com.volantis.mcs.protocols.wml.css.emulator.styles.WapTV5_WMLVersion1_3Style class.
     */
    public void testAddBackgroundPosition()
            throws Exception {
        //
        // Test public void addPosition ( Element,String ) method.
        //
        privateSetUp();

        StyleLength styleX = styleValueFactory.getLength(null, 25.0, LengthUnit.PX);

        StyleLength styleY = styleValueFactory.getLength(null, 50.0, LengthUnit.PX);

        StylePair pair = styleValueFactory.
                getPair(styleX, styleY);
        properties.setComputedValue(
                StylePropertyDetails.BACKGROUND_POSITION, pair);

        Element element = domFactory.createElement();
        style.addBackgroundPosition(element, "bgoffset");

        String position = element.getAttributeValue("bgoffset");

        assertEquals("Method addBackgroundPosition added incorrect " +
                "value to the given Element.", "25,50", position);
    }

/*    public void testDoAddLengthPair() {
        MyWapTV5_WMLVersion1_3 protocol = new MyWapTV5_WMLVersion1_3();
        protocol.setMarinerPageContext(pageContext);

        WapTV5_WMLVersion1_3Style style =
                new WapTV5_WMLVersion1_3Style(properties, protocol);

        StyleInteger styleInteger1 =
                styleValueFactory.getInteger(25, StyleValue.PRIORITY_NORMAL);

        StyleInteger styleInteger2 =
                styleValueFactory.getInteger(50, StyleValue.PRIORITY_NORMAL);

        Element element = new Element();
        String attribute = new String("myAttr");
        style.doAddLengthPair(styleInteger1, styleInteger2, element,
                attribute, LengthUnit.PX);

        String position = element.getAttribute(attribute);

        assertNull("Method addPosition should not have added a value as " +
                "only StyleLengths are valid", position);

        //
        // Test with StyleLength values but incorrect unit constraint.
        //
        StyleLength styleLength1 = styleValueFactory.getLength(
                25.0, LengthUnit.PX, StyleValue.PRIORITY_NORMAL);

        StyleLength styleLength2 = styleValueFactory.getLength(
                50.0, LengthUnit.PX, StyleValue.PRIORITY_NORMAL);

        element = new Element();
        style.doAddLengthPair(styleLength1, styleLength2, element,
                attribute, LengthUnit.EM);

        position = element.getAttribute(attribute);

        assertNull("Method addPosition should not have added a value as " +
                "the units did not meet the unit constraint", position);


        //
        // Test with StyleLength values but correct unit constraint.
        //
        element = new Element();
        style.doAddLengthPair(styleLength1, styleLength2, element,
                attribute, LengthUnit.PX);

        position = element.getAttribute(attribute);

        assertEquals("Method addPosition added incorrect value to the given" +
                "Element.", "25,50", position);
    } */

    public void testAddVspace() throws Exception {
        privateSetUp();
        doAddVspace(1.0, LengthUnit.MM, null);
        doAddVspace(0.0, LengthUnit.MM, null);
        doAddVspace(0.9, LengthUnit.MM, null);

        doAddVspace(1.0, LengthUnit.PX, "1");
        doAddVspace(0.0, LengthUnit.PX, "0");
        doAddVspace(0.9, LengthUnit.PX, "1");
    }

    /**
     * Helper method for addPixelLength test.
     */
    private void doAddVspace(double value, LengthUnit unitType, String expected) {
        Element element = domFactory.createElement();
        StyleValue styleValue = styleValueFactory.getLength(null, value, unitType);
        properties.setComputedValue(
                StylePropertyDetails.MCS_PARAGRAPH_GAP, styleValue);
        style.addVspace(element, "attr");
        assertEquals(expected, element.getAttributeValue("attr"));
    }

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        styleValueFactory = StyleValueFactory.getDefaultInstance();
    }

    /**
     * Create a set of mutable property values.
     *
     * @return A newly created set of mutable property values.
     */
    protected MutablePropertyValues createPropertyValues() {
        StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();
        return StylingFactory.getDefaultInstance()
                .createPropertyValues(definitions);
    }

    /**
     * Create a Styles.
     *
     * @return A newly created set of mutable property values.
     */
    protected Styles createStyles() {
        return StylingFactory.getDefaultInstance().createStyles(null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 02-Jul-03	685/1	sumit	VBM:2002101505 Added unit tests for font family addition for WAP TV

 02-Jul-03	682/1	sumit	VBM:2002101505 Added unit tests for font family addition for WAP TV

 ===========================================================================
*/
