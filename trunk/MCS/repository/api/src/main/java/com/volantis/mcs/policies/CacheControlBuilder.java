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

package com.volantis.mcs.policies;

/**
 * Builder of {@link CacheControl} instances.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see CacheControl
 * @see PolicyFactory#createCacheControlBuilder()
 * @since 3.5.1
 */
public interface CacheControlBuilder {

    /**
     * Get the built {@link CacheControl}.
     *
     * <p>Returns a newly created instance the first time it is called and
     * if the state has changed since the last time this method was called,
     * otherwise it returns the same instance as the previous call.</p>
     *
     * @return The built {@link CacheControl}.
     */
    CacheControl getCacheControl();

    /**
     * Setter for the <a href="CacheControl.html#cacheThisPolicy">cache this policy</a> property.
     *
     * @param cacheThisPolicy New value of the
     *                        <a href="CacheControl.html#cacheThisPolicy">cache this policy</a> property.
     */
    public void setCacheThisPolicy(boolean cacheThisPolicy);

    /**
     * Getter for the <a href="CacheControl.html#cacheThisPolicy">cache this policy</a> property.
     *
     * @return Value of the <a href="CacheControl.html#cacheThisPolicy">cache this policy</a>
     *         property.
     */
    public boolean getCacheThisPolicy();

    /**
     * Setter for the <a href="CacheControl.html#retainDuringRetry">retain during retry</a> property.
     *
     * @param retainDuringRetry New value of the
     * <a href="CacheControl.html#retainDuringRetry">retain during retry</a> property.
     */
    public void setRetainDuringRetry(boolean retainDuringRetry);

    /**
     * Getter for the <a href="CacheControl.html#retainDuringRetry">retain during retry</a> property.
     * @return Value of the <a href="CacheControl.html#retainDuringRetry">retain during retry</a>
     * property.
     */
    public boolean getRetainDuringRetry();

    /**
     * Setter for the <a href="CacheControl.html#retryFailedRetrieval">retry failed retrieval</a> property.
     *
     * @param retryFailedRetrieval New value of the
     * <a href="CacheControl.html#retryFailedRetrieval">retry failed retrieval</a> property.
     */
    public void setRetryFailedRetrieval(boolean retryFailedRetrieval);

    /**
     * Getter for the <a href="CacheControl.html#retryFailedRetrieval">retry failed retrieval</a> property.
     * @return Value of the <a href="CacheControl.html#retryFailedRetrieval">retry failed retrieval</a>
     * property.
     */
    public boolean getRetryFailedRetrieval();

    /**
     * Setter for the <a href="CacheControl.html#retryInterval">retry interval</a> property.
     *
     * @param retryInterval New value of the
     * <a href="CacheControl.html#retryInterval">retry interval</a> property.
     */
    public void setRetryInterval(int retryInterval);

    /**
     * Getter for the <a href="CacheControl.html#retryInterval">retry interval</a> property.
     * @return Value of the <a href="CacheControl.html#retryInterval">retry interval</a>
     * property.
     */
    public int getRetryInterval();

    /**
     * Setter for the <a href="CacheControl.html#retryMaxCount">retry max count</a> property.
     *
     * @param retryMaxCount New value of the
     * <a href="CacheControl.html#retryMaxCount">retry max count</a> property.
     */
    public void setRetryMaxCount(int retryMaxCount);

    /**
     * Getter for the <a href="CacheControl.html#retryMaxCount">retry max count</a> property.
     * @return Value of the <a href="CacheControl.html#retryMaxCount">retry max count</a>
     * property.
     */
    public int getRetryMaxCount();

    /**
     * Setter for the <a href="CacheControl.html#timeToLive">time to live</a> property.
     *
     * @param timeToLive New value of the
     * <a href="CacheControl.html#timeToLive">time to live</a> property.
     */
    public void setTimeToLive(int timeToLive);

    /**
     * Getter for the <a href="CacheControl.html#timeToLive">time to live</a> property.
     * @return Value of the <a href="CacheControl.html#timeToLive">time to live</a>
     * property.
     */
    public int getTimeToLive();

}
