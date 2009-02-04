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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/dissection/DissectableAnnotationTestCase.java,v 1.1.2.2 2003/04/16 08:46:27 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-May-03    Steve           VBM:2003041606 - Created to test OutputBufferWriter
 *                              and DOMOutputBuffer 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import junit.framework.TestCase;

/**
 * Class to test the DissectableAnnotation
 */
public class OutputBufferWriterTestCase extends TestCase {

    private InternalDevice internalDevice;


    protected void setUp() throws Exception {
        super.setUp();

        internalDevice = InternalDeviceTestHelper.createTestDevice();


    }

    public void testIgnorableWhitespace() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        TestDOMOutputBuffer domOut = new TestDOMOutputBuffer();
        
        OutputBufferWriter writer = new OutputBufferWriter( domOut );
        
        // Generate
        // <ul>
        //     <li>First Element</li>
        //     <li>Second Element</li>
        // </ul>
        domOut.openElement( "ul" );
        writer.setElementHasMixedContent( false );
        writer.setElementIsPreFormatted( false );
        writer.setElementIsBlock( false );
        writer.write( "\n    " );
        domOut.openElement( "li" );
        writer.write( "First Element");
        domOut.closeElement( "li" );
        writer.write( "\n    " );
        domOut.openElement( "li" );
        writer.write( "Second Element");
        domOut.closeElement( "li" );
        writer.write( "\n" );
        domOut.closeElement( "ul" );
        
