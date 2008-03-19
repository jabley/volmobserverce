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
package com.volantis.mcs.migrate.api.framework;

/**
 * Enumeration representing the types of migration steps.
 */
public class StepType {

    /**
     * The first step in a sequence
     */
    public static final StepType FIRST =
        new StepType("First step", true, false);

    /**
     * The last step in a sequence
     */
    public static final StepType LAST =
        new StepType("Last step", false, true);

    /**
     * The only step in a sequence, both first and last
     */
    public static final StepType ONLY =
        new StepType("Only step", true, true);

    /**
     * The intermediate step
     */
    public static final StepType INTERMEDIATE =
        new StepType("Intermediate step", false, false);

    // Textual representation of the type of step, used for debugging only
    private String type;

    /**
     * Is this the first step
     */
    private final boolean first;

    /**
     * Is this the last step
     */
    private final boolean last;

    /**
     * Given two booleans return the correct StepType object.
     *
     * @param first denotes that the returned StepType should have first set
     * @param last denotes that the returned StepType should have last set
     * @return the correct StepType enum
     */
    public static StepType getType(boolean first, boolean last) {

        StepType selected = null;

        if (first && last) {
            selected = ONLY;
        } else if (first && !last) {
            selected = FIRST;
        } else if (!first && !last) {
            selected = INTERMEDIATE;
        } else if (!first && last) {
            selected = LAST;
        }
        return selected;
    }

    // Prevent object construction
    private StepType(String type, boolean first, boolean last) {
        this.type = type;
        this.first = first;
        this.last = last;
    }

    /**
     * Returns true if this a first step.
     *
     * @return true if this is a first or only step.
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * Returns true if this is a last step.
     *
     * @return true if this is a last or only step.
     */
    public boolean isLast() {
        return last;
    }

    /**
     * Return the type of the instance.
     *
     * @return string name of the type
     */
    public String toString() {
        return type;
    }

}
