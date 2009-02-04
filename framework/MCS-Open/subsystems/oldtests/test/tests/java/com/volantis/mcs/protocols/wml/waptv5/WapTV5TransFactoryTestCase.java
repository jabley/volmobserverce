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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/waptv5/WapTV5TransFactoryTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-03    Phil W-S        VBM:2002110402 - Created. Tests associated
 *                              class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml.waptv5;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Assert;

/**
 * Tests the TransFactory behaviour in conjunction with its superclasses.
 */
public class WapTV5TransFactoryTestCase extends TestCaseAbstract {
    
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

    protected TransFactory getFactory() {
        return new WapTV5TransFactory(null);
    }

    protected Class getFactoryClass() {
        return WapTV5TransFactory.class;
    }

    protected Class getTableClass() {
        return WapTV5TransTable.class;
    }

    protected Class getVisitorClass() {
        return WapTV5TransVisitor.class;
    }

    /**
     * This method tests the method public TransCell getCell(Element,Element,int,int,DOMProtocol)
     * for the com.volantis.mcs.protocols.wml.waptv5.WapTV5TransFactory class.
     */
    public void notestGetCell()
        throws Exception {
        //
        // Test public TransCell getCell(Element,Element,int,int,DOMProtocol) method.
        //
        Assert.fail("public TransCell getCell(Element,Element,int,int,DOMProtocol) not tested.");
    }

    /**
     * This method tests the method public void release(TransElement)
     * for the com.volantis.mcs.protocols.wml.waptv5.WapTV5TransFactory class.
     */
    public void notestRelease()
        throws Exception {
        //
        // Test public void release(TransElement) method.
        //
        Assert.fail("public void release(TransElement) not tested.");
    }

    /**
     * This method tests the method public LCM getLCM()
     * for the com.volantis.mcs.protocols.wml.waptv5.WapTV5TransFactory class.
     */
    public void notestGetLCM()
        throws Exception {
        //
        // Test public LCM getLCM() method.
        //
        Assert.fail("public LCM getLCM() not tested.");
    }

    /**
     * This method tests the method public AbridgedTransMapper getMapper()
     * for the com.volantis.mcs.protocols.wml.waptv5.WapTV5TransFactory class.
     */
    public void notestGetMapper()
        throws Exception {
        //
        // Test public AbridgedTransMapper getMapper() method.
        //
        Assert.fail("public AbridgedTransMapper getMapper() not tested.");
    }

    /**
     * This method tests the method public ElementHelper getElementHelper()
     * for the com.volantis.mcs.protocols.wml.waptv5.WapTV5TransFactory class.
     */
    public void notestGetElementHelper()
        throws Exception {
        //
        // Test public ElementHelper getElementHelper() method.
        //
        Assert.fail("public ElementHelper getElementHelper() not tested.");
    }

    /**
     * This method tests the method public TransContext getContext(Element)
     * for the com.volantis.mcs.protocols.wml.waptv5.WapTV5TransFactory class.
     */
    public void notestGetContext()
        throws Exception {
        //
        // Test public TransContext getContext(Element) method.
        //
        Assert.fail("public TransContext getContext(Element) not tested.");
    }

    /**
     * This method tests the method public ContainerValidator getContainerValidator()
     * for the com.volantis.mcs.protocols.wml.waptv5.WapTV5TransFactory class.
     */
    public void notestGetContainerValidator()
        throws Exception {
        //
        // Test public ContainerValidator getContainerValidator() method.
        //
        Assert.fail("public ContainerValidator getContainerValidator() not tested.");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
