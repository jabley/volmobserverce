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
 * Determines how an associated policy will be cached.
 *
 * <p>Instances of this type can be constructed using a
 * {@link CacheControlBuilder}.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="cacheThisPolicy">
 * <td align="right" valign="top" width="1%"><b>cache&nbsp;this&nbsp;policy</b></td>
 * <td>determines whether this policy should be cached at all.</td>
 * </tr>
 *
 * <tr id="retainDuringRetry">
 * <td align="right" valign="top" width="1%"><b>retain&nbsp;during&nbsp;retry</b></td>
 * <td>determines whether the policy is retained while retrying to retrieve a more up to date version of the policy.</td>
 * </tr>
 *
 * <tr id="retryFailedRetrieval">
 * <td align="right" valign="top" width="1%"><b>retry&nbsp;failed&nbsp;retrieval</b></td>
 * <td>determines whether MCS will attempt to retrieve a later version of the policy if a previous attempt failed.</td>
 * </tr>
 *
 * <tr id="retryInterval">
 * <td align="right" valign="top" width="1%"><b>retry&nbsp;interval</b></td>
 * <td>the number of seconds between retry attempts.</td>
 * </tr>
 *
 * <tr id="retryMaxCount">
 * <td align="right" valign="top" width="1%"><b>retry&nbsp;max&nbsp;count</b></td>
 * <td>the maximum number of retries before MCS gives up.</td>
 * </tr>
 *
 * <tr id="timeToLive">
 * <td align="right" valign="top" width="1%"><b>time&nbsp;to&nbsp;live</b></td>
 * <td>the number of seconds that this policy will remain fresh while in the cache.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate
 * @see CacheControlBuilder
 * @see Policy
 * @since 3.5.1
 */
public interface CacheControl {

    /**
     * Get a new builder instance for {@link CacheControl}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link CacheControlBuilder#getCacheControl()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    CacheControlBuilder getCacheControlBuilder();

    /**
     * Getter for the <a href="#cacheThisPolicy">cache this policy</a> property.
     *
     * @return Value of the <a href="#cacheThisPolicy">cache this policy</a>
     *         property.
     */
    public boolean getCacheThisPolicy();

    /**
     * Getter for the <a href="#retainDuringRetry">retain during retry</a> property.
     *
     * @return Value of the <a href="#retainDuringRetry">retain during retry</a>
     *         property.
     */
    public boolean getRetainDuringRetry();

    /**
     * Getter for the <a href="#retryFailedRetrieval">retry failed retrieval</a> property.
     *
     * @return Value of the <a href="#retryFailedRetrieval">retry failed retrieval</a>
     *         property.
     */
    public boolean getRetryFailedRetrieval();

    /**
     * Getter for the <a href="#retryInterval">retry interval</a> property.
     *
     * @return Value of the <a href="#retryInterval">retry interval</a>
     *         property.
     */
    public int getRetryInterval();

    /**
     * Getter for the <a href="#retryMaxCount">retry max count</a> property.
     *
     * @return Value of the <a href="#retryMaxCount">retry max count</a>
     *         property.
     */
    public int getRetryMaxCount();

    /**
     * Getter for the <a href="#timeToLive">time to live</a> property.
     * @return Value of the <a href="#timeToLive">time to live</a>
     * property.
     */
    public int getTimeToLive();

}
