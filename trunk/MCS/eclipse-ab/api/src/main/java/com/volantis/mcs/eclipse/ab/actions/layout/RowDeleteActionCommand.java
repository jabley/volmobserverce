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
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the action command used for each Row Delete action within the Grid
 * menu. It deletes the currently-selected row, but will not allow the new row
 * count to reach zero. It is appropriate to the Layout Outline page and the
 * Layout Graphical Editor page. It is enabled for a single grid cell format
 * selection (where a grid cell format is a format that is an immediate child
 * of a grid row).
 */
public class RowDeleteActionCommand extends LayoutActionCommand {

    /**
     * An element filter used to filter out all non element nodes.
     */
    private static final ElementFilter ELEMENT_FILTER = new ElementFilter();

    /**
     * Initializes the new instance using the given parameters.
     * @param selectionManager selection manager that allows the
     *        action to perform a selection after deletion.  Can not be null.
     */
    public RowDeleteActionCommand(ODOMSelectionManager selectionManager) {
        super(selectionManager);
    }

    /**
     * Delete Row is only valid when there is a single grid cell selected, and
     * when there is more than one row in the grid.
     * @return true if enabled; false otherwise.
     */
    public boolean enable(ODOMActionDetails details) {
        return ((details.getNumberOfElements() == 1) &&
                selectionIsAppropriate(details.getElement(0)));
    }

    /**
     * Supporting method that is used to make sure that the selection is
     * appropriate for this action command.
     *
     * @param selection the single selected element
     * @return true if the element's grandparent is a grid or segment grid.
     */
    private boolean selectionIsAppropriate(Element selection) {
        boolean isAppropriate = false;
        Element parent = selection.getParent();
        if (parent != null) {
            parent = parent.getParent();
        }
        if (parent != null) {
            String grandParentName = parent.getName();
            // The grandparent is having rows deleted. It must be a grid or
            // segment grid with more than one row.
            if (grandParentName.equals(FormatType.GRID.getElementName()) ||
                    grandParentName.equals(
                            FormatType.SEGMENT_GRID.getElementName())) {
                try {
                    int rowCount =
                            parent.getAttribute("rows").getIntValue();
                    isAppropriate = (rowCount > 1);
                } catch (DataConversionException e) {
                    // Safe to ignore as the selection will then not be
                    // appropriate.
                }
            }
        }
        return isAppropriate;
    }

    /**
     * Runs the DeleteRowActionCommand with the supplied details.
     * @param details the details needed for running the command
     */
    public void run(ODOMActionDetails details) {
        if (details.getNumberOfElements() != 1) {
            throw new IllegalStateException(
                    "Only a single selection should be available when the " +
                    "action is run (" + details.getNumberOfElements() +
                    " elements selected)");
        } else {
            List selection =
                    deleteRow(details.getElement(0));
            if (selectionManager != null) {
                // Select the selection post-deletion.
                selectionManager.setSelection(selection);
            }
        }
    }

    /**
     * Deletes the row of the currently-selected grid cell from the grid.
     * <strong>
     * {@link com.volantis.mcs.eclipse.ab.editors.layout.states.RowChangeState}
     * is tightly-coupled with this method's implementation.
     * </strong>
     * @param gridCell the grid cell whose row is to be deleted
     * @return a list of the cells to select, post-deletion
     * @throws IllegalArgumentException if deletion cannot be performed for
     *         gridCell
     */
    private List deleteRow(Element gridCell) {
        List selectedElements = new ArrayList(1);
        Element grandParent = gridCell.getParent().getParent();

        int rowCount = 0;
        try {
            rowCount = grandParent.getAttribute("rows").getIntValue();
        } catch (DataConversionException e) {
            throw new UndeclaredThrowableException(e);
        }

        List gridContent = grandParent.getContent(ELEMENT_FILTER);

        // The row element to delete.
        Element rowElement = gridCell.getParent();

        // Get the column position of the grid cell.
        int colPos = rowElement.getContent(ELEMENT_FILTER).indexOf(gridCell);
        // Get the row position of the grid cell.
        int rowPos = gridContent.indexOf(rowElement);

        // Update the grid with the new row count. This must be done first so
        // that the {@link GridFormatComposite} knows that a row is being
        // deleted.
        grandParent.setAttribute("rows", String.valueOf(--rowCount));

        // Remove the row element.
        rowElement.detach();

        // Find the "next" appropriate row in which to select, post delete.
        if (rowPos == gridContent.size()) {
            rowPos--;
        }

        // Find the cell to return for selection.
        Element selectionRow = (Element) gridContent.get(rowPos);
        Element selectedCell =
                (Element) selectionRow.getContent(ELEMENT_FILTER).get(colPos);
        selectedElements.add(selectedCell);

        return selectedElements;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-04	4886/1	allan	VBM:2004052812 Workaround TreeItem.getItems() bug and tidy up.

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 13-Feb-04	2915/1	pcameron	VBM:2004020905 Used LayoutSchemaType for attribute names

 09-Feb-04	2906/3	pcameron	VBM:2004020204 Removed one use of UndeclaredThrowableException

 05-Feb-04	2875/1	pcameron	VBM:2004020202 Added RowDeleteAction

 ===========================================================================
*/
