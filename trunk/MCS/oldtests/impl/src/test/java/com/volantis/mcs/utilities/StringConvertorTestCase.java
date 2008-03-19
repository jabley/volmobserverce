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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/utilities/StringConvertorTestCase.java,v 1.3 2003/04/16 10:23:22 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-Jan-03    Steve           VBM:2002121208 - Test case for number 
 *                              to string conversions.
 * 17-Apr-03    Geoff           VBM:2003041505 - Commented out System.out 
 *                              calls which clutter the JUnit console output.
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.utilities;

import com.volantis.mcs.utilities.StringConvertor;
import junit.framework.*;

public class StringConvertorTestCase extends TestCase
{
    
    public StringConvertorTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    /** Test of valueOf method */
    public void testValueOf()
    {
        //System.out.println("testValueOf");

        int boundary = StringConvertor.DEFAULT_INT_ARRAY_SIZE;
        String vlu;
        int i;
        
        for( i = -( boundary + 1 ); i < -( boundary - 1 ); i++ ) {
            vlu = StringConvertor.valueOf( i );
            assertEquals( String.valueOf( i ), vlu );
        }
        for( i = boundary - 1; i < boundary + 1; i++ ) {
            vlu = StringConvertor.valueOf( i );
            assertEquals( String.valueOf( i ), vlu );
        }
        assertEquals( "0", StringConvertor.valueOf( 0 ) );
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
