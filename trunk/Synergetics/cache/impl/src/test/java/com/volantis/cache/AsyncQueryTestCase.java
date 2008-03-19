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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.cache;

import com.volantis.cache.impl.CacheBuilderImpl;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Class for testing asynchronous query capabilities of the cache.
 */
public class AsyncQueryTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that an asynchronous update of the cache works correctly.
     */
    public void testAsyncUpdate() throws Exception {

        CacheBuilder builder = new CacheBuilderImpl();
        builder.setMaxCount(10);

        Cache cache = builder.buildCache();

        AsyncResult async = cache.asyncQuery("key", Period.inMilliSeconds(50));
        Object value;
        if (async.isReady()) {
            value = async.getValue();
        } else {
            Throwable throwable = null;
            boolean failed = true;
            try {
                CacheEntry entry = async.getEntry();
                boolean cacheable;
                if (entry == null) {
                    // Previous result was not cached so this code could
                    // be run by multiple threads so just get the value.
                    value = "uncacheable value";
                    cacheable = false;
                } else {
                    // This thread is responsible for updating the entry, all
                    // other threads requesting the content with the same key
                    // is blocked waiting for this to update the entry.
                    Object oldValue = entry.getValue();
                    if (oldValue == null) {
                        // The entry is new so just get the value.
                        value = "value";
                    } else {
                        // The entry has expired and needs updating
                        value = "updated " + oldValue;
                    }
                    cacheable = true;
                }

                ProviderResult result = new ProviderResult(value,
                        cache.getRootGroup(), cacheable, null);
                async.update(result);
                failed = false;
            } catch (RuntimeException e) {
                throwable = e;

                throw e;
            } catch (Error e) {
                throwable = e;

                throw e;
            } finally {
                if (failed) {
                    async.failed(throwable);
                }
            }
        }

        assertEquals(value, "value");
    }

    public void testAsyncResultFinalize()
        throws Exception {

        CacheBuilder builder = new CacheBuilderImpl();
        builder.setMaxCount(10);

        Cache cache = builder.buildCache();
        Period period = Period.inMilliSeconds(5000);

        Runnable runnable = new FinalizeRunnable(cache, period);

        Thread[] threads = new Thread[2];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnable, "Thread " + i);
        }

        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            thread.start();
        }

        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            thread.join();
        }

        threads = null;
        runnable = null;
        cache = null;
        System.gc();
    }

    private static class FinalizeRunnable implements Runnable {
        private final Cache cache;
        private final Period period;

        public FinalizeRunnable(Cache cache, Period period) {
            this.cache = cache;
            this.period = period;
        }

        public void run() {

            try {
            AsyncResult result = cache.asyncQuery(
                    "xyz", period);
            if (!result.isReady()) {
                result = null;
                System.gc();
            }
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
