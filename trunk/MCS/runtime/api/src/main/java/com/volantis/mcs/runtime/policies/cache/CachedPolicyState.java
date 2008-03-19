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
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Comparator;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;

/**
 * Encapsulates all the state necessary to determine whether a policy has
 * expired, and also to handle retries.
 */
public class CachedPolicyState {

    /**
     * Indicates whether an expired policy should be retained while attempting
     * to retry retrieving it.
     */
    private final boolean retainDuringRetry;

    /**
     * The maximum number of times that a retry will be performed.
     */
    private final int maxRetryCount;

    /**
     * The interval between retries in ms.
     */
    private final Period retryIntervalInMS;

    /**
     * Indicates whether should retry a failed attempt to retrieve a policy.
     */
    private final boolean retryFailedRetrieval;

    /**
     * The expiration time.
     */
    private Time expirationTime;

    /**
     * The number of times that this has been retried since the last time
     * that the policy was successfully retrieved.
     */
    private int retryCount;

    /**
     * Initialise.
     *
     * @param clock         The clock used to calculate and check expiration
     *                      times.
     * @param cacheControl  The cache control that determine how this behaves.
     * @param initialPolicy The initial policy, if null this means that this
     *                      should immediately enter retry state.
     */
    public CachedPolicyState(
            SystemClock clock, CacheControl cacheControl,
            ActivatedPolicy initialPolicy) {

        // Initialise the cache control values.
        retainDuringRetry = cacheControl.getRetainDuringRetry();
        retryFailedRetrieval = cacheControl.getRetryFailedRetrieval();
        retryIntervalInMS = Period.inSeconds(cacheControl.getRetryInterval());
        maxRetryCount = cacheControl.getRetryMaxCount();

        Time currentTime = clock.getCurrentTime();
        if (initialPolicy == null) {
            // Calculate the expiration time based on the retry interval. A
            // retry interval of 0 (or less) will result in the policy being
            // retried immediately on the next request.
            expirationTime = currentTime.addPeriod(retryIntervalInMS);

            // The next attempt by the provider to retrieve the object is the
            // first retry.
            retryCount = 1;

        } else {
            Period timeToLive = Period.treatNonPositiveAsIndefinitely(
                    cacheControl.getTimeToLive() * 1000);
            expirationTime = currentTime.addPeriod(timeToLive);

            // No retrievals have failed yet so the next retrieval is not a
            // retry.
            retryCount = 0;
        }
    }

    /**
     * Check whether the associated policy has expired.
     *
     * @return True if it has false otherwise.
     * @param clock
     */
    public boolean hasExpired(SystemClock clock) {
        if (expirationTime == Time.NEVER) {
            return false;
        } else {
            Time currentTime = clock.getCurrentTime();
            return Comparator.GE.compare(currentTime, expirationTime);
        }
    }

    /**
     * Update the internal state when a retrieve failed.
     *
     * @param clock
     * @param expiredPolicy The policy
     */
    public ActivatedPolicy retrieveFailed(
            SystemClock clock, ActivatedPolicy expiredPolicy) {

        ActivatedPolicy policy = null;

        // If retry is supported then check to see whether another retry is
        // allowed.
        if (retryFailedRetrieval && retryCount < maxRetryCount) {

            // Retry is supported so set the expiration time so
            // that this will expire when it needs to retry.
            Time currentTime = clock.getCurrentTime();
            expirationTime = currentTime.addPeriod(retryIntervalInMS);

            // If the previous policy should be retained while retrying
            // then return that instead of null.
            if (retainDuringRetry) {
                policy = expiredPolicy;
            }

            // Increment the retry count as the next time this is
            // called will be after the provider has retried and we
            // need to make sure that it does not keep retrying.
            retryCount += 1;
        }

        return policy;
    }
}
