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

package com.volantis.shared.servlet.http;

import junit.framework.TestCase;
import com.volantis.shared.net.http.cookies.CookieVersion;

/**
 * HTTPServletCookieTestCase
 *
 * @author steve
 *
 */
public class HTTPServletCookieTestCase extends TestCase {
    /**
     * Constructor for HTTPServletCookieTestCase.
     * @param arg0
     */
    public HTTPServletCookieTestCase(String arg0) {
        super(arg0);
    }

    /** Test name constructor */
    public void testHttpCookieString() {
        // The name field should be set and the response cookie should have a value
        HTTPServletCookie c = new HTTPServletCookie( "fred" );
        assertEquals( "fred", c.getName() );
        assertNull( c.getComment() );
        assertNull( c.getDomain() );
        assertNull( c.getPath() );
        assertNotNull( c.getServletCookie() );
        assertEquals( -1, c.getMaxAge() );
        assertNull( c.getValue() );
        assertSame(CookieVersion.NETSCAPE, c.getVersion());
    }

    /** Test name value constructor */
    public void testHttpCookieStringString() {
        // The name and value fields should be set and the
        // response cookie should have a value
        HTTPServletCookie c = new HTTPServletCookie( "fred", null, null);
        c.setValue("wilma");
        assertEquals( "fred", c.getName() );
        assertNull( c.getComment() );
        assertNull( c.getDomain() );
        assertNull( c.getPath() );
        assertNotNull( c.getServletCookie() );
        assertEquals( -1, c.getMaxAge() );
        assertEquals( "wilma", c.getValue() );
        assertSame(CookieVersion.NETSCAPE, c.getVersion());

    }

    /** Test cookie constructor */
    public void testHttpCookieCookie() {
        // The fields should have the same value as the cookie and the returned
        // cookie should be identical to the one passed in
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie( "fred", "wilma" );
        cookie.setDomain( ".volantis.com" );
        cookie.setPath( "/" );
        cookie.setMaxAge( 100 );
        cookie.setVersion(CookieVersion.RFC2109.getNumber());

        HTTPServletCookie c = new HTTPServletCookie( cookie );
        assertEquals( "fred", c.getName() );
        assertNull( c.getComment() );
        assertEquals( ".volantis.com", c.getDomain() );
        assertEquals( "/", c.getPath() );
        assertEquals( cookie, c.getServletCookie() );
        assertEquals( 100, c.getMaxAge() );
        assertEquals( "wilma", c.getValue() );
        assertSame( CookieVersion.RFC2109, c.getVersion());
    }


    /**
     * Create a cookie then get the javax.servlet version of it.
     * If the cookie is created from a javax cookie then the returned cookie
     * should be the same as the one passed in, unless the cookie name is changed.
     */
    public void testGetServletResponseCookie() {

        // Create a name/value cookie
        HTTPServletCookie c = new HTTPServletCookie( "fred", ".volantis.com", "/");
        c.setValue("wilma");
        javax.servlet.http.Cookie cookie = c.getServletCookie();

        // Check that the fields are set
        assertNotNull( cookie );
        assertEquals( "fred", cookie.getName() );
        assertNull( cookie.getComment() );
        assertEquals( ".volantis.com", cookie.getDomain() );
        assertEquals( "/", cookie.getPath() );
        assertEquals( -1, cookie.getMaxAge() );
        assertEquals( "wilma", cookie.getValue() );

        // Create a new cookie from the returned cookie
        HTTPServletCookie c2 = new HTTPServletCookie( cookie );

        // Modify the comment field
        c2.setComment( "I am a comment" );

        // Get the javax cookie
        javax.servlet.http.Cookie cookie2 = c2.getServletCookie();
        // This should be identical to the last cookie
        assertEquals( cookie, cookie2 );
        // Verify the fields
        assertEquals( "fred", cookie2.getName() );
        assertEquals( "I am a comment", cookie2.getComment() );
        assertEquals( ".volantis.com", cookie2.getDomain() );
        assertEquals( "/", cookie2.getPath() );
        assertEquals( -1, cookie2.getMaxAge() );
        assertEquals( "wilma", cookie2.getValue() );
        assertEquals(CookieVersion.NETSCAPE.getNumber(), cookie2.getVersion());

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Mar-05	7337/1	matthew	VBM:2005030809 Add a CookieVersion typesafe enum and use it in the Cookie interface and its implementations

 08-Mar-05	7331/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 08-Mar-05	7284/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 01-Aug-03	217/6	allan	VBM:2003071702 Rename and re-write HttpCookie

 31-Jul-03	217/3	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 28-Jul-03	217/1	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 18-Jul-03	220/1	steve	VBM:2003071705 Renamed mutableCopy to createMutableCopy

 15-Jul-03	197/1	steve	VBM:2003071501 Cookie and environment implementation

 11-Jul-03	181/1	steve	VBM:2003070802 Environment implementation

 ===========================================================================
*/
