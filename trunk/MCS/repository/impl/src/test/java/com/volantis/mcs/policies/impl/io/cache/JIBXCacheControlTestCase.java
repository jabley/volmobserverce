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

package com.volantis.mcs.policies.impl.io.cache;

import com.volantis.mcs.policies.impl.io.JIBXTestAbstract;
import com.volantis.mcs.policies.InternalCacheControlBuilder;
import com.volantis.mcs.policies.impl.io.JIBXTestAbstract;

/**
 * Test cases ensure that properties for cache control builder round trip
 * properly if they are not set, set to the default value for the type, or
 * set to a non default value.
 *
 * <p>The reason these tests are needed is because the behaviour is different
 * whether a property is not set, or it is set to its default.</p>
 */
public class JIBXCacheControlTestCase
        extends JIBXTestAbstract {

    /**
     * Ensure that data round trips when none of the properties are set.
     */
    public void testNoneSet() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, false, // retryInterval
                0, false, // retryMaxCount
                0, false // timeToLive
        );
    }

    /**
     * Ensure that data round trips when cacheThisPolicy is set to the default
     * value.
     */
    public void testCacheThisPolicyDefault() throws Exception {
        checkBuilder(
                false, true, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, false, // retryInterval
                0, false, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when cacheThisPolicy is set to a non
     * default value.
     */
    public void testCacheThisPolicyNonDefault() throws Exception {
        checkBuilder(
                true, true, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, false, // retryInterval
                0, false, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when retainDuringRetry is set to the default
     * value.
     */
    public void testRetainDuringRetryDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, true, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, false, // retryInterval
                0, false, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when retainDuringRetry is set to a non
     * default value.
     */
    public void testRetainDuringRetryNonDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                true, true, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, false, // retryInterval
                0, false, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when retryFailedRetrieval is set to the default
     * value.
     */
    public void testRetryFailedRetrievalDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, true, // retryFailedRetrieval
                0, false, // retryInterval
                0, false, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when retryFailedRetrieval is set to a non
     * default value.
     */
    public void testRetryFailedRetrievalNonDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, false, // retainDuringRetry
                true, true, // retryFailedRetrieval
                0, false, // retryInterval
                0, false, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when retryInterval is set to the default
     * value.
     */
    public void testRetryIntervalDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, true, // retryInterval
                0, false, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when retryInterval is set to a non
     * default value.
     */
    public void testRetryIntervalNonDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, false, // retryFailedRetrieval
                10, true, // retryInterval
                0, false, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when retryMaxCount is set to the default
     * value.
     */
    public void testRetryMaxCountDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, false, // retryInterval
                0, true, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when retryMaxCount is set to a non
     * default value.
     */
    public void testRetryMaxCountNonDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, false, // retryInterval
                3, true, // retryMaxCount
                0, false  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when timeToLive is set to the default
     * value.
     */
    public void testTimeToLiveDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, false, // retryInterval
                0, false, // retryMaxCount
                0, true  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when timeToLive is set to a non
     * default value.
     */
    public void testTimeToLiveNonDefault() throws Exception {
        checkBuilder(
                false, false, // cacheThisPolicy
                false, false, // retainDuringRetry
                false, false, // retryFailedRetrieval
                0, false, // retryInterval
                0, false, // retryMaxCount
                99, true  // timeToLive
        );
    }

    /**
     * Ensure that data round trips when all of the properties are set.
     */
    public void testAllSet() throws Exception {
        checkBuilder(
                true, true, // cacheThisPolicy
                true, true, // retainDuringRetry
                false, true, // retryFailedRetrieval
                10, true, // retryInterval
                5, true, // retryMaxCount
                100, true // timeToLive
        );
    }

    private InternalCacheControlBuilder doRoundTripResource()
            throws Exception {

        String resource = getName() + ".xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(resource);
        return (InternalCacheControlBuilder) doRoundTrip(sourceXML, null);
    }

    private void checkBuilder(
            final boolean expectedCacheThisPolicy,
            final boolean expectedCacheThisPolicyWasSet,
            final boolean expectedRetainDuringRetry,
            final boolean expectedRetainDuringRetryWasSet,
            final boolean expectedRetryFailedRetrieval,
            final boolean expectedRetryFailedRetrievalWasSet,
            final int expectedRetryInterval,
            final boolean expectedRetryIntervalWasSet,
            final int expectedRetryMaxCount,
            final boolean expectedRetryMaxCountWasSet,
            final int expectedTimeToLive,
            final boolean expectedTimeToLiveWasSet) throws Exception {

        InternalCacheControlBuilder builder = doRoundTripResource();

        assertEquals("cacheThisPolicy", expectedCacheThisPolicy,
                builder.getCacheThisPolicy());
        assertEquals("cacheThisPolicyWasSet", expectedCacheThisPolicyWasSet,
                builder.cacheThisPolicyWasSet());
        assertEquals("retainDuringRetry", expectedRetainDuringRetry,
                builder.getRetainDuringRetry());
        assertEquals("retainDuringRetryWasSet", expectedRetainDuringRetryWasSet,
                builder.retainDuringRetryWasSet());
        assertEquals("retryFailedRetrieval", expectedRetryFailedRetrieval,
                builder.getRetryFailedRetrieval());
        assertEquals("retryFailedRetrievalWasSet", expectedRetryFailedRetrievalWasSet,
                builder.retryFailedRetrievalWasSet());
        assertEquals("retryInterval", expectedRetryInterval,
                builder.getRetryInterval());
        assertEquals("retryIntervalWasSet", expectedRetryIntervalWasSet,
                builder.retryIntervalWasSet());
        assertEquals("retryMaxCount", expectedRetryMaxCount,
                builder.getRetryMaxCount());
        assertEquals("retryMaxCountWasSet", expectedRetryMaxCountWasSet,
                builder.retryMaxCountWasSet());
        assertEquals("timeToLive", expectedTimeToLive, builder.getTimeToLive());
        assertEquals("timeToLiveWasSet", expectedTimeToLiveWasSet,
                builder.timeToLiveWasSet());
    }
}
