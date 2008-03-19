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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/XFContentElementImplTestCase.java,v 1.4 2003/03/26 15:32:13 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Doug            VBM:2002111806 - Created. Unit tests the
 *                              com.volantis.mcs.papi.XFContentElement class.
 * 30-Jan-03    Geoff           VBM:2003012101 - Modified to use the new
 *                              TestMarinerRequestContext rather than a "cut &
 *                              paste" inner class which extends 
 *                              MarinerRequestContext. 
 * 26-Mar-03    Allan           VBM:2003021803 - Modified usages of the 
 *                              DeviceLayout no-arg constructor to use the 
 *                              String, String version instead. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.XFContentAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.forms.AbstractForm;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class unit test the XFContentElementclass.
 */
public class XFContentElementImplTestCase
        extends TestCaseAbstract {

    /**
     * attributes
     */
    private XFContentAttributes attributes;

    /**
     * element being tested
     */
    private XFContentElementImpl element;

    /**
     * Pane
     */
    private Pane pane;

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
    private TestDeviceLayoutContext deviceLayoutContext;

    // javadoc inherited from superclass
    public XFContentElementImplTestCase(String name) {
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
     * This method tests the constructors for
     * the com.volantis.mcs.papi.XFContentElement class.
     */
    public void testConstructors() {
       // just check that no exception is thrown
        XFContentElementImpl element = new XFContentElementImpl();
    }

    /**
     * This method tests the method public int elementStart ( MarinerRequestContext,PAPIAttributes )
     * for the com.volantis.mcs.papi.XFContentElement class.
     */
    public void testElementStart()
            throws Exception {

        initialiseContexts();
        pageContext.pushElement(createDummyFormElement(pane));
        TestPaneInstance paneContext = new TestPaneInstance();
        paneContext.setFormat(pane);
        paneContext.setDeviceLayoutContext(deviceLayoutContext);
        pageContext.setFormatInstance(paneContext);
        assertEquals("Wrong code returned by elementStart",
                     PAPIElement.PROCESS_ELEMENT_BODY,
                     element.elementStart(requestContext, attributes));

        // check to see that the XFFomElements Pane has been pushed onto
        // the Page Contexts pane stack
//        assertEquals("Pane Stack size should be 1",
//                     pageContext.getPaneStack().size(), 1);

        Pane currentPane = pageContext.getCurrentPane();
        assertSame("Current pane should be " + currentPane, currentPane, pane);


        // check to see that pageContext is using the corrent output buffer.
//        assertEquals("OutputBuffer stack size should be 1",
//                     pageContext.getBufferStack().size(), 1);

        assertSame("Current outputBuffer should be " + outputBuffer,
                   pageContext.getCurrentOutputBuffer(), outputBuffer);


        initialiseContexts();
        pageContext.pushElement(createDummyFormElement(pane));
        Pane namedPane = new Pane(canvasLayout);
        namedPane.setName("NamedPane");
        attributes.setPane(namedPane.getName());
        pageContext.addPaneMapping(namedPane);
        paneContext = new TestPaneInstance();
        paneContext.setFormat(namedPane);
        paneContext.setDeviceLayoutContext(deviceLayoutContext);
        pageContext.setFormatInstance(paneContext);
        element.elementStart(requestContext, attributes);

        currentPane = pageContext.getCurrentPane();
        assertSame("Current pane should be " + currentPane,
                   currentPane, namedPane);

        // check to see that pageContext is using the corrent output buffer.
        assertSame("Current outputBuffer should be " + outputBuffer,
                   pageContext.getCurrentOutputBuffer(), outputBuffer);

    }

    /**
     * This method tests the method public int elementEnd ( MarinerRequestContext,PAPIAttributes )
     * for the com.volantis.mcs.papi.XFContentElement class.
     */
    public void testElementEnd()
            throws Exception {

        initialiseContexts();
        pageContext.pushElement(createDummyFormElement(pane));
        TestPaneInstance paneContext = new TestPaneInstance();
        paneContext.setFormat(pane);
        paneContext.setDeviceLayoutContext(deviceLayoutContext);
        pageContext.setFormatInstance(paneContext);
        element.elementStart(requestContext, attributes);

        element.elementEnd(requestContext, attributes);
    }

    /**
     * This method tests the method public void elementReset ( MarinerRequestContext )
     * for the com.volantis.mcs.papi.XFContentElement class.
     */
    public void testElementReset()
            throws Exception {
        // ensure that no exception is thrown
        initialiseContexts();
        element.elementReset(requestContext);

    }

    /**
     * Initialise the context objects that this test uses
     */
    private void initialiseContexts() throws RepositoryException {
        attributes = new XFContentAttributes();
        element = new XFContentElementImpl();

        requestContext = new TestMarinerRequestContext();

        pageContext = new TestMarinerPageContext();
        VolantisProtocolStub protocol = new VolantisProtocolStub();
        protocol.setOutputBufferFactory(bufferFactory);
        pageContext.setProtocol(protocol);

        canvasLayout = new CanvasLayout();
        pane = new Pane(canvasLayout);
        pane.setName("TestPane");

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        deviceLayoutContext = new TestDeviceLayoutContext();
        deviceLayoutContext.setDeviceLayout(runtimeDeviceLayout);
        pageContext.pushDeviceLayoutContext(deviceLayoutContext);

        pageContext.setDeviceLayout(runtimeDeviceLayout);

        ContextInternals.setMarinerPageContext(requestContext, pageContext);
    }


    /** Create a dummy XFForm element
     * @param defaultPane The default Pane for the Form element
     * @return the XFFormElement
     */
    public XFFormElementImpl createDummyFormElement(Pane defaultPane) {

        final Pane pane = defaultPane;

        final Form form = new Form((CanvasLayout) defaultPane.getLayout()) {
            public Pane getDefaultPane() {
                return pane;
            }
        };

        final FormInstance formInstance = new FormInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(form);

        final com.volantis.mcs.protocols.XFFormAttributes formAttributes
                = new com.volantis.mcs.protocols.XFFormAttributes() {
                    public AbstractForm getFormData() {
                        return formInstance;
                    }
        };

        XFFormElementImpl formElement = new XFFormElementImpl() {
            XFFormAttributes getProtocolAttributes() {
                return formAttributes;
            }
        };

        return formElement;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 22-Aug-05	9298/4	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 04-Jul-03	337/1	chrisw	VBM:2003020609 implemented rework, added testcases

 ===========================================================================
*/
