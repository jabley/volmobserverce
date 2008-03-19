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

package com.volantis.osgi.cm;

/**
 * The state of the configuration object.
 */
public class State {
    /**
     * The configuration is new, does not have any properties yet.
     */
    public static final State NEW = new State("NEW");

    /**
     * The configuration has been updated so it has properties.
     */
    public static final State ACTIVE = new State("ACTIVE");

    /**
     * The configuration has been deleted but it has not yet completed.
     */
    public static final State DELETING = new State("DELETING");

    /**
     * The configuration has been deleted but it has not yet completed.
     */
    public static final State DELETED = new State("DELETED");

    /**
     * The name.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name.
     */
    private State(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
