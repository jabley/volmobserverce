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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/FormatTypeMapperTestCase.java,v 1.2 2003/03/11 12:42:56 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Dec-02    Allan           VBM:2002121017 - A testcase for
 *                              FormatTypeMapper.
 * 11-Mar-03    Allan           VBM:2003010303 - Removed all references to 
 *                              DeviceLayoutFormat since this no longer has a 
 *                              FormatType. Updated 
 *                              testTypeStringToFormatTypeMap to assert 14 
 *                              instead of 15 for the size of 
 *                              typeStringToFormatType. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import junit.framework.TestCase;

import java.util.Map;

public class FormatTypeMapperTestCase
        extends TestCase {
    
    /**
     * Test that the typeStringToFormatType map is set up correctly.
     */
    public void testTypeStringToFormatTypeMap() {
        assertEquals(14, FormatTypeMapper.typeStringToFormatType.size());

        Map map = FormatTypeMapper.typeStringToFormatType;
        FormatType type;

        type = (FormatType) map.get("ColumnIteratorPane");
        assertEquals(FormatType.COLUMN_ITERATOR_PANE, type);

        type = (FormatType) map.get("DissectingPane");
        assertEquals(FormatType.DISSECTING_PANE, type);

        type = (FormatType) map.get("Form");
        assertEquals(FormatType.FORM, type);

        type = (FormatType) map.get("FormFragment");
        assertEquals(FormatType.FORM_FRAGMENT, type);

        type = (FormatType) map.get("Fragment");
        assertEquals(FormatType.FRAGMENT, type);

        type = (FormatType) map.get("Grid");
        assertEquals(FormatType.GRID, type);

        type = (FormatType) map.get("Pane");
        assertEquals(FormatType.PANE, type);

        type = (FormatType) map.get("Region");
        assertEquals(FormatType.REGION, type);

        type = (FormatType) map.get("Replica");
        assertEquals(FormatType.REPLICA, type);

        type = (FormatType) map.get("RowIteratorPane");
        assertEquals(FormatType.ROW_ITERATOR_PANE, type);

        type = (FormatType) map.get("Segment");
        assertEquals(FormatType.SEGMENT, type);

        type = (FormatType) map.get("SegmentGrid");
        assertEquals(FormatType.SEGMENT_GRID, type);

        type = (FormatType) map.get("SpatialFormatIterator");
        assertEquals(FormatType.SPATIAL_FORMAT_ITERATOR, type);

        type = (FormatType) map.get("TemporalFormatIterator");
        assertEquals(FormatType.TEMPORAL_FORMAT_ITERATOR, type);
    }


    /**
     * Construct a new FormatTypeMapperTestCase.
     * @param name
     */
    public FormatTypeMapperTestCase(String name) {
        super(name);
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

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
