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
package com.volantis.synergetics.cache;

import junit.framework.TestCase;

import java.lang.reflect.InvocationTargetException;

/**
 * Generic testcase for caches.
 */
public abstract class GenericCacheTestAbstract extends TestCase {

    /**
     * Create a testable GenericCache.
     */
    protected abstract GenericCache createTestableGenericCache();

    /**
     * test that the cache "says" it is empty after a clear. the size method of
     * Generic cache returns only the approximate size of the real cache but a
     * simple single thread test like this should return 0 after a call to
     * clear().
     */
    public void testClear() {
        GenericCache cache = createTestableGenericCache();

        Object value = new Object();
        Object key = new Object();
        cache.put(key, value);
        assertEquals("Should be one entry in the cache", 1, cache.size());
        cache.clear();
        assertEquals("Should be no entry in the cache", 0, cache.size());

    }

    /**
     * Test a timed out object.
     */
    public void testGetTimedOut() throws InvocationTargetException,
        InterruptedException {
        // This minumum timeout value is 1 second since that is the
        // default clock interval and the ManagedCache uses the default.
        GenericCache cache = createTestableGenericCache();
        cache.setTimeout(1);
        Object value = new Object();
        Object key = new Object();

        cache.put(key, value);
        assertSame(value, cache.get(key));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNull("Object should have timed out", cache.get(key));
    }

    /**
     * Test a non-timed out object.
     */
    public void testGetNonTimedOut() throws InvocationTargetException,
        InterruptedException {
        // This minumum timeout value is 1 second since that is the
        // default clock interval and the ManagedCache uses the default.
        GenericCache cache = createTestableGenericCache();
        cache.setTimeout(10);
        Object value = new Object();
        Object key = new Object();

        cache.put(key, value);
        assertSame(value, cache.get(key));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertSame("Object should still be there after 1 second",
                   value, cache.get(key));
    }

    /**
     * Test timeToLive
     */
    public void testTimeToLive() throws InvocationTargetException,
        InterruptedException {
        GenericCache cache = createTestableGenericCache();
        Object o1 = new Object();
        Object k1 = new Object();
        Object o2 = new Object();
        Object k2 = new Object();

        cache.put(k1, o1, 7);
        cache.put(k2, o2, 2);

        assertEquals("Cache size check", 2, cache.size());

        try {
            // Wait 3 seconds.
            Thread.sleep(3000);

            assertNull("k2 object should have timed out", cache.get(k2));

            // note that even though the object has timed out it still exists
            // in the cache. The previous test is a special case where put
            // methods are being used to populate the cache. To produce similar
            // behaviour to the old cache the Callable objects used to populate
            // the cache return null after they have timed out. Correct use of
            // the cache (using proper Callable objects would just cause the
            // object to be refreshed).
            assertEquals("Cache size check", 2, cache.size());

            // Wait 5 more seconds
            Thread.sleep(5000);

            // this will return null but only because it is using the
            // DirectValueDataAccessor which returns null after its reset
            // method is called
            assertNull("k1 object should have timed out", cache.get(k1));

            // See comment above
            assertEquals("Cache size check", 2, cache.size());

            cache.clear();
            assertEquals("Cache should be empty", 0, cache.size());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	379/5	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 07-Aug-03	42/1	allan	VBM:2003080502 Add timeToLive, do some refactoring, make Clock more accurate

 ===========================================================================
*/
