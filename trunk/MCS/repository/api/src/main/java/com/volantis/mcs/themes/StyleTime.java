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
package com.volantis.mcs.themes;

import com.volantis.mcs.themes.values.TimeUnit;

/**
 * This interface represents the time value of a style property.
 */
public interface StyleTime extends StyleValue {
    /**
     * Get the value of the unit property.
     *
     * @return The value of the unit property.
     */
    TimeUnit getUnit();

    /**
     * Get the value of the number property.
     *
     * @return The value of the number property.
     */
    double getNumber();
}
