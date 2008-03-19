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

import java.util.TimeZone;

/**
 * Represents an XPath 2.0 time value.
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
public interface TimeValue extends AtomicValue {

    /**
     * Returns the hours component of the time. Hours are in the range
     * (0, 23).
     *
     * @return the hour number within the day
     */
    int getHours();

    /**
     * Returns the minutes component of the time. Minutes are in the range
     * (0, 59).
     *
     * @return the minute number within the hour
     */
    int getMinutes();

    /**
     * Returns the seconds component of the time. Seconds are in the
     * range (0, 59).
     *
     * @return the seconds within the minute
     */
    int getSeconds();

    /**
     * Returns the milliseconds component of the time. Milliseconds are in the
     * range (0, 999).
     *
     * @return the seconds within the minute
     */
    int getMilliseconds();

    /**
     * Indicates that a Timezone was specified as part of the representation of
     * this time object.
     *
     * @return whether the time was specified in UTC or not
     */
    boolean isTimezoned();

    /**
     * Returns the timezone for the time.
     *
     * @return the timezone for the time. May be null
     */
    TimeZone getTimeZone();

}
