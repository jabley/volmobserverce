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
 * Represents an XPath 2.0 date value.
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
public interface DateValue extends AtomicValue {

    /**
     * Returns the year value. May be negative for BC dates. Year 0000 is not
     * permitted.
     *
     * @return the year value
     */
    int getYear();

    /**
     * Returns the month number. Months are numbered from 1 through 12 (January
     * to December, respectively).
     *
     * @return the month number within the year
     */
    int getMonth();

    /**
     * Returns the day number. Day numbers are the day of the month and are
     * therefore in the range 1 to 28, 29, 30 or 31 (depending on the month and
     * year as normal).
     *
     * @return the day number within the month
     */
    int getDay();
}
