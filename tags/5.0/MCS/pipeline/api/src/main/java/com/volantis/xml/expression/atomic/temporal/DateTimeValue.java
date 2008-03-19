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
package com.volantis.xml.expression.atomic.temporal;

import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.AtomicValue;

import java.util.TimeZone;
import java.util.Calendar;

/**
 * Represents an XPath 2.0 dateTime value.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface DateTimeValue extends AtomicValue, TimeValue, DateValue {

    /**
     * Provides the internal "time-lined" value derived from the date/time
     * specification.
     *
     * @return the internal "time-lined" value that can be used to compare
     *         date/times with each other
     */
    double getTimeOnTimeline();

    /**
     * Indicates that the date/time was specified in UTC (if true) or in some
     * other timezone (if false). If false then the time may represent a value
     * including any appropriate daylight savings based on the date and the
     * daylight savings adjustments for that timezone.
     *
     * @return whether the date/time was specified in UTC or not
     */
    boolean isTimezoned();

    /**
     * Returns the timezone for the time.
     *
     * @return the timezone for the time. Will not be null
     */
    TimeZone getTimeZone();

    /**
     * Return the dateTime as a calendar instance. Changes to this calendar
     * will not be reflected in the DateTimeValue.
     *
     * @return a Calendar representing this DateTimeValue.
     */
    Calendar getCalendar();
}
