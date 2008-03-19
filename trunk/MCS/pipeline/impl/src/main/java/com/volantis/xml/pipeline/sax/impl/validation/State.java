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

package com.volantis.xml.pipeline.sax.impl.validation;



/**
 * The state of the template model while processing the template elements.
 *
 * @todo should use facilities provided by xml-validation subsystem.
 */
public abstract class State {

    /**
     * Expects nothing, the pipeline has finished.
     */
    public static final State DONE =
            new State("DONE") {
                public State transition(Event event) {
                    return null;
                }
            };

    /**
     * The name of the enumeration.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name of the enumeration.
     */
    public State(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }

    /**
     * Transition to another state based on the event.
     *
     * @param event The event, may not be null.
     * @return The new state, null if there is no transition.
     */
    public abstract State transition(Event event);
}
