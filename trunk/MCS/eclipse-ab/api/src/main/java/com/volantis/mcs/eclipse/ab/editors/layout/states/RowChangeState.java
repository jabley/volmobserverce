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

import com.volantis.mcs.eclipse.ab.editors.layout.FormatCompositeModifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;

/**
 * The RowChangeState is transitioned to when row insertions or deletions
 * have been detected. Since these operations are not atomic, a sequence of
 * ODOMChangeEvents will be generated. The RowChangeState knows how to process
 * this sequence.
 */
public class RowChangeState implements GridModifierState {

    /**
     * The FormatCompositeModifier used to access the GridFormatComposite's data.
     */
    private FormatCompositeModifier gridModifier;

    /**
     * The next state for RowChangeState which is returned to the caller.
     */
    private GridModifierState nextState;

    /**
     * The row modification state to use: either InsertRowsState or
     * DeleteRowsState.
     */
    private GridModifierState rowModificationState;

    /**
     * Creates a RowChangeState using the supplied FormatCompositeModifier.
     * @param gridModifier the FormatCompositeModifier
     */
    public RowChangeState(FormatCompositeModifier gridModifier) {
        this.gridModifier = gridModifier;
    }

    /**
     * Processes the RowChangeState. There is a sequence of events generated
     * from row insertions and deletions. All but the last of these events are
     * ignored. To ignore them, a WaitState is created, initialised with the
     * number of events to ignore. The WaitState becomes the next state of this
     * RowChangeState. The next state of the WaitState is either an
     * InsertRowsState or a DeleteRowsState, depending on the change
     * encapsulated in the ODOMChangeEvent. When the WaitState detects that it
     * has ceased waiting, it immediately processes its next state: the row
     * insertions or deletions.
     * @param event the ODOMChangeEvent to process
     * @return the WaitState initialised with the number of events to ignore
     */
    public GridModifierState processEvent(ODOMChangeEvent event) {
        int oldCount =
                Integer.parseInt(event.getOldValue().toString());
        int newCount =
                Integer.parseInt(event.getNewValue().toString());
        int numChanges = newCount - oldCount;

        // Calculate the number of events to ignore. This is tightly-coupled
        // with the implementation of the {@link RowInsertActionCommand} (or
        // the {@link RowDeleteActionCommand}. These action commands process
        // row insertions or deletions by:
        //
        // 1. Setting the rows attribute to the new value. This generates one
        //    ODOMChangeEvent, which this RowChangeState is dealing with right
        //    now.
        // 2. For each new row (or row to be deleted), add (or detach) the row.
        //    This generates numChanges ODOMChangeEvents, where numChanges is
        //    the number of insertions (or deletions).
        //
        // Therefore, the number of events to ignore is the absolute value of
        // the change in the attribute's value.
        int numEventsToIgnore = Math.abs(numChanges);

        if (numChanges > 0) {
            rowModificationState =
                    new InsertRowsState(gridModifier, numChanges);
        } else {
            rowModificationState =
                    new DeleteRowsState(gridModifier);
        }
        // The next state of the InsertRowsState or DeleteRowsState is
        // actually the next state of the RowChangeState set by the caller.
        rowModificationState.setNextState(nextState);
        GridModifierState waitState = new WaitState(numEventsToIgnore);
        waitState.setNextState(rowModificationState);

        return waitState;
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

 03-Aug-04	4902/2	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 13-Feb-04	2915/3	pcameron	VBM:2004020905 Used LayoutSchemaType for attribute names

 12-Feb-04	2915/1	pcameron	VBM:2004020905 Refactored GridFormatComposite to use the State pattern for grid mods

 ===========================================================================
*/
