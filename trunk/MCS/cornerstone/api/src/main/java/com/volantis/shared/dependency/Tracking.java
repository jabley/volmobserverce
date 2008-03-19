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

package com.volantis.shared.dependency;

/**
 * An enumeration of the different types of dependency tracking that can be
 * done within the {@link DependencyContext}.
 */
public class Tracking {

    /**
     * A tracker that inherits the type of the containing tracker.
     */
    public static final Tracking INHERIT = new Tracking("INHERIT");

    /**
     * A tracker that will track all added dependencies.
     */
    public static final Tracking ENABLED = new Tracking("ENABLED");

    /**
     * A tracker that will not track any dependencies and will ignore the
     * ones that are added.
     */
    public static final Tracking DISABLED = new Tracking("DISABLED");

    /**
     * The name of the enumeration, for debug only.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name  The name of the enumeration.
     */
    private Tracking(String name) {
        this.name = name;  
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
