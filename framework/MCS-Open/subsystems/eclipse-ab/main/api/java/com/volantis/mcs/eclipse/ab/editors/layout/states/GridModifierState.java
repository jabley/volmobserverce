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
 * The interface describing
 * {@link com.volantis.mcs.eclipse.ab.editors.layout.FormatCompositeModifier} state
 * behaviour. The GridModifierState implementations are used by FormatCompositeModifier
 * when it is processing row and column insertions and deletions. Since these
 * operations are not atomic, the State pattern is used to implement state
 * processing and transitions. The GridModifierState processes
 * {@link ODOMChangeEvent}s.
 */
public interface GridModifierState {

    /**
     * Sets the next state of the GridModifierState. This state is returned
     * as the result of processing the state.
     * @param nextState the next state
     */
    void setNextState(GridModifierState nextState);

    /**
     * Process the GridModifierState and return the next state.
     * @param event the ODOMChangeEvent to process
     * @return the next state
     */
    GridModifierState processEvent(ODOMChangeEvent event);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Aug-04	4902/1	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 12-Feb-04	2915/1	pcameron	VBM:2004020905 Refactored GridFormatComposite to use the State pattern for grid mods

 ===========================================================================
*/
