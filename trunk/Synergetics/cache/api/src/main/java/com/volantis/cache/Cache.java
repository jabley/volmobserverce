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

import com.volantis.cache.group.Group;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.shared.time.Period;


/**
 * A cache.
 * 
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface Cache {

    /**
     * Retrieve the object associated with the specified key.
     *
     * <p>If no entry for the object exists in the cache then it will invoke
     * the {@link CacheableObjectProvider} associated with the cache.</p>
     *
     * @param key The key to the object.
     * @return The object associated with the specified key.
     */
    Object retrieve(Object key);

    /**
     * Retrieve the object associated with the specified key.
     *
     * <p>If no entry for the object exists in the cache then it will invoke
     * the specified provider.</p>
     *
     * @param key      The key to the object.
     * @param provider The object responsible for providing the object to the
     *                 cache.
     * @return The object associated with the specified key.
     */
    Object retrieve(Object key, CacheableObjectProvider provider);

    /**
     * Perform an asynchronous query on the cache.
     *
     * <p>See {@link AsyncResult} for more information about how to handle an
     * asynchronous query.</p>
     *
     * <p>If the entry is being updated then this method will block until it
     * has been updated, or the timeout period has elapsed.</p>
     *
     * <p>If the timeout is not {@link Period#INDEFINITELY} and the entry
     * requires updating then a timer is started that will cause the update to
     * fail if it has not been completed before the specified timeout period
     * has elapsed.</p>
     *
     * <p>It is strongly recommended that a limited timeout, i.e. not
     * {@link Period#INDEFINITELY} is specified to prevent blocking of queries
     * in the event that the update could not be completed successfully. This
     * is especially important if the caller does not maintain control for the
     * whole period between invoking this method and completing the update.</p>
     *
     * @param key     The key that identifies the entry to retrieve.
     * @param timeout The maximum time to spend updating an entry, or waiting
     *                for the entry to be updated.
     * @return The result of the query.
     */
    AsyncResult asyncQuery(Object key, Period timeout);

    /**
     * Get the root group, that is the top level group that contains all
     * entries and groups.
     *
     * @return The root group.
     */
    Group getRootGroup();

    /**
     * Remove the entry with the specified key.
     *
     * @param key The key of the entry to remove.
     * @return The entry that was removed, or null if no such entry existed.
     */
    CacheEntry removeEntry(Object key);
}
