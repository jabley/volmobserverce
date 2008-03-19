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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/themes/StyleLengthTestCase.java,v 1.2 2003/03/19 09:52:58 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Mar-03    Byron           VBM:2003031105 - Created to test the pixels
 *                              method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes;

import com.volantis.mcs.themes.values.LengthUnit;
import junit.framework.TestCase;

/**
 * This class tests the StyleLength class.
 */
public class StyleLengthTestCase extends TestCase {

    /**
     * The constructor for this test case
     */
    public StyleLengthTestCase(String name) {
        super(name);
    }
    /**
     * Test the pixels method.
     */
    public void testPixels() throws Exception {
        StyleLength length;

        length = StyleValueFactory.getDefaultInstance().getLength(
            null, 1.5, LengthUnit.PT);
        try {
            assertEquals(0, length.pixels());
            fail("Illegal state exception should've been thrown");
        } catch (IllegalStateException e) {
        }

        length = StyleValueFactory.getDefaultInstance().getLength(
            null, 1.5, LengthUnit.PX);
        assertEquals(2, length.pixels());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
