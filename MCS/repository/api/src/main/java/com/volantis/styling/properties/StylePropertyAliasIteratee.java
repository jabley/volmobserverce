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

package com.volantis.styling.properties;

import com.volantis.shared.iteration.IterationAction;

/**
 * Provides external control over an internal iterator over a sequence of
 * {@link StylePropertyAlias} instances.
 */
public interface StylePropertyAliasIteratee {

    /**
     * Called for each {@link StylePropertyAlias} in the sequence being iterated.
     *
     * @param property The {@link StylePropertyAlias} to process.
     *
     * @return A value that determines whether the iteration stops or continues
     * onto the next {@link StylePropertyAlias}.
     */
    IterationAction visit(StylePropertyAlias alias);
}
