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
package com.volantis.mcs.papi;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.papi.impl.AbstractElementImplTestAbstract;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;

/**
 * This class tests PaneElement papi element
 */
public class PaneElementTestCase extends AbstractElementImplTestAbstract {

    /**
     * The PaneElement being tested
     */
    private PaneElement element;
    
    /**
     * The attributes associated with the PaneElement being tested
     */
    private PaneAttributes attributes;
    
    /**
     * A pane
     */
    private Pane pane;
    
    /**
     * The pane instance associated with the pane
     */
    private PaneInstance paneInstance;
    
    /**
     * A spatial format iterator is needed so we can test pane indices.
     */
    private SpatialFormatIterator sfi;
    
    /**
     * The MarinerRequestContext
     */
    private MarinerRequestContext requestContext;
    
    /**
     * The MarinerPageContext
     */
    private TestMarinerPageContext pageContext;
    
    /**
     * The Protocol used to render the PaneElement
     */
    private VolantisProtocol protocol;
           
    /**
     * The Layout
     */
    private CanvasLayout canvasLayout;
    
    /**
     * The DeviceLayoutContext
     */
    private DeviceLayoutContext deviceLayoutContext;

    // JavaDoc inherited
    protected PAPIElement createTestablePAPIElement() {        
        return new PaneElement();
    }

    /**
     * Set up the test cases
     */
    private void privateSetUp() {
    
        requestContext = new TestMarinerRequestContext();
        protocol = new VolantisProtocolStub();

        canvasLayout = new CanvasLayout();
        deviceLayoutContext = new TestDeviceLayoutContext();

        pane = new Pane(canvasLayout);
        pane.setName("pane");

        paneInstance = new PaneInstance(NDimensionalIndex.ZERO_DIMENSIONS);
        paneInstance.setDeviceLayoutContext(deviceLayoutContext);
        
        sfi = new SpatialFormatIterator(canvasLayout);
        sfi.setName("sfi");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "2");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "fixed");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "1");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "fixed");
        
        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        pageContext = new TestMarinerPageContext();
        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);
        pageContext.setDeviceLayout(runtimeDeviceLayout);
        pageContext.pushDeviceLayoutContext(deviceLayoutContext);
        pageContext.setFormatInstance(paneInstance);
        pageContext.addPaneMapping(pane);
    
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);   
    
        element = (PaneElement)createTestablePAPIElement();
        attributes = new PaneAttributes();        

    }


    /**
     * Test the elementStart method with a pane that's not in a format iterator
     */ 
    public void testElementStartZeroDimensions() throws Exception {
        privateSetUp();
        
        attributes.setName("pane");
        paneInstance.setFormat(pane);
        paneInstance.initialise();

        assertEquals("Wrong code returned by elementStart",
                     PAPIElement.PROCESS_ELEMENT_BODY,
                     element.elementStart(requestContext, attributes));
    }

    
    /**
     * Test the elementStart method with a pane that's in a one dimensional
     * spatial format iterator and shouldn't be skipped
     */ 
    public void testElementStartOneDimensionNotSkippable() throws Exception {
        privateSetUp();

        attributes.setName("pane.1");
                                
        pane.setParent(sfi);
        paneInstance.setFormat(pane);
        paneInstance.initialise();
                 
        assertEquals("Wrong code returned by elementStart",
                     PAPIElement.PROCESS_ELEMENT_BODY,
                     element.elementStart(requestContext, attributes));
    }

    /**
     * Test the elementStart method with a pane that's in a one dimensional
     * spatial format iterator and shouldn't be skipped
     */ 
    public void testElementStartOneDimensionSkippable() throws Exception {
        privateSetUp();

        attributes.setName("pane.10");

        pane.setParent(sfi);
        paneInstance.setFormat(pane);
        paneInstance.initialise();

        paneInstance = new PaneInstance(new NDimensionalIndex(new int[] {10}));
        paneInstance.setFormat(pane);
        paneInstance.setDeviceLayoutContext(deviceLayoutContext);
        pageContext.setFormatInstance(paneInstance);
        
        assertEquals("Wrong code returned by elementStart",
                     PAPIElement.SKIP_ELEMENT_BODY,
                     element.elementStart(requestContext, attributes));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 04-Jul-03	337/1	chrisw	VBM:2003020609 implemented rework, added testcases

 ===========================================================================
*/
