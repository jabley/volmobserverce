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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/servlet/CookieManagerTestCase.java,v 1.1 2003/03/10 14:22:22 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Mar-03    Steve           VBM:2003021101 - Cookie support for a session.
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.servlet;

import junit.framework.*;

public class CookieManagerTestCase extends TestCase
{
    
    public CookieManagerTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    /** Test of getCookieStore method, of class com.volantis.mcs.servlet.CookieManager. */
    public void testGetCookieStore()
    {
        CookieManager manager = new CookieManager();
        assertNull( manager.getCookieStore( "wibble" ) );
        manager.createCookieStore( "wibble" );
        assertNotNull( manager.getCookieStore( "wibble" ) );
    }
    
    /** Test of createCookieStore method, of class com.volantis.mcs.servlet.CookieManager. */
    public void testCreateCookieStore()
    {
        CookieManager manager = new CookieManager();
        manager.createCookieStore( "wibble" );
        assertNotNull( manager.getCookieStore( "wibble" ) );
    }
    
    /** Test of removeCookieStore method, of class com.volantis.mcs.servlet.CookieManager. */
    public void testRemoveCookieStore()
    {
        CookieManager manager = new CookieManager();
        manager.createCookieStore( "wibble" );
        assertNotNull( manager.getCookieStore( "wibble" ) );
        manager.removeCookieStore( "wibble" );
        assertNull( manager.getCookieStore( "wibble" ) );
    }
    
    /** Test of setCookieStoreEnabled method, of class com.volantis.mcs.servlet.CookieManager. */
    public void testSetCookieStoreEnabled()
    {
        CookieManager manager = new CookieManager();
        assertNull( manager.getCookieStore( "wibble" ) );

        manager.setCookieStoreEnabled( "wibble", true );
        assertNotNull( manager.getCookieStore( "wibble" ) );
        
        manager.setCookieStoreEnabled( "wibble", false );
        assertNull( manager.getCookieStore( "wibble" ) );
    }
    
    /** Test of isCookieStoreEnabled method, of class com.volantis.mcs.servlet.CookieManager. */
    public void testIsCookieStoreEnabled()
    {
        CookieManager manager = new CookieManager();
        assertEquals( false, manager.isCookieStoreEnabled( "wibble" ) );

        manager.setCookieStoreEnabled( "wibble", true );
        assertEquals( true, manager.isCookieStoreEnabled( "wibble" ) );
        
        manager.setCookieStoreEnabled( "wibble", false );
        assertEquals( false, manager.isCookieStoreEnabled( "wibble" ) );
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

 30-Jun-03	617/1	steve	VBM:2003061908 Repackage httpclient

 ===========================================================================
*/