        String str = DOMUtilities.toString(
                domOut.getRoot(), protocol.getCharacterEncoder() );
        assertEquals( "<ul><li>First Element</li><li>Second Element</li></ul>", str );
    
    }

    public void testStartAndEndTags() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        TestDOMOutputBuffer domOut = new TestDOMOutputBuffer();
        
        OutputBufferWriter writer = new OutputBufferWriter( domOut );
        
        // Generate
        // <p>We offer free <b>technical support</b> for subscribers</p>
        // <p>We offer<b> No Support At All </b>to non-subscribers</p>
        domOut.openElement( "p" );
        writer.setElementHasMixedContent( true );
        writer.setElementIsPreFormatted( false );
        writer.setElementIsBlock( false );
        writer.write( "We offer free " );
        domOut.openElement( "b" );
        writer.write( "technical support" );
        domOut.closeElement( "b" );
        writer.write( " for subscribers" );
        domOut.closeElement( "p" );
        domOut.openElement( "p" );
        writer.write( "We offer" );
        domOut.openElement( "b" );
        writer.write( " No Support At All " );
        domOut.closeElement( "b" );
        writer.write( "to non-subscribers" );
        domOut.closeElement( "p" );
        
        
        String str = DOMUtilities.toString(
                domOut.getRoot(), protocol.getCharacterEncoder() );
        assertEquals( "<p>We offer free <b>technical support</b> for subscribers</p>" +
                      "<p>We offer<b>No Support At All</b>to non-subscribers</p>", str );
    
    }
    
    public void testBlockElements() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        TestDOMOutputBuffer domOut = new TestDOMOutputBuffer();
        
        OutputBufferWriter writer = new OutputBufferWriter( domOut );
        
        // Generate
        // <pane>Some text.
        //       <p>More text.</p>
        // </pane>
        domOut.openElement( "pane" );
        writer.setElementIsPreFormatted( false );
        writer.setElementHasMixedContent( true );
        writer.setElementIsBlock( false );
        writer.write( "Some text.");
        writer.write( "\n    " );
        domOut.openElement( "p" );
        writer.setElementIsPreFormatted( false );
        writer.setElementHasMixedContent( true );
        writer.setElementIsBlock( true );
        writer.write( "More text.");
        domOut.closeElement( "p" );
        writer.setElementIsBlock( false );
        writer.write( "\n" );
        domOut.closeElement( "pane" );
        
        
        String str = DOMUtilities.toString(
                domOut.getRoot(), protocol.getCharacterEncoder() );
        assertEquals( "<pane>Some text. <p>More text.</p></pane>", str );        
    }

    public void testCollapseWhitespace() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        TestDOMOutputBuffer domOut = new TestDOMOutputBuffer();
        
        OutputBufferWriter writer = new OutputBufferWriter( domOut );
        
        // Generate
        // <p>Extra     spaces    have    no   visible   effect</p>
        domOut.openElement( "p" );
        writer.setElementIsPreFormatted( false );
        writer.setElementHasMixedContent( true );
        writer.setElementIsBlock( true );
        writer.write( "Extra     spaces    have    no   visible   effect" );
        domOut.closeElement( "p" );
                
        
        String str = DOMUtilities.toString(
                domOut.getRoot(), protocol.getCharacterEncoder() );
        assertEquals( "<p>Extra spaces have no visible effect</p>", str );        
    }
    
    /**
     * Tests that whitespace processing works correctly with some "simple"
     * content for a mixed content type element.
     */
    public void testMixedContentWhitespace() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        TestDOMOutputBuffer dom = new TestDOMOutputBuffer();

        OutputBufferWriter writer = new OutputBufferWriter(dom);

        // Generate the following markup (where . is a space where clarity is needed!):
        //
        // <p>.<b>Simple test</b>..of..<em>mixed content</em>..whitespaces.</p>
        dom.openElement("p");
        writer.setElementHasMixedContent(true);
        writer.setElementIsPreFormatted(false);
        // A leading space in the <p> is ignored.
        writer.write(" ");
        dom.openElement("b");
        // The leading and trailing spaces in <b> are ignored.
        writer.write("Simple test");
        dom.closeElement("b");
        // The spaces around the "of" will be collapsed and output since they
        // are not leading or trailing spaces for <p>.
        writer.write("  of  ");
        dom.openElement("em");
        writer.write("mixed content");
        dom.closeElement("em");
        // The spaces before "whitespaces" will be collapsed and output, since
        // <p> has mixed content and the spaces aren't leading or trailing.
        // The space after "whitespaces" is trailing for <p> so will be ignored.
        writer.write("  whitespaces ");
        dom.closeElement("p");

        String doc = DOMUtilities.toString(
                dom.getRoot(), protocol.getCharacterEncoder());
        assertEquals("<p><b>Simple test</b> of <em>mixed content</em>" +
                " whitespaces</p>", doc);
    }

    /**
     * Tests that whitespace processing works correctly with the content of a
     * mixed content type element where the mixed content element has mixed
     * content children.
     */
    public void testNestedMixedContentWhitespace() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        TestDOMOutputBuffer dom = new TestDOMOutputBuffer();

        OutputBufferWriter writer = new OutputBufferWriter(dom);

        // Generate the following markup (where . is a space where clarity is needed!):
        // <dl>
        //     <dt>...q...<span><b>..mySpan..</b>..<em>..is..fab..</em>..so there..</span>...<p>..myPara..</p>...w....;z.....</dt>
        // </dl>
        dom.openElement("dl");
        writer.setElementHasMixedContent(false);
        writer.setElementIsPreFormatted(false);
        dom.openElement("dt");
        writer.setElementHasMixedContent(true);
        // The spaces before the "q" are leading for the <dt> so will be
        // ignored.  The spaces after the "q" are collapsed and output since
        // they are not trailing (or leading) and <dt> has mixed content.
        writer.write("  q   ");
        dom.openElement("span");
        dom.openElement("b");
        // The spaces before/after the "mySpan" are leading/trailing for the
        // <b> so are ignored.
        writer.write("  mySpan  ");
        dom.closeElement("b");
        // The spaces between the </b> and <em> are collapse ands output
        // because they are not leading or trailing for the <span>.
        writer.write("  ");
        dom.openElement("em");
        // The spaces around "is fab" are leading and trailing for the <em>
        // so are ignored. The spaces between the words are collapsed before
        // output.
        writer.write("   is  fab   ");
        dom.closeElement("em");
        // The spaces before "so there" are collapsed and output since they
        // are neither leading nor trailing for the <span>. The spaces
        // afterwards are ignored since they are trailing for the <span>.
        writer.write("  so there  ");
        dom.closeElement("span");
        // These spaces are collapsed and output since they are neither leading
        // nor trailing for the <dt> and <dt> has mixed content.
        writer.write("   ");
        dom.openElement("p");
        // The spaces around "myPara" are leading and trailing for the <p> so
        // are ignored.
        writer.write("  myPara  ");
        dom.closeElement("p");
        // The spaces before the "w" are collapsed and output since they are
        // neither leading nor trailing for the mixed-content <dt>. The spaces
        // between the "w" and "z" are collapsed and output. The spaces after
        // the "z" are ignored since they are trailing for the <dt>.
        writer.write("   w   z     ");
        dom.closeElement("dt");
        dom.closeElement("dl");

        String doc = DOMUtilities.toString(
                dom.getRoot(), protocol.getCharacterEncoder());
        assertEquals("<dl><dt>q <span><b>mySpan</b> <em>is fab</em>" +
                " so there</span> <p>myPara</p> w z</dt></dl>", doc);
    }

    /**
     * Tests that whitespace processing works correctly when there is some
     * trailing whitespace pending but no sibling element at the same
     * nesting level.
     */
    public void testTrailingWhitespacePendingWithNoSibling() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        TestDOMOutputBuffer dom = new TestDOMOutputBuffer();

        OutputBufferWriter writer = new OutputBufferWriter(dom);

        // Generate the following markup where the only space used is the one
        // in <td>b </td>
        //
        // <table>
        //   <tr>
        //     <td>
        //       <table>
        //         <tr>
        //          <td>b </td>
        //        </tr>
        //      </table>
        //    </td>
        //    <td>q</td>
        //   </tr>
        // </table>

        dom.openElement("table");
        writer.setElementHasMixedContent(false);
        writer.setElementIsPreFormatted(false);
        dom.openElement("tr");
        dom.openElement("td");
        writer.setElementHasMixedContent(true);
        dom.openElement("table");
        writer.setElementHasMixedContent(false);
        dom.openElement("tr");
        dom.openElement("td");
        writer.setElementHasMixedContent(true);
        // The trailing whitespace here should not be output. It will be
        // marked as pending but should be ignored as we climb back up the
        // tree.
        writer.write("b ");
        // Closing the element should reset the whitespace processor so no
        // whitespace is pending.
        dom.closeElement("td");
        dom.closeElement("tr");
        dom.closeElement("table");
        dom.closeElement("td");
        // This is the next <td> to be opened. By now there should be no
        // pending whitespace.
        dom.openElement("td");
        writer.setElementHasMixedContent(true);
        writer.write("q");
        dom.closeElement("td");
        writer.setElementHasMixedContent(false);
        dom.closeElement("tr");
        dom.closeElement("table");

        String doc = DOMUtilities.toString(
                dom.getRoot(), protocol.getCharacterEncoder());
        assertEquals("<table><tr><td><table><tr><td>b</td></tr></table></td><td>q</td></tr></table>", doc);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Oct-04	5924/1	pcameron	VBM:2004101914 Fixed whitespace processing when popping elements

 04-Oct-04	5719/1	pcameron	VBM:2004092213 Fixed whitespace handling for mixed content types

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
