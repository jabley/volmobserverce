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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/GridTestCase.java,v 1.5 2003/04/01 16:35:09 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Feb-03    Allan           VBM:2003021803 - A testcase for Grid. 
 * 04-Mar-03    Allan           VBM:2003021802 - Modified 
 *                              testHashCodeNonEqual... methods to use 
 *                              ObjectTestHelper. 
 * 25-Mar-03    Allan           VBM:2003021803 - Removed suite() and main(). 
 * 01-Apr-03    Chris W         VBM:2003031106 - Override testGetChildAt() as
 *                              createTestableFormat() creates a Grid with more
 *                              than one child. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 * This class unit test the Format class.
 */
public class GridTestCase 
    extends AbstractGridTestAbstract {

    // javadoc inherited
    protected Format createTestableFormat() {
        Grid grid = new Grid(new CanvasLayout());

        setupGrid(grid);
        
        return grid;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8992/2	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 21-Feb-05	7037/3	pcameron	VBM:2005021704 Width units default to percent if not present

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
