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
 * This interface represents a keyword of a style property as an enumeration
 * enumerationValue.
 */
public interface StyleKeyword extends StyleValue {
    /**
     * Check to see whether the keyword is for internal use only.
     *
     * @return True if it is, false otherwise.
     */
    boolean isInternal();

    String getName();
}
