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

import com.volantis.cache.group.GroupBuilder;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Factory for creating cache related objects.
 */
public abstract class CacheFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    private static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.cache.impl.CacheFactoryImpl",
                        CacheFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static CacheFactory getDefaultInstance() {
        return (CacheFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }
    
    /**
     * Create an object to use to build a cache.
     *
     * @return An object to use to build a cache.
     */
    public abstract CacheBuilder createCacheBuilder();


    /**
     * Create an object to use to build a group.
     *
     * @return An object to use to build a group.
     */
    public abstract GroupBuilder createGroupBuilder();
}
