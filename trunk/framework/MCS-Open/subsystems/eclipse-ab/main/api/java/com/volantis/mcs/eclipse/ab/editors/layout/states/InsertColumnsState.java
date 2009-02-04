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

import java.util.List;

/**
 * The InsertColumnsState is used by {@link ColumnChangeState} to insert
 * columns into the
 * {@link com.volantis.mcs.eclipse.ab.editors.layout.FormatComposite} of a
 * {@link FormatCompositeModifier}. The ColumnChangeState waits for a specified period
 * before transitioning to InsertColumnsState (or DeleteColumnsState). This
 * class is package local because it should only be used by ColumnChangeState,
 */
class InsertColumnsState implements GridModifierState {

    /**
     * An element filter used to filter out all non element nodes.
     */
    private static final ElementFilter ELEMENT_FILTER = new ElementFilter();

    /**
     * The next state for InsertColumnsState.
     */
    private GridModifierState nextState;

    /**
     * The FormatCompositeModifier used to access the GridFormatComposite's data.
     */
    private FormatCompositeModifier formatCompositeModifier;

    /**
     * The number of columns to insert.
     */
    private int noOfColumnsToInsert;

    /**
     * Creates an InsertColumnsState for inserting the specified number of
     * columns in the GridFormatComposite of the supplied FormatCompositeModifier.
     * @param formatCompositeModifier the FormatCompositeModifier
     * @param noOfColumnsToInsert the number of columns to insert
     */
    InsertColumnsState(FormatCompositeModifier formatCompositeModifier,
                       int noOfColumnsToInsert) {
        this.formatCompositeModifier = formatCompositeModifier;
        this.noOfColumnsToInsert = noOfColumnsToInsert;
    }

    /**
     * Processes the ODOMChangeEvent using the InsertColumsState.
     * @param event the event to process
     * @return the next state
     */
    public GridModifierState processEvent(ODOMChangeEvent event) {
        doColumnInsertion(event);
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
    private void doColumnInsertion(ODOMChangeEvent event) {
        ODOMElement source = (ODOMElement) event.getSource();
        ODOMElement parent = (ODOMElement) event.getNewValue();
        int firstColumnInsertionPos = parent.getContent(ELEMENT_FILTER).
                indexOf(source);
        firstColumnInsertionPos -= noOfColumnsToInsert - 1;
        formatCompositeModifier.insertColumns(firstColumnInsertionPos,
                getInsertedElements(firstColumnInsertionPos));
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

        ODOMElement[] insertedElements =
                new ODOMElement[gridRowFormats.size() * noOfColumnsToInsert];

        // It is assumed that all the inserted elements start at start and
        // continue sequentially in columns for the number of columns inserted
        // Loop through each grid row. This is not a particularly effiecient
        // way to build the array but is necessary so that users of the
        // array know how many columns there are to insert and how to insert
        // them.
        int insertIndex = 0;
        int noOfRows = gridRowFormats.size();
        for(int column=start; column<start+noOfColumnsToInsert; column++) {
            for(int row=0; row<noOfRows; row++) {
                Element gridRowFormat = (Element) gridRowFormats.get(row);
                List rowFormats = gridRowFormat.getContent(ELEMENT_FILTER);
                insertedElements[insertIndex] =
                        (ODOMElement) rowFormats.get(column);
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

 03-Aug-04	4902/5	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 24-Feb-04	3021/5	pcameron	VBM:2004020211 Some tweaks to StyledGroup and FormatComposites

 24-Feb-04	3021/3	pcameron	VBM:2004020211 Added StyledGroup and background colours

 19-Feb-04	3021/1	pcameron	VBM:2004020211 Committed for integration

 12-Feb-04	2915/1	pcameron	VBM:2004020905 Refactored GridFormatComposite to use the State pattern for grid mods

 ===========================================================================
*/
