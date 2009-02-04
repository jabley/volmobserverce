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
/*
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/NativeMarkupElementImplTestCaseNew.java,v 1.2 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-2003  Chris W         VBM:2003030404 - Ported from metis.
 * ----------------------------------------------------------------------------

 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.expression.SelectState;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.PAPIConstants;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.EmptyStackException;

/**
 * This class tests NativeMarkupElement
 */
public class NativeMarkupElementImplTestCase extends TestCaseAbstract
{  
    /**
     * attributes
     */
    private com.volantis.mcs.papi.NativeMarkupAttributes attributes;

    /**
     * element being tested
     */
    private NativeMarkupElementImpl element;

    /**
     * Pane
     */
    private Pane pane;

    /**
     * PaneInstance
     */
    private PaneInstance paneInstance;


    /**
     * PageContext
     */
    private TestMarinerPageContext pageContext;

    /**
     * Request Context
     */
    private MarinerRequestContext requestContext;

    /**
     * OutputBufferFactory
     */
    private OutputBufferFactory bufferFactory;

    /**
     * OutputBuffer
     */
    private OutputBuffer outputBuffer;

    /**
     * Layout
     */
    private CanvasLayout canvasLayout;

    /**
     * DeviceLayoutContent
     */
    private TestDeviceLayoutContext deviceLayoutContext;

    /**
     * Default junit constructor
     */
    public NativeMarkupElementImplTestCase(String name)
    {
        super(name);        
    }
    
    // javadoc inherited from super class
    protected void setUp() throws Exception {
        super.setUp();

        outputBuffer = new DOMOutputBuffer();

        bufferFactory = new OutputBufferFactory() {
            public OutputBuffer createOutputBuffer() {
               return outputBuffer;
            }

        };
    }

    // javadoc inherited from super class
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Tests the elementStart method
     * @throws Exception
     */
    public void testElementStart() throws Exception
    {
        // Test with protocol that doesn't support native markup.
        initialiseContexts();
        useNativeMarkupNotSupportedProtocol();
        assertEquals("protocol shouldn't support native markup", 
                     PAPIConstants.SKIP_ELEMENT_BODY,
                     element.elementStart(requestContext, attributes));
                     
        // Test with protocol that supports native markup and a pane that
        // should be processed.                     
        initialiseContexts();
        useNativeMarkupSuppportedProtocol();
        attributes.setPane("TestPane");
        attributes.setTargetLocation("pane");
        assertEquals("WMLVersion1_1 should support native markup", 
                     PAPIConstants.PROCESS_ELEMENT_BODY, 
                     element.elementStart(requestContext, attributes));                            
        assertSame("Current pane is wrong", pane, pageContext.getCurrentPane());
        assertSame("Current element is wrong", element, pageContext.getCurrentElement());
    }

    /**
     * Tests the elementEnd method
     * @throws Exception
     */
    public void testElementEnd() throws Exception
    {
        // Test with protocol that doesn't support native markup.
        initialiseContexts();
        useNativeMarkupNotSupportedProtocol();
        assertEquals("protocol shouldn't support native markup", 
                     PAPIConstants.SKIP_ELEMENT_BODY, 
                     element.elementStart(requestContext, attributes));
        assertEquals("should return correct code", 
                     PAPIConstants.CONTINUE_PROCESSING, 
                     element.elementEnd(requestContext, attributes));
        
        
        // Test with protocol that supports native markup and a pane that
        // should be processed.                     
        initialiseContexts();
        useNativeMarkupSuppportedProtocol();
        attributes.setPane("TestPane");
        attributes.setTargetLocation("pane");
        assertEquals("WMLVersion1_1 should support native markup", 
                     PAPIConstants.PROCESS_ELEMENT_BODY, 
                     element.elementStart(requestContext, attributes));                            
        assertSame("Current pane is wrong", pane, pageContext.getCurrentPane());
        assertSame("Current element is wrong", element, pageContext.getCurrentElement());

        assertEquals("should return correct code", 
                     PAPIConstants.CONTINUE_PROCESSING, 
                     element.elementEnd(requestContext, attributes));
        assertNull("Current element is wrong", pageContext.getCurrentElement());
    }

    /**
     * Tests the elementReset method
     * All the attributes are private and have no get methods so we check
     * that no exception is thrown. 
     * @throws Exception
     */
    public void testElementReset() throws Exception
    {
        initialiseContexts();
        element.elementReset(requestContext);
    }

    /**
     * Initialise the context objects that this test uses, including a
     * new TestMarinerPageContext
     */
    private void initialiseContexts() {
        initialiseContexts(new TestMarinerPageContext());
    }

