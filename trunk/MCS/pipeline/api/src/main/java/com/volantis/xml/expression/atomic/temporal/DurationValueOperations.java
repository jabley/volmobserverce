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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic.temporal;

/**
 * Internal API exposing the operations that Durations can have performed on
 * them.
 */
public interface DurationValueOperations {
    /**
     * Return true if this is less than <code>duration</code>.
     *
     * @param duration the duration to compare with this
     * @return true if this is less than the supplied parameter
     */
    public boolean lessThan(DurationValue duration);

    /**
     * Return true if this is greater than <code>duration</code>.
     *
     * @param duration the duration to compare with this
     * @return true if this is greater than the supplied parameter
     */
    public boolean greaterThan(DurationValue duration);
}
