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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/FormatReferenceTestCase.java,v 1.2 2002/12/05 12:33:58 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Dec-2002  Chris W         VBM:2002111102 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import junit.framework.TestCase;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.FormatReference;

/**
 * This class test the FormatReference class
 */
public class FormatReferenceTestCase extends TestCase
{
    private FormatReference formatReference;

    /**
     * Constructor for FormatReferenceTestCase.
     * @param name
     */
    public FormatReferenceTestCase(String name)
    {
        super(name);
    }

    public void setUp()
    {
        formatReference = new FormatReference();
    }
    
    public void tearDown()
    {
        formatReference = null;
    }

    public void testGetInstance()
    {
        NDimensionalIndex index = new NDimensionalIndex(new int[] {0, 1});
        formatReference = new FormatReference("stem", index);                            
        assertEquals("wrong fir returned", index, formatReference.getIndex());
    }

    public void testGetStem()
    {
        NDimensionalIndex index = new NDimensionalIndex(new int[] {0, 1});
        formatReference = new FormatReference("stem", index);
        assertEquals("wrong stem returned", "stem", formatReference.getStem());
    }

    public void testSetStem()
    {
        NDimensionalIndex index = new NDimensionalIndex(new int[] {0, 1});
        formatReference = new FormatReference("stem", index);
        formatReference.setStem("different");
        assertEquals("wrong stem returned", "different", formatReference.getStem());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
