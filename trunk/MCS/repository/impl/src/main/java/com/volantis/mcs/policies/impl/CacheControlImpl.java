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

import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.InternalCacheControl;
import com.volantis.mcs.policies.InternalCacheControlBuilder;

public class CacheControlImpl
        extends EqualsHashCodeBase
        implements InternalCacheControl {

    /**
     * Flag to determine if the component is to be cached
     */
    private final boolean cacheThisPolicy;

    /**
     * Flag to determine if the component is to be retained during retries
     */
    private final boolean retainDuringRetry;

    /**
     * Flag to determine if retries are attempted if retrieval fails
     */
    private final boolean retryFailedRetrieval;

    /**
     * Number of seconds between retreival attempts
     */
    private final int retryInterval;

    /**
     * Number of retry attempts before failing
     */
    private final int retryMaxCount;

    /**
     * Number of seconds to hold this component in the cache
     */
    private final int timeToLive;

    /**
     * Flag that indicates whether the {@link #cacheThisPolicy} property was
     * explicitly set.
     */
    private final boolean cacheThisPolicyWasSet;

    /**
     * Flag that indicates whether the {@link #retainDuringRetry} property was
     * explicitly set.
     */
    private final boolean retainDuringRetryWasSet;

    /**
     * Flag that indicates whether the {@link #retryFailedRetrieval} property
     * was explicitly set.
     */
    private final boolean retryFailedRetrievalWasSet;

    /**
     * Flag that indicates whether the {@link #retryInterval} property was
     * explicitly set.
     */
    private final boolean retryIntervalWasSet;

    /**
     * Flag that indicates whether the {@link #retryMaxCount} property was
     * explicitly set.
     */
    private final boolean retryMaxCountWasSet;

    /**
     * Flag that indicates whether the {@link #timeToLive} property was
     * explicitly set.
     */
    private final boolean timeToLiveWasSet;

    public CacheControlImpl(InternalCacheControlBuilder builder) {
        cacheThisPolicy = builder.getCacheThisPolicy();
        retainDuringRetry = builder.getRetainDuringRetry();
        retryFailedRetrieval = builder.getRetryFailedRetrieval();
        retryInterval = builder.getRetryInterval();
        retryMaxCount = builder.getRetryMaxCount();
        timeToLive  = builder.getTimeToLive();

        cacheThisPolicyWasSet = builder.cacheThisPolicyWasSet();
        retainDuringRetryWasSet = builder.retainDuringRetryWasSet();
        retryFailedRetrievalWasSet = builder.retryFailedRetrievalWasSet();
        retryIntervalWasSet = builder.retryIntervalWasSet();
        retryMaxCountWasSet = builder.retryMaxCountWasSet();
        timeToLiveWasSet = builder.timeToLiveWasSet();
    }

    public CacheControlBuilder getCacheControlBuilder() {
        return new CacheControlBuilderImpl(this);
    }

    public boolean getCacheThisPolicy() {
        return cacheThisPolicy;
    }

    public boolean getRetainDuringRetry() {
        return retainDuringRetry;
    }

    public boolean getRetryFailedRetrieval() {
        return retryFailedRetrieval;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public int getRetryMaxCount() {
        return retryMaxCount;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public boolean cacheThisPolicyWasSet() {
        return cacheThisPolicyWasSet;
    }

    public boolean retainDuringRetryWasSet() {
        return retainDuringRetryWasSet;
    }

    public boolean retryFailedRetrievalWasSet() {
        return retryFailedRetrievalWasSet;
    }

    public boolean retryIntervalWasSet() {
        return retryIntervalWasSet;
    }

    public boolean retryMaxCountWasSet() {
        return retryMaxCountWasSet;
    }

    public boolean timeToLiveWasSet() {
        return timeToLiveWasSet;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof CacheControlImpl) ?
                equalsSpecific((CacheControlImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    public boolean equalsSpecific(CacheControlImpl other) {

        return super.equalsSpecific(other) &&
                equals(cacheThisPolicy, other.cacheThisPolicy) &&
                equals(retainDuringRetry, other.retainDuringRetry) &&
                equals(retryFailedRetrieval, other.retryFailedRetrieval) &&
                equals(retryInterval, other.retryInterval) &&
                equals(retryMaxCount, other.retryMaxCount) &&
                equals(timeToLive, other.timeToLive);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, cacheThisPolicy);
        result = hashCode(result, retainDuringRetry);
        result = hashCode(result, retryFailedRetrieval);
        result = hashCode(result, retryInterval);
        result = hashCode(result, retryMaxCount);
        result = hashCode(result, timeToLive);
        return result;
    }
}
