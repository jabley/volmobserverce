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
import org.jdom.filter.ElementFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the action command used for each Column Insert action within the
 * Grid menu. It allows 1 or more columns to be inserted before or after the
 * column of the currently selected format (if appropriate). It is appropriate
 * to the Layout Outline page and the Layout Graphical Editor page. It is
 * enabled for a single grid cell format selection (where a grid cell format is
 * a format that is an immediate child of a grid row).
 */
public class ColumnInsertActionCommand extends LayoutActionCommand {

    /**
     * An element filter used to filter on elements. This is required for
     * skipping ODOMElement types such as ODOMText.
     */
    private static final ElementFilter ELEMENT_FILTER = new ElementFilter();

     /**
     * Initializes the new instance using the given parameters.
     * @param selectionManager selection manager that allows the
     *        action to select all new cells of all new columns. Cannot be null.
     */
    public ColumnInsertActionCommand(ODOMSelectionManager selectionManager) {
        super(selectionManager);
    }

    /**
     * Insert Column is only valid when there is a single selection and that
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
            // The grandparent is having columns inserted. It must be a grid or
            // segment grid.
            isAppropriate =
                    grandParentName.equals(FormatType.GRID.getElementName()) ||
                    grandParentName.equals(
                            FormatType.SEGMENT_GRID.getElementName());
        }
        return isAppropriate;
    }

    /**
     * Runs the InsertColumnActionCommand with the supplied details.
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
                int numColumns = insertDetails.getNumToInsert();
                boolean isBefore = insertDetails.isBefore();
                List selection =
                        insertColumns(details.getElement(0), numColumns,
                                isBefore);
                if (selectionManager != null) {
                    // Select all new cells of all new columns.
                    selectionManager.setSelection(selection);
                }
            }
        }
    }

    /**
     * Interactively determines the number of columns to add, and where.
     * See {@link RowColumnInsertionDialog}.
     *
     * @return the required insertion data or null if dialog was cancelled.
     */
    protected RowColumnInsertionDialog.InsertionData getInsertionDetails() {
        RowColumnInsertionDialog.InsertionData result = null;
        RowColumnInsertionDialog dialog =
                new RowColumnInsertionDialog(
                        Display.getCurrent().getActiveShell(),
                        RowColumnInsertionDialog.Type.COLUMN);
        int returnCode = dialog.open();

        if (returnCode == IDialogConstants.OK_ID) {
            result =
                    (RowColumnInsertionDialog.InsertionData)
                    dialog.getResult()[0];
        }
        return result;
    }

    /**
     * Inserts new columns into the grid with each column filled with empty
     * formats.
     * <strong>
     * {@link com.volantis.mcs.eclipse.ab.editors.layout.states.ColumnChangeState}
     * is tightly-coupled with this method's implementation.
     * </strong>
     * @param gridCell the selected cell about which to insert new columns
     * @param numNewColumns the number of new columns to insert
     * @param insertBefore true if the insertion is before the column
     *                          containing gridCell; false if after the cell
     * @return a list of all empty format cells of all new columns
     * @throws IllegalArgumentException if insertion cannot be performed for
     *         gridCell
     */
    private List insertColumns(Element gridCell, int numNewColumns,
                               boolean insertBefore) {
        Element grandParent = gridCell.getParent().getParent();
        List selectedElements = null;

        if (!selectionIsAppropriate(gridCell)) {
            throw new IllegalArgumentException(
                    "The action cannot insert a column in " +
                    grandParent.getName());
        } else {
            // Find the column position of the selected grid cell.
            int colPos =
                    gridCell.getParent().getContent(ELEMENT_FILTER).
                    indexOf(gridCell);

            if (!insertBefore) {
                // Skip over column containing gridElement cell
                colPos++;
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

            selectedElements = new ArrayList(numNewColumns * rowCount);

            // Update the gridElement with the new column count. This MUST be
            // done first so that the {@link GridFormatComposite} knows that
            // columns are being inserted.
            grandParent.setAttribute("columns",
                    String.valueOf(colCount + numNewColumns));

            String gridName = grandParent.getName();
            String colName = LayoutSchemaType.getGridColumnName(gridName);

            // Update the columns data first.
            List content = grandParent.getContent(ELEMENT_FILTER);
            ODOMElement colsElement = (ODOMElement) content.get(0);
            List colsContent = colsElement.getContent(ELEMENT_FILTER);
            for (int newCol = 0; newCol < numNewColumns; newCol++) {
                ODOMElement col =
                        (ODOMElement) FormatPrototype.factory.element(colName);
                colsContent.add(colPos + newCol, col);
            }

            // Add the new columns to each row.
            for (int row = 1; row <= rowCount; row++) {
                // Find each row element and its content.
                ODOMElement rowElement =
                        (ODOMElement) content.get(row);
                List rowContent = rowElement.getContent(ELEMENT_FILTER);
                for (int col = 0; col < numNewColumns; col++) {
                    ODOMElement empty = FormatPrototype.get(FormatType.EMPTY);
                    selectedElements.add(empty);
                    rowContent.add(colPos + col, empty);
                }
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

 04-Feb-04	2848/7	pcameron	VBM:2004020203 Element filters are now used in the ColumnInsertActionCommand

 04-Feb-04	2848/5	pcameron	VBM:2004020203 Added ColumnInsertActionCommand

 ===========================================================================
*/
