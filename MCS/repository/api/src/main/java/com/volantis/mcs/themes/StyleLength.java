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

import com.volantis.mcs.themes.values.LengthUnit;

/**
 * This interface represents the length value of a style property.
 */
public interface StyleLength extends StyleValue {
    /**
     * Get the value of the unit property.
     *
     * @return The value of the unit property.
     */
    LengthUnit getUnit();

    /**
     * Get the value of the number property.
     *
     * Use this method for any length except pixels (PX). For pixels use the
     * the {@link #pixels()} method.
     *
     * @return The value of the number property.
     */
    double getNumber();

    /**
     * Return the pixel representation for the number object. A call to this
     * method is valid only if the current unit type is LengthUnit.__PX.
     *
     * @return the pixel value rounded up to the nearest integer.
     */
    int pixels();

    String getPixelsAsString();
}
