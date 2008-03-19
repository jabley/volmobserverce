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
package com.volantis.mcs.layouts;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A testcase for {@link FormatNamespace}.
 */ 
public class FormatNamespaceTestCase
        extends TestCaseAbstract {

    /**
     * This method tests the method public int getIndex()
     */
    public void testIndex() {

        assertEquals(0, FormatNamespace.PANE.getIndex());
        assertEquals(1, FormatNamespace.FORM.getIndex());
        assertEquals(2, FormatNamespace.FORM_FRAGMENT.getIndex());
        assertEquals(3, FormatNamespace.FRAGMENT.getIndex());
        assertEquals(4, FormatNamespace.GRID.getIndex());
        assertEquals(5, FormatNamespace.REGION.getIndex());
        assertEquals(6, FormatNamespace.REPLICA.getIndex());
        assertEquals(7, FormatNamespace.SEGMENT.getIndex());
        assertEquals(8, FormatNamespace.SEGMENT_GRID.getIndex());
        assertEquals(9, FormatNamespace.SPATIAL_FORMAT_ITERATOR.getIndex());
        assertEquals(10, FormatNamespace.TEMPORAL_FORMAT_ITERATOR.getIndex());
        // todo: add EMPTY in here once we figure out what it should be. 

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

 ===========================================================================
*/
