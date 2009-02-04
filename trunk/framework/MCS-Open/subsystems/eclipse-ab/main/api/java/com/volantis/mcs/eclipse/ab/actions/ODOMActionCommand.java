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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.actions;

/**
 * The methods on this interface are invoked by the associated {@link
 * ODOMAction} to determine the action's enablement status and to perform
 * the action's processing.
 */
public interface ODOMActionCommand {
    /**
     * Returns true when the action should be enabled, false otherwise.
     *
     * @param context contains contextual information associated with the
     *                action
     * @return true if the action can be performed
     */
    boolean enable(ODOMActionDetails context);

    /**
     * Performs the action's processing.
     *
     * @param context contains contextual information associated with the
     *                action
     */
    void run(ODOMActionDetails context);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
