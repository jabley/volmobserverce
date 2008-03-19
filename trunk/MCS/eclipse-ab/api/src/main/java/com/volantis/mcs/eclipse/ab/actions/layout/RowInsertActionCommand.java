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
import com.volantis.mcs.eclipse.ab.editors.layout.RowColumnInsertionDialog;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.jdom.DataConversionException;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the action command used for each Row Insert action within the Grid
 * menu. It allows 1 or more rows to be inserted before or after the row of the
 * currently selected format (if appropriate). It is appropriate to the Layout
 * Outline page and the Layout Graphical Editor page. It is enabled for a
 * single grid cell format selection (where a grid cell format is a format that
 * is an immediate child of a grid row).
 */
public class RowInsertActionCommand extends LayoutActionCommand {

    /**
     * Initializes the new instance using the given parameters.
     * @param selectionManager selection manager that allows the
     *        action to select all new cells of all new rows. Can not be null.
     */
    public RowInsertActionCommand(ODOMSelectionManager selectionManager) {
        super(selectionManager);
    }

    /**
     * Insert Row is only valid when there is a single selection and that
     * selection is a grid cell.
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
            // The grandparent is having rows inserted. It must be a grid or
            // segment grid.
            isAppropriate =
                    grandParentName.equals(FormatType.GRID.getElementName()) ||
                    grandParentName.equals(FormatType.SEGMENT_GRID.getElementName());
        }
        return isAppropriate;
    }

    /**
     * Runs the InsertRowActionCommand with the supplied details.
     * @param details the details needed for running the command
     */
    public void run(ODOMActionDetails details) {
        if (details.getNumberOfElements() != 1) {
            throw new IllegalStateException(
                    "Only a single selection should be available when the " +
                    "action is run (" + details.getNumberOfElements() +
                    " elements selected)");
        } else {
            RowColumnInsertionDialog.InsertionData insertDetails =
                    getInsertionDetails();
            if (insertDetails != null) {
                int numRows = insertDetails.getNumToInsert();
                boolean isBefore = insertDetails.isBefore();
                List selection =
                        insertRows(details.getElement(0), numRows, isBefore);
                if (selectionManager != null) {
                    // Select all new cells of all new rows.
                    selectionManager.setSelection(selection);
                }
            }
        }
    }

    /**
     * Interactively determines the number of rows to add, and where.
     * See {@link RowColumnInsertionDialog}.
     *
     * @return the required insertion data or null if dialog was cancelled.
     */
    protected RowColumnInsertionDialog.InsertionData getInsertionDetails() {
        RowColumnInsertionDialog.InsertionData result = null;
        RowColumnInsertionDialog dialog =
                new RowColumnInsertionDialog(
                        Display.getCurrent().getActiveShell());
        int returnCode = dialog.open();

        if (returnCode == IDialogConstants.OK_ID) {
            result =
                    (RowColumnInsertionDialog.InsertionData) dialog.getResult()[0];
        }
        return result;
    }

    /**
     * Inserts new rows into the grid with each row filled with empty formats.
     * <strong>
     * {@link com.volantis.mcs.eclipse.ab.editors.layout.states.RowChangeState}
     * is tightly-coupled with this method's implementation.
     * </strong>
     * @param gridCell the selected cell about which to insert new rows
     * @param numNewRows the number of new rows to insert
     * @param insertBefore true if the insertion is before the row containing
     *        gridCell; false if after the cell
     * @return a list of all empty format cells of all new rows
     * @throws IllegalArgumentException if insertion cannot be performed for
     *         gridCell
     */
    private List insertRows(Element gridCell, int numNewRows,
                            boolean insertBefore) {
        Element grandParent = gridCell.getParent().getParent();
        List selectedElements = null;

        if (!selectionIsAppropriate(gridCell)) {
            throw new IllegalArgumentException(
                    "The action cannot insert a row in " +
                    grandParent.getName());
        } else {
            Element rowElement = gridCell.getParent();

            List content = grandParent.getContent();
            int rowPos = content.indexOf(rowElement);

            if (!insertBefore) {
                // Skip over rowElement containing gridElement cell
                rowPos++;
            }

            int rowCount = 1;
            int colCount = 1;
            try {
                rowCount =
                        grandParent.getAttribute("rows").getIntValue();
                colCount =
                        grandParent.getAttribute("columns").getIntValue();
            } catch (DataConversionException e) {
                throw new UndeclaredThrowableException(e);
            }

            selectedElements = new ArrayList(numNewRows * colCount);

            // Update the gridElement with the new rowElement count. This must
            // be done first so that the {@link GridFormatComposite} knows that
            // rows are being inserted.
            grandParent.setAttribute("rows",
                    String.valueOf(rowCount + numNewRows));

            String gridName = grandParent.getName();
            String rowName = LayoutSchemaType.getGridRowName(gridName);

            // Create and add the new rows to the grid.
            for (int newRow = 0; newRow < numNewRows; newRow++) {
                ODOMElement newRowElement = (ODOMElement)
                        FormatPrototype.factory.element(rowName);
                // Fill each row with an empty format.
                for (int col = 0; col < colCount; col++) {
                    Element empty = FormatPrototype.get(FormatType.EMPTY);
                    newRowElement.addContent(empty);
                    selectedElements.add(empty);
                }
                // Add the new row at the correct position.
                content.add(rowPos + newRow, newRowElement);
            }
        }
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

 09-Feb-04	2881/1	pcameron	VBM:2004020515 Added graphical row and column deletion

 04-Feb-04	2848/3	pcameron	VBM:2004020203 Added ColumnInsertActionCommand

 03-Feb-04	2824/12	pcameron	VBM:2004020201 Changed the selection of new row elements

 03-Feb-04	2824/9	pcameron	VBM:2004020201 A few tweaks

 03-Feb-04	2824/7	pcameron	VBM:2004020201 Added RowInsertActionCommand

 03-Feb-04	2824/5	pcameron	VBM:2004020201 Added RowInsertActionCommand

 03-Feb-04	2824/3	pcameron	VBM:2004020201 Added RowInsertActionCommand

 ===========================================================================
*/
