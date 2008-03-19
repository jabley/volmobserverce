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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.controls.MessageAreaSelectionDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.awt.*;
import java.text.MessageFormat;

/**
 * This dialog is used to gather grid dimensions.
 */
public class GridDimensionsDialog extends MessageAreaSelectionDialog {
    /**
     * Typesafe enumeration that defines the types of dialog that can be shown.
     */
    public final static class Type {
        /**
         * Show as an "Add" dialog for adding grids.
         */
        public static final Type ADD = new Type(""); //$NON-NLS-1$

        /**
         * Show as a "Wrap" dialog for wrapping a format in a new grid.
         */
        public static final Type WRAP = new Type("wrapIn"); //$NON-NLS-1$

        /**
         * Internal value for use by the dialog in determining the title for
         * the dialog given the type required.
         */
        final String key;

        /**
         * Private to ensure that the literals above are the only instances.
         *
         * @param key the key for the given instance
         */
        private Type(String key) {
            this.key = key;
        }
    }

    /**
     * Resource prefix for the dialog.
     */
    private final static String RESOURCE_PREFIX = "GridDimensionsDialog."; //$NON-NLS-1$

    /**
     * The title of the dialog.
     */
    private final String DIALOG_TITLE;

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
        LayoutMessages.getInteger(RESOURCE_PREFIX +
                                  "horizontalSpacing").intValue(); //$NON-NLS-1$

    /**
     * The margin height of the dialog.
     */
    private static final int MARGIN_HEIGHT =
        LayoutMessages.getInteger(RESOURCE_PREFIX +
                                  "marginHeight").intValue(); //$NON-NLS-1$

    /**
     * The margin width of the dialog.
     */
    private static final int MARGIN_WIDTH =
        LayoutMessages.getInteger(RESOURCE_PREFIX +
                                  "marginWidth").intValue(); //$NON-NLS-1$

    /**
     * The rows label text.
     */
    private static final String rowsLabelText =
        LayoutMessages.getString(RESOURCE_PREFIX +
                                 "rows.labelText"); //$NON-NLS-1$

    /**
     * The columns label text.
     */
    private static final String columnsLabelText =
        LayoutMessages.getString(RESOURCE_PREFIX +
                                 "columns.labelText"); //$NON-NLS-1$

    /**
     * The message format for row input errors.
     */
    private static final MessageFormat rowErrorFormat =
        new MessageFormat(LayoutMessages.getString(RESOURCE_PREFIX +
                                                   "rows.errorFormat")); //$NON-NLS-1$

    /**
     * The message format for column input errors.
     */
    private static final MessageFormat columnErrorFormat =
        new MessageFormat(LayoutMessages.getString(RESOURCE_PREFIX +
                                                   "columns.errorFormat")); //$NON-NLS-1$

    /**
     * The result array
     */
    private Object[] result;

    /**
     * Entered number of rows.
     */
    private int rows;

    /**
     * Entered number of columns.
     */
    private int columns;

    /**
     * The text input box for rows.
     */
    private Text rowsInput;

    /**
     * The text input box for columns.
     */
    private Text columnsInput;

