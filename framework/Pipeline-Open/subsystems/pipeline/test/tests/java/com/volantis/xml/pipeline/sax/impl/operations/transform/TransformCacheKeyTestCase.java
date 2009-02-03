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

package com.volantis.xml.pipeline.sax.impl.operations.transform;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.net.URL;
import java.net.MalformedURLException;

/**
 * This class is the key used when storing or retrieving cached templates
 */
public class TransformCacheKeyTestCase extends TestCaseAbstract {

    /**
     * The instance of the class being tested
     */
    protected TransformCacheKey key;

    /**
     * The URL being used for the test case - no real xml here!
     */
    protected String location = "http://www.volantis.com/";

    /**
     * Default JUnit constructor
     * @param name
     */
    public TransformCacheKeyTestCase(String name) {
        super(name);
    }

    /**
     * Utility method to create key instances for testing purposes.
     *
     * @param compilable Whether the template represted by the key is compiled
     * @param url The location of the template that the key created represents
     * @return The template key used for caching
     */
    public TransformCacheKey createKey(boolean compilable, String url) {
        return new TransformCacheKey(compilable, url);
    }

    /**
     * Tests the retrieval of the values the class was instantiated with.
     */
    public void testGetValues() {
        key = createKey(false, location);
        assertFalse("Should be uncompiled", key.getCompilable());
        assertEquals("Should be volantis URL", key.getLocation(), location);
    }

    /**
     * Tests the equality method of the key.
     */
    public void testEquality() {
        key = createKey(false, location);
        TransformCacheKey testKey = createKey(false, location);
        assertEquals("Keys should be the same", key, testKey);

        // Test other combinations...
        String url = "http://wwww.bbc.co.uk/";
        TransformCacheKey keyOne = createKey(true, url);
        TransformCacheKey keyTwo = createKey(false, url);
        assertNotEquals(keyOne, keyTwo);
        keyTwo = createKey(true, url);
        assertEquals("Keys should be the same", keyOne, keyTwo);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 26-Jan-04	551/5	claire	VBM:2004012204 Caching clean-up

 26-Jan-04	551/3	claire	VBM:2004012204 Fixed and optimised caching code

 26-Jan-04	551/1	claire	VBM:2004012204 Implementing caching for transforms

 ===========================================================================
*/
