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
 * $Header: /src/voyager/testsuite/unit/com/volantis/testtools/marinerurl/MarinerURLRegistryTestCase.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 Test case for MarinerURLRegistry
 * 12-May-03    Steve           VBM:2003021815 Added java doc to test cases 
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.testurl;

import java.io.*;

import junit.framework.*;

public class TestURLRegistryTestCase extends TestCase {
    
    public TestURLRegistryTestCase( String name ) {
        super( name );
    }

    /**
     * Test that calls to getInstance return the same object
     * @throws Exception
     */
    public void testSingleton() throws Exception {
        assertSame( TestURLRegistry.getInstance(), 
                    TestURLRegistry.getInstance() );
    }

    /**
     * Test that registration of streams works
     * @throws Exception
     */
    public void testRegister() throws Exception {
        InputStream is1 = new ByteArrayInputStream( "".getBytes() );
        InputStream is2 = new ByteArrayInputStream( "foo".getBytes() );
        TestURLRegistry.register( "is1", is1 );
        TestURLRegistry.register( "is2", is2 );
        assertSame( is1, 
                    TestURLRegistry.getInstance().getInputStream( "is1" ) );
        assertSame( is2, 
                    TestURLRegistry.getInstance().getInputStream( "is2" ) );
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 06-Aug-03	956/4	geoff	VBM:2003080601 finally fix bodgy marinerurlregistry

 ===========================================================================
*/