    /**
     * Initializes the new instance using the given parameters. Type defaults
     * to {@link Type#ADD}.
     *
     * @param parent the shell within which the dialog is displayed
     */
    public GridDimensionsDialog(Shell parent) {
        this(parent, Type.ADD);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param parent the shell within which the dialog is displayed
     * @param type   the type of usage for the dialog. Must not be null.
     */
    public GridDimensionsDialog(Shell parent,
                                Type type) {
        super(parent);

        if (type == null) {
            throw new IllegalArgumentException(
                    "A non-null type must be given"); //$NON-NLS-1$
        }

        DIALOG_TITLE = LayoutMessages.getString(
            new StringBuffer(RESOURCE_PREFIX).append(type.key).
            append("title").toString()); //$NON-NLS-1$

        setTitle(DIALOG_TITLE);
        setBlockOnOpen(true);

        result = null;
    }

    // javadoc inherited
    public Object[] getResult() {
        return result;
    }

    // javadoc inherited
    protected void okPressed() {
        result = new Object[] {new Dimension(columns, rows)};

        super.okPressed();
    }

    // javadoc inherited
    protected void cancelPressed() {
        result = null;

        super.cancelPressed();
    }

    /**
     * Creates the dialog's control. This consists of a two column grid where
     * the first column contains the labels for the two inputs and the second
     * column contains the text input boxes. The first column is sized to the
     * labels while the second column is sized to fill the remaining horizontal
     * space. The error label spans both columns to fill the horizontal space
     * available.
     *
     * @param parent the parent of the dialog's control
     * @return the created top-most control
     */
    protected Control createDialogArea(Composite parent) {
        Composite topLevel = (Composite)super.createDialogArea(parent);

        // Set up the grid layout
        GridLayout topLevelGrid = new GridLayout(2, false);
        topLevelGrid.horizontalSpacing = HORIZONTAL_SPACING;
        topLevelGrid.marginHeight = MARGIN_HEIGHT;
        topLevelGrid.marginWidth = MARGIN_WIDTH;
        topLevel.setLayout(topLevelGrid);

        // Create the rows label and input
        Label rowsLabel = new Label(topLevel, SWT.NONE);
        rowsLabel.setText(rowsLabelText);

        rowsInput = new Text(topLevel, SWT.SINGLE | SWT.BORDER);
        rowsInput.setLayoutData(
            new GridData(GridData.FILL_HORIZONTAL));
        rowsInput.addModifyListener(new ModifyListener() {
            // javadoc inherited
            public void modifyText(ModifyEvent modifyEvent) {
                validateDialog();
            }
        });

        // Create the columns label and input
        Label columnsLabel = new Label(topLevel, SWT.NONE);
        columnsLabel.setText(columnsLabelText);

        columnsInput = new Text(topLevel, SWT.SINGLE | SWT.BORDER);
        columnsInput.setLayoutData(
            new GridData(GridData.FILL_HORIZONTAL));
        columnsInput.addModifyListener(new ModifyListener() {
            // javadoc inherited
            public void modifyText(ModifyEvent modifyEvent) {
                validateDialog();
            }
        });

        // Create the error message area and span it across both columns
        Composite errorMessageComposite =
            (Composite)createErrorMessageArea(topLevel);
        GridData errorMessageCompositeGridData =
            new GridData(GridData.FILL_HORIZONTAL);
        errorMessageCompositeGridData.horizontalSpan = 2;
        errorMessageComposite.setLayoutData(errorMessageCompositeGridData);

        return topLevel;
    }

    // javadoc inherited
    protected Button createButton(
        Composite parent,
        int id,
        String label,
        boolean defaultButton) {
        Button button = super.createButton(parent, id, label, defaultButton);

        if (id == IDialogConstants.OK_ID) {
            // The inputs will be empty by default, which gives invalid values
            // without an error message, so simply disable the OK button
            button.setEnabled(false);
        }

        return button;
    }

    /**
     * Validates the rows and columns inputs. An empty input is invalid but
     * does not generate an error message. Other errors in the input generate
     * an error message and are deemed to make the input invalid.
     *
     * <p>If the inputs are invalid in any way the OK button is disabled.</p>
     */
    protected void validateDialog() {
        String rowsText = rowsInput.getText();
        String columnsText = columnsInput.getText();
        MessageFormat format = null;
        Object[] args = null;
        boolean rowsValid = false;
        boolean valid = false;

        // Validate the input for the rows. An empty input is invalid without
        // an error message
        if (!"".equals(rowsText)) { //$NON-NLS-1$
            try {
                rows = Integer.valueOf(rowsText).intValue();

                valid = rowsValid = (rows > 0);
            } catch (NumberFormatException e) {
                // Do nothing
            }

            if (!valid) {
                // Record the information for generating the error message
                format = rowErrorFormat;
                args = new Object[]{rowsText};
            }
        }

        // If rows is valid or empty validate the columns
        if (valid || (format == null)) {
            valid = false;

            // Validate the input for the columns. An empty input is invalid
            // without an error message
            if (!"".equals(columnsText)) { //$NON-NLS-1$
                try {
                    columns = Integer.valueOf(columnsText).intValue();

                    valid = (columns > 0);
                } catch (NumberFormatException e) {
                    // Do nothing
                }

                if (!valid) {
                    // Record the information for generating the error message
                    format = columnErrorFormat;
                    args = new Object[] {columnsText};
                } else {
                    // Over-all validity now depends on the row input being
                    // valid (i.e. non-empty and a valid value)
                    valid = rowsValid;
                }
            }
        }

        getOkButton().setEnabled(valid);

        if (valid || (format == null)) {
            // Clear the error message
            setErrorMessage(null);
        } else {
            // Generate the required error message
            String message = format.format(args);

            setErrorMessage(message);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Feb-04	2895/3	philws	VBM:2004020605 Fix Grid Dimensions Dialog to ensure that OK is disabled when any invalid value combination exists

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 ===========================================================================
*/
