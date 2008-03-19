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

import com.volantis.mcs.eclipse.ab.editors.layout.FormatComposite;
import com.volantis.mcs.eclipse.ab.editors.layout.FormatCompositeModifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;

/**
 * The DeleteColumnsState is used by {@link RowChangeState} to delete columns
 * from the
 * {@link com.volantis.mcs.eclipse.ab.editors.layout.GridFormatComposite} of a
 * {@link FormatCompositeModifier}. The ColumnChangeState waits for a specified period
 * before transitioning to DeleteColumnsState (or InsertColumnsState). This
 * class is package local because it should only be used by ColumnChangeState,
 */
class DeleteColumnsState implements GridModifierState {

    /**
     * The next state for DeleteRowsState.
     */
    private GridModifierState nextState;

    /**
     * The FormatCompositeModifier used to access the GridFormatComposite's data.
     */
    private FormatCompositeModifier gridModifier;

    /**
     * Creates a DeleteRowsState for deleting the specified number of rows
     * in the GridFormatComposite of the supplied FormatCompositeModifier.
     * @param gridModifier the FormatCompositeModifier
     */
    DeleteColumnsState(FormatCompositeModifier gridModifier) {
        this.gridModifier = gridModifier;
    }

    /**
     * Processes the ODOMChangeEvent using the DeleteColumnsState.
     * @param event the event to process
     * @return the next state
     */
    public GridModifierState processEvent(ODOMChangeEvent event) {
        doColumnDeletion(event);

        return nextState;
    }

    // javadoc inherited
    public void setNextState(GridModifierState nextState) {
        this.nextState = nextState;
    }

    /**
     * Initiates the actual graphical column deletion.
     * @param event the ODOM change event associated with the last cell
     *              removed from the column.
     */
    private void doColumnDeletion(ODOMChangeEvent event) {
        ODOMElement source = (ODOMElement) event.getSource();
        // Find the FC associated with the source's element. This is needed
        // to determine which column was removed.
        FormatComposite childFC = gridModifier.getChildFCForElement(source);
        int[] columnsToDelete = new int[]{childFC.getColumn()};
        gridModifier.deleteColumns(columnsToDelete);
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
