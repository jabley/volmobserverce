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
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.StylesBuilder;

import java.io.IOException;

/**
 * This class tests the XHTMLMobile1_0Openwave6 protocol
 */
public class XHTMLMobile1_0Openwave6TestCase extends XHTMLMobile1_0TestCase {

    private XHTMLMobile1_0 protocol;

    /**
     * Create a new instance of XHTMLMobile1_0
     * @param name
     */
    public XHTMLMobile1_0Openwave6TestCase(String name) {
        super(name);
    }

    // javadoc inhertied from superclass
    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLMobile1_0Openwave6Factory(),
                internalDevice);
        return protocol;
    }

    // javadoc inhertied from superclass
    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (XHTMLMobile1_0) protocol;
    }

    /**
     * Test for doProtocolString
     */
    public void testDoProtocolString() throws IOException {
        String expected = "<!DOCTYPE html PUBLIC " +
                "\"-//OPENWAVE//DTD XHTML Mobile 1.0//EN\" " +
                "\"http://www.openwave.com/dtd/xhtml-mobile10.dtd\">";
        checkDoProtocolString(protocol, expected);
    }

    /**
     * Test the method doImage
     */
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

        protocol.doImage(buffer, attrs);

        String expected = "<img alt=\"my_alt_text\" height=\"20\" " +
                "localsrc=\"http://www.volantis.com/my_image.jpg\" " +
                "src=\"\" width=\"10\"/>";

        assertEquals("Unexpected img markup generated.", expected,
                bufferToString(buffer));
    }


    /**
     * Test the addition of the mode="nowrap" attribute with styles.
     */
    public void testModeAttributeWithStyles() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        TestMarinerPageContext testContext = new TestMarinerPageContext();
        testContext.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, testContext);
        protocol.setMarinerPageContext(testContext);

        ParagraphAttributes paraAttrs = new ParagraphAttributes();
        paraAttrs.setStyles(StylesBuilder.getCompleteStyles(
                "white-space: nowrap"));

        // Create a style with a nowrap whitespace. This should mean that the
        // protocol creates a paragraph with mode="nowrap".

        protocol.openParagraph(buffer, paraAttrs);
        Element element = buffer.getCurrentElement();
        String modeValue = element.getAttributeValue("mode");
        // There should be mode="nowrap" attribute.
        assertEquals("Unexpected mode value on p", "nowrap", modeValue);

        // Create a style with a pre whitespace. This should mean that the
        // protocol creates a paragraph with no mode attribute.
        paraAttrs.setStyles(StylesBuilder.getCompleteStyles(
                "white-space: pre"));

        protocol.openParagraph(buffer, paraAttrs);
        element = buffer.getCurrentElement();
        modeValue = element.getAttributeValue("mode");
        
        // There should be no mode attribute.
        assertEquals("Unexpected mode value on p", null, modeValue);
    }

    /**
     * Tests that the mode attribute is not added when there is no style.
     */
    public void testModeAttributeWithNoStyle() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        TestMarinerPageContext testContext = new TestMarinerPageContext();
        testContext.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, testContext);
        protocol.setMarinerPageContext(testContext);

        // The default paragraph attributes has no class attribute.
        ParagraphAttributes attributes = new ParagraphAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        protocol.openParagraph(buffer, attributes);
        Element element = buffer.getCurrentElement();
        String modeValue = element.getAttributeValue("mode");
        // There should be no mode attribute because there is no style.
        assertNull("There should be no mode attribute", modeValue);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 12-Jul-05	9015/1	amoore	VBM:2005052307 Inserted single whitespace between public Id and DTD location in Doctype to ensure correct format is sent to device

 12-Jul-05	9019/1	amoore	VBM:2005052307 Inserted single whitespace between public Id and DTD location in Doctype to ensure correct format is sent to device

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 03-Sep-04	4998/12	pcameron	VBM:2004072805 Check that style isn't null when adding mode attribute

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 29-Jul-04	4998/1	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements
 03-Sep-04	5054/7	pcameron	VBM:2004072805 Check that style isn't null when adding mode attribute

 02-Aug-04	5054/1	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 29-Jul-04	4980/1	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 02-Aug-04	5054/1	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements
 03-Sep-04	4995/3	pcameron	VBM:2004072805 Check that style isn't null when adding mode attribute

 29-Jul-04	4995/1	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 29-Jul-04	4980/1	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 29-Jul-04	4980/1	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 03-Nov-03	1760/1	philws	VBM:2003031710 Port image alt text component reference handling from PROTEUS

 02-Sep-03	1305/1	adrian	VBM:2003082108 added new openwave6 xhtml protocol

 ===========================================================================
*/
