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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Nov-02    Sumit           VBM:2002111102 - Created to test the
 *                              FormatReferenceParser
 * 28-Nov-02    Allan           VBM:2002110102 - createDeviceLayout() to not
 *                              add the Format since setName() does this.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses the 
 *                              new TestMariner...Context classes rather than 
 *                              "cut & paste" inner classes which extend 
 *                              Mariner...Context.
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.runtime;

import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.TestPane;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class FormatReferenceParserTestCase
        extends TestCaseAbstract {

    private TestMarinerPageContext context = new TestMarinerPageContext();

    private TestPane pane;

    private TestPaneInstance fContext;
    
    /**
	 * Constructor for FormatReferenceParserTestCase.
	 * @param arg0
	 */
	public FormatReferenceParserTestCase(String arg0) {
		super(arg0);
	}
    
    public void setUp() {
        CanvasLayout deviceLayout = new CanvasLayout();

        pane = new TestPane(deviceLayout);
        pane.setName("beer");
        context.addPaneMapping(pane);

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(deviceLayout);

        context.setDeviceLayout(runtimeDeviceLayout);
        fContext = new TestPaneInstance();
        context.setFormatInstance(fContext);
    }
    
    public void tearDown(){
    }
    
    /**
     * Tests the parsePane() method to see if it 
     * returns the correct format instance ref for a pane name.
     * Our pane name is of a smaller dimension than the number of dimensions
     * for this layout so it should be padded out with zeros
     */
    public void testParsePaneSmaller(){
        FormatReference fr;
        String paneName="beer.0.2.3";
        pane.setDimensions(5);
        fr = FormatReferenceParser.parsePane(paneName, context);
        assertTrue(fr.getStem().equals("beer"));
        int index[] = {0,2,3,0,0};
        assertEquals("instance not as",
                     new NDimensionalIndex(index, 3),
                     fr.getIndex());
    }
    
    /**
     * Tests the parsePane() method to see if it
     * returns the correct format instance ref for a pane name.
     * Our pane name is of a larger dimension than the number of dimensions
     * for this layout so it should be truncated to 3
     */
    public void testParsePaneLarger(){
        FormatReference fr;
        String paneName="beer.0.2.3.4.5";
        pane.setDimensions(3);
        fr = FormatReferenceParser.parsePane(paneName, context);
        assertTrue(fr.getStem().equals("beer"));
        int index[] = {0,2,3};
        assertEquals("instance not as",
                     new NDimensionalIndex(index),
                     fr.getIndex());
    }

    /**
     * Make sure that things work correctly when no indices are specified and
     * the pane is not iterated
     */
    public void testParsePaneNoIndicesInEither() {
        FormatReference fr;
        String paneName="beer";
        pane.setDimensions(0);
        fr = FormatReferenceParser.parsePane(paneName, context);
        assertEquals("stem not as",
                     "beer",
                     fr.getStem());
        assertEquals("instance not as",
                     new NDimensionalIndex(new int[0]),
                     fr.getIndex());
    }

    /**
     * Make sure that things work correctly when no indices are specified and
     * the pane is iterated
     */
    public void testParsePaneNoIndicesSpecified() {
        FormatReference fr;
        String paneName = "beer";
        pane.setDimensions(1);
        fr = FormatReferenceParser.parsePane(paneName, context);
        assertEquals("stem not as",
                     "beer",
                     fr.getStem());
        assertEquals("instance not as",
                     new NDimensionalIndex(new int[1], 0),
                     fr.getIndex());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 06-May-04	3999/4	philws	VBM:2004042202 Review updates

 06-May-04	3999/2	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
