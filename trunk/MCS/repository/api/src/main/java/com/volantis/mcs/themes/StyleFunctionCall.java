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

import java.util.List;

/**
 * A value that represents a function call.
 */
public interface StyleFunctionCall extends StyleValue {
    /**
     * Get the name of the function being invoked.
     *
     * @return The name of the function.
     */
    String getName();

    /**
     * Get the arguments of the function being invoked.
     *
     * @return The arguments.
     */
    List getArguments();
}
