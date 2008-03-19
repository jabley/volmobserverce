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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/DeviceLayoutTestCase.java,v 1.5 2003/03/31 14:56:31 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Oct-02    Allan           VBM:2002102501 - Testcase for DeviceLayout.
 * 01-Nov-02    Allan           VBM:2002103107 - Added format register method
 *                              tests.
 * 28-Nov-02    Allan           VBM:2002110102 - Register methods replaced
 *                              by add/remove methods and FormatRegister has
 *                              been replaced by FormatScope.
 * 07-Mar-03    Allan           VBM:2003021801 - Added tests for equals() and 
 *                              hashCode(). 
 * 31-Mar-03    Allan           VBM:2003030601 - Added testClone(). Removed
 *                              suite() and main().
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class unit test the DeviceLayoutclass.
 * @todo later re-factor to make the hashCode and equals test work withing
 * a more generic framework e.g. using ObjectTestHelper and some reflection.
 */
public class DeviceLayoutTestCase
        extends TestCaseAbstract {

    /**
     * Create a Format that is not completely trivial e.g. contains
     * some children.
     * Formats created by this method are always equal.
     * @param dl The Layout to associate with the Format.
     * @return a Format that is not completely trivial
     */
    protected Format createFormat(CanvasLayout dl) {
        Grid grid = new Grid(dl);
        grid.setRows(2);
        grid.setColumns(2);
        grid.attributesHaveBeenSet();
        
        Pane pane1 = new Pane(new CanvasLayout());
        Pane pane2 = new Pane(new CanvasLayout());
        Pane pane3 = new Pane(new CanvasLayout());
        Pane pane4 = new Pane(new CanvasLayout());
                
        try {
            grid.setChildAt(pane1, 0);
            grid.setChildAt(pane2, 1);
            grid.setChildAt(pane3, 2);
            grid.setChildAt(pane4, 3);
        } catch(Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }         
        
        return grid;
    }
    
    /**
     * Create a Layout with everything set including some Formats.
     * DeviceLayouts created by this method are always equal.
     * @return a Layout that is reasonably comprehensive in terms of
     * its contents.
     */ 
    protected CanvasLayout createDeviceLayout() {
        CanvasLayout dl = new CanvasLayout();
        
        dl.setDefaultSegmentName("default-segment-name");
        dl.setDefaultFormFragmentName("default-form-fragment-name");
        dl.setDefaultFragmentName("default-fragment-name");
        dl.setDestinationLayout("destination-layout");
        dl.setFormatCount(10); // this is not a true reflection
        dl.setLayoutGroupName("layout-group-name");
        Format root = createFormat(dl);
        dl.setRootFormat(root);
        Pane pane = new Pane(dl); 
        pane.setName("pane"); // this actually adds the Pane to the layout!

        return dl;
    }


    /**
     * This method tests the method public void addFormat ( Format )
     * for the com.volantis.mcs.layouts.FormatRegister class.
     */
    public void testAddFormat()
            throws Exception {

        CanvasLayout dl = createDeviceLayout();
        Pane format = new Pane(dl); 
        format.setName("add-format-pane"); // this actually adds the Pane to the dl!
 
        assertNotNull(dl.formatScope);

        Format retrieved = dl.retrieveFormat(format.getName(),
                                                 format.getFormatType());
        assertSame(format, retrieved);
    }

    /**
     * This method tests the method public void removeFormat ( Format )
     * for the com.volantis.mcs.layouts.FormatRegister class.
     */
    public void testRemoveFormat()
            throws Exception {

        CanvasLayout dl = createDeviceLayout();
        Pane format = new Pane(dl);         
        format.setName("remove-format-pane");

        assertNotNull(dl.removeFormat(format));
        assertNull(dl.retrieveFormat(format.getName(),
                                         format.getFormatType()));
    }

    /**
     * This method tests the method public Format retrieveFormat (Class,String)
     * for the com.volantis.mcs.layouts.FormatRegister class.
     */
    public void testRetrieveFormat()
            throws Exception {

        CanvasLayout dl = createDeviceLayout();
        Pane format = new Pane(dl);         
        format.setName("retrieve-format-pane");

        Format retrieved = dl.retrieveFormat(format.getName(),
                                                 format.getFormatType());
        assertSame(format, retrieved);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/2	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Feb-04	3060/3	philws	VBM:2004021701 Correct accidental rename

 18-Feb-04	3060/1	philws	VBM:2004021701 Implement runtime device repository accessor

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 ===========================================================================
*/
