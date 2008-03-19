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

/**
 */
public interface StyleColorPercentages extends StyleColor {
    /**
     * Get the percentage value for the red color component.
     *
     * @return The percentage value.
     */
    double getRed();

    /**
     * Get the percentage value for the green color component.
     *
     * @return The percentage value.
     */
    double getGreen();

    /**
     * Get the percentage value for the blue color component.
     *
     * @return The percentage value.
     */
    double getBlue();
}