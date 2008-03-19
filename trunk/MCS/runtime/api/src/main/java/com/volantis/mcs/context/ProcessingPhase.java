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

package com.volantis.mcs.context;

/**
 * A type safe enumeration of the different processing phases of MCS.
 */
public class ProcessingPhase {

    /**
     * Phase 1 - this is the phase in which input markup is processed and
     * targeted at the layout.
     */
    public static final ProcessingPhase PHASE1 = new ProcessingPhase("PHASE1");

    /**
     * Phase 2 - this is the phase where the layout and its contents are
     * rendered into a single document which is then transformed into the
     * result that is sent to the device.
     */
    public static final ProcessingPhase PHASE2 = new ProcessingPhase("PHASE2");

    private final String name; // for debug only

    /**
     * Initialise.
     *
     * @param name The name of the phase.
     */
    private ProcessingPhase(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
