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
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import java.util.Iterator;
import java.util.List;

/**
 * The InsertRowsState is used by {@link RowChangeState} to insert rows
 * into the
 * {@link com.volantis.mcs.eclipse.ab.editors.layout.FormatComposite} of a
 * {@link FormatCompositeModifier}. The RowChangeState waits for a specified period before
 * transitioning to InsertRowsState (or DeleteRowsState). This class is package
 * local because it should only be used by RowChangeState,
 */
class InsertRowsState implements GridModifierState {

    /**
     * An element filter used to filter out all non element nodes.
     */
    private static final ElementFilter ELEMENT_FILTER = new ElementFilter();

    /**
     * The next state for InsertRowsState.
     */
    private GridModifierState nextState;

    /**
     * The FormatCompositeModifier used to access the GridFormatComposite's data.
     */
    private FormatCompositeModifier formatCompositeModifier;

    /**
     * The number of rows to insert.
     */
    private int noOfRowsToInsert;

    /**
     * Creates an InsertRowsState for inserting the specified number of rows
     * in the FormatComposite of the supplied FormatCompositeModifier.
     * @param formatCompositeModifier the FormatCompositeModifier
     * @param noOfRowsToInsert the number of rows to insert
     */
    InsertRowsState(FormatCompositeModifier formatCompositeModifier,
                    int noOfRowsToInsert) {
        this.formatCompositeModifier = formatCompositeModifier;
        this.noOfRowsToInsert = noOfRowsToInsert;
    }

    /**
     * Processes the ODOMChangeEvent using the InsertRowsState.
     * @param event the event to process
     * @return the next state
     */
    public GridModifierState processEvent(ODOMChangeEvent event) {
        doRowInsertion(event);
        return nextState;
    }

    // javadoc inherited
    public void setNextState(GridModifierState nextState) {
        this.nextState = nextState;
    }

    /**
     * Finds the position for the first insertion and updates the grid.
     * @param event the ODOMChangeEvent with the update details.
     */
    private void doRowInsertion(ODOMChangeEvent event) {
        ODOMElement row = (ODOMElement) event.getSource();

        List rows =
                formatCompositeModifier.getFormatComposite().getRowElements();

        int firstRowInsertionPos = rows.indexOf(row);

        firstRowInsertionPos -= noOfRowsToInsert - 1;

        formatCompositeModifier.insertRows(firstRowInsertionPos,
                getInsertedElements(firstRowInsertionPos));
    }

    /**
     * Provide the emptyFormat elements that were inserted as a result of
     * the action that caused this state to process an event.
     * @param start the start index of the inserted columns
     * @return an array of the Elements that were inserted
     */
    private ODOMElement[] getInsertedElements(int start) {

        List gridRowFormats =
                formatCompositeModifier.getFormatComposite().getRowElements();

        // Determine the number of columns from the number of elements in
        // a row format.
        Element rowFormat = (Element) gridRowFormats.get(0);
        int columns = rowFormat.getContent(ELEMENT_FILTER).size();

        ODOMElement[] insertedElements =
                new ODOMElement[columns * noOfRowsToInsert];

        // It is assumed that all the inserted elements start at start and
        // continue sequentially in rows for the number of rows inserted
        // Loop through each grid row.
        int insertIndex = 0;
        for (int row = start; row < start + noOfRowsToInsert; row++) {
            Element insertedRow = (Element) gridRowFormats.get(row);
            Iterator rowFormats =
                    insertedRow.getContent(ELEMENT_FILTER).iterator();
            while (rowFormats.hasNext()) {
                insertedElements[insertIndex] =
                        (ODOMElement) rowFormats.next();
                insertIndex++;
            }
        }

        return insertedElements;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Aug-04	4902/4	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 24-Feb-04	3021/5	pcameron	VBM:2004020211 Some tweaks to StyledGroup and FormatComposites

 24-Feb-04	3021/3	pcameron	VBM:2004020211 Added StyledGroup and background colours

 19-Feb-04	3021/1	pcameron	VBM:2004020211 Committed for integration

 12-Feb-04	2915/1	pcameron	VBM:2004020905 Refactored GridFormatComposite to use the State pattern for grid mods

 ===========================================================================
*/
