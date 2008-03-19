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
 * Simple FutureResultObject that always returns null.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class NullFutureResult extends ReadThroughFutureResult {

    // javadoc inherited
    protected NullFutureResult(Object key, int timeout) {
        super(key, timeout);
    }

    /**
     * @param key This parameter is ignored by this implementation
     * @return null.
     */
    protected Object performUpdate(Object key) {
        return null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Feb-05	391/1	matthew	VBM:2005020308 Make cache implementation slightly more flexible

 ===========================================================================
*/
