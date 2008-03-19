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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression;

/**
 * Encapsulates the information needed to control the response of the
 * XPath position function.
 *
 * @volantis-api-include-in InternalAPI
 */
public interface PositionScope {
    /**
     * Returns the current position value in the context. This starts at the
     * value of 0 and may be incremented in steps of 1 from there. To conform
     * with the XPath specification it would be necessary to perform pre-
     * increments.
     *
     * @return the current position value
     */
    int get();

    /**
     * Permits the position value to be incremented by one on each invocation.
     */
    void increment();
}
