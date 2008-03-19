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

import com.volantis.cache.provider.ProviderResult;

/**
 * The result of an asynchronous query on the cache.
 *
 * <p>Once the asynchronous query has been performed and an instance of this
 * created then the caller must perform the following steps to ensure the
 * integrity of the cache and prevent any other threads waiting for the
 * response from being blocked indefinitely or for a long period of time.</p>
 *
 * <p>First the {@link #isReady()} method must be invoked to see whether the
 * entry is already ready. If it returns true then the entry is up to date and
 * the {@link #getValue()} method can be called to get the value. No other
 * methods can be called and will throw an exception.</p>
 *
 * <p>If {@link #isReady()} returns false then it is the responsibility of the
 * caller to update the entry. This may either be because:
 * <ul>
 * <li>The entry is new, this can be distinguished from the other cases because
 * the value for the entry is null.</li>
 * <li>The entry has expired, this can be distinguished from the other cases
 * because the value for the entry is not null.</li>
 * <li>A previous result indicated that the entry should not be
 * cached, this can be distinguished from the other cases because the entry is
 * null. In this case the entry does not have to be updated but can be if the
 * newly retrieved object can now be cached.</li>
 * </ul>
 *
 * <p>The first two cases above require that the entry is updated which simply
 * involves retrieving the object to cache, creating a {@link ProviderResult}
 * instance and then calling {@link #update(ProviderResult)}. If an error
 * occurs after retrieving the instance of this and before updating then the
 * {@link #failed(Throwable)} method must be called with the {@link Throwable}
 * that represents the error.</p>
 *
 * <p>The following code illustrates how to use the {@link AsyncResult}.</p>
 * <pre>
 * AsyncResult async = cache.asyncQuery(key, timeout);
 * Object value;
 * if (async.isReady()) {
 *     value = async.getValue();
 * } else {
 *     Throwable throwable = null;
 *     boolean failed = true;
 *     try {
 *         CacheEntry entry = async.getEntry();
 *         Object extensionObject;
 *         boolean cacheable;
 *         if (entry == null) {
 *             // Previous result was not cached so this code could
 *             // be run by multiple threads so just get the value.
 *             value = ... get value ...;
 *             cacheable = ... check if cacheable ...;
 *             extensionObject = ... get extension object ...;
 *         } else {
 *             // This thread is responsible for updating the entry, all
 *             // other threads requesting the content with the same key
 *             // is blocked waiting for this to update the entry.
 *             Object oldValue = entry.getValue();
 *             if (oldValue == null) {
 *                 // The entry is new so just get the value.
 *                 value = ... get the value ...;
 *             } else {
 *                 // The entry has expired and needs updating
 *                 value = ... update value ...;
 *             }
 *             cacheable = ... check if cacheable ...;
 *             extensionObject = ... get extension object ...;
 *         }
 *
 *         ProviderResult result = new ProviderResult(value,
 *                 cache.getRootGroup(), cacheable, extensionObject);
 *         async.update(result);
 *         failed = false;
 *     } catch (RuntimeException e) {
 *         throwable = e;
 *
 *         throw e;
 *     } catch (Error e) {
 *         throwable = e;
 *
 *         throw e;
 *     } finally {
 *         if (failed) {
 *             async.failed(throwable);
 *         }
 *     }
 * }
 * </pre>
 *
 * <p>If the returned {@link AsyncResult} requires that an update is performed
 * but the caller discards the reference before updating then it will be
 * updated with an error either after the timeout has expired, or the
 * {@link AsyncResult} is garbage collected.</p> 
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface AsyncResult {

    /**
     * Indicates whether the entry is ready.
     *
     * <p>The return value of this method determines which other methods it is
     * valid to call. When this is true the {@link #getValue()} method can be
     * called, otherwise the other methods can be called. Attempting to call
     * invalid methods will result in an {@link IllegalStateException} being
     * thrown.</p>
     *
     * @return True if the entry is ready false otherwise.
     */
    boolean isReady();

    /**
     * Get the value of the entry.
     *
     * @return The value of the entry.
     * @throws IllegalStateException if {@link #isReady()} returns false.
     */
    Object getValue();

    /**
     * Update the value of the entry.
     *
     * @param result Contains the value for the entry along with a number of
     *               other pieces of information.
     * @return The value of the entry.
     * @throws IllegalStateException if {@link #isReady()} returns true.
     */
    Object update(ProviderResult result);

    /**
     * Mark the entry as being in error.
     *
     * @param throwable The {@link Throwable} that caused the failure.
     * @throws IllegalStateException if {@link #isReady()} returns true.
     */
    void failed(Throwable throwable);

    /**
     * Get the entry with which the result is associated.
     *
     * <p>If the previous attempt to retrieve the entry indicated that the
     * entry was not cacheable then this will return null.</p>
     *
     * @return The entry with which the result is associated.
     * @throws IllegalStateException if {@link #isReady()} returns true.
     */
    CacheEntry getEntry();
}
