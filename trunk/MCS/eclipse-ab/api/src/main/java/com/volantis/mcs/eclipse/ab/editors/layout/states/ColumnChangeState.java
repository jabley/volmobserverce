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
 * The ColumnChangeState is transitioned to when column insertions or deletions
 * have been detected. Since these operations are not atomic, a sequence of
 * ODOMChangeEvents will be generated. The ColumnChangeState knows how to
 * process this sequence.
 */
public class ColumnChangeState implements GridModifierState {

    /**
     * The FormatCompositeModifier used to access the GridFormatComposite's data.
     */
    private FormatCompositeModifier gridModifier;

    /**
     * The next state of the ColumnChangeState which is returned to the caller.
     */
    private GridModifierState nextState;

    /**
     * The row modification state to use: either InsertColumnsState or
     * DeleteColumnsState.
     */
    private GridModifierState columnModificationState;

    /**
     * Creates a ColumnChangeState using the supplied FormatCompositeModifier.
     * @param gridModifier the FormatCompositeModifier
     */
    public ColumnChangeState(FormatCompositeModifier gridModifier) {
        this.gridModifier = gridModifier;
    }

    /**
     * Processes the ColumnChangeState. There is a sequence of events generated
     * from column insertions and deletions. All but the last of these events
     * are ignored. To ignore them, a WaitState is created, initialised with
     * the number of events to ignore. The WaitState becomes the next state of
     * this ColumnChangeState. The next state of the WaitState is either an
     * InsertColumnsState or a DeleteColumnsState, depending on the change
     * encapsulated in the ODOMChangeEvent. When the WaitState detects that it
     * has ceased waiting, it immediately processes its next state: the column
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
        // with the implementation of the {@link ColumnInsertActionCommand} (or
        // the {@link ColumnDeleteActionCommand}. These action commands process
        // columns insertions or deletions by:
        //
        // 1. Setting the columns attribute to the new value. This generates
        //    one ODOMChangeEvent, which this ColumnChangeState is dealing with
        //    right now.
        // 2. The "columns" element is updated by adding or removing numChanges
        //    "column" elements to it. This generates numChanges
        //     ODOMChangeEvents.
        // 3. For each row, add (or detach) the numChanges elements to (from)
        //    the row element. This generates another numChanges
        //    ODOMChangeEvents per row.
        //
        // Therefore, the number of events to ignore is:
        //     numChanges * (1 + numRows)
        int numEventsToIgnore = Math.abs(numChanges) *
                (1 + gridModifier.getNumRows());

        if (numChanges > 0) {
            columnModificationState =
                    new InsertColumnsState(gridModifier, numChanges);
        } else {
            columnModificationState =
                    new DeleteColumnsState(gridModifier);
        }
        // The next state of the InsertColumnsState or DeleteColumnsState is
        // actually the next state of the ColumnChangeState set by the caller.
        columnModificationState.setNextState(nextState);
        GridModifierState waitState = new WaitState(numEventsToIgnore);
        waitState.setNextState(columnModificationState);

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
