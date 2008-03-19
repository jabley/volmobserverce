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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/voicexml/VoiceXMLRootTestAbstract.java,v 1.3 2003/04/30 08:35:40 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Adrian          VBM:2003040903 - Created this class to test
 *                              VoiceXMLRoot
 * 24-Apr-03    Byron           VBM:2003042402 - Modified testDoMenuItem.
 * 25-Apr-03    Allan           VBM:2003041710 - Added tests for
 *                              open/closeSpan.
 * 25-Apr-03    Allan           VBM:2003042302 - Added testGetDOMTransformer().
 *                              Fixed testOpenSpanSrcExpressionAttribute so
 *                              that it provided well-formed xml. Modified
 *                              testDoMenu so that it uses the transformer
 *                              and expects valid VoiceXML.
 *                              that it provided well-formed xml.
 * 23-Apr-03    Adrian          VBM:2003041104 - Added  testDoMenu2 to test the
 *                              doMenu method when there is a Style available.
 *                              Also updated the test protocol to override
 *                              getStyle to return a known style if a flag has
 *                              been set.
 * 25-Apr-03    Allan           VBM:2003042207 - Updated testDoMenu so that the
 *                              transformer can handle the xml it gives it and
 *                              the expected result is correct.
 * 28-Apr-03    Adrian          VBM:2003041104 - Renamed testDoMenu2 to more
 *                              meaningful testDoMenuWithCSSEmulationStyle
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package.
 * 28-Apr-03    Adrian          VBM:2003042807 - Made this class the abstract
 *                              and renamed to VoiceXMLRootTestAbstract
 * 29-Apr-03    Adrian          VBM:2003042807 - Actually made the class
 *                              abstract this time!
 * 29-Apr-03    Byron           VBM:2003042812 - Added testDoImplicitValue and
 *                              getContentBuffer (in inner class).
 * 12-May-03    Phil W-S        VBM:2002111502 - Change sayas class from phone
 *                              to literal to allow handling of MSISDN
 *                              international dialling conventions.
 * 27-May-03    Byron           VBM:2003051904 - Added testDoMenuHorizontal().
 *                              Updated TestVoiceXMLRoot inner class to be able
 *                              to get/set the style.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocolTestAbstract;
import com.volantis.mcs.protocols.DOMProtocolTestable;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.styling.StylesBuilder;
import org.xml.sax.XMLReader;

/**
 * This class tests the protocol VoiceXMLRoot.
 */
public abstract class VoiceXMLRootTestAbstract extends DOMProtocolTestAbstract {

    /**
     * The protocol to use in testing.
     */
    protected VoiceXMLRoot protocol;

    /**
     * The protocol to using in testing stored as a Testable to give us access
     * to the Testable interface methods.
     */
    protected DOMProtocolTestable testable;

    /**
     * Construct a new instance of VoiceXMLRootTestCase
     */
    public VoiceXMLRootTestAbstract(String name) {
        super(name);
    }

