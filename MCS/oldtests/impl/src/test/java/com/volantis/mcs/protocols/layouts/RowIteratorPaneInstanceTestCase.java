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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/RowIteratorPaneInstanceTestCase.java,v 1.3 2003/02/06 11:38:48 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Dec-2002  Sumit           VBM:2002111103 - Created
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses the 
 *                              new shared Test... versions of classes rather  
 *                              their own "cut & paste" inner classes.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.RowIteratorPane;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;

/**
 * 
 */
public class RowIteratorPaneInstanceTestCase extends AbstractPaneInstanceTestAbstract {
    private CanvasLayout canvasLayout;
    private RowIteratorPaneInstance paneInstance;
    
    public void setUp(){
        canvasLayout = new CanvasLayout();
        paneInstance=new RowIteratorPaneInstance(NDimensionalIndex.ZERO_DIMENSIONS);
        paneInstance.setFormat(createPane());
        paneInstance.setDeviceLayoutContext(new TestDeviceLayoutContext());
        paneInstance.initialise();
    }

    public Pane createPane(){
        RowIteratorPane pane = new RowIteratorPane(canvasLayout);
        pane.setName("pane");
        return pane;
    }
	
    /**
     * Tests that on subsequent calls of getCurrentBuffer() method the
     * same output buffer instance is returned
     */
    public void testGetCurrentBuffer(){
        OutputBuffer old = paneInstance.getCurrentBuffer(true);
        OutputBuffer exists = paneInstance.getCurrentBuffer(true);
        assertEquals("OutputBuffer must be the same", old, exists);
    }
    /**
     * Tests that calls to endCurrentBuffer followed by a getCurrentBuffer
     * returns different output buffer instances.
     */
    
    public void testEndCurrentBuffer(){
        OutputBuffer old = paneInstance.getCurrentBuffer();
        paneInstance.endCurrentBuffer();
        OutputBuffer exists = paneInstance.getCurrentBuffer();
        assertTrue("OutputBuffer must be different", old!=exists);
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 16-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
