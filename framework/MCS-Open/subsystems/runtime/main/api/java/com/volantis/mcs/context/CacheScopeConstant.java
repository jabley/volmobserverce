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
package com.volantis.mcs.context;

/**
 * Type Safe Enum for values indicating the cache scope of a page.
 * This is currently only can or can't cache but may be required to be
 * extended to use none|optimistic|safe.
 */
public class CacheScopeConstant {
    /**
     * attribute value to represent the cache scope
     */
    public static final String CACHE_SCOPE_ATTRIBUTE = "cacheScope";

    /**
     * CacheScopeConstant to show pages can be cached
     */
    public static final CacheScopeConstant CAN_CACHE_PAGE =
            new CacheScopeConstant("true");

    /**
     * CacheScopeConstant to show pages can not be cached
     */
    public static final CacheScopeConstant CAN_NOT_CACHE_PAGE =
            new CacheScopeConstant("false");

    //string representation of the cache scope
    private String cacheScope;

    //constructor
    private CacheScopeConstant(String cacheScope) {
        this.cacheScope = cacheScope;
    }

    //javadoc unnecessary
    public String getCacheScope() {
        return cacheScope;
    }
}
