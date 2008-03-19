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

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for HTML_iModeTableEmualtionTransformer.
 */
public class HTML_iModeTableEmulationTransformerTestCase
        extends TestCaseAbstract {

    private StrictStyledDOMHelper helper;

    protected void setUp() throws Exception {
        super.setUp();

        helper = new StrictStyledDOMHelper(null);
    }

    /**
     * Test that tables that have no row data do not cause rows to be
     * generated.
     */
    public void testTableTransformationEmptyRows() throws Exception {
        String inputText =
                "<body>" +
                    "<p>" +
                        "Hello Allan" +
                        "<table>" +
                            "<tr>" +
                                "<td>r1c1</td>" +
                                "<td/>" +
                            "</tr>" +
                            "<tr>" +
                                "<td/>" +
                                "<td/>" +
                            "</tr>" +
                            "<tr>" +
                                "<td>" +
                                    "<table>" +
                                        "<tr>" +
                                            "<td/>" +
                                        "</tr>" +
                                    "</table>" +
                                "</td>" +
                                "<td/>" +
                            "</tr>" +
                            "<tr>" +
                                "<td>" +
                                    "<table>" +
                                        "<tr>" +
                                            "<td>some content</td>" +
                                            "<td>some more content</td>" +
                                        "</tr>" +
                                    "</table>" +
                                "</td>" +
                            "</tr>" +
                        "</table>" +
                    "</p>" +
                "</body>";

        String expectedText =
                "<body>" +
                    "<p>" +
                        "Hello Allan" +
                        "<p>" +
                            "r1c1 <br/>" +
                            "some content some more content <br/>" +
                        "</p>" +
                    "</p>" +
                "</body>";

        Document dom = helper.parse(inputText);
        Document expected = helper.parse(expectedText);

        DOMTransformer transformer = new HTML_iModeTableEmulationTransformer();

        HTML_iMode protocol = createProtocol();
        protocol.deviceTablesCapable = false;

        dom = transformer.transform(protocol, dom);

        String domAsString = DOMUtilities.toString(
                dom, protocol.getCharacterEncoder());
        String expectedAsString = DOMUtilities.toString(
                expected, protocol.getCharacterEncoder());

        assertEquals(expectedAsString, domAsString);
    }

    private HTML_iMode createProtocol() {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder builder = new ProtocolBuilder();
        HTML_iMode protocol = (HTML_iMode) builder.build(
                new TestProtocolRegistry.TestHTML_iModeFactory(),
                internalDevice);
        return protocol;
    }

    /**
     * Test that tables are transformed as expected. Nested tables
     * should be removed. Non-nested tables should be replaced by
     * p, tr by their contents followed by a br, and td removed and
     * followed by a space.
     */
    public void testTableTransformation() throws Exception {
        String inputText =
                "<body>" +
                    "<p>" +
                        "Hello Allan" +
                        "<table>" +
                            "<tr>" +
                                "<td>r1c1</td>" +
                                "<td>r1c2</td>" +
                                "<td>" +
                                    "<table>" +
                                        "<tr>" +
                                            "<td>r1c3(a).</td>" +
                                        "</tr>" +
                                    "</table>" +
                                "r1c3(b)</td>" +
                            "</tr>" +
                            "<tr>" +
                                "<td>r2c1</td>" +
                                "<td>r2c2l1<br/>r2c2l2</td>" +
                                "<td>r2c3<p>r2c3p1</p></td>" +
                            "</tr>" +
                        "</table>" +
                        "Bye Allan" +
                    "</p>" +
                "</body>";

        String expectedText =
                "<body>" +
                    "<p>" +
                        "Hello Allan" +
                        "<p>" +
                            "r1c1 r1c2 r1c3(a). <br/>r1c3(b) <br/>" +
                            "r2c1 r2c2l1<br/>r2c2l2 r2c3<p>r2c3p1</p> <br/>" +
                        "</p>" +
                        "Bye Allan" +
                    "</p>" +
                "</body>";

        Document dom = helper.parse(inputText);
        Document expected = helper.parse(expectedText);

        DOMTransformer transformer = new HTML_iModeTableEmulationTransformer();

        HTML_iMode protocol = createProtocol();
        protocol.deviceTablesCapable = false;

        dom = transformer.transform(protocol, dom);

        String domAsString = DOMUtilities.toString(
                dom, protocol.getCharacterEncoder());
        String expectedAsString = DOMUtilities.toString(
                expected, protocol.getCharacterEncoder());
        
        assertEquals(expectedAsString, domAsString);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Apr-04	3867/1	allan	VBM:2004040615 Patched empty table, tr, td handling from GA line.

 14-Apr-04	3820/2	allan	VBM:2004040615 Remove obselete table, tr, and td elements.

 12-Oct-03	1540/1	allan	VBM:2003101101 Add emulated and native table support on HTML_iMode

 ===========================================================================
*/
