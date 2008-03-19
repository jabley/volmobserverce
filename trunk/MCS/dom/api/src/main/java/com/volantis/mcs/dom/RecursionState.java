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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

/**
 * Enumeration of the state of a recursing visitor.
 */
class RecursionState {

    /**
     * Recursing visitor is continuing as normal.
     */
    public static final RecursionState CONTINUE =
            new RecursionState("CONTINUE");

    /**
     * Recursing visitor is skipping the siblings.
     */
    public static final RecursionState SKIPPING_SIBLINGS =
            new RecursionState("SKIPPING_SIBLINGS");

    /**
     * Recursing visitor is skipping all remaining nodes.
     */
    public static final RecursionState SKIPPING_REMAINING =
            new RecursionState("SKIPPING_REMAINING");

    /**
     * The name.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name.
     */
    private RecursionState(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
