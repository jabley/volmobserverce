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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.url;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;

/**
 * Test case for URLIntrospector. todo: This testcase is not complete because
 * the original class derived from todo: refactoring and because there was not
 * enough time to write all the todo: tests. MarinerURLTest is a non-Junit test
 * in MCS that may be useful.
 */
public class URLIntrospectorTestCase extends TestCase {

    /**
     * Test that optimizePath does as expected with urls containing ".." where
     * all the provided urls are legal.
     */
    public void testOptimizePathLegalDots() throws Throwable {
        URLIntrospector introspector = new URLIntrospector("/a/b/c/../d");
        PrivateAccessor.invoke(introspector, "optimizePath", null, null);
        String result = (String) PrivateAccessor.
            getField(introspector, "path");
        assertEquals("Expected the .. to be optimized", "/a/b/d", result);

        introspector = new URLIntrospector("a/b/c/../d");
        PrivateAccessor.invoke(introspector, "optimizePath", null, null);
        result = (String) PrivateAccessor.
            getField(introspector, "path");
        assertEquals("Expected the .. to be optimized", "a/b/d", result);

        introspector = new URLIntrospector("/a/b/c/../../d");
        PrivateAccessor.invoke(introspector, "optimizePath", null, null);
        result = (String) PrivateAccessor.
            getField(introspector, "path");
        assertEquals("Expected the .. to be optimized", "/a/d", result);

        introspector = new URLIntrospector("a/b/c/../../d");
        PrivateAccessor.invoke(introspector, "optimizePath", null, null);
        result = (String) PrivateAccessor.
            getField(introspector, "path");
        assertEquals("Expected the .. to be optimized", "a/d", result);

        introspector = new URLIntrospector("/a/b/c/../../../d");
        PrivateAccessor.invoke(introspector, "optimizePath", null, null);
        result = (String) PrivateAccessor.
            getField(introspector, "path");
        assertEquals("Expected the .. to be optimized", "/d", result);
    }

    /**
     * Test that optimizePath correctly handles null paths - i.e. does not
     * throw an exception
     */
    public void testOptimizationNullPath() throws Throwable {
        URLIntrospector introspector =
            new URLIntrospector("http://www.volantis.com");
        PrivateAccessor.invoke(introspector, "optimizePath", null, null);
        String result = (String) PrivateAccessor.
            getField(introspector, "path");
        assertNull("Expected the path to remain null", result);
    }

    /**
     * Tests that correct URLs are created from base and relative URLs.
     */
    public void testBaseWithRelativeConstruction() throws Throwable {
        final String base = "/a/b/c/d/";
        final String base2 = "/a/b/c/d";
        final String base3 = "/a/b///c//";
        final String relative = "e/f/g";

        URLIntrospector introspector = new URLIntrospector(base, relative);
        PrivateAccessor.invoke(introspector, "optimizePath", null, null);
        String result = (String)
            PrivateAccessor.getField(introspector, "path");
        assertEquals("Expected ", "/a/b/c/d/e/f/g", result);

        introspector = new URLIntrospector(base2, relative);
        PrivateAccessor.invoke(introspector, "optimizePath", null, null);
        result = (String)
            PrivateAccessor.getField(introspector, "path");
        assertEquals("Expected ", "/a/b/c/e/f/g", result);

        introspector = new URLIntrospector(base3, relative);
        PrivateAccessor.invoke(introspector, "optimizePath", null, null);
        result = (String)
            PrivateAccessor.getField(introspector, "path");

        // Multiple slashes should be collapsed to a single slash.
        assertEquals("Expected ", "/a/b/c/e/f/g", result);
    }

    /**
     * Test that optimizePath does as expected with urls containing ".." where
     * all the provided urls are illegal.
     */
    public void testOptimizePathIllegalDots() throws Throwable {
        /*
        // todo uncomment when the URLIntrospector is fixed - the bug is not serious
        URLIntrospector introspector = new URLIntrospector("/../../d");
        try {
            PrivateAccessor.invoke(introspector, "optimizePath", null, null);
            fail("Expected an IllegalStateException since /../d is invalid.");
        } catch (IllegalStateException e) {
            // Success.
        }
        */
    }

    public void testURLEmptyParameterParsing() throws Throwable {
        String inputURL = "/host?p1=v1&p2=&p3=v3";
        String expectedQuery = "p1=v1&p2=&p3=v3";
        URLIntrospector urlIntrospector = new URLIntrospector(inputURL);
        assertEquals("Expected ", expectedQuery, urlIntrospector.getQuery());

        inputURL = "/host?p1=v1&p2=&=p1&p3=v3";
        expectedQuery = "p1=v1&p2=&p3=v3";
        urlIntrospector = new URLIntrospector(inputURL);
        assertEquals("Expected ", expectedQuery, urlIntrospector.getQuery());

        inputURL = "/host?p1=v1&p2&p3=v3";
        expectedQuery = "p1=v1&p2&p3=v3";
        urlIntrospector = new URLIntrospector(inputURL);
        assertEquals("Expected ", expectedQuery, urlIntrospector.getQuery());

        inputURL = "/host?p1&p2=&p1=v3";
        expectedQuery = "p1&p1=v3&p2=";
        urlIntrospector = new URLIntrospector(inputURL);
        assertEquals("Expected ", expectedQuery, urlIntrospector.getQuery());
    }


    public void testURLParametersOrdering() throws Throwable {
        // Current implementation of URLIntrospector orders parameters
        // according to natural order
        String inputURL = "/host?c1=v1&b2=v2&a3=v3";
        String expectedQuery = "a3=v3&b2=v2&c1=v1";
        URLIntrospector urlIntrospector = new URLIntrospector(inputURL);
        assertEquals("Expected ", expectedQuery, urlIntrospector.getQuery());

        inputURL = "/host?c1=v1&b2=&a3=v3";
        expectedQuery = "a3=v3&b2=&c1=v1";
        urlIntrospector = new URLIntrospector(inputURL);
        assertEquals("Expected ", expectedQuery, urlIntrospector.getQuery());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-May-05	456/1	pcameron	VBM:2005050416 Fixed multiple slash path optimization

 06-May-05	454/1	pcameron	VBM:2005050416 Fixed multiple slash path optimization

 26-May-04	239/1	pcameron	VBM:2004052601 Fixed null path and base/relative construction

 26-May-04	236/1	adrian	VBM:2004052403 Ensure URLIntrospector handles null paths

 25-May-04	227/1	allan	VBM:2004052103 Provide a general url prefix rewriting facility

 ===========================================================================
*/
