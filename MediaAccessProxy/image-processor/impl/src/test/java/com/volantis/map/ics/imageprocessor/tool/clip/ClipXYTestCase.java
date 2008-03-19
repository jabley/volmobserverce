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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.tool.clip;

import junit.framework.TestCase;

public class ClipXYTestCase extends TestCase {

    public void testMain() {
        // just a simple test to make sure X is not mixed with Y
        ClipXY clipper = new ClipXY(100, 25, 74, 200, 50, 149);
        clipper.clipTo(100, 200);
        assertEquals(0, clipper.getX().getClipOffset());
        assertEquals(0, clipper.getY().getClipOffset());
        assertEquals(100, clipper.getX().getClipSize());
        assertEquals(200, clipper.getY().getClipSize());

        clipper.clipTo(10, 10);
        assertEquals(25, clipper.getX().getClipOffset());
        assertEquals(50, clipper.getY().getClipOffset());
        assertEquals(50, clipper.getX().getClipSize());
        assertEquals(100, clipper.getY().getClipSize());

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Nov-05	554/3	pszul	VBM:2005102504 intendation corrected to 4 spaces
 
 04-Nov-05	554/1	pszul	VBM:2005102504 intelligent clipping implemented
 
 ===========================================================================
 */
