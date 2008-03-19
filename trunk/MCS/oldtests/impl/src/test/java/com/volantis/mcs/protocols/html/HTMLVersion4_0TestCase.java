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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/HTMLVersion4_0TestCase.java,v 1.3 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Nov-02    Adrian          VBM:2002100310 - Created this class as a
 *                              testcase for HTMLVersion3_2
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes.
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Changed inheritance to
 *                              accurately reflect the shadowed protocol
 *                              hierarchy.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.StylesBuilder;

import java.io.IOException;

/**
 * This class unit test the HTMLVersion4_0 class.
 */
public class HTMLVersion4_0TestCase 
    extends HTMLRootTestAbstract {

    /**
     * The HTMLVersion4_0 protocol string.
     */
    private static final String PROTOCOL_STRING = "<!DOCTYPE HTML"
            + " PUBLIC \"-//W3C//DTD HTML 4.0//EN\" "
            + "\"http://www.w3.org/TR/REC-html40/loose.dtd\">";

    private HTMLVersion4_0 protocol;
    private HTMLVersion4_0Testable testable;

    /**
     * Create a new instance of this testcase.
     * @param name The name of the testcase.
     */
    public HTMLVersion4_0TestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestHTMLVersion4_0Factory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (HTMLVersion4_0) protocol;
        this.testable = (HTMLVersion4_0Testable) testable;
    }

    public void testOpenPane() {
        // stub out XHTMLBasic temporary
        // panes in html don't use DIVs like they do in xhtml
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    /**
     * Set up the tests to check that CSS2 border-spacing is emulated in table
     * attributes correctly
     * @param borderSpacing An int containing the value of the border spacing
     * @return Element The table element
     * @throws Exception
     */
    private Element setUpTableAttributesBorderSpacingTests(int borderSpacing)
            throws Exception {
        // Set up the required contexts
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, context);
        testable.setStyleSheetRenderer(CSSStyleSheetRenderer.getSingleton());
        context.setDeviceName("PC-Win32-IE5.5");
        protocol.setMarinerPageContext(context);
        testable.setUpCssEmulation(true);

        // Set up the test
        Element table = domFactory.createElement();
        table.setName("table");
        TableAttributes attributes = new TableAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "border-spacing: " + borderSpacing + "px"));

        protocol.addTableAttributes(table, attributes);

        return table;
    }

    /**
     * Test the addTableAttributes method
     * @throws Exception
     */
    public void testAddTableAttributesBorderSpacingZero() throws Exception {
        Element table = setUpTableAttributesBorderSpacingTests(0);

        assertEquals("Wrong value for border attribute",
                     "0", table.getAttributeValue("border"));
        assertEquals("Wrong value for cellspacing attribute",
                     null, table.getAttributeValue("cellspacing"));
    }

    /**
     * Test the addTableAttributes method
     * @throws Exception
     */
    public void testAddTableAttributesBorderSpacingNonZero() throws Exception {
        Element table = setUpTableAttributesBorderSpacingTests(10);

        assertNull("border attribute should be null",
                   table.getAttributeValue("border"));
        assertEquals("Wrong value for cellspacing attribute",
                     "10", table.getAttributeValue("cellspacing"));
    }

    /**
     * This method tests doProtocolString(DOMOutputBuffer)
     * @todo should factor out the temporary of this with the parent temporary too
     */
    public void testDoProtocolString() throws IOException {
        checkDoProtocolString(protocol, PROTOCOL_STRING);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 21-Aug-03	1240/3	chrisw	VBM:2003070811 implemented rework

 21-Aug-03	1240/1	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from mimas to proteus

 21-Aug-03	1219/2	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from metis to mimas

 20-Aug-03	1152/1	chrisw	VBM:2003070811 Emulate CSS2 border-spacing using cellspacing on table element

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 ===========================================================================
*/
