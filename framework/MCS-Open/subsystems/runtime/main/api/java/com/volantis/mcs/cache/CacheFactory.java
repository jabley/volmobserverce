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

package com.volantis.mcs.cache;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * This Factory provide access via reflection to implementations of @link
 * CacheStore and @link CacheManager objects for building @link Cache
 * implemetations.
 */
public abstract class CacheFactory {


    /**
     * The default single instance of this factory
     */
    private static CacheFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = CacheFactory.class.getClassLoader();
            Class implClass = loader.loadClass(
                    "com.volantis.mcs.cache.impl.DefaultCacheFactory");
            defaultInstance = (CacheFactory) implClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the single instance of the DefaultCacheFactory.
     *
     * @return The default instance of the DefaultCacheFactory;
     */
    public static CacheFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Creates a default implementation of CacheStore
     * @param timeToLive The amount of time an entry can remain in cache before
     * it is considered to be out of date.
     * @return The Default @link CacheStore
     */
    public abstract CacheStore createDefaultCacheStore(long timeToLive);


    /**
     * Creates an implementation of CacheManager that flushes expired
     * CacheIdentities from the cache automatically.
     *
     * @return The an implementation of @link CacheManager that runs in the 
     * background.
     */
    public abstract CacheManager createBackgroundCacheManager();


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
