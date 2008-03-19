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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasicTransFactoryTestCase.java,v 1.5 2003/01/17 12:03:40 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Sep-02    Phil W-S        VBM:2002091901 - Created. 
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Added getVisitor test.
 * 09-Jan-03    Phil W-S        VBM:2003010902 - Updates to test cases.
 * 17-Jan-03    Phil W-S        VBM:2003010606 - Rework: Add test of container
 *                              validator.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.trans.ContainerActions;
import com.volantis.mcs.protocols.trans.ContainerValidator;
import com.volantis.mcs.protocols.trans.LCMImpl;
import com.volantis.mcs.protocols.trans.TransElement;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This is the unit test for the XHTMLBasicTransFactory class and the
 * TransFactory interface.
 *
 * @todo make sure all methods are tested, specifically the visitor methods
 */
public class XHTMLBasicTransFactoryTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Protocol specific configuration.
     */
    protected XHTMLBasicConfiguration configuration =
            new XHTMLBasicConfiguration();

    /**
     * Test version of protocol.
     */
    protected XHTMLBasic protocol;
    private InternalDevice internalDevice;

    protected void setUp() throws Exception {
        super.setUp();

        internalDevice = InternalDeviceTestHelper.createTestDevice();

        protocol = (XHTMLBasic) new ProtocolBuilder().build(
                new TestProtocolRegistry.TestXHTMLBasicFactory(),
                internalDevice);
    }

    /**
     * Simple method test
     */
    public void testGetTable() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        Element element = domFactory.createElement();
        TransElement trans = factory.getTable(element, protocol);
        
        assertTrue("getTable returned an instance of " +
                   trans.getClass().getName(),
                   trans instanceof XHTMLBasicTransTable);
    }

    /**
     * Simple method test
     */
    public void testGetCell() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        Element element = domFactory.createElement();
        TransElement trans = factory.getCell(element, element, 0, 0, null);
        
        assertTrue("getCell returned an instance of " +
                   trans.getClass().getName(),
                   trans instanceof XHTMLBasicTransCell);    }

    /**
     * Simple method test
     */
    public void testGetVisitor() throws Exception {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);

        TransVisitor visitor = factory.getVisitor(protocol);

        assertTrue("The visitor should be an XHTMLBasicTransVisitor",
                   visitor instanceof XHTMLBasicTransVisitor);
    }

    /**
     * Simple method test
     */
    public void testGetLCM() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);

        assertSame("expect the factory to return the default LCM singleton",
                   factory.getLCM(),
                   LCMImpl.getInstance());
    }

    /**
     * Simple method test
     */
    public void testGetMapper() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);

        assertTrue("expect the factory to return a new XHTMLBasicTransMapper",
                   factory.getMapper(protocol) instanceof XHTMLBasicTransMapper);
    }

    /**
     * Simple method test
     */
    public void testGetElementHelper() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);

        assertSame("expect the factory to return the " +
                   "XHTMLBasicElementHelper singleton",
                   factory.getElementHelper(),
                   XHTMLBasicElementHelper.getInstance());
    }

    /**
     * Simple method test.
     */
    public void testGetContainerValidator() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        String[] elementNames =
          {"table",
           "tr",
           "td",
           "span",
           "div",
           "form",
           "strong"};
        int[] expectedResult =
            {ContainerActions.PROMOTE,
             ContainerActions.PROMOTE,
             ContainerActions.PROMOTE,
             ContainerActions.PROMOTE,
             ContainerActions.PROMOTE,
             ContainerActions.INVERSE_REMAP,
             ContainerActions.PROMOTE};
        ContainerValidator cv = factory.getContainerValidator(protocol);
        Element element = domFactory.createElement();

        for (int i = 0;
             i < elementNames.length;
             i++) {
            element.setName(elementNames[i]);
            assertEquals(elementNames[i] + " action " + i + " not as expected",
                         cv.getAction(element, null),
                         expectedResult[i]);
        }
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

 ===========================================================================
*/
