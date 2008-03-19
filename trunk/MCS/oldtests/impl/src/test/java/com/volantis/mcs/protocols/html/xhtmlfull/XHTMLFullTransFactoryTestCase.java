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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/xhtmlfull/XHTMLFullTransFactoryTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Tests associated
 *                              class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.xhtmlfull;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.TestDOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.html.XHTMLBasicTransCell;
import com.volantis.mcs.protocols.html.XHTMLFullConfiguration;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Assert;

/**
 * Test case for XHTMLFullTransFactory.
 */
public class XHTMLFullTransFactoryTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();
    private InternalDevice internalDevice;


    protected void setUp() throws Exception {
        super.setUp();

        internalDevice = InternalDeviceTestHelper.createTestDevice();
    }

    public void testGetTable() throws Exception {
        Element table = domFactory.createElement();
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);

        table.setName("table");

        TransTable transTable = getFactory().getTable(table, protocol);

        assertEquals("unexpected class for table instance",
                getTableClass().getName(),
                transTable.getClass().getName());
    }

    public void testGetVisitor() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);

        TransVisitor visitor = getFactory().getVisitor(protocol);

        assertEquals("unexpected class for visitor instance",
                getVisitorClass().getName(),
                visitor.getClass().getName());
    }

    public void testGetCell() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);

        TransCell cell = getFactory().getCell(null, null, 0, 0, protocol);

        assertEquals("unexpected class for cell instance",
                getCellClass().getName(),
                cell.getClass().getName());
    }

    protected TransFactory getFactory() {
        return new XHTMLFullTransFactory(new XHTMLFullConfiguration());
    }

    protected Class getFactoryClass() {
        return XHTMLFullTransFactory.class;
    }

    protected Class getTableClass() {
        return XHTMLFullTransTable.class;
    }

    protected Class getVisitorClass() {
        return XHTMLFullTransVisitor.class;
    }

    protected Class getCellClass() {
        return XHTMLBasicTransCell.class;
    }

    /**
     * This method tests the method public AbridgedTransMapper getMapper()
     * for the com.volantis.mcs.protocols.html.xhtmlfull.XHTMLFullTransFactory
     * class.
     */
    public void testGetMapper() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        TestDOMProtocol protocol = (TestDOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);

        final TransFactory factory = getFactory();
        // default is true for nested table support.
        assertTrue("There should not be mapper if nested tables are supported",
                factory.getMapper(protocol) == null);

        protocol.setSupportsNestedTables(true);
        assertTrue("There should not be mapper if nested tables are supported",
                factory.getMapper(protocol) == null);

        protocol.setSupportsNestedTables(false);
        assertTrue("There should be a mapper if nested tables are not supported",
                factory.getMapper(protocol) != null);
    }

    /**
     * This method tests the method getContainerValidator()
     * for the com.volantis.mcs.protocols.html.xhtmlfull.XHTMLFullTransFactory
     * class.
     */
    public void notestGetContainerValidator()
            throws Exception {
        //
        // Test public ContainerValidator getContainerValidator() method.
        //
        Assert.fail("public ContainerValidator getContainerValidator() " +
                "not tested.");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 12-Jul-05	8990/2	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
