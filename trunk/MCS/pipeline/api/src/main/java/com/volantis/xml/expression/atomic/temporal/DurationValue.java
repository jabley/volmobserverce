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

import com.volantis.xml.expression.atomic.AtomicValue;

/**
 * Represents an XPath 2.0 duration value. This is the interval between two
 * {@link DateTimeValue} and/or {@link DateValue} instances or between two
 * {@link TimeValue} instances.
 *
 * <p>Note that if isPositive() is false then all getDate and getTime methods
 * will return negative values.</p>
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 */
public interface DurationValue extends AtomicValue {

    /**
     * Returns true if the duration is positive, false if it is negative.
     *
     * @return true if the duration is positive, false if it is negative
     */
    public boolean isPositive();

    /**
     * Get the absoulte number of years (positive or negative).
     *
     * @return the number of years
     */
    public int getYears();

    /**
     * Return the absolute number of months (positive or negative).
     *
     * @return the absolute number of months (positive or negative)
     */
    public int getMonths();

    /**
     * Return the absolute number of days (positive or negative).
     *
     * @return the absolute number of days (positive or negative)
     */
    public int getDays();

    /**
     * Return the absolute number of hours (positive or negative).
     *
     * @return the absolute number of hours (positive or negative)
     */
    public int getHours();

    /**
     * Return the absolute number of minutes (positive or negative).
     *
     * @return the absolute number of minutes (positive or negative)
     */
    public int getMinutes();

    /**
     * Return the absolute number of seconds and fractions of a second
     * (positive or negative).
     *
     * @return the absolute number of seconds and fractions of a second
     * (positive or negative)
     */
    public int getSeconds();

    /**
     * Return the absolute number of milliseconds (positive or negative).
     *
     * @return the absolute number of milliseconds
     *         (positive or negative)
     */
    public int getMilliseconds();

    /**
     * Provides the internal "time-lined" value derived from the date/time
     * specification.
     *
     * @return the internal "time-lined" value that can be used to compare
     *         date/times with each other
     */
    public double getTimeOnTimeline();
}
