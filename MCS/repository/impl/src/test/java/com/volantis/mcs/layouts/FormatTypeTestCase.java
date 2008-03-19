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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/FormatTypeTestCase.java,v 1.2 2003/03/11 12:42:56 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Dec-02    Allan           VBM:2002120615 - Testcase for FormatType.
 * 11-Mar-03    Allan           VBM:2003010303 - Removed all references to 
 *                              DeviceLayoutFormat since this no longer has a 
 *                              FormatType. Updated testFormatCount to use 14 
 *                              instead of 15. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class unit test the FormatTypeclass.
 */
public class FormatTypeTestCase
        extends TestCase {

    /**
     * This method tests the method public int getNamespace()
     * for the com.volantis.mcs.layouts.FormatType class.
     */
    public void testGetNamespace()
            throws Exception {

        // Check the individual types have the correct namespaces.
        assertEquals(FormatNamespace.PANE, 
                FormatType.COLUMN_ITERATOR_PANE.getNamespace());
        assertEquals(FormatNamespace.PANE, 
                FormatType.DISSECTING_PANE.getNamespace());
        assertEquals(FormatNamespace.FORM, 
                FormatType.FORM.getNamespace());
        assertEquals(FormatNamespace.FORM_FRAGMENT,
                FormatType.FORM_FRAGMENT.getNamespace());
        assertEquals(FormatNamespace.FRAGMENT,
                FormatType.FRAGMENT.getNamespace());
        assertEquals(FormatNamespace.GRID,
                FormatType.GRID.getNamespace());
        assertEquals(FormatNamespace.PANE,
                FormatType.PANE.getNamespace());
        assertEquals(FormatNamespace.REGION,
                FormatType.REGION.getNamespace());
        assertEquals(FormatNamespace.REPLICA,
                FormatType.REPLICA.getNamespace());
        assertEquals(FormatNamespace.PANE,
                FormatType.ROW_ITERATOR_PANE.getNamespace());
        assertEquals(FormatNamespace.SEGMENT,
                FormatType.SEGMENT.getNamespace());
        assertEquals(FormatNamespace.SEGMENT_GRID,
                FormatType.SEGMENT_GRID.getNamespace());
        assertEquals(FormatNamespace.SPATIAL_FORMAT_ITERATOR,
                FormatType.SPATIAL_FORMAT_ITERATOR.getNamespace());
        assertEquals(FormatNamespace.TEMPORAL_FORMAT_ITERATOR,
                FormatType.TEMPORAL_FORMAT_ITERATOR.getNamespace());

        // Check that all the Panes have the same namespace.
        // This is a belt and braces test ;-).
        assertEquals(FormatType.PANE.getNamespace(),
                FormatType.COLUMN_ITERATOR_PANE.getNamespace());
        assertEquals(FormatType.PANE.getNamespace(),
                FormatType.DISSECTING_PANE.getNamespace());
        assertEquals(FormatType.PANE.getNamespace(),
                FormatType.ROW_ITERATOR_PANE.getNamespace());

    }

    /**
     * This method tests the method public String getTypeName ( )
     * for the com.volantis.mcs.layouts.FormatType class.
     */
    public void testGetTypeName()
            throws Exception {
        assertEquals("ColumnIteratorPane",
                     FormatType.COLUMN_ITERATOR_PANE.getTypeName());
        assertEquals("DissectingPane",
                     FormatType.DISSECTING_PANE.getTypeName());
        assertEquals("Form",
                     FormatType.FORM.getTypeName());
        assertEquals("FormFragment",
                     FormatType.FORM_FRAGMENT.getTypeName());
        assertEquals("Fragment",
                     FormatType.FRAGMENT.getTypeName());
        assertEquals("Grid",
                     FormatType.GRID.getTypeName());
        assertEquals("Pane",
                     FormatType.PANE.getTypeName());
        assertEquals("Region",
                     FormatType.REGION.getTypeName());
        assertEquals("Replica",
                     FormatType.REPLICA.getTypeName());
        assertEquals("RowIteratorPane",
                     FormatType.ROW_ITERATOR_PANE.getTypeName());
        assertEquals("Segment",
                     FormatType.SEGMENT.getTypeName());
        assertEquals("SegmentGrid",
                     FormatType.SEGMENT_GRID.getTypeName());
        assertEquals("SpatialFormatIterator",
                     FormatType.SPATIAL_FORMAT_ITERATOR.getTypeName());
        assertEquals("TemporalFormatIterator",
                     FormatType.TEMPORAL_FORMAT_ITERATOR.getTypeName());

    }

    /**
     * Ensure the format type count constant is manintained correctly.
     */ 
    public void testFormatTypeCount() {
        assertEquals(14, FormatType.FORMAT_TYPE_COUNT);
    }
    
    /**
     * Test the iterator method.
     */ 
    public void testIterator() {
        ArrayList formatTypes = new ArrayList(FormatType.FORMAT_TYPE_COUNT);

        formatTypes.add(FormatType.COLUMN_ITERATOR_PANE);
        formatTypes.add(FormatType.DISSECTING_PANE);
        formatTypes.add(FormatType.FORM);
        formatTypes.add(FormatType.FORM_FRAGMENT);
        formatTypes.add(FormatType.FRAGMENT);
        formatTypes.add(FormatType.GRID);
        formatTypes.add(FormatType.PANE);
        formatTypes.add(FormatType.REGION);
        formatTypes.add(FormatType.REPLICA);
        formatTypes.add(FormatType.ROW_ITERATOR_PANE);
        formatTypes.add(FormatType.SEGMENT);
        formatTypes.add(FormatType.SEGMENT_GRID);
        formatTypes.add(FormatType.SPATIAL_FORMAT_ITERATOR);
        formatTypes.add(FormatType.TEMPORAL_FORMAT_ITERATOR);
            
        // The following check can be replaced with assert in jdk1.4.
        assertEquals(FormatType.FORMAT_TYPE_COUNT, formatTypes.size());

        Iterator iterator = FormatType.iterator();
        while(iterator.hasNext()) {
            Object next = iterator.next();
            assertTrue("Failed to find FormatType " + next,
                       formatTypes.remove(next));            
        }
        
        assertEquals("Expected iterator.size() of 0 but was " + 
                   formatTypes.size(), 0, formatTypes.size());
    }    
    
    public FormatTypeTestCase(String name) {
        super(name);
    }

    public void testGetElementName() throws Exception {
        assertEquals("columnIteratorPaneFormat",
                     FormatType.COLUMN_ITERATOR_PANE.getElementName());
        assertEquals("dissectingPaneFormat",
                     FormatType.DISSECTING_PANE.getElementName());
        assertEquals("formFormat",
                     FormatType.FORM.getElementName());
        assertEquals("formFragmentFormat",
                     FormatType.FORM_FRAGMENT.getElementName());
        assertEquals("fragmentFormat",
                     FormatType.FRAGMENT.getElementName());
        assertEquals("gridFormat",
                     FormatType.GRID.getElementName());
        assertEquals("paneFormat",
                     FormatType.PANE.getElementName());
        assertEquals("regionFormat",
                     FormatType.REGION.getElementName());
        assertEquals("replicaFormat",
                     FormatType.REPLICA.getElementName());
        assertEquals("rowIteratorPaneFormat",
                     FormatType.ROW_ITERATOR_PANE.getElementName());
        assertEquals("segmentFormat",
                     FormatType.SEGMENT.getElementName());
        assertEquals("segmentGridFormat",
                     FormatType.SEGMENT_GRID.getElementName());
        assertEquals("spatialFormatIterator",
                     FormatType.SPATIAL_FORMAT_ITERATOR.getElementName());
        assertEquals("temporalFormatIterator",
                     FormatType.TEMPORAL_FORMAT_ITERATOR.getElementName());
        assertEquals("emptyFormat",
                     FormatType.EMPTY.getElementName());
    }

    public void testGetStructure() throws Exception {
        assertSame(FormatType.Structure.LEAF,
                   FormatType.COLUMN_ITERATOR_PANE.getStructure());
        assertSame(FormatType.Structure.LEAF,
                   FormatType.DISSECTING_PANE.getStructure());
        assertSame(FormatType.Structure.SIMPLE_CONTAINER,
                   FormatType.FORM.getStructure());
        assertSame(FormatType.Structure.SIMPLE_CONTAINER,
                   FormatType.FORM_FRAGMENT.getStructure());
        assertSame(FormatType.Structure.SIMPLE_CONTAINER,
                   FormatType.FRAGMENT.getStructure());
        assertSame(FormatType.Structure.GRID,
                   FormatType.GRID.getStructure());
        assertSame(FormatType.Structure.LEAF,
                   FormatType.PANE.getStructure());
        assertSame(FormatType.Structure.LEAF,
                   FormatType.REGION.getStructure());
        assertSame(FormatType.Structure.LEAF,
                   FormatType.REPLICA.getStructure());
        assertSame(FormatType.Structure.LEAF,
                   FormatType.ROW_ITERATOR_PANE.getStructure());
        assertSame(FormatType.Structure.LEAF,
                   FormatType.SEGMENT.getStructure());
        assertSame(FormatType.Structure.GRID,
                   FormatType.SEGMENT_GRID.getStructure());
        assertSame(FormatType.Structure.SIMPLE_CONTAINER,
                   FormatType.SPATIAL_FORMAT_ITERATOR.getStructure());
        assertSame(FormatType.Structure.SIMPLE_CONTAINER,
                   FormatType.TEMPORAL_FORMAT_ITERATOR.getStructure());
        assertSame(FormatType.Structure.LEAF,
                   FormatType.EMPTY.getStructure());
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

 21-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