    /**
     * Initialise the context objects that this test uses, including the
     * provided TestMarinerPageContext
     * @param pageContext - the TestMarinerPageContext to be initialised
     */
    private void initialiseContexts(TestMarinerPageContext pageContext) {
        attributes = new com.volantis.mcs.papi.NativeMarkupAttributes();
        element = new NativeMarkupElementImpl();

        canvasLayout = new CanvasLayout();

        pane = new Pane(canvasLayout);
        pane.setName("TestPane");                
        
        paneInstance = new TestPaneInstance();
        paneInstance.setFormat(pane);
        
        deviceLayoutContext = new TestDeviceLayoutContext();
        deviceLayoutContext.setFormatInstance(pane,
                NDimensionalIndex.ZERO_DIMENSIONS, paneInstance);

        paneInstance.setDeviceLayoutContext(deviceLayoutContext);

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        pageContext.setFormatInstance(paneInstance);
        pageContext.setDeviceLayout(runtimeDeviceLayout);
        pageContext.pushDeviceLayoutContext(deviceLayoutContext);
        pageContext.addPaneMapping(pane);

        final PolicyReferenceResolverMock referenceResolverMock =
                new PolicyReferenceResolverMock("referenceResolverMock",
                        expectations);

        pageContext.setPolicyReferenceResolver(referenceResolverMock);

        referenceResolverMock.expects.resolveQuotedTextExpression(null)
                .returns(null).any();
        referenceResolverMock.expects.resolveQuotedTextExpression("pane")
                .returns(new LiteralTextAssetReference("pane")).any();

        requestContext = new TestMarinerRequestContext();
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        pageContext.pushRequestContext(requestContext);

        this.pageContext = pageContext;
    }
    
    /**
     * Sets the protocol used to one that doesn't support native markup
     */   
    private void useNativeMarkupNotSupportedProtocol()
    {
        VolantisProtocolStub protocol = new VolantisProtocolStub();
        protocol.setOutputBufferFactory(bufferFactory);
        pageContext.setProtocol(protocol);
    }

    /**
     * Sets the protocol used to one that support native markup
     */   
    private void useNativeMarkupSuppportedProtocol() {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestWMLRootFactory(),
                InternalDeviceTestHelper.createTestDevice());
        protocol.setMarinerPageContext(pageContext);
        pageContext.setProtocol(protocol);
    }

    /**
     * Test to see that the NativeMarkupElement works when using the VDXML
     * protocol
     * @throws RepositoryException
     * @throws PAPIException
     */
    public void testVDXMLNativeSupport() throws RepositoryException,
            PAPIException {
        //create a page context which does not throw an exception when the
        //peekSelectState method is called. This will act as if there is
        //a value in the stack of SelectState items because otherwise
        //an EmptyStackException would be thrown.
        TestMarinerPageContext pageContext = new TestMarinerPageContext() {
            public SelectState peekSelectState() {
                return null;
            }
        };
        initialiseContexts(pageContext);

        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.VDXMLVersion2_0Factory(),
                InternalDeviceTestHelper.createTestDevice());
        protocol.setMarinerPageContext(pageContext);
        pageContext.setProtocol(protocol);

        attributes.setPane("TestPane");
        attributes.setTargetLocation("pane");


        assertEquals("VDXMLVersion2_0 should support native markup",
                     PAPIConstants.PROCESS_ELEMENT_BODY,
                     element.elementStart(requestContext, attributes));

        assertEquals("should return correct code",
                     PAPIConstants.CONTINUE_PROCESSING,
                     element.elementEnd(requestContext, attributes));
    }

    /**
     * Test to see that the NativeMarkupElement works when using the
     * XHTMLBasic protocol when there are items in the stack of select items
     * @throws RepositoryException
     * @throws PAPIException
     */
    public void testXHTMLBasicNativeSupportWithSelectItems()
            throws RepositoryException, PAPIException {
        //create a page context which does not throw an exception when the
        //peekSelectState method is called. This will act as if there is
        //a value in the stack of SelectState items because otherwise
        //an EmptyStackException would be thrown.
        TestMarinerPageContext pageContext = new TestMarinerPageContext() {
            public SelectState peekSelectState() {
                return null;
            }
        };
        initialiseContexts(pageContext);

        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLBasicFactory(),
                InternalDeviceTestHelper.createTestDevice());
        protocol.setMarinerPageContext(pageContext);
        pageContext.setProtocol(protocol);

        attributes.setPane("TestPane");
        attributes.setTargetLocation("pane");


        assertEquals("XHTMLBasic should support native markup",
                     PAPIConstants.PROCESS_ELEMENT_BODY,
                     element.elementStart(requestContext, attributes));

        assertEquals("should return correct code",
                     PAPIConstants.CONTINUE_PROCESSING,
                     element.elementEnd(requestContext, attributes));
    }

    /**
     * Test to see that the NativeMarkupElement works when using the
     * XHTMLBasic protocol when there are no items in the stack of
     * select items
     * @throws RepositoryException
     * @throws PAPIException
     */
    public void testXHTMLBasicNativeSupportWithoutSelectItems()
            throws RepositoryException, PAPIException {
        //create a page context which throws an exception when the
        //peekSelectState method is called. This will act as if there are no
        //values in the stack of SelectState items.
        TestMarinerPageContext pageContext = new TestMarinerPageContext() {
            public SelectState peekSelectState() {
                throw new EmptyStackException();
            }
        };
        initialiseContexts(pageContext);

        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLBasicFactory(),
                InternalDeviceTestHelper.createTestDevice());
        protocol.setMarinerPageContext(pageContext);
        pageContext.setProtocol(protocol);

        attributes.setPane("TestPane");
        attributes.setTargetLocation("pane");


        assertEquals("XHTMLBasic should skip native markup",
                     PAPIConstants.SKIP_ELEMENT_BODY,
                     element.elementStart(requestContext, attributes));

        assertEquals("should return correct code",
                     PAPIConstants.CONTINUE_PROCESSING,
                     element.elementEnd(requestContext, attributes));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/4	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
