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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/HTMLPalmWCATestCase.java,v 1.2 2003/01/17 10:56:35 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-Jan-03    Byron           VBM:2003011501 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.styling.StylesBuilder;

/**
 * This class unit tests the HTMLPalmWCA class.
 */
public class HTMLPalmWCATestCase
        extends HTMLVersion3_2TestCase {

    private HTMLPalmWCA protocol;
    private XHTMLBasicTestable testable;

    public HTMLPalmWCATestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestHTMLPalmWCAFactory(),
                internalDevice);
        return protocol;
    }

    // this will be used when we add some of our test here rather than
    // just inherit parent tests
    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (HTMLPalmWCA) protocol;
        this.testable = (XHTMLBasicTestable) testable;
    }

    /**
     * This method tests the constructors for
     * the com.volantis.mcs.protocols.html.HTMLPalmWCA class.
     */
    public void testConstructors() {
        //
        // Test public HTMLPalmWCA() constructor
        //

        assertNotNull("Optimizing transformer should not be null",
                      protocol.optimizingTransformer);

        assertEquals("Supports scripts should be false",
                     false,
                     protocol.supportsScripts);

        assertEquals("Supports events should be false",
                     false,
                     protocol.supportsEvents());

        assertEquals("Supports optimization should be false",
                     false,
                     protocol.supportsFormatOptimization);

        assertTrue("Wrong type of configuration defined (" +
                   protocol.getProtocolConfiguration() + ")",
                   protocol.getProtocolConfiguration() instanceof
                   HTMLPalmWCAConfiguration);
    }

    // javadoc inherited
    public void testAddAnchorAttributesLinkStyle() throws Exception {

        // Set up the required contexts
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, context);
        testable.setStyleSheetRenderer(CSSStyleSheetRenderer.getSingleton());
        context.setDeviceName("PC-Win32-IE5.5");
        protocol.setMarinerPageContext(context);

        AnchorAttributes attributes = new AnchorAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        // test no link style
        Element element = domFactory.createStyledElement(attributes.getStyles());
        protocol.addAnchorAttributes(element, attributes);
        assertEquals("no link style means no button attribute",
                null, element.getAttributeValue("button"));

        // test unrelated numeric shortcut link style
        attributes.setStyles(StylesBuilder.getCompleteStyles(
                "mcs-link-style: numeric-shortcut"));
        element = domFactory.createStyledElement(attributes.getStyles());
        protocol.addAnchorAttributes(element, attributes);
        assertEquals("unrelated link style means no button attribute",
                null, element.getAttributeValue("button"));

        // test proper button link style
        attributes.setStyles(StylesBuilder.getCompleteStyles(
                "mcs-link-style: button"));
        element = domFactory.createStyledElement(attributes.getStyles());
        protocol.addAnchorAttributes(element, attributes);
        assertEquals("button link style means button attribute",
                "button", element.getAttributeValue("button"));

    }

    public void testAddPhoneNumberAttributesLinkStyle() throws Exception {

        // works exactly the same way as anchor for link styles.
        testAddAnchorAttributesLinkStyle();

    }

//    protected void verifyStyleManySet(String actual) {
//        assertEquals("Markup should match: ",
//                "", actual);
//    }


//    protected void verifyStyleAlign(String actual) {
//        assertEquals("Markup should match: ",
//                "<p/>", actual);
//    }

//    protected void verifyStyleVerticalAlignTD(final String actual) {
//        assertEquals("Markup should match: ",
//                "<table>" +
//                    "<td>" +
//                        "<img border=\"0\" src=\"images/background.jpg\"/>" +
//                    "</td>" +
//                "</table>",
//                actual);
//    }

//    protected void verifyStyleVerticalAlignIMG(String actual) {
//        assertEquals("Markup should match: ",
//                "<img border=\"0\" src=\"images/background.jpg\"/>",
//                actual);
//    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 23-Nov-05	10402/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 23-Nov-05	10381/3	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 23-Nov-05	10381/3	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/3	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 19-May-05	8335/1	philws	VBM:2005051705 Port Palm WCA style emulation from 3.3

 19-May-05	8305/1	philws	VBM:2005051705 Provide style emulation rendering for HTML Palm WCA version 1.1

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 02-Nov-04	6068/4	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 01-Nov-04	6064/1	claire	VBM:2004102801 mergevbm: Handling background colour for HTML 3.2 portlets

 01-Nov-04	6014/1	claire	VBM:2004102801 Handling background colour for HTML 3.2 portlets

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 15-Jul-04	4869/1	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 28-Jun-04	4720/5	byron	VBM:2004061604 Core Emulation Facilities - rework issues

 25-Jun-04	4720/3	byron	VBM:2004061604 Core Emulation Facilities

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 26-May-04	4589/1	steve	VBM:2004051102 Output text colour in body tag

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
