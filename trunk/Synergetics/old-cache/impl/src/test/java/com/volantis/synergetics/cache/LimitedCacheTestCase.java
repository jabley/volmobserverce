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
 * $Header: /src/voyager/tests/t2002080702/TestLimitedPolicyCache.java,v 1.2 2002/08/07 16:47:36 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Aug-02    Allan           VBM:2002080702 - A JUnit testcase for
 *                              LimitedCache. The creation of this 
 *                              testcase was brought about by this vbm hence
 *                              the relationship. Currently this testcase only
 *                              tests the case described in the vbm.
 * 07-May-02    Allan           VBM:2003050605 - Rescued from no-mans land and
 *                              moved into Synergetics.
 * 20-May-03    Adrian          VBM:2003051901 - Refactored existing test to 
 *                              remove need for member fields and setup. Added 
 *                              tests for LeastUsed and LeastRecentlyUsed cache
 *                              use and pruning. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.cache;

import com.volantis.synergetics.BooleanWrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.ConcurrentModificationException;

/**
 * A JUnit TestCase for the LimitedCache class. This test case is likely to be
 * moved into a different package in the future.
 */
public class LimitedCacheTestCase extends GenericCacheTestAbstract {

    /**
     * Create a LimitedCache with an arbitrary limit and strategy that uses
     * hard references.
     *
     * @return A LimitedCache.
     */
    protected GenericCache createTestableGenericCache() {
        return new LimitedCache(null, 1000, new LeastUsedCacheStrategy());
    }


    /**
     * Test that the LimitedCache works as expected when using a least-used
     * pruning strategy.
     *
     * @todo This test is really obsoleate as the put methods are depricated.
     */
    public void testLeastUsedPrunning() {
        LimitedCache cache = (LimitedCache) createTestableGenericCache();
        cache.setMaxEntries(6);
        CacheStrategy strategy =
            CacheStrategyFactory.getCacheStrategy(
                GenericCacheConfiguration.LEAST_USED);
        cache.setCacheStrategy(strategy);
        cache.setFreeupSize(1);

        Object object1 = new String("Object1");
        String key1 = new String("Key1");
        cache.put(key1, object1);

        Object object2 = new String("Object2");
        String key2 = new String("Key2");
        cache.put(key2, object2);

        Object object3 = new String("Object3");
        String key3 = new String("Key3");
        cache.put(key3, object3);

        Object object4 = new String("Object4");
        String key4 = new String("Key4");
        cache.put(key4, object4);

        Object object5 = new String("Object5");
        String key5 = new String("Key5");
        cache.put(key5, object5);
        
        // hit count 3
        cache.get(key1);
        cache.get(key1);
        cache.get(key1);
        
        // hit count 2
        cache.get(key2);
        cache.get(key2);
        
        // hit count 4
        cache.get(key3);
        cache.get(key3);
        cache.get(key3);
        cache.get(key3);
        
        // hit count 1 - this is least so we will expect this entry to be
        // removed on the next new addition.
        cache.get(key4);
        
        // hit count 2
        cache.get(key5);
        cache.get(key5);
        
        // Check we have the correct number of items
        assertEquals("Incorrect number of elements in cache", 5, cache.size());
        
        // Check that the original items are still all in the cache...
        assertSame("Object1 missing from cache.", object1, cache.get(key1));
        assertSame("Object2 missing from cache.", object2, cache.get(key2));
        assertSame("Object3 missing from cache.", object3, cache.get(key3));
        assertSame("Object4 missing from cache.", object4, cache.get(key4));
        assertSame("Object5 missing from cache.", object5, cache.get(key5));
        
        
        // Now add a new Object.
        Object object6 = new String("Object6");
        String key6 = new String("Key6");
        cache.put(key6, object6);
        cache.get(key6);
        
        // Check we still have the correct number of items
        assertEquals("Incorrect number of elements in cache", 5, cache.size());

        assertSame("Object1 missing from cache.", object1, cache.get(key1));
        assertSame("Object2 missing from cache.", object2, cache.get(key2));
        assertSame("Object3 missing from cache.", object3, cache.get(key3));
        assertSame("Object5 missing from cache.", object5, cache.get(key5));
        assertSame("Object6 missing from cache.", object6, cache.get(key6));
        
        // Check that object4 is really gone...
        assertNull("Object4 should be removed.", cache.get(key4));
    }

