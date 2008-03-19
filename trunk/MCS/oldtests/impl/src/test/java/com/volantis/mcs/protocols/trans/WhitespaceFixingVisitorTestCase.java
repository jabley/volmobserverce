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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.TestDOMProtocol;
import com.volantis.mcs.protocols.TransformingVisitor;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.XMLReader;

import java.util.HashSet;
import java.util.Set;

/**
 * Test case for the {@link WhitespaceFixingVisitor}
 */
public class WhitespaceFixingVisitorTestCase extends TestCaseAbstract {


    /**
     * Creates an instance of the class that is being tested
     *
     * @return an instance of the class that is being tested.
     */
    private TransformingVisitor createVisitor() {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder builder = new ProtocolBuilder();
        TestDOMProtocol protocol = (TestDOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.setDevicePolicyValue(DevicePolicyConstants.FIX_FOR_OPEN_INLINE_STYLING_ELEMENTS,
                                     DevicePolicyConstants.WHITESPACE_INSIDE);
        context.setDevicePolicyValue(DevicePolicyConstants.FIX_FOR_CLOSING_INLINE_STYLING_ELEMENTS,
                                     DevicePolicyConstants.WHITESPACE_INSIDE);
        context.setDevicePolicyValue(DevicePolicyConstants.FIX_FOR_OPEN_ANCHOR_ELEMENT,
                                     DevicePolicyConstants.WHITESPACE_INSIDE_AND_OUTSIDE);
        context.setDevicePolicyValue(DevicePolicyConstants.FIX_FOR_CLOSING_ANCHOR_ELEMENT,
                                     DevicePolicyConstants.NON_BREAKING_SPACE_OUTSIDE);

        protocol.setMarinerPageContext(context);
        Set inlineStyleElements = new HashSet();
        inlineStyleElements.add("strong");
        inlineStyleElements.add("i");
        Set inlineLinkElements = new HashSet();
        inlineLinkElements.add("a");
        return new WhitespaceFixingVisitor(
                inlineStyleElements, inlineLinkElements, protocol);
    }

    /**
     * Test the recursive nature of pushing a matching parent down.
     */
    public void testInlineStyleElementFixUp() throws Exception {
        TransformingVisitor visitor = createVisitor();
        String input =
                "<b>textA <strong>txt-2</strong> textE <strong>txt-5</strong> a <strong>txt-5</strong> </b>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);
        visitor.transform(dom);

        String actual = DOMUtilities.toString(dom);

        String expected =
                "<b>textA<strong> txt-2 </strong>textE<strong> txt-5 </strong>a<strong> txt-5 </strong></b>";

        verifyDOMMatches(null, expected, actual);
    }

    /**
     * Test the recursive nature of pushing a matching parent down.
     */
    public void testNestedInlineStyleElementFixUp() throws Exception {
        TransformingVisitor visitor = createVisitor();
        String input =
                "<b>textA <strong>txt-2 <strong>txt-3</strong> txt-4</strong> textE</b>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);
        visitor.transform(dom);

        String actual = DOMUtilities.toString(dom);

        String expected =
                "<b>textA<strong> txt-2<strong> txt-3 </strong>txt-4 </strong>textE</b>";

        verifyDOMMatches(null, expected, actual);
    }


    /**
     * Test the recursive nature of pushing a matching parent down.
     */
    public void testLinkElementFixUp() throws Exception {
        TransformingVisitor visitor = createVisitor();
        String input = "<b>textA <a>txt-2</a> textE</b>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);
        visitor.transform(dom);

        String actual = DOMUtilities.toString(dom);

        String expected = "<b>textA <a> txt-2</a>" +
                VolantisProtocol.NBSP + " textE</b>";

        verifyDOMMatches(null, expected, actual);
    }

    /**
     * Test the recursive nature of pushing a matching parent down.
     */
    public void testNestedLinkElementFixUp() throws Exception {
        TransformingVisitor visitor = createVisitor();
        String input = "<b>textA <a>txt-2 <a>txt-3</a> txt-4</a> textE</b>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);
        visitor.transform(dom);

        String actual = DOMUtilities.toString(dom);

        String expected =
                "<b>textA <a> txt-2 <a> txt-3</a>" + VolantisProtocol.NBSP +
                " txt-4</a>" + VolantisProtocol.NBSP + " textE</b>";

        verifyDOMMatches(null, expected, actual);
    }

    /**
     * Helper method that formats the failure nicely so that it is easy
     * to see the difference in the dom.
     *
     * @param msg      the message to display.
     * @param expected the expected result.
     * @param actual   the actual result.
     */
    protected void verifyDOMMatches(String msg,
                                    String expected,
                                    String actual) {
        if (msg == null) {
            msg = "Transformed result should match: ";
        }
        assertEquals(msg +
                     "\nEXPECTED: " + expected +
                     "\nACTUAL  : " + actual + "\n",
                     expected,
                     actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10675/7	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 03-Aug-05	9139/3	doug	VBM:2005071403 Fixed whitespace issues

 02-Aug-05	9139/1	doug	VBM:2005071403 Finished off whitespace fixes

 ===========================================================================
*/
