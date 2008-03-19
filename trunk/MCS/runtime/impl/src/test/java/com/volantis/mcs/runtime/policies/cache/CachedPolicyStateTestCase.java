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

package com.volantis.mcs.runtime.policies.cache;

import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.runtime.policies.ActivatedPolicyMock;
import com.volantis.shared.system.SystemClockMock;
import com.volantis.shared.time.Time;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

/**
 * Test cases for {@link CachedPolicyState}.
 */
public class CachedPolicyStateTestCase
        extends TestCaseAbstract {
    private SystemClockMock clockMock;
    private ActivatedPolicyMock policyMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        clockMock = new SystemClockMock("clockMock", expectations);

        policyMock = new ActivatedPolicyMock("policyMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The creation time of the state.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1000));

    }

    /**
     * Ensure that when it is created with a valid cache control and not in
     * an error state that it sets the expiration time correctly from the
     * time to live value.
     */
    public void testInitialiseExpirationFromTimeToLive() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(4000));
                clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(5000));
                clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(5999));
                clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(6000));
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheControl cacheControl = createCacheControl(true, true, true, 2, 1,
                5);

        CachedPolicyState state = new CachedPolicyState(clockMock, cacheControl,
                policyMock);

        assertFalse(state.hasExpired(clockMock));
        assertFalse(state.hasExpired(clockMock));
        assertFalse(state.hasExpired(clockMock));
        assertTrue(state.hasExpired(clockMock));
    }

    /**
     * Ensure that when it is created with a valid cache control and not in
     * an error state that it sets the expiration time correctly from an
     * unlimited time to live.
     */
    public void testInitialiseExpirationFromUnlimitedTimeToLive()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheControl cacheControl = createCacheControl(true, true, true, 2, 1,
                0);

        CachedPolicyState state = new CachedPolicyState(clockMock, cacheControl,
                policyMock);

        assertFalse(state.hasExpired(clockMock));
    }

    private CacheControl createCacheControl(
            final boolean cacheThisPolicy, final boolean retainDuringRetry,
            final boolean retryFailedRetrieval,
            final int retryInterval,
            final int retryMaxCount,
            final int timeToLive) {
        PolicyFactory factory = PolicyFactory.getDefaultInstance();
        CacheControlBuilder builder = factory.createCacheControlBuilder();
        builder.setCacheThisPolicy(cacheThisPolicy);
        builder.setRetainDuringRetry(retainDuringRetry);
        builder.setRetryFailedRetrieval(retryFailedRetrieval);
        builder.setRetryInterval(retryInterval);
        builder.setRetryMaxCount(retryMaxCount);
        builder.setTimeToLive(timeToLive);
        CacheControl cacheControl = builder.getCacheControl();
        return cacheControl;
    }

    /**
     * Ensure that when it is created with a valid cache control and not in
     * an error state that it sets the expiration time correctly.
     */
    public void testInitialiseExpirationFromRetryInterval()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(2000));
                clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(2999));
                clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(3000));
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheControl cacheControl = createCacheControl(true, true, true, 2, 1,
                5);

        CachedPolicyState state = new CachedPolicyState(clockMock, cacheControl,
                null);

        assertFalse(state.hasExpired(clockMock));
        assertFalse(state.hasExpired(clockMock));
        assertTrue(state.hasExpired(clockMock));
    }

    /**
     * Ensure that when retry is disabled that retrieveFailed returns null.
     */
    public void testRetrieveFailedWhenRetryDisabled()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheControl cacheControl = createCacheControl(true, false, false, 0, 0,
                5);

        CachedPolicyState state = new CachedPolicyState(clockMock, cacheControl,
                policyMock);

        assertNull(state.retrieveFailed(clockMock, policyMock));
    }

    /**
     * Ensure that when retry interval is 0 that retrieveFailed returns the
     * policy mock.
     */
    public void testRetrieveFailedWhenRetryIntervalZero()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheControl cacheControl = createCacheControl(true, true, true, 0, 2,
                5);

        CachedPolicyState state = new CachedPolicyState(clockMock, cacheControl,
                policyMock);

        // The time used to calculate expiration time after retry.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(2000));

        assertSame(policyMock, state.retrieveFailed(clockMock, policyMock));
    }

    /**
     * Ensure that when retry count is greater than or equal to the maximum
     * that retrieveFailed returns null.
     */
    public void testRetrieveFailedWhenRetryCountExceedsMaximum()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheControl cacheControl = createCacheControl(true, true, true, 2, 0,
                5);

        CachedPolicyState state = new CachedPolicyState(clockMock, cacheControl,
                policyMock);

        assertNull(state.retrieveFailed(clockMock, policyMock));
    }

    /**
     * Ensure that when retry during retry is disabled that retrieveFailed
     * returns null.
     */
    public void testRetrieveFailedWhenRetainDuringRetryDisabled()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheControl cacheControl = createCacheControl(true, false, true, 2, 3,
                5);

        CachedPolicyState state = new CachedPolicyState(clockMock, cacheControl,
                policyMock);

        // The time used to calculate expiration time after retry.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(2000));

        assertNull(state.retrieveFailed(clockMock, policyMock));
    }

    /**
     * Ensure that retrieveFailed correctly retries as many times as it
     * is allowed while attempting to update an existing policy.
     */
    public void testRetrieveFailedWhenUpdatingExistingPolicy()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheControl cacheControl = createCacheControl(true, true, true, 2, 2,
                5);

        CachedPolicyState state = new CachedPolicyState(clockMock, cacheControl,
                policyMock);

        // ---------------------------------------------------------------------
        //   Retrieve Failed
        // ---------------------------------------------------------------------

        // The time used to calculate expiration time after retry.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(2000));

        assertSame(policyMock, state.retrieveFailed(clockMock, policyMock));

        // The time used to check expiration.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(3999));
        assertFalse(state.hasExpired(clockMock));

        // The time used to check expiration.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(4000));
        assertTrue(state.hasExpired(clockMock));

        expectations.verify();

        // ---------------------------------------------------------------------
        //   First Retry Failed
        // ---------------------------------------------------------------------

        // The time used to calculate expiration time after retry.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(5000));

        assertSame(policyMock, state.retrieveFailed(clockMock, policyMock));

        expectations.verify();

        // ---------------------------------------------------------------------
        //   Second Retry Failed
        // ---------------------------------------------------------------------

        // Second retry failed so it has exceeded the maximum retry count so
        // behaves as if policy could not be found.
        assertNull(state.retrieveFailed(clockMock, policyMock));
    }

    /**
     * Ensure that retrieveFailed correctly retries as many times as it
     * is allowed after it failed to retrieve the initial policy.
     */
    public void testRetrieveFailedAfterFailedInitialRetrieval()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheControl cacheControl = createCacheControl(true, true, true, 2, 2,
                5);

        CachedPolicyState state = new CachedPolicyState(clockMock, cacheControl,
                null);

        // ---------------------------------------------------------------------
        //   First Retry Failed
        // ---------------------------------------------------------------------

        // The time used to calculate expiration time after retry.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(3000));

        assertSame(policyMock, state.retrieveFailed(clockMock, policyMock));

        expectations.verify();

        // ---------------------------------------------------------------------
        //   Second Retry Failed
        // ---------------------------------------------------------------------

        // Second retry failed so it has no exceeded the number of

        assertNull(state.retrieveFailed(clockMock, policyMock));
    }
}
