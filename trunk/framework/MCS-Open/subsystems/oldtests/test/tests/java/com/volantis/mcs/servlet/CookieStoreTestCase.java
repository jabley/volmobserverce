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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/servlet/CookieStoreTestCase.java,v 1.1 2003/03/10 14:22:22 sfound Exp $
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
import our.apache.commons.httpclient.Cookie;
import our.apache.commons.httpclient.cookie.MalformedCookieException;
import our.apache.commons.httpclient.cookie.RFC2109Spec;

public class CookieStoreTestCase extends TestCase
{
    
    public CookieStoreTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    /** Test of addCookies method, of class com.volantis.mcs.servlet.CookieStore. */
    public void testAddCookies() throws MalformedCookieException {
        CookieStore store = new CookieStore();
        
        RFC2109Spec spec = new RFC2109Spec();
        Cookie[] c = spec.parse( "www.roadrunner.com", 80, "/acme/login", 
                    false, 
                    "Customer=\"WILE_E_COYOTE\"; Version=\"1\"; " + 
                    "Path=\"/acme\"" );
        store.addCookies(c);
        assertEquals( 1, store.getCookies( 
                        "www.roadrunner.com", 80, "/acme/pickitem" ).length );
    }
    
    /** Test of getCookies method, of class com.volantis.mcs.servlet.CookieStore. */
    public void testGetCookies() throws MalformedCookieException {
            
        Cookie[] cookies;
        Cookie[] sentCookies;
        
        RFC2109Spec spec = new RFC2109Spec();
        
        CookieStore store = new CookieStore();
        // POST /acme/login HTTP/1.1 - User identifies self via a form
        cookies = spec.parse( "www.roadrunner.com", 80, "/acme/login", false, 
                    "Customer=\"WILE_E_COYOTE\"; Version=\"1\"; " + 
                    "Path=\"/acme\"" );
        store.addCookies( cookies );

        // User chooses an item. Should send the Customer cookie
        // POST /acme/pickitem HTTP/1.1 - User selects item
        sentCookies = store.getCookies( "www.roadrunner.com", 80, 
                                     "/acme/pickitem" );
        assertEquals( 1, sentCookies.length );
        assertEquals( "Customer", sentCookies[0].getName() );
        assertEquals( "WILE_E_COYOTE", sentCookies[0].getValue() );
        assertEquals( "/acme", sentCookies[0].getPath() );
        
        cookies = spec.parse( "www.roadrunner.com", 80, "/acme/pickitem", false,
                "Part_Number=\"Rocket_Launcher_0001\"; Version=\"1\"; " +
                "Path=\"/acme\"" ); 
        store.addCookies( cookies );
        
        // User selects ammo for the chosen item
        // POST /acme/pickitem/parts HTTP/1.1 - User selects parts
        sentCookies = store.getCookies( "www.roadrunner.com", 80, 
                                     "/acme/pickitem/ammo" );
        assertEquals( 2, sentCookies.length );
        assertEquals( "Customer", sentCookies[0].getName() );
        assertEquals( "WILE_E_COYOTE", sentCookies[0].getValue() );
        assertEquals( "/acme", sentCookies[0].getPath() );
        assertEquals( "Part_Number", sentCookies[1].getName() );
        assertEquals( "Rocket_Launcher_0001", sentCookies[1].getValue() );
        assertEquals( "/acme", sentCookies[1].getPath() );

        cookies = spec.parse( "www.roadrunner.com", 80, "/acme/pickitem/ammo", 
                false, "Part_Number=\"Riding_Rocket_0023\"; Version=\"1\"; " +
                "Path=\"/acme/pickitem\"" ); 
        store.addCookies( cookies );

        // This is a test for the path on the ammo cookie. Because it is
        // longer, it should be sent ahead of the customer and launcher
        // cookies. It will not be sent in the next request when we go
        // back 'above' the ammo path.
        sentCookies = store.getCookies( "www.roadrunner.com", 80, 
                                     "/acme/pickitem/ammo" );
        assertEquals( 3, sentCookies.length );
        assertEquals( "Part_Number", sentCookies[0].getName() );
        assertEquals( "Riding_Rocket_0023", sentCookies[0].getValue() );
        assertEquals( "/acme/pickitem", sentCookies[0].getPath() );
        assertEquals( "Customer", sentCookies[1].getName() );
        assertEquals( "WILE_E_COYOTE", sentCookies[1].getValue() );
        assertEquals( "/acme", sentCookies[1].getPath() );
        assertEquals( "Part_Number", sentCookies[2].getName() );
        assertEquals( "Rocket_Launcher_0001", sentCookies[2].getValue() );
        assertEquals( "/acme", sentCookies[2].getPath() );
        
        
        // User selects shipping method from a form. Should send both the
        // Customer and part number cookies.
        // POST /acme/shipping HTTP/1.1 - User selects shipping method 
        sentCookies = store.getCookies( "www.roadrunner.com", 80, 
                                     "/acme/shipping" );
        assertEquals( 2, sentCookies.length );
        assertEquals( "Customer", sentCookies[0].getName() );
        assertEquals( "WILE_E_COYOTE", sentCookies[0].getValue() );
        assertEquals( "/acme", sentCookies[0].getPath() );
        assertEquals( "Part_Number", sentCookies[1].getName() );
        assertEquals( "Rocket_Launcher_0001", sentCookies[1].getValue() );
        assertEquals( "/acme", sentCookies[1].getPath() );
        
        cookies = spec.parse( "www.roadrunner.com", 80, "/acme/shipping", false, 
                "Shipping=\"FedEx\"; Version=\"1\"; Path=\"/acme\"" );
        store.addCookies(cookies);
        
        // User chooses to process the order. Should send the Customer, part 
        // number and shpping cookies.
        // POST /acme/process HTTP/1.1 - User selects process
        sentCookies = store.getCookies( "www.roadrunner.com", 80, 
                                     "/acme/process" );
        assertEquals( 3, sentCookies.length );
        assertEquals( "Customer", sentCookies[0].getName() );
        assertEquals( "WILE_E_COYOTE", sentCookies[0].getValue() );
        assertEquals( "/acme", sentCookies[0].getPath() );
        assertEquals( "Part_Number", sentCookies[1].getName() );
        assertEquals( "Rocket_Launcher_0001", sentCookies[1].getValue() );
        assertEquals( "/acme", sentCookies[1].getPath() );
        assertEquals( "Shipping", sentCookies[2].getName() );
        assertEquals( "FedEx", sentCookies[2].getValue() );
        assertEquals( "/acme", sentCookies[2].getPath() );
    }

    private Cookie createHttpCookie( 
                        String name, String value, String domain, String path )
    {
        Cookie cookie = new Cookie( domain, name, value );
        cookie.setPath( path );
        cookie.setPathAttributeSpecified( true );
        return cookie;
    }
    
    private javax.servlet.http.Cookie createServletCookie(
                        String name, String value, String domain, String path )
    {
        javax.servlet.http.Cookie cookie = 
                        new javax.servlet.http.Cookie( name, value );
        cookie.setDomain( domain );
        cookie.setPath( path );
        return cookie;
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