    /**
     * Test that the LimitedCache works as expected when using a
     * least-recently-used pruning strategy.
     */
    public void testLeastRecentlyUsedPrunning()
        throws InvocationTargetException, InterruptedException {
        LimitedCache cache = (LimitedCache) createTestableGenericCache();
        cache.setMaxEntries(6);
        CacheStrategy strategy =
            CacheStrategyFactory.
            getCacheStrategy(GenericCacheConfiguration.LEAST_RECENTLY_USED);
        cache.setCacheStrategy(strategy);
        cache.setFreeupSize(1);

        Object object1 = new String("Object1");
        String key1 = new String("Key1");
        cache.put(key1, object1);

        Object object2 = new String("Object2");
        String key2 = new String("Key2");
        cache.put(key2, object2);

        Object object3 = new String("Object3");
        String key3 = new String("Key3");
        cache.put(key3, object3);

        Object object4 = new String("Object4");
        String key4 = new String("Key4");
        cache.put(key4, object4);

        Object object5 = new String("Object5");
        String key5 = new String("Key5");
        cache.put(key5, object5);
        
        // Check we have the correct number of items
        assertEquals("Incorrect number of elements in cache", 5, cache.size());
        
        // Check that the original items are still all in the cache...
        // The act of looking at the items changes the most recently accessed
        // times.  We are trying to lose object4 so we will check that first.
        try {
            // The cache clock counts in seconds sounds we need to wait a
            // second between additions to ensure that this test will work.
            Thread.sleep(1000);
            assertSame("Object4 missing from cache.", object4,
                       cache.get(key4));
            Thread.sleep(1000);
            assertSame("Object1 missing from cache.", object1,
                       cache.get(key1));
            Thread.sleep(1000);
            assertSame("Object2 missing from cache.", object2,
                       cache.get(key2));
            Thread.sleep(1000);
            assertSame("Object3 missing from cache.", object3,
                       cache.get(key3));
            Thread.sleep(1000);
            assertSame("Object5 missing from cache.", object5,
                       cache.get(key5));
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
        }
        
        // Now add a new Object.
        Object object6 = new String("Object6");
        String key6 = new String("Key6");
        cache.put(key6, object6);
        cache.get(key6);
        
        // Check we still have the correct number of items
        assertEquals("Incorrect number of elements in cache", 5, cache.size());

        assertSame("Object1 missing from cache.", object1, cache.get(key1));
        assertSame("Object2 missing from cache.", object2, cache.get(key2));
        assertSame("Object3 missing from cache.", object3, cache.get(key3));
        assertSame("Object5 missing from cache.", object5, cache.get(key5));
        assertSame("Object6 missing from cache.", object6, cache.get(key6));
        
        // Check that object4 is really gone...
        assertNull("Object4 should be removed.", cache.get(key4));
    }

    /**
     * Tests pruning the cache while gc'ing. This is done by a thread that runs
     * perpetually adding new objects to the cache. Meanwhile, this method
     * calls System.gc() every 100th of a second 500 times. This tests takes a
     * minute or two to complete when successful.
     */
    public void testPruningAndGC() {
        LimitedCache cache = (LimitedCache) createTestableGenericCache();
        cache.setMaxEntries(10000);

        // Test with least-recently-used strategy
        CacheStrategy strategy =
            CacheStrategyFactory.
            getCacheStrategy(GenericCacheConfiguration.LEAST_RECENTLY_USED);
        cache.setCacheStrategy(strategy);

        doTestPruningAndGC(cache);
        
        // Test with least-used strategy
        strategy =
            CacheStrategyFactory.getCacheStrategy(
                GenericCacheConfiguration.LEAST_USED);
        cache.setCacheStrategy(strategy);
        doTestPruningAndGC(cache);
    }

    /**
     * Implementation for testPruningAndGC()
     */
    public void doTestPruningAndGC(final GenericCache cache) {

        final BooleanWrapper stop = new BooleanWrapper(false);
        final BooleanWrapper concurrentModificationExceptionThrown =
            new BooleanWrapper(false);

        Thread fillCache = new Thread("Cache Filler") {
            public void run() {
                int count = 0;
                while (!stop.getValue() &&
                    !concurrentModificationExceptionThrown.getValue()) {
                    try {
                        cache.put(new Integer(count), "An Object");
                        count++;
                        // The following sleep allows other threads
                        // to be scheduled. Without this, this test
                        // causes an OutOfMemoryError (tested on IBM JDK).
                        sleep(1);
                    } catch (ConcurrentModificationException e) {
                        concurrentModificationExceptionThrown.setValue(true);
                        throw e;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        fillCache.start();

        for (int i = 0; i < 100 &&
            !concurrentModificationExceptionThrown.getValue(); i++) {
            System.gc();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stop.setValue(true);

        assertTrue(!concurrentModificationExceptionThrown.getValue());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 25-Jun-04	259/1	claire	VBM:2004060803 Refactored location of cache config related constants

 05-Sep-03	53/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored - update internal variables, logging and comments

 07-Aug-03	42/1	allan	VBM:2003080502 Add timeToLive, do some refactoring, make Clock more accurate

 ===========================================================================
*/