    // javadoc inherited from superclass
    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (VoiceXMLRoot) protocol;
        this.testable = (DOMProtocolTestable) testable;
    }

    // Javadoc inherited
    protected void checkRenderAltText(String altText, DOMOutputBuffer buffer) {
        // The default behaviour in the superclass is to enclose alt text in a
        // paragraph tag.  In vxml we enclose it in block and prompt tags so
        // we override this method to perform that check.
        Element root = buffer.getCurrentElement();
        Element block = checkElementEquals("block", root.getHead());

        Element prompt = checkElementEquals("prompt", block.getHead());

        checkTextEquals(altText, prompt.getHead());
    }


    /**
     * There is no menu element in VoiceXML.
     */
    // rest of javadoc inherited
    protected String getMenuElementName() {
        return "";
    }

    /**
     * Negative test for createElementFromString - should throw an exception
     */
    public void testCreateElementFromStringBadXML() {
        String xml = "not well formed xml";
        VoiceXMLRoot protocol = (VoiceXMLRoot) createTestableProtocol(
                internalDevice);
        try {
            protocol.createElementFromString(xml);
            fail("Expected a ProtocolException.");
        }
        catch (ProtocolException e) {
            // If we are here then the test has passed.
        }
    }

    /**
     * Positive test for createElementFromString.
     */
    public void testCreateElementFromStringGoodXML() throws Exception {
        String xml = "<prompt>well formed xml</prompt>";
        VoiceXMLRoot protocol = (VoiceXMLRoot) createTestableProtocol(
                internalDevice);
        Element element = protocol.createElementFromString(xml);
        assertEquals("prompt", element.getName());
        Text text = (Text) element.getHead();
        String contents = new String(text.getContents(), 0,
                                     text.getLength());
        assertEquals("well formed xml", contents);
    }

    /**
     * Test that the openSpan method behaves correctly when there is
     * a src attribute with a value it can use.
     */
    public void testOpenSpanSrcExpressionAttribute() throws Exception {
        final String text = "<xml>{an expression}</xml>";
        TestMarinerPageContext context = new TestMarinerPageContext() {
            public String getTextFromObject(Object object, int encoding) {
                return text;
            }
        };

        protocol.setMarinerPageContext(context);

        SpanAttributes attributes = new SpanAttributes();
        attributes.setSrc(new LiteralTextAssetReference(text));

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        protocol.openSpan(buffer, attributes);
        protocol.closeSpan(buffer, attributes);

        String expectedString =
                "<block><prompt><xml>{an expression}</xml></prompt></block>";
        XMLReader reader = DOMUtilities.getReader();

        Document expectedDom = DOMUtilities.read(reader, expectedString);
        expectedString = DOMUtilities.toString(
                expectedDom, protocol.getCharacterEncoder());
        String actualString = DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder());

        assertEquals(expectedString, actualString);

        assertTrue("skipElementBody should be true",
                   protocol.skipElementBody());
        assertTrue("skipElementBody should have been reset to false by" +
                   " previous call to skipElementBody()",
                   !protocol.skipElementBody());
    }


    /**
     * Test that the openSpan method behaves correctly when there is
     * a src attribute with a value it can use that evaluates to some
     * VoiceXML that is a prompt tag.
     */
    public void testOpenSpanSrcNestedPrompt() throws Exception {
        final String text = "<prompt>a voicexml prompt</prompt>";
        TestMarinerPageContext context = new TestMarinerPageContext() {
            public String getTextFromObject(Object object, int encoding) {
                return text;
            }
        };

        protocol.setMarinerPageContext(context);

        SpanAttributes attributes = new SpanAttributes();
        attributes.setSrc(new LiteralTextAssetReference(text));

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        protocol.openSpan(buffer, attributes);
        protocol.closeSpan(buffer, attributes);

        Document document = domFactory.createDocument();
        document.addNode(buffer.getRoot());

        document = protocol.getDOMTransformer().transform(protocol, document);

        String expectedString =
                "<block><prompt>a voicexml prompt</prompt></block>";
        XMLReader reader = DOMUtilities.getReader();

        Document expectedDom = DOMUtilities.read(reader, expectedString);
        expectedString = DOMUtilities.toString(
                expectedDom, protocol.getCharacterEncoder());
        String actualString = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());

        assertEquals(expectedString, actualString);
    }

    /**
     * Test that the openSpan method behaves correctly when there is no
     * src attribute with a value it can use.
     */
    public void testOpenCloseSpanNoSrcAttribute() throws Exception {

        SpanAttributes attributes = new SpanAttributes();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        protocol.openSpan(buffer, attributes);
        protocol.closeSpan(buffer, attributes);

        String expectedString =
                "<block><prompt></prompt></block>";
        XMLReader reader = DOMUtilities.getReader();

        Document expectedDom = DOMUtilities.read(reader, expectedString);
        expectedString = DOMUtilities.toString(
                expectedDom, protocol.getCharacterEncoder());
        String actualString = DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder());

        assertEquals(expectedString, actualString);

        assertTrue("skipElementBody should be false",
                   !protocol.skipElementBody());
        assertTrue("skipElementBody should have been reset to false by" +
                   " previous call to skipElementBody()",
                   !protocol.skipElementBody());
    }

    /**
     * Test that the doMenu method creates the correct vxml markup
     */
    public void testDoMenuWithCSSEmulationStyle() throws Exception {
        final DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        testable.setCurrentBuffer(null, buffer);

        TestMarinerPageContext context = new TestMarinerPageContext();

        PaneInstance paneInstance = new TestPaneInstance() {
            public OutputBuffer getCurrentBuffer() {
                return buffer;
            }
        };
        context.setFormatInstance(paneInstance);

        TestDeviceLayoutContext layoutContext = new TestDeviceLayoutContext();
        context.pushDeviceLayoutContext(layoutContext);

        protocol.setMarinerPageContext(context);

        MenuAttributes menuAttrs = new MenuAttributes();
        menuAttrs.setStyles(StylesBuilder.getCompleteStyles(
                "mcs-aural-dtmf-allocation: automatic; " +
                "mcs-aural-menu-scope: document"));

        // We use Strings for errmsg, prompt, and help to avoid having to
        // resolve TextComponentIdentity which would require a repository
        menuAttrs.setErrmsg(new LiteralTextAssetReference(
                "<noinput>Sorry I did not hear you</noinput>" +
                            "<nomatch>Sorry I did not understand you</nomatch>"));
        menuAttrs.setHelp(new LiteralTextAssetReference("<help>Please select an option from the menu</help>"));
        menuAttrs.setPrompt(new LiteralTextAssetReference("<prompt>Welcome home.<enumerate>For <value " +
                            "expr=\"_prompt\"/>, press <value expr=\"_dtmf\"/> or say " +
                            "<value expr=\"_prompt\"/></enumerate></prompt>"));

        MenuItem item1 = new MenuItem();
        item1.setText("Sports News");
        item1.setHref("http://www.volantis.com:8080/volantis/sports.jsp");
        menuAttrs.addItem(item1);

        MenuItem item2 = new MenuItem();
        item2.setText("Astrology");
        item2.setHref("http://www.volantis.com:8080/volantis/astrology.jsp");
        item2.setPrompt(new LiteralTextAssetReference("<prompt><audio src=\"rtsp://www.volantis.com/" +
                        "mysticmeg.wav\">Mystic Megs Astrology</audio></prompt>"));
        menuAttrs.addItem(item2);

        MenuItem item3 = new MenuItem();
        item3.setText("Fun and Games");
        item3.setHref("http://www.volantis.com:8080/volantis/games.jsp");
        item3.setShortcut(new LiteralTextAssetReference("9"));
        menuAttrs.addItem(item3);

        protocol.doMenu(menuAttrs);

        String actual = DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder());

        String expected =
                "<menu dtmf=\"true\" scope=\"document\">" +
                "<prompt>Welcome home.<enumerate>" +
                "For <value expr=\"_prompt\"/>, press " +
                "<value expr=\"_dtmf\"/> or say " +
                "<value expr=\"_prompt\"/></enumerate></prompt>" +
                "<choice next=\"http://www.volantis.com:8080/volantis" +
                "/sports.jsp\">Sports News</choice>" +
                "<choice next=\"http://www.volantis.com:8080/volantis" +
                "/astrology.jsp\"><prompt><audio src=\"rtsp://www.volantis" +
                ".com/mysticmeg.wav\">Mystic Megs Astrology</audio>" +
                "</prompt></choice>" +
                "<choice next=\"http://www.volantis.com:8080/" +
                "volantis/games.jsp\">Fun and Games</choice>" +
                "<help>Please select an option from the menu</help>" +
                "<noinput>Sorry I did not hear you</noinput>" +
                "<nomatch>Sorry I did not understand you</nomatch>" +
                "</menu>";

        XMLReader reader = DOMUtilities.getReader();

        Document expectedDom = DOMUtilities.read(reader, expected);
        String expectedDOMAsString =
                DOMUtilities.toString(
                        expectedDom, protocol.getCharacterEncoder());

        assertEquals("the processed DOM is not as expected.",
                     expectedDOMAsString, actual);
    }

    /**
     * Get a new DOMOutputBuffer and set it as the current output buffer on the
     * protocol field.
     * @return The new DOMOutputBuffer.
     */
    private DOMOutputBuffer getNewDOMOutputBuffer() {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        testable.setCurrentBuffer(null, buffer);
        return buffer;
    }

    /**
     * Test the doMenuItem method to ensure that menu items are being correctly
     * processed.
     */
    public void testDoMenuItem() throws Exception {
        DOMOutputBuffer buffer = getNewDOMOutputBuffer();

        TestMarinerPageContext context = new TestMarinerPageContext();
        protocol.setMarinerPageContext(context);

        XMLReader reader = DOMUtilities.getReader();

        MenuItem item1 = new MenuItem();
        item1.setText("Website Index.");
        item1.setHref("http://www.volantis.com/index.jsp");
        item1.setShortcut(new LiteralTextAssetReference("9"));
        MenuAttributes attributes = new MenuAttributes();
        attributes.setManualDTMF(true);
        protocol.doMenuItem(buffer, attributes, item1);
        String actual = DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder());

        String expected = "<choice next=\"http://www.volantis.com/index." +
                "jsp\" dtmf=\"9\">Website Index.</choice>";
        Document expectedDom = DOMUtilities.read(reader, expected);
        String expectedDOMAsString =
                DOMUtilities.toString(
                        expectedDom, protocol.getCharacterEncoder());

        assertEquals("the processed DOM is not as expected.",
                     expectedDOMAsString, actual);


        // Same again but with manualDTMF set to false.
        buffer = getNewDOMOutputBuffer();
        expected = "<choice next=\"http://www.volantis.com/index.jsp\">" +
                "Website Index.</choice>";
        expectedDom = DOMUtilities.read(reader, expected);
        expectedDOMAsString = DOMUtilities.toString(
                expectedDom, protocol.getCharacterEncoder());

        attributes.setManualDTMF(false);
        protocol.doMenuItem(buffer, attributes, item1);
        actual = DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder());

        assertEquals("the processed DOM is not as expected.",
                     expectedDOMAsString, actual);


        // With a prompt in the source this time.  Demonstrates that the text
        // attribute is overriden by the prompt attribute.
        buffer = getNewDOMOutputBuffer();
        MenuItem item2 = new MenuItem();
        item2.setText("Website Index.");
        item2.setHref("http://www.volantis.com/index.jsp");
        item2.setPrompt(new LiteralTextAssetReference("<prompt><audio src=\"rtsp://www.volantis" +
                        ".com/mysticmeg.wav\">Mystic Megs Astrology</audio>" +
                        "</prompt>"));

        expected = "<choice next=\"http://www.volantis.com/index.jsp\">" +
                "<prompt><audio src=\"rtsp://www.volantis.com/mysticmeg" +
                ".wav\">Mystic Megs Astrology</audio></prompt></choice>";
        expectedDom = DOMUtilities.read(reader, expected);
        expectedDOMAsString = DOMUtilities.toString(
                expectedDom, protocol.getCharacterEncoder());

        attributes.setManualDTMF(true);
        protocol.doMenuItem(buffer, attributes, item2);
        actual = DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder());

        assertEquals("the processed DOM is not as expected.",
                     expectedDOMAsString, actual);

        // Null Href - there should be no output from the doMenuItem method.
        buffer = getNewDOMOutputBuffer();
        MenuItem item3 = new MenuItem();
        item3.setText("Some arbitrary text.");
        item3.setShortcut(new LiteralTextAssetReference("5"));

        expectedDOMAsString = "";

        attributes.setManualDTMF(true);
        protocol.doMenuItem(buffer, attributes, item3);
        actual = DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder());

        assertEquals("the processed DOM is not as expected.",
                     expectedDOMAsString, actual);
    }

    /**
     * Test the protocols doImplicitValue
     */
    public void testDoImplicitValue() throws Exception {
        DOMOutputBuffer buffer = getNewDOMOutputBuffer();

        TestMarinerPageContext context = new TestMarinerPageContext();
        protocol.setMarinerPageContext(context);

        XMLReader reader = DOMUtilities.getReader();

        XFImplicitAttributes attributes = new XFImplicitAttributes();
        final String xfName = "xfImplicitAtributesName";
        final String xfValue = "xfImplicitAtributesValue";
        attributes.setName(xfName);
        attributes.setValue(xfValue);

        XFFormAttributes formAttributes = new XFFormAttributes();
        String name = "This is the name";
        formAttributes.setName(name);
        Form form = new Form(new CanvasLayout());
        FormInstance formInstance = new FormInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(form);

        formAttributes.setFormData(formInstance);
        attributes.setFormAttributes(formAttributes);
        attributes.setFormData(formInstance);

        protocol.doImplicitValue(attributes);

        String actual = DOMUtilities.toString(
                buffer.getRoot(), protocol.getCharacterEncoder());

        String expected = "<var expr=\"'" + xfValue +
            "'\" name=\"" + xfName + "\"/>";
        Document expectedDom = DOMUtilities.read(reader, expected);
        String expectedDOMAsString =
                DOMUtilities.toString(
                        expectedDom, protocol.getCharacterEncoder());

        assertEquals("the processed DOM is not as expected.",
                expectedDOMAsString, actual);
    }

    public void testAddPhoneNumberContentsNull() throws Exception {
        doPhoneNumberContentTest(null,
                                 "<block><prompt>" +
                                 "<sayas class=\"literal\">" + phoneNumber +
                                 "</sayas></prompt></block>",
                                 "null");
    }

    public void testAddPhoneNumberContentsString() throws Exception {
        doPhoneNumberContentTest("example with text",
                                 "<block>" +
                                 "<prompt>example with text" +
                                 "<sayas class=\"literal\">" + phoneNumber +
                                 "</sayas></prompt></block>",
                                 "string");
    }

    public void testAddPhoneNumberContentsPopulatedDom() throws Exception {
        DOMOutputBuffer contentDom = new DOMOutputBuffer();
        contentDom.initialise();
        contentDom.openElement("content");
        contentDom.addElement("example");
        contentDom.appendEncoded("with text");
        contentDom.closeElement("content");

        doPhoneNumberContentTest(contentDom,
                                 "<block><prompt><content>" +
                                 "<example/>with text</content>" +
                                 "<sayas class=\"literal\">" + phoneNumber +
                                 "</sayas></prompt></block>",
                                 "DOM");
    }

    public void testAddPhoneNumberContentsEmptyDom() throws Exception {
        DOMOutputBuffer contentDom = new DOMOutputBuffer();
        contentDom.initialise();

        doPhoneNumberContentTest(contentDom,
                                 "<block><prompt>" +
                                 "<sayas class=\"literal\">" + phoneNumber +
                                 "</sayas></prompt></block>",
                                 "empty DOM");
    }

    protected String expectedQualifiedFullNumber(String prefix,
                                                 String fullNumber) {
        String noPrefixFullNumber = fullNumber;

        if (noPrefixFullNumber.charAt(0) == '+') {
            noPrefixFullNumber = noPrefixFullNumber.substring(1);
        }

        return super.expectedQualifiedFullNumber(prefix, noPrefixFullNumber);
    }

}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 01-Sep-05	9375/5	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/4	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/6	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/3	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/4	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9324/2	ianw	VBM:2005080202 Move validation for WapCSS into styling

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 22-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 22-Jul-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 17-Aug-03	1052/2	allan	VBM:2003073101 Support styles on menu and menuitems

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 ===========================================================================
*/
