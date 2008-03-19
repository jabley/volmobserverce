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
 * Simple implementation of ReadThroughFutureResult that produces the value set
 * at its time of construction. If reset is called on instances of this then
 * future calls to performUpdate will return null.
 *
 * @deprecated This class only exists to provide backwards compatibility with
 *             the old cache mechanism (old style meaning lock cache, test if
 *             it contains the value, if not put it in the cache, release
 *             lock).
 */
public class DirectValueFutureResult extends ReadThroughFutureResult {

    /**
     * Hold the value that will be returned from performUpdate()
     */
    private Object tmpValue = null;

    /**
     * Construct this with a specific timeout.
     *
     * @param timeout the time to live of the value of this FutureResult (in
     *                seconds)
     */
    protected DirectValueFutureResult(Object key, Object value, int timeout) {
        super(key, timeout);
        this.tmpValue = value;
    }

    /**
     * On the first call to this method we return the value set in the
     * constructor. Subsequent calls return null. This behaviour allows
     * backwards compatibility when useing the deprecated put methods of the
     * caches.
     *
     * @param key this parameter is ignored in this implementation.
     * @return
     */
    protected Object performUpdate(Object key) {
        Object result = tmpValue;
        tmpValue = null;
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	391/3	matthew	VBM:2005020308 Javadoc changes

 09-Feb-05	391/1	matthew	VBM:2005020308 Make cache implementation slightly more flexible

 ===========================================================================
*/
