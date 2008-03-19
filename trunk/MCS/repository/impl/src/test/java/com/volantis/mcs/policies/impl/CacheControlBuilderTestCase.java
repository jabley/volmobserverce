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

package com.volantis.mcs.policies.impl;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.InternalCacheControlBuilder;

/**
 * Test cases for {@link CacheControlBuilder}.
 */
public class CacheControlBuilderTestCase
        extends TestCaseAbstract {

    /**
     * Make sure that the three methods related to cacheThisPolicy property
     * work correctly.
     */
    public void testCacheThisPolicy()
            throws Exception {

        InternalCacheControlBuilder builder = new CacheControlBuilderImpl();

        // After creation it should have the default value and marked as not
        // set.
        assertEquals(false, builder.getCacheThisPolicy());
        assertFalse(builder.cacheThisPolicyWasSet());

        // Set it to a non default value should mark it as not set.
        builder.setCacheThisPolicy(true);
        assertEquals(true, builder.getCacheThisPolicy());
        assertTrue(builder.cacheThisPolicyWasSet());

        // Clearing should set it back to the same state as creation.
        builder.clearCacheThisPolicy();
        assertEquals(false, builder.getCacheThisPolicy());
        assertFalse(builder.cacheThisPolicyWasSet());

        // Set it to the default value should mark it as not set.
        builder.setCacheThisPolicy(false);
        assertEquals(false, builder.getCacheThisPolicy());
        assertTrue(builder.cacheThisPolicyWasSet());
    }

    /**
     * Make sure that the three methods related to retainDuringRetry property
     * work correctly.
     */
    public void testRetainDuringRetry()
            throws Exception {

        InternalCacheControlBuilder builder = new CacheControlBuilderImpl();

        // After creation it should have the default value and marked as not
        // set.
        assertEquals(false, builder.getRetainDuringRetry());
        assertFalse(builder.retainDuringRetryWasSet());

        // Set it to a non default value should mark it as not set.
        builder.setRetainDuringRetry(true);
        assertEquals(true, builder.getRetainDuringRetry());
        assertTrue(builder.retainDuringRetryWasSet());

        // Clearing should set it back to the same state as creation.
        builder.clearRetainDuringRetry();
        assertEquals(false, builder.getRetainDuringRetry());
        assertFalse(builder.retainDuringRetryWasSet());

        // Set it to the default value should mark it as not set.
        builder.setRetainDuringRetry(false);
        assertEquals(false, builder.getRetainDuringRetry());
        assertTrue(builder.retainDuringRetryWasSet());
    }

    /**
     * Make sure that the three methods related to retryFailedRetrieval property
     * work correctly.
     */
    public void testRetryFailedRetrieval()
            throws Exception {

        InternalCacheControlBuilder builder = new CacheControlBuilderImpl();

        // After creation it should have the default value and marked as not
        // set.
        assertEquals(false, builder.getRetryFailedRetrieval());
        assertFalse(builder.retryFailedRetrievalWasSet());

        // Set it to a non default value should mark it as not set.
        builder.setRetryFailedRetrieval(true);
        assertEquals(true, builder.getRetryFailedRetrieval());
        assertTrue(builder.retryFailedRetrievalWasSet());

        // Clearing should set it back to the same state as creation.
        builder.clearRetryFailedRetrieval();
        assertEquals(false, builder.getRetryFailedRetrieval());
        assertFalse(builder.retryFailedRetrievalWasSet());

        // Set it to the default value should mark it as not set.
        builder.setRetryFailedRetrieval(false);
        assertEquals(false, builder.getRetryFailedRetrieval());
        assertTrue(builder.retryFailedRetrievalWasSet());
    }

    /**
     * Make sure that the three methods related to retryInterval property
     * work correctly.
     */
    public void testRetryInterval()
            throws Exception {

        InternalCacheControlBuilder builder = new CacheControlBuilderImpl();

        // After creation it should have the default value and marked as not
        // set.
        assertEquals(0, builder.getRetryInterval());
        assertFalse(builder.retryIntervalWasSet());

        // Set it to a non default value should mark it as not set.
        builder.setRetryInterval(10);
        assertEquals(10, builder.getRetryInterval());
        assertTrue(builder.retryIntervalWasSet());

        // Clearing should set it back to the same state as creation.
        builder.clearRetryInterval();
        assertEquals(0, builder.getRetryInterval());
        assertFalse(builder.retryIntervalWasSet());

        // Set it to the default value should mark it as not set.
        builder.setRetryInterval(0);
        assertEquals(0, builder.getRetryInterval());
        assertTrue(builder.retryIntervalWasSet());
    }

    /**
     * Make sure that the three methods related to retryMaxCount property
     * work correctly.
     */
    public void testRetryMaxCount()
            throws Exception {

        InternalCacheControlBuilder builder = new CacheControlBuilderImpl();

        // After creation it should have the default value and marked as not
        // set.
        assertEquals(0, builder.getRetryMaxCount());
        assertFalse(builder.retryMaxCountWasSet());

        // Set it to a non default value should mark it as not set.
        builder.setRetryMaxCount(10);
        assertEquals(10, builder.getRetryMaxCount());
        assertTrue(builder.retryMaxCountWasSet());

        // Clearing should set it back to the same state as creation.
        builder.clearRetryMaxCount();
        assertEquals(0, builder.getRetryMaxCount());
        assertFalse(builder.retryMaxCountWasSet());

        // Set it to the default value should mark it as not set.
        builder.setRetryMaxCount(0);
        assertEquals(0, builder.getRetryMaxCount());
        assertTrue(builder.retryMaxCountWasSet());
    }

    /**
     * Make sure that the three methods related to timeToLive property
     * work correctly.
     */
    public void testTimeToLive()
            throws Exception {

        InternalCacheControlBuilder builder = new CacheControlBuilderImpl();

        // After creation it should have the default value and marked as not
        // set.
        assertEquals(0, builder.getTimeToLive());
        assertFalse(builder.timeToLiveWasSet());

        // Set it to a non default value should mark it as not set.
        builder.setTimeToLive(10);
        assertEquals(10, builder.getTimeToLive());
        assertTrue(builder.timeToLiveWasSet());

        // Clearing should set it back to the same state as creation.
        builder.clearTimeToLive();
        assertEquals(0, builder.getTimeToLive());
        assertFalse(builder.timeToLiveWasSet());

        // Set it to the default value should mark it as not set.
        builder.setTimeToLive(0);
        assertEquals(0, builder.getTimeToLive());
        assertTrue(builder.timeToLiveWasSet());
    }
}
