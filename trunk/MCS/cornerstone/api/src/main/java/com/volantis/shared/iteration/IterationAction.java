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

package com.volantis.shared.iteration;

/**
 * An enumeration of the possible actions that should be taken on behalf of an
 * internal iterator.
 *
 * <p>An internal iterator is one where the iteration is performed internally
 * within the class whose contents are being iterated.</p>
 */
public class IterationAction {
    /**
     * End the iteration immediately.
     */
    public static final IterationAction BREAK = new IterationAction("BREAK");

    /**
     * Continue the iteration.
     */
    public static final IterationAction CONTINUE = new IterationAction("CONTINUE");

    /**
     * Name of the value.
     */
    private final String myName; // for debug only

    /**
     * Initialise.
     *
     * @param name The name of the value.
     */
    private IterationAction(String name) {
        myName = name;
    }

    /**
     * Override to provide debug information.
     *
     * @return The name of the value.
     */
    public String toString() {
        return myName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8856/2	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
