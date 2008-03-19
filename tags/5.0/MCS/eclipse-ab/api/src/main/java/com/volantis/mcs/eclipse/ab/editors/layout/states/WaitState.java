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
package com.volantis.mcs.eclipse.ab.editors.layout.states;

import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;

/**
 * The WaitState waits for a specified number of {@link ODOMChangeEvent}s to
 * occur. Except for the last event, each is ignored. When the last event is
 * received, it is immediately processed by the WaitState's next state. This
 * sequence of state transitions is used by {@link RowChangeState} and
 * {@link ColumnChangeState} to process a sequence of ODOMChangeEvents where,
 * all but the last event is ignored.
 */
public class WaitState implements GridModifierState {
    /**
     * The number of ODOMChangeEvents to ignore.
     */
    private int wait;

    /**
     * The next state for WaitState.
     */
    private GridModifierState nextState;

    /**
     * Creates a new WaitState to wait on <code>wait</code> ODOMChangeEvents.
     * @param wait the number of events to wait for
     */
    public WaitState(int wait) {
        this.wait = wait;
    }

    /**
     * Decrement the wait. If the wait reaches 0, process <code>event</code>
     * using the WaitState's next state, and return the result of this
     * processing as the next state. Otherwise, return the WaitState itself
     * as the next state.
     * @param event the ODOMChangeEvent to process
     * @return the next state
     */
    public GridModifierState processEvent(ODOMChangeEvent event) {
        GridModifierState newState = this;
        if (--wait < 0) {
            wait = 0;
        }
        if (wait == 0) {
            // The waiting is over, so process the last event with the
            // next state.
            newState = nextState.processEvent(event);
        }
        
        return newState;
    }

    // javadoc inherited
    public void setNextState(GridModifierState nextState) {
        this.nextState = nextState;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Feb-04	2915/3	pcameron	VBM:2004020905 Used LayoutSchemaType for attribute names

 12-Feb-04	2915/1	pcameron	VBM:2004020905 Refactored GridFormatComposite to use the State pattern for grid mods

 ===========================================================================
*/
