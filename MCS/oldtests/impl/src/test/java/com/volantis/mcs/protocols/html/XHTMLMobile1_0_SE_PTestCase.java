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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.devices.InternalDevice;
import junitx.util.PrivateAccessor;

/**
 * This class tests the XHTMLMobile1_0_SE_P protocol.
 */
public class XHTMLMobile1_0_SE_PTestCase extends XHTMLMobile1_0TestCase {

    private XHTMLMobile1_0_SE_P protocol;
    private XHTMLBasicTestable testable;

    // javadoc inherited
    public XHTMLMobile1_0_SE_PTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLMobile1_0_SE_PFactory(),
                internalDevice);
        return protocol;
    }

    // javadoc inherited
    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (XHTMLMobile1_0_SE_P) protocol;
        this.testable = (XHTMLBasicTestable) testable;
    }

    /**
     * Tests that the private addDefaultCellspacing method adds cellspacing=0
     * when necessary.
     */
    public void testAddDefaultCellspacing() throws Throwable {
        final Class classes [] = new Class[]{Element.class};

        Element table = domFactory.createElement();
        table.setName("table");
        PrivateAccessor.invoke(protocol,
                "addDefaultCellspacing",
                classes, new Object[]{table});

        checkTableAttributes(table);

        table = domFactory.createElement();
        table.setName("table");
        table.setAttribute("cellspacing", "5");
        PrivateAccessor.invoke(protocol,
                "addDefaultCellspacing",
                classes, new Object[]{table});
        assertEquals("Cellspacing should be 5",
                "5", table.getAttributeValue("cellspacing"));
    }

    /**
     * Helper method which checks that the cellspacing attribute of the given
     * table element has a 0 value.
     * @param table the table element whose cellspacing value to check
     */
    protected void checkTableAttributes(Element table) {
        assertEquals("Cellspacing should be 0", "0",
                table.getAttributeValue("cellspacing"));
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

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jun-05	8784/1	philws	VBM:2005061402 Port SE P800/P900 width attribute from 3.3.1

 14-Jun-05	8779/1	philws	VBM:2005061402 Port SE P800/P900 width rendering from 3.2.3

 14-Jun-05	8756/1	philws	VBM:2005061402 Provide markup layout width rendering for SE P800/P900 phones to resolve device CSS deficiency

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 06-Sep-04	5361/6	pcameron	VBM:2004082517 New subprotocol for SE P800/P900 devices which adds cellspacing=0 to all tables

 06-Sep-04	5361/4	pcameron	VBM:2004082517 New subprotocol for SE P800/P900 devices which adds cellspacing=0 to all tables

 03-Sep-04	5331/3	pcameron	VBM:2004082517 New subprotocol for SE P800/P900 devices which adds cellspacing=0 to all tables

 ===========================================================================
*/
