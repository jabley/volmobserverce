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
 * Default implementation of the FutureResultFactory. This returns null.
 * This maintains deprecated the behaviour of the old cache (lock, get, put,
 * unlock).
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class DefaultFutureResultFactory extends FutureResultFactory {
    /**
     * @see com.volantis.synergetics.cache.FutureResultFactory
     */
    public DefaultFutureResultFactory() {
        super();
    }

    /**
     * Return null.
     *
     * @param key        this pararmeter is ignored.
     * @param timeToLive this pararmeter is also ignored.
     * @return null.
     */
    protected ReadThroughFutureResult
        createCustomFutureResult(Object key, int timeToLive) {
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	397/1	matthew	VBM:2005020308 Implement CachingPluggableHTTPManager

 14-Feb-05	391/3	matthew	VBM:2005020308 More minor changes

 09-Feb-05	391/1	matthew	VBM:2005020308 Make cache implementation slightly more flexible

 03-Feb-05	379/3	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 ===========================================================================
*/
