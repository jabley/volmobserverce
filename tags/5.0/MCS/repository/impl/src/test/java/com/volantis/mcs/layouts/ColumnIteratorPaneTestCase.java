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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/ColumnIteratorPaneTestCase.java,v 1.3 2003/04/01 16:35:09 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Feb-03    Allan           VBM:2003021803 - A testcase for 
 *                              ColumnIteratorPane. 
 * 25-Mar-02    Allan           VBM:2003021803 - Removed suite() and main().
 * 01-Apr-03    Chris W         VBM:2003031106 - Changed class hierarchy of
 *                              test cases to be same as layouts.  
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;


/**
 * This class unit test the Formatclass.
 */
public class ColumnIteratorPaneTestCase 
    extends IteratorPaneTestAbstract {


    // javadoc inherited
    protected Format createTestableFormat() {
        return new ColumnIteratorPane(new CanvasLayout());
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

 ===========================================================================
*/