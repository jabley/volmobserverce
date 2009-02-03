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

import com.volantis.cache.expiration.ExpirationChecker;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.shared.system.SystemClock;

/**
 * Builds a {@link Cache}.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="objectProvider">
 * <td align="right" valign="top" width="1%"><b>object provider</b></td>
 * <td>The object responsible for providing objects to the cache when the
 * object is not present.</td>
 * </tr>
 *
 * <tr id="maxCount">
 * <td align="right" valign="top" width="1%"><b>max count</b></td>
 * <td>The maximum number of entries allowed in the cache. This must be greater
 * than 0.</td>
 * </tr>
 *
 * <tr id="groupOrganizer">
 * <td align="right" valign="top" width="1%"><b>group organizer</b></td>
 * <td>The object responsible for selecting the group into which a newly
 * created {@link CacheEntry} is to be placed.</td>
 * </tr>
 *
 * <tr id="clock">
 * <td align="right" valign="top" width="1%"><b>clock</b></td>
 * <td>The object responsible for providing times to the cache, typically
 * only used when testing.</td>
 * </tr>
 *
 * <tr id="expirationChecker">
 * <td align="right" valign="top" width="1%"><b>expiration checker</b></td>
 * <td>The object responsible for checking whether an entry has expired. If
 * this is not set then entries will never expire.</td>
 * </tr>
 *
 * </table>
 */
public interface CacheBuilder {

    /**
     * Build a {@link Cache} from the information stored within this builder.
     *
     * <p>Each call to this method will create another instance of the cache
     * with the same structure.</p>
     *
     * @return The newly built cache.
     */
    Cache buildCache();

    /**
     * Getter for the <a href="#objectProvider">object provider</a> property.
     *
     * @return Value of the <a href="#objectProvider">object provider</a>
     *         property.
     */
    CacheableObjectProvider getObjectProvider();

    /**
     * Setter for the <a href="#objectProvider">object provider</a> property.
     *
     * @param objectProvider New value of the
     *                       <a href="#objectProvider">object provider</a>
     *                       property.
     */
    void setObjectProvider(CacheableObjectProvider objectProvider);

    /**
     * Getter for the <a href="#maxCount">max count</a> property. This
     * represents the maximum number of entries allowed in the cache.
     *
     * @return Value of the <a href="CacheBuilder.html#maxCount">max count</a>
     * property.
     */
    int getMaxCount();

    /**
     * Setter for the <a href="#maxCount">max count</a> property. This
     * represents the maximum number of entries allowed in the cache.
     *
     * @param maxCount New value of the
     *                 <a href="CacheBuilder.html#maxCount">max count</a>
     *                 property.
     */
    void setMaxCount(int maxCount);

    /**
     * Getter for the <a href="#clock">clock</a> property.
     *
     * @return Value of the <a href="#clock">clock</a> property.
     */
    SystemClock getClock();

    /**
     * Setter for the <a href="#clock">clock</a> property.
     *
     * @param clock New value of the
     *              <a href="#clock">clock</a> property.
     */
    void setClock(SystemClock clock);

    /**
     * Getter for the <a href="#expirationChecker">expiration checker</a>
     * property.
     *
     * @return Value of the <a href="#expirationChecker">expiration checker</a>
     *         property.
     */
    ExpirationChecker getExpirationChecker();

    /**
     * Setter for the <a href="#expirationChecker">expiration checker</a>
     * property.
     *
     * @param expirationChecker New value of the
     *                          <a href="#expirationChecker">expiration checker</a> property.
     */
    void setExpirationChecker(ExpirationChecker expirationChecker);
}
