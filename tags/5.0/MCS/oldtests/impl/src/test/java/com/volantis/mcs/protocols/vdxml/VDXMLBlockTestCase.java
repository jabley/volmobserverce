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
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DebugCharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleFactory;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.CharArrayWriter;
import java.io.IOException;

/**
 * A test case for VDXML block elements.
 * <p>
 * This tests those things which are specific to block elements - i.e. the 
 * basic style property rendering related to block elements is currently 
 * assumed to be tested by the inline test case, given that the style handling 
 * is identical for block and inline.
 * 
 * @todo implement and test copying of styles during promotion.
 */ 
public class VDXMLBlockTestCase extends TestCaseAbstract {

    VDXMLStyleFactory vdStyleFactory;
    CharArrayWriter writer;
    VDXMLDocumentWriter docWriter;
    DocumentOutputter outputter;
    TestDOMOutputBuffer buffer;
    Styles empty;
    VDXMLBlockTransformer transformer;

    ProtocolBuilder builder;
    DOMProtocol protocol;


    protected void setUp() throws Exception {
        super.setUp();

    }

    public VDXMLBlockTestCase() {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        buffer = new TestDOMOutputBuffer();
        buffer.clear();
        vdStyleFactory = new VDXMLStyleFactory();
        writer = new CharArrayWriter();
        docWriter = new VDXMLDocumentWriter(new XMLDocumentWriter(writer));
        outputter = new DOMDocumentOutputter(
                docWriter, new DebugCharacterEncoder());
        empty = StylesBuilder.getEmptyStyles();
        transformer = new VDXMLBlockTransformer();
        builder = new ProtocolBuilder();
        protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
    }

    /**
     * Test the rendering of the block example from the architecture paper,
     * in AN061 3.1.3.
     */ 
    public void testArchitectureExample() throws Exception {

        openTexte(empty);
            open(empty);
                open(empty);
                    text("Some text in paragraph 1.");
                close();
                open(empty);
                    text("Some text before paragraph 2.");
                    open(empty);
                        text("Some text in paragraph 2.");
                    close();
                    text("Some text after paragraph 2.");
                close();
                open(empty);
                    text("Some text in paragraph 3.");
                close();
            close();
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "Some text in paragraph 1." +
            "<BR />" + 
            "Some text before paragraph 2." +
            "<BR />" + 
            "Some text in paragraph 2." +
            "<BR />" + 
            "Some text after paragraph 2." +
            "<BR />" + 
            "Some text in paragraph 3." +
        "</TEXTE>";

        String actual = render();

        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Open a block element.
     *
     * todo Use MCSAttributesMock rather than StyleAttributes when mocks can be used from integration tests
     *
     * @param styles the styles to use for the element.
     */ 
    private void open(Styles styles) {

        // Create an instance of MCSAttributes - the type doesn't matter
        MCSAttributes attributes = new StyleAttributes();
        attributes.setStyles(styles);
        buffer.openStyledElement(
                VDXMLConstants.PSEUDO_BLOCK_ELEMENT, attributes);
    }

    /**
     * Add some text to the currently open element.
     * 
     * @param text
     */ 
    private void text(String text) {
        
        buffer.writeText(text);
    }

    /**
     * Close an inline element.
     */ 
    private void close() {
        
        buffer.closeElement(VDXMLConstants.PSEUDO_BLOCK_ELEMENT);
    }

    /**
     * Open a TEXTE element (display context).
     * <p/>
     * todo Use MCSAttributesMock rather than StyleAttributes when mocks can be used from integration tests
     *
     * @param begin the styles to use for the element.
     */ 
    private void openTexte(Styles begin) {

        // Create an instance of MCSAttributes - the type doesn't matter
        MCSAttributes attributes = new StyleAttributes();
        attributes.setStyles(begin);
        buffer.openStyledElement(VDXMLConstants.TEXT_BLOCK_ELEMENT, attributes);
    }

    /**
     * Close a TEXTE element (display context).
     */ 
    private void closeTexte() {
        
        buffer.closeElement(VDXMLConstants.TEXT_BLOCK_ELEMENT);
    }

    /**
     * Render the DOM created and return it as a string. 
     * 
     * @return the rendered DOM.
     * @throws IOException
     */ 
    private String render() throws IOException {
        
        // NOTE: This is only rendering the first TEXTE at present...
        Element texte = (Element) buffer.getCurrentElement().getHead();
        //System.out.println("current: " + element);
        //System.out.println("before: " + DOMUtilities.toString(element, protocol.getCharacterEncoder()));
        transformer.transform(protocol.getDOMFactory(), texte);
        //System.out.println("after : " + DOMUtilities.toString(element, protocol.getCharacterEncoder()));
        outputter.output(texte);
        String actual = writer.toString();
        return actual;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 25-Nov-05	10453/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 30-Jun-05	8893/2	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 02-Jun-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 28-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support (add block test case)

 28-May-04	4575/6	geoff	VBM:2004051807 Minitel VDXML protocol support (fix underline)

 28-May-04	4575/4	geoff	VBM:2004051807 Minitel VDXML protocol support (incomplete inline integration)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
