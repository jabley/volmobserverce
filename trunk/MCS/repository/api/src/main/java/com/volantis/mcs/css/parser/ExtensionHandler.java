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

package com.volantis.mcs.css.parser;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.Priority;

import java.util.Collection;

/**
 * Handles extensions to the CSS language.
 *
 * @mock.generate
 */
public interface ExtensionHandler {

    /**
     * Converts the token into a {@link StyleValue}.
     *
     * @param token The input token.
     * @return The style value, if null then it is assumed that the token is
     *         invalid and a syntax error will be thrown.
     */
    StyleValue customValue(String token);

    /**
     * Converts the token into a {@link Priority}.
     *
     * @param token The input token.
     * @return The priority, if null then it is assumed that the token is
     *         invalid and a syntax error will be thrown.
     */
    Priority customPriority(String token);

    /**
     * The set of supported custom priorities.
     *
     * @return A {@link Collection} of the supported priorities as
     *         {@link String}.
     */
    Collection getCustomPriorities();

    /**
     * The set of supported custom values.
     *
     * @return A {@link Collection} of the supported values as
     *         {@link String}.
     */
    Collection getCustomValues();
}
