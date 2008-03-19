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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/xhtmltransitional/XHTMLTransitionalTransVisitorTestCase.java,v 1.3 2003/04/07 09:34:37 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-03    Phil W-S        VBM:2002110402 - Created. Tests associated
 *                              class.
 * 04-Apr-03    Adrian          VBM:2003031701 - Made this a subclass of
 *                              XHTMLBasicTransVisitorTestCase and removed some
 *                              redundant code.
 * 03-Jun-03    Byron           VBM:2003042204 - Now subclasses
 *                              XHTMLFullTransVisitorTestCase.
 * 04-Jun-03    Byron           VBM:2003042204 - Removed createTransVisitor.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.xhtmltransitional;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.Utils;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.html.xhtmlfull.XHTMLFullTransVisitorTestCase;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.devices.InternalDevice;

/**
 * Tests the TransVisitor behaviour in conjunction with its superclasses.
 */
public class XHTMLTransitionalTransVisitorTestCase
        extends XHTMLFullTransVisitorTestCase {

    // javadoc inherited from superclass
    protected DOMProtocol createDOMProtocol(InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestXHTMLTransitionalFactory(),
                internalDevice);
        return protocol;
    }

    // javadoc inherited from superclass
    protected TransFactory getProtocolSpecificFactory() {
        return new XHTMLTransitionalTransFactory(null);
    }

    public void testGetPromotePreserveStyleAttributes() throws Exception {
        // class is handled separately
        String[] expected =
            {"align", "bgcolor", "border", "cellspacing", "cellpadding",
             "dir", "frame", "id", "lang",
             "onclick", "ondblclick", "onkeydown", "onkeypress", "onkeyup",
             "onmousedown", "onmousemove", "onmouseout", "onmouseover",
             "onmouseup", "rules", "style", "summary", "title", "width",
             "xml:lang"};

        XHTMLTransitionalTransVisitor visitor = (XHTMLTransitionalTransVisitor)
            getProtocolSpecificFactory().getVisitor(protocol);
        String result = Utils.findMismatches(expected,
                visitor.getPromotePreserveStyleAttributes());

        assertTrue("Preserve style attribute mismatches: " + result,
                   (result == null));
    }

    /**
     * todo Implement this test!
     * This has been stubbed out for the moment as this will currently fail
     * with the XHTMLBasicTransVisitorTestCase expected results.  this is
     * because XHTMLBasicTransVisitor optimizes away nested tables whereas
     * we do not here.
     *
     * When implemented this test should demonstrate that this output is the
     * same as the input but with whitespace removed from tables siblings.
     */
    public void testBasicInterface() throws Exception {
    }

    /**
     * todo Implement this test!
     * This has been stubbed out for the moment as this will currently fail
     * with the XHTMLBasicTransVisitorTestCase expected results.  this is
     * because XHTMLBasicTransVisitor optimizes away nested tables whereas
     * we do not here.
     *
     * When implemented this test should demonstrate that this output is the
     * same as the input but with whitespace removed from tables siblings.
     */
    public void testStyleClassRetention() throws Exception {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
