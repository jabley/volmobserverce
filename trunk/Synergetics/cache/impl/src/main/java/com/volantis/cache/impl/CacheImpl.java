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

package com.volantis.cache.impl;

import com.volantis.cache.AsyncResult;
import com.volantis.cache.Cache;
import com.volantis.cache.CacheEntry;
import com.volantis.cache.expiration.ExpirationChecker;
import com.volantis.cache.group.Group;
import com.volantis.cache.impl.expiration.DefaultExpirationChecker;
import com.volantis.cache.impl.group.InternalGroup;
import com.volantis.cache.impl.group.InternalGroupBuilder;
import com.volantis.cache.impl.integrity.IntegrityCheckingReporter;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.shared.io.IndentingWriter;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.shared.time.Countdown;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.TimedOutException;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * The implementation of a {@link Cache}.
 *
 * <p>Maintains a map to get an entry based on a hashing key and an ordered
 * linked list of the entries for the purpose of pruning entries.</p>
 */
public class CacheImpl
        implements InternalCache {

    /**
     * A timer to use to clean up any asynchronous queries.
     */
    private static final Timer TIMER = new Timer(true);

    /**
     * Default object reponsible for proving the cacheable objects.
     *
     * <p>This may be overridden by a provider supplied directly to the
     * {@link #retrieve(Object, CacheableObjectProvider)} method.</p>
     */
    private final CacheableObjectProvider provider;

    /**
     * The expiration checker.
     */
    private final ExpirationChecker expirationChecker;

    /**
     * The default group.
     */
    private final InternalGroup rootGroup;

    /**
     * The map from key to {@link CacheEntryImpl}.
     *
     * <p>Must only be accessed while synchronized upon it.</p>
     */
    private final Map map;

    /**
     * The clock to use by the cache for computing expiration times, etc.
     */
    private final SystemClock clock;

    /**
     * Initialise.
     *
     * @param builder The builder.
     */
    public CacheImpl(CacheBuilderImpl builder) {
        map = new HashMap();

        provider = builder.getObjectProvider();

        // Initialise the expiration checker, if none is specified then use the
        // default one.
        ExpirationChecker expirationChecker = builder.getExpirationChecker();
        if (expirationChecker == null) {
            expirationChecker = DefaultExpirationChecker.getDefaultInstance();
        }
        this.expirationChecker = expirationChecker;

        // Initialise the clock, if none is specified then use the default
        // instance.
        SystemClock clock = builder.getClock();
        if (clock == null) {
            clock = SystemClock.getDefaultInstance();
        }
        this.clock = clock;

        InternalGroupBuilder rootGroupBuilder = builder.getRootGroupBuilder();
        rootGroup = rootGroupBuilder.buildGroup(this, null, "root", clock);
    }

    // Javadoc inherited.
    public Object retrieve(Object key) {
        return retrieveInternal(key, provider);
    }

    // Javadoc inherited.
    public Object retrieve(Object key, CacheableObjectProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("provider cannot be null");
        }

        return retrieveInternal(key, provider);
    }

    /**
     * Retrieve the object for the key.
     *
     * @param key      The key to use to retrieve the object.
     * @param provider The {@link CacheableObjectProvider} to use.
     * @return The object for the key, may be null.
     */
    private Object retrieveInternal(
            Object key, final CacheableObjectProvider provider) {

        Object value = null;
        AsyncResult async = asyncQuery(key, Period.INDEFINITELY);

        // Act on the state of the entry. It is possible that within this
        // window where the entry is not synchronized that the entry could
        // change, the only case were it cannot is if it was in the update
        // state as all other threads will see it as pending and wait.
        // Some of the things that can happen in this window are:
        // 1) Entries may have been removed, either implicitly, or explicitly.
        // 2) Entries may have expired on another thread.
        // 3) Entries may have moved groups.
        // 4) Entries may have new values.

        if (async.isReady()) {
            value = async.getValue();
        } else {
            ProviderResult result = null;
            Throwable throwable = null;
            boolean failed = true;
            try {
                // Get the entry, this will return null if the entry is marked
                // as being uncacheable.
                CacheEntry entry = async.getEntry();

                // Get the provider to retrieve the object. It is given the
                // whole entry so that it can perform revalidation of an
                // existing entry if it has expired.
                result = provider.retrieve(clock, key, entry);
                if (result == null) {
                    throw new IllegalStateException("Result from provider is null");
                }

                // Request to the provider did not fail.
                failed = false;

            } catch (RuntimeException e) {

                // Remember the exception so that it can be stored in the
                // entry to indicate the original cause.
                throwable = e;

                // Rethrow the exceptions.
                throw e;

            } catch (Error e) {

                // Remember the exception so that it can be stored in the
                // entry to indicate the original cause.
                throwable = e;

                // Rethrow the exceptions.
                throw e;

            } finally {
                if (failed) {
                    async.failed(throwable);
                } else {
                    // the cache must be updated even if the result has a
                    // non-null throwable
                    value = async.update(result);

                    // if the result has a throwable, thow it wrapped inside a
                    // runtime exception
                    if (result.getThrowable() != null) {
                        // The entry has a throwable so throw a runtime
                        // exception, encapsulating the original throwable.
                        throw new ExtendedRuntimeException(
                            "Attempt to access " + key +" failed due" +
                            " to the following error", result.getThrowable());
                    }
                }
            }
        }

        return value;
    }

    // Javadoc inherited.
    public AsyncResult asyncQuery(Object key, Period timeout) {

        InternalAsyncResult async;

        // Get the entry from the map while synchronized.
        InternalCacheEntry entry = getCacheEntry(key);

        // Synchronize on the entry in order to get and possibly update the
        // state of the entry.
        synchronized (entry) {

            EntryState state = entry.getState();

            // If the entry may have expired then check it.
            if (state.mayHaveExpired()) {
                // Check the entry and if it has expired then it requires
                // updating.
                if (expirationChecker.hasExpired(clock, entry)) {
                    state = EntryState.UPDATE;
                }
            }

            // If the state requires it then wait until the state changes.
            if (state.mustWait()) {
                Countdown countdown = Countdown.getCountdown(timeout, clock);
                do {
                    try {
                        Period remaining = countdown.countdown();
                        entry.wait(remaining.inMillisTreatIndefinitelyAsZero());
                        state = entry.getState();
                    } catch (InterruptedException e) {
                        throw new ExtendedRuntimeException(e);
                    } catch (TimedOutException e) {
                        throw new ExtendedRuntimeException(e);
                    }
                } while (state.mustWait());
            }

            // At this point we know that the state will not be pending.
            if (state == EntryState.UPDATE) {
                // The entry is new so mark it as pending so other threads
                // will wait.
                entry.setState(EntryState.PENDING);
                entry.setAsyncResult(null);

                // Create a new one each time and do not hold a reference to it
                // within the cache as it needs to clean up if the caller
                // discards its reference to it before updating the entry.
                async = new AsyncUpdate(entry);

            } else if (state == EntryState.READY || state == EntryState.ERROR) {

                // Check to see whether the entry is in error or not.
                Throwable throwable = entry.getThrowable();
                if (throwable == null) {
                    // Entry is not in error.
                    async = entry.getAsyncResult();

                    if (entry.inCache()) {
                        // Inform the group that one of its entries was hit so
                        // it can update the structure to support the least
                        // recently used strategy.
                        InternalGroup group = entry.getGroup();
                        group.hitEntry(entry);
                    }
                } else {
                    // The entry is in the error state so throw an
                    // exception, encapsulating the reason why the entry
                    // is in that state.
                    throw new ExtendedRuntimeException(
                            "Previous attempt to access " + key +
                            " failed due to the following error",
                            throwable);
                }
            } else if (state == EntryState.UNCACHEABLE) {
                async = entry.getAsyncResult();
            } else {
                throw new IllegalStateException("Unknown state " + state);
            }
        }

        // If a limited timeout has been specified then schedule a task to
        // ensure it is updated within that period if necessary.
        if (timeout != Period.INDEFINITELY) {
            async.schedule(TIMER, timeout);
        }

        return async;
    }

    /**
     * Get the cache entry from the map.
     *
     * <p>If the entry does not exist then a new one is created and added to
     * the cache.</p>
     *
     * <p>This obtains the mutex for the cache map.</p>
     *
     * @param key The key to use.
     * @return The entry.
     */
    private InternalCacheEntry getCacheEntry(Object key) {
        InternalCacheEntry entry;
        synchronized (map) {

            entry = (CacheEntryImpl) map.get(key);
            if (entry == null) {
                // If the entry could not be found then create a new one and
                // add it to the map. It needs adding to the map so that other
                // concurrent requests can find it and wait on it but it is
                // not added to the list of entries, or cause other entries to
                // be removed until the object has been retrieved.
                entry = new CacheEntryImpl(this, key);
                map.put(key, entry);
            }
        }
        return entry;
    }

    // Javadoc inherited.
    public Group getRootGroup() {
        return rootGroup;
    }

    // Javadoc inherited.
    public void removeFromMap(Object key) {

        // Protect the map from being accessed concurrently.
        synchronized (map) {
            map.remove(key);
        }
    }

    // Javadoc inherited.
    public void performIntegrityCheck(IntegrityCheckingReporter reporter) {
        rootGroup.performIntegrityCheck(reporter);
    }

    // Javadoc inherited.
    public void debugStructure(IndentingWriter writer) {
        rootGroup.debugStructure(writer);
    }

    // Javadoc inherited.
    public CacheEntry removeEntry(Object key) {
        InternalCacheEntry entry;
        synchronized (map) {
            entry = (CacheEntryImpl) map.get(key);
        }

        if (entry == null) {
            return null;
        }

        entry.removeFromCache();

        return entry;
    }
}
