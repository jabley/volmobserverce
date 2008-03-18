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
package com.volantis.synergetics.cache;



/**
 * Implementaions of this class are used to produce The ReadThroughFutureResult
 * objects used to populate the ReadThroughCache. Implementors should extend
 * the this and implement the getDataAccessor method.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public abstract class FutureResultFactory {

    /**
     * This is the default timeout value that will be applied to objects
     * generated by this factory. Access to it is not synchronized and
     * therefore changes to it are only guarenteed to be seen "promptly" by
     * other threads.
     */
    private volatile int timeout = -1;

    /**
     * Create the factory with a default timeout value.
     */
    protected FutureResultFactory() {
    }

    /**
     * @return the timeout that has been set for this FutureResultFactory.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set the timeout associated with this factory.
     *
     * @param timeout the timeout to set.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Create a future result object that will be placed in the
     * ReadThroughCache. This future result will always return null when asked
     * for its value.
     *
     * @param key        the key that is to be used to populate the cache
     *                   entry
     * @param timeToLive the time to live for this cache object.
     * @return A FutureResult object that will be placed in the cache.
     */
    public final ReadThroughFutureResult createNullFutureResult(Object key,
                                                                int timeToLive) {
        ReadThroughFutureResult result = new NullFutureResult(key, timeToLive);
        return initialiseFutureResult(result, timeToLive);
    }

    /**
     * Create a future result object that will be placed in the
     * ReadThroughCache. This future result value allows a value to be set when
     * you create it. It then returns that value until its reset method is
     * called.
     *
     * @param key        the key that is to be used to populate the cache
     *                   entry.
     * @param value      the value to assign to this future result object.
     * @param timeToLive the time to live for this cache object.
     * @return A FutureResult object that will be placed in the cache.
     *
     * @deprecated This is only here for backwards compatibility. It is used by
     *             the deprecated put() methods.
     */
    final ReadThroughFutureResult createDirectValueFutureResult(Object key,
                                                                Object value,
                                                                int timeToLive) {
        ReadThroughFutureResult result =
            new DirectValueFutureResult(key, value, timeToLive);
        return initialiseFutureResult(result, timeToLive);
    }

    /**
     * Create a FutureResult to be put into the cache.
     *
     * @param key        The key for the FutureResult.
     * @param timeToLive The timeToLive for the CacheObject. then the
     *                   createDataAccessor method will be called to obtain a
     *                   value.
     */
    final ReadThroughFutureResult createFutureResult(Object key,
                                                     int timeToLive) {

        ReadThroughFutureResult result =
            createCustomFutureResult(key, timeToLive);
        return initialiseFutureResult(result, timeToLive);
    }

    /**
     * Common routine for initialising some aspects
     *
     * @param fr         The ReadThroughFutureResult object to initialize
     * @param timeToLive
     * @return the initialized FutureResultObject.
     */
    private ReadThroughFutureResult initialiseFutureResult(
        ReadThroughFutureResult fr, int timeToLive) {
        if (timeToLive == GenericCacheConfiguration.FOREVER &&
            getTimeout() != -1) {
            fr.setTimeToLive(getTimeout());
        } else {
            fr.setTimeToLive(timeToLive);
        }
        return fr;
    }

    /**
     * Override this method to return the FutureResult imlpementation to be
     * used to populate the cache.
     *
     * @param key the key used to get the data.
     * @return an object that can be used to populate a FutureResultObject.
     */
    protected abstract ReadThroughFutureResult
        createCustomFutureResult(Object key, int timeToLive);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	397/1	matthew	VBM:2005020308 Implement CachingPluggableHTTPManager

 14-Feb-05	391/3	matthew	VBM:2005020308 More minor changes

 09-Feb-05	391/1	matthew	VBM:2005020308 Make cache implementation slightly more flexible

 03-Feb-05	379/4	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 ===========================================================================
*/
