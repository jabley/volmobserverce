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

import com.volantis.servlet.HttpServletRequestStub;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.cookies.CookieImpl;


public class DefaultHttpServletEnvironmentFactoryTestCase
        extends HttpServletEnvironmentFactoryTestAbstract {

    /**
     * Instance of the class being tested
     */
    private DefaultHttpServletEnvironmentFactory factory;

    /**
     * Creates a new DefaultHttpServletEnvironmentFactoryTestCase instance
     * @param name the name of the test
     */
    public DefaultHttpServletEnvironmentFactoryTestCase(String name) {
        super(name);
    }

    // javadoc ineherited
    protected void setUp() throws Exception {
        super.setUp();
        factory = new DefaultHttpServletEnvironmentFactory();
    }

    // javadoc ineherited
    protected void tearDown() throws Exception {
        super.tearDown();
        factory = null;
    }

    /**
     * tests the createEnvironmentInteraction method
     * @throws Exception if an error occurs
     */
    public void testCreateEnvironmentInteraction() throws Exception {
        // factor an EnvironmentInteraction
        EnvironmentInteraction interaction =
                factory.createEnvironmentInteraction(null,
                                                     null,
                                                     null,
                                                     null,
                                                     null);

        assertEquals("createEnvironmentInteraction should return a " +
                     "HttpServletEnvironmentInteractionImpl instance",
                     HttpServletEnvironmentInteractionImpl.class,
                     interaction.getClass());
    }

    /**
     * tests the reateCookies method
     * @throws Exception if an error occurs
     */
    public void testCreateCookies() throws Exception {
        HTTPMessageEntities entities = factory.createCookies();
        // enusre that a SimpleHTTPMessageEntities is factored
        assertEquals("createCookies should return a " +
                     "SimpleHTTPMessageEntities instance",
                     SimpleHTTPMessageEntities.class,
                     entities.getClass());

        // ensure the HTTPMessageEntities has not been populated
        assertEquals("createCookies should return an empty " +
                     "HTTPMessageEntities",
                     0, entities.size());
    }

    /**
     * tests the createCookies(HttpServletRequest) method
     * @throws Exception if an error occurs
     */
    public void testCreateCookiesFromHttpServletRequest() throws Exception {
        HttpServletRequestStub request = new HttpServletRequestStub();
        javax.servlet.http.Cookie[] cookies = new javax.servlet.http.Cookie[2];
        cookies[0] = new javax.servlet.http.Cookie("One", "One");
        cookies[1] = new javax.servlet.http.Cookie("Two", "Two");
        request.setCookies(cookies);

        HTTPMessageEntities entities = factory.createCookies(request);

        Cookie cookieArray [] = new Cookie[2];
        cookieArray[0] = new CookieImpl("One", null, null);
        cookieArray[1] = new CookieImpl("Two", null, null);

        // enusre that a SimpleHTTPMessageEntities is factored
        assertEquals("createCookies should return a " +
                     "SimpleHTTPMessageEntities instance",
                     SimpleHTTPMessageEntities.class,
                     entities.getClass());

        // ensure the HTTPMessageEntities has been populated with 2
        // cookies
        assertEquals("entities should have a size of 2",
                     2, entities.size());

        HTTPMessageEntity[] entity =
                entities.remove(cookieArray[0].getIdentity());
        entity = entities.remove(cookieArray[1].getIdentity());

        // ensure the HTTPMessageEntities only contained the two cookies
        assertEquals("entities should now be empty ", 0, entities.size());

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	217/6	allan	VBM:2003071702 Ensure correct array types created. Add our-commons-logging to build.

 31-Jul-03	217/3	allan	VBM:2003071702 Ensure correct array types created. Add our-commons-logging to build.

 31-Jul-03	217/1	allan	VBM:2003071702 Ensure correct array types created. Add our-commons-logging to build.

 31-Jul-03	271/1	doug	VBM:2003073002 Implemented various environment fatories

 ===========================================================================
*/
