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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.net.http.cookies.CookieImpl;
import com.volantis.shared.net.http.cookies.Cookie;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * This class is responsible for testing the behaviour of
 * {@link CookieJar}.
 */
public class CookieJarTestCase extends TestCaseAbstract  {

    /**
     * Helpful constant to use when setting the age of a cookie.
     */
    private static final int COOKIE_AGE = 1;

    /**
     * Constant for the max age property of a cookie that has expired.
     */
    private static final int COOKIE_EXPIRED = 0;

    /**
     * The value of the maz age value set for session cookies
     * (ie cookies that should not persist once the browser is closed).
     */
    private static final int AGE_OF_SESSION_COOKIE = -1;

    /**
     * Test a single cookie can be added to a cookie jar
     */
    public void testAddSingleCookie() {

        CookieJar cookieJar = new CookieJar();

        String name = "myCookie";
        String path = "/";
        String domain = ".mysite.com";
        String value = "1";

        Cookie cookie = createCookie(name, domain, path, value, COOKIE_AGE);

        cookieJar.addCookie(cookie);

        int expectedSize = 1;
        assertTrue(expectedSize == cookieJar.size());

        // Check that the cookie has been stored
        Collection cookies = toCollection(cookieJar.iterator());
        assertTrue(cookies.contains(cookie));

    }

    /**
     * Test a single session cookie can be added to a cookie jar
     */
    public void testAddSessionCookie() {

        CookieJar cookieJar = new CookieJar();

        String name = "JSESSIONID";
        String path = "/";
        String domain = ".mysite.com";
        String value = "1";

        Cookie cookie = createCookie(name, domain, path, value,
                                     AGE_OF_SESSION_COOKIE);

        cookieJar.addCookie(cookie);

        int expectedSize = 1;
        assertTrue(expectedSize == cookieJar.size());

        // Check that the cookie has been stored
        Collection cookies = toCollection(cookieJar.iterator());
        assertTrue(cookies.contains(cookie));
    }

    /**
     * Test multiple cookies can be added to the cookie jar.
     */
    public void testAddMultipleCookies() {

        CookieJar cookieJar = new CookieJar();

        Cookie cookie1 = createCookie("cookie1", ".mysite.com", "/", "1",
                                      COOKIE_AGE);
        Cookie cookie2 = createCookie("cookie2", ".mysite.com", "/", "2",
                                      COOKIE_AGE);
        Cookie cookie3 = createCookie("cookie3", ".mysite.com", "/", "3",
                                      COOKIE_AGE);
        Cookie cookie4 = createCookie("cookie4", ".mysite.com", "/", "4",
                                      COOKIE_AGE);

        cookieJar.addCookie(cookie1);
        cookieJar.addCookie(cookie2);
        cookieJar.addCookie(cookie3);
        cookieJar.addCookie(cookie4);

        int expectedSize = 4;
        assertTrue(expectedSize == cookieJar.size());

        // Check that the cookie has been stored
        Collection cookies = toCollection(cookieJar.iterator());
        assertTrue(cookies.contains(cookie1));
        assertTrue(cookies.contains(cookie2));
        assertTrue(cookies.contains(cookie3));
        assertTrue(cookies.contains(cookie4));
    }

    /**
     * Test cookie over-riding in the cookie jar.
     */
    public void
            testAddingCookieWithIdenticalNamePathAndDomainOfStoredCookie() {

        CookieJar cookieJar = new CookieJar();

        Cookie cookie1 = createCookie("cookie1", ".mysite.com", "/", "1",
                                      COOKIE_AGE);
        Cookie cookie2 = createCookie("cookie1", ".mysite.com", "/", "2",
                                      COOKIE_AGE);

        // The second cookie added should overide the first cookie
        // as per RFC 2109, Section 4.3.3
        cookieJar.addCookie(cookie1);
        cookieJar.addCookie(cookie2);

        int expectedSize = 1;
        assertTrue("Cookie jar size should be 1",
                   expectedSize == cookieJar.size());

        // Check that only the second cookie remains.
        Collection cookies = toCollection(cookieJar.iterator());
        assertTrue(cookies.contains(cookie2));
    }

    /**
     * Test cookies with the same name but different paths do not override
     * each other in the cookie jar
     */
    public void testAddingCookiesWithSameNameButDifferentPaths() {

        CookieJar cookieJar = new CookieJar();

        Cookie cookie1 = createCookie("cookie1", ".mysite.com", "/", "1",
                                      COOKIE_AGE);
        Cookie cookie2 = createCookie("cookie1", ".mysite.com", "/foo", "2",
                                      COOKIE_AGE);

        cookieJar.addCookie(cookie1);
        cookieJar.addCookie(cookie2);

        int expectedSize = 2;
        assertTrue("Cookie jar size should be 2",
                   expectedSize == cookieJar.size());

         // Check that only the second cookie remains.
        Collection cookies = toCollection(cookieJar.iterator());
        assertTrue(cookies.contains(cookie1));
        assertTrue(cookies.contains(cookie2));

    }

    /**
     * Test 0 max age cookies are not kept in the cookie jar.
     */
    public void testAddingCookieWithMaxAgeOfZero() {

        CookieJar cookieJar = new CookieJar();

        Cookie cookie1 = createCookie("cookie1", ".mysite.com", "/", "1",
                                      COOKIE_EXPIRED);
        cookieJar.addCookie(cookie1);

        int expectedSizeOfJar = 0;
        assertEquals("A cookie with a max age of 0 should NOT be added to jar",
                     expectedSizeOfJar, cookieJar.size());
    }

    /**
     * Test the overriding of a cookie with an expired cookie effectively
     * removes the cookie from the cookie jar
     */
    public void
            testAddingExpiredCookieWhenCookieWithSameNamePathAndDomainExists() {


        CookieJar cookieJar = new CookieJar();
        Cookie cookie1 = createCookie("cookie1", ".mysite.com", "/", "1",
                                      COOKIE_AGE);
        cookieJar.addCookie(cookie1);

        Cookie deadCookie = createCookie("cookie1", ".mysite.com", "/", "1",
                                      COOKIE_EXPIRED);

        cookieJar.addCookie(deadCookie);

        int expectedSizeOfJar = 0;
        assertEquals("Cookie jar should be empty",
                     expectedSizeOfJar, cookieJar.size());
    }

    /**
     * Create a Cookie
     * @param name - name of the cookie
     * @param domain - domain of the cookie
     * @param path - path of the cookie
     * @param value - HttpMessageEntity string value
     * @param maxAge - max age of the coookie
     * @return new Cookie                                
     */
    private Cookie createCookie(String name, String domain, String path,
                                String value, int maxAge) {
        CookieImpl cookie = new CookieImpl(name, domain, path);
        cookie.setValue(value);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    /**
     * Converts the given iterator to a collection.
     * @param iter the iterator to be converted to a Collection.
     *
     * @return the collection created from the elements in the supplied
     * iterator.
     */
    private Collection toCollection(Iterator iter) {
        Collection collection = new ArrayList();
        while (iter.hasNext()) {
            collection.add(iter.next());
        }
        return collection;
    }

}
