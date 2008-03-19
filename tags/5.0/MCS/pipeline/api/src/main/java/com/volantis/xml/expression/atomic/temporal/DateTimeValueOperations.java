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
 * An internal API that exposes the operations that can be performed on a
 * DateTimeValue.
 */
public interface DateTimeValueOperations extends DateTimeValue {

    /**
     * Add the specified duration to the date and time represented by this
     * DateTimeValue and return the result as a new DateTimeValue
     *
     * @param duration the duration to add to this dateTime
     * @return a new DateTime object that represents the duration added to this
     *         date time.
     */
    DateTimeValue add(DurationValue duration);

    /**
     * Return true if this is less than the provided dateTime
     *
     * @param dateTime the date time to compare this against
     * @return true if this is less than the provided dateTime
     */
    boolean lessThan(DateTimeValue dateTime);

    /**
     * Return true if this is greater than the provided dateTime
     *
     * @param dateTime the date time to compare this against
     * @return true if this is greater than the provided dateTime
     */
    boolean greaterThan(DateTimeValue dateTime);

    /**
     * Return the duration value representing the difference between this
     * DateTimeValue and the specified dateTime value
     *
     * @param dateTime the DateTimeValue to subtract from this date time value
     * @return a DurationValue representing the difference between the two
     *         dateTimeValues
     */
    DurationValue subtract(DateTimeValue dateTime);

}
